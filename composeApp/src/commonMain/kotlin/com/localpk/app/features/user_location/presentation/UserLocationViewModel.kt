package com.localpk.app.features.user_location.presentation

import androidx.lifecycle.viewModelScope
import com.localpk.app.base.BaseViewModel
import com.localpk.app.common_components.GlobalDialogManager
import com.localpk.app.common_components.GlobalToastManager
import com.localpk.app.common_components.SnackbarType
import com.localpk.app.features.address.data.UserAddress
import com.localpk.app.features.banner.data.BannerUseCaseParams
import com.localpk.app.features.category.data.SuperParent
import com.localpk.app.features.home.presentation.HomeViewModel
import com.localpk.app.features.productlist.viewModel.ProductListViewModel
import com.localpk.app.features.user_location.data.Geolocation
import com.localpk.app.features.user_location.data.UserLocationRequest
import com.localpk.app.features.user_location.data.UserLocationResponse
import com.localpk.app.features.user_location.data.doesMatchSearchQuery
import com.localpk.app.features.user_location.domain.OperationalCitiesUseCase
import com.localpk.app.features.user_location.domain.UserLocationWarehouseUseCase
import com.localpk.app.utils.DEFAULT_COORDINATES
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_dealcart_white
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import dev.jordond.compass.permissions.LocationPermissionController
import dev.jordond.compass.permissions.exception.PermissionDeniedException
import dev.jordond.compass.permissions.exception.PermissionDeniedForeverException
import dev.jordond.compass.permissions.mobile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

expect fun openUserLocationSettings()

class UserLocationViewModel(
    private val userLocationWarehouseUseCase: UserLocationWarehouseUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val homeViewModel: HomeViewModel,
    private val productListViewModel: ProductListViewModel
) : BaseViewModel<Any>() {

    private val _selectedAddress = MutableStateFlow<UserAddress?>(null)
    val selectedAddress = _selectedAddress.asStateFlow()

    private val _currentLocation = MutableStateFlow<Geolocation?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    private val _isLoadingAddress = MutableStateFlow(false)
    val isLoadingAddress = _isLoadingAddress.asStateFlow()

    private val _isGettingCurrentLocation = MutableStateFlow(false)
    val isGettingCurrentLocation = _isGettingCurrentLocation.asStateFlow()

    private val _locationError = MutableStateFlow<String?>(null)
    val locationError: StateFlow<String?> = _locationError

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted.asStateFlow()

    private val _isPermissionDeniedForever = MutableStateFlow(false)
    val isPermissionDeniedForever: StateFlow<Boolean> = _isPermissionDeniedForever.asStateFlow()


    private val _geolocationResult = MutableStateFlow<GeolocatorResult?>(null)
    val geolocationResult: StateFlow<GeolocatorResult?> = _geolocationResult.asStateFlow()

    private val _isObservingLocationPermission = MutableStateFlow(true)
    val isObservingLocationPermission = _isObservingLocationPermission.asStateFlow()

    // Flag to track if permission observer is already running
    private var isPermissionObserverRunning = false

    private val geolocator = Geolocator.mobile()
    private val locationPermissionController = LocationPermissionController.mobile()

    fun getUserLocationWarehouse(
        lat: Double,
        lng: Double,
        city: String,
        isEditAddressRequest: Boolean = false,
        isAddAddressRequest: Boolean = false,
        isFromMapScreen: Boolean = false,
        warehouseId: Long? = null,
        successCallback: () -> Unit = {},
        backToUserLocationScreen: () -> Unit = {},
        addressDialogSuccessCallback: () -> Unit = {},
        warehouseIdCallback: (Long) -> Unit = {},
    ) {
        if (isEditAddressRequest && warehouseId != null) {
            updateWarehouseData(
                lat = lat,
                lng = lng,
                city = city,
                warehouseId = warehouseId,
                addressDialogSuccessCallback = addressDialogSuccessCallback,
                isWarehouseChanged = sharedPreferencesManager.getSelectedWarehouseId() != warehouseId
            )

        } else if (isAddAddressRequest && warehouseId != null) {
            updateWarehouseData(
                lat = lat,
                lng = lng,
                city = city,
                warehouseId = warehouseId,
                isWarehouseChanged = sharedPreferencesManager.getSelectedWarehouseId() != warehouseId,
                addressDialogSuccessCallback = addressDialogSuccessCallback
            )

        } else {
            val request = UserLocationRequest(lat, lng, city)
            unZipViewStateFlow(
                userLocationWarehouseUseCase(request),
                onSuccess = {
                    val response = it as UserLocationResponse

                    warehouseIdCallback(response.body)

                    if (!isFromMapScreen)
                        updateWarehouseDataWithLocationDialog(
                            lat = lat,
                            lng = lng,
                            city = city,
                            warehouseId = response.body,
                            isWarehouseChanged = sharedPreferencesManager.getSelectedWarehouseId() != response.body,
                            showLocationDialog = sharedPreferencesManager.getSelectedWarehouseId() == 1L && sharedPreferencesManager.getSelectedWarehouseId() != response.body,
                            addressDialogSuccessCallback = addressDialogSuccessCallback,
                            backToUserLocationScreen = backToUserLocationScreen
                        )
                    successCallback()
                },
                onError = {
                    if (it.message.contains("maujooda location"))
                        GlobalToastManager.showToast(
                            message = "DealCart’s service isn’t available at the current location",
                            type = SnackbarType.ERROR
                        )
                    else
                        GlobalToastManager.showToast(
                            message = "Oops! Something went wrong. Please try again in a moment.",
                            type = SnackbarType.ERROR
                        )
                }
            )
        }
    }


    /**
     * Start observing location permission status continuously
     * When permission becomes true, automatically call getCurrentLocation
     * Loop breaks when location is successfully obtained
     */
    fun startObservingLocationPermission(navigateToHome: () -> Unit) {

        // Prevent multiple observers from running simultaneously
        if (isPermissionObserverRunning) {
            return
        }

        isPermissionObserverRunning = true

        viewModelScope.launch {
            try {
                while (true) {
                    val hasPermission = locationPermissionController.hasPermission()
                    _permissionGranted.value = hasPermission

                    // If permission is granted and we don't have a current location, get it
                    if (hasPermission && _currentLocation.value == null && !_isGettingCurrentLocation.value) {

                        getCurrentLocation { navigateToHome() }
                        isPermissionObserverRunning = false
                        break
                    }


                    if (isObservingLocationPermission.value == false) {
                        break
                    }

                    // Check every 2 seconds
                    delay(2000)
                }
            } finally {
                // Always reset the flag when the observer stops
                isPermissionObserverRunning = false
            }
        }
    }

    fun hasLocationPermission(): Boolean {
        val hasPermission = locationPermissionController.hasPermission()
        _permissionGranted.value = hasPermission
        return hasPermission
    }


    suspend fun requestLocationPermission() {
        try {
            locationPermissionController.requirePermissionForOrThrow(dev.jordond.compass.Priority.Balanced)
        } catch (e: PermissionDeniedForeverException) {
            _isPermissionDeniedForever.value = true
            _locationError.value =
                "Location permission denied permanently. Please enable location access in Settings."
        } catch (e: PermissionDeniedException) {
            _isPermissionDeniedForever.value = true
            _locationError.value =
                "Location permission denied. Please allow access to use your current location."
        } catch (e: Exception) {
            _isPermissionDeniedForever.value = false
            _locationError.value = e.message ?: "Unable to request location permission."
        }
    }

    suspend fun getCurrentLocation(
        isFromMapScreen: Boolean = false,
        warehouseIdCallback: (Long) -> Unit = {},
        listenerCallback: () -> Unit = {},
        backToUserLocationScreen: () -> Unit = {}
    ) {

        if (isGettingCurrentLocation.value) {
            return
        }

        _isGettingCurrentLocation.value = true

        _locationError.value = null

        // Check if permission is denied forever first
        if (isPermissionDeniedForever.value) {
            _isGettingCurrentLocation.value = false

            GlobalDialogManager.showDialog(
                title = "Location Permission Required",
                message = "Location permission is required to provide you with the best experience. Please enable location access in Settings.",
                confirmButton = "Open Settings",
                cancelButton = "Cancel",
                onConfirm = {
                    // Open app settings to enable location permission
                    openLocationSettings()
                    _isPermissionDeniedForever.value = false
                    _isObservingLocationPermission.value = false
                    GlobalDialogManager.hideDialog()
                },
                onCancel = {
                    // User can continue without location
                    _isGettingCurrentLocation.value = false
                    GlobalDialogManager.hideDialog()
                }
            )
            return
        }

        _isObservingLocationPermission.value = true
        startObservingLocationPermission {
            listenerCallback()
        }

        // Check permission first
        val initialPermission = hasLocationPermission()

        if (!initialPermission) {
            requestLocationPermission()
            _isGettingCurrentLocation.value = false
            delay(500) // Wait for permission to be processed
        }


        val result = withContext(Dispatchers.Default) {
            geolocator.current()
        }


        _geolocationResult.value = result
        observeGeolocationResult(
            isFromMapScreen = isFromMapScreen,
            warehouseIdCallback,
            listenerCallback,
            backToUserLocationScreen

        )
    }

    fun observeGeolocationResult(
        isFromMapScreen: Boolean = false,
        warehouseIdCallback: (Long) -> Unit = {},
        listenerCallback: () -> Unit = {},
        backToUserLocationScreen: () -> Unit = {}
    ) {
        viewModelScope.launch {
            var shouldContinue = true
            while (shouldContinue) {
                geolocationResult.collect { result ->

                    when (result) {
                        is GeolocatorResult.Success -> {

                            val location = Geolocation(
                                latitude = result.data.coordinates.latitude,
                                longitude = result.data.coordinates.longitude
                            )
                            _currentLocation.value = location
                            _isGettingCurrentLocation.value = false

                            onLocationSelected(
                                location,
                                isFromMapScreen = isFromMapScreen,
                                warehouseIdCallback = warehouseIdCallback,
                                successCallback = {
                                    listenerCallback()
                                },
                                backToUserLocationScreen = backToUserLocationScreen
                            )

                            _isPermissionDeniedForever.value = false

                            shouldContinue = false
                        }

                        is GeolocatorResult.Error -> {
                            _isGettingCurrentLocation.value = false
                            _currentLocation.value = DEFAULT_COORDINATES

                            val errorMessage = when (result) {
                                is GeolocatorResult.NotSupported -> {
                                    "Location services are not supported on this device."
                                }

                                is GeolocatorResult.NotFound -> {
                                    "Unable to determine your location. Please check your GPS signal."
                                }

                                is GeolocatorResult.PermissionDenied -> {
                                    _isPermissionDeniedForever.value = true
                                    getCurrentLocation()
                                    "Location permission denied. Please enable location access in Settings."
                                }

                                is GeolocatorResult.GeolocationFailed -> {
                                    "Failed to get your location. If you're using iOS Simulator, please set a custom location in Features > Location > Custom Location."
                                }

                                else -> {
                                    "Location error: ${result.message}"
                                }
                            }

                            _locationError.value = errorMessage
                            shouldContinue = false

                        }

                        null -> {
                            shouldContinue = false
                        }
                    }

                    if (shouldContinue) {
                        delay(100) // Check every 100ms
                    }
                }
            }

        }
    }

    fun onLocationSelected(
        coordinates: Geolocation,
        listenerCallback: () -> Unit = {},
        successCallback: () -> Unit = {},
        warehouseIdCallback: (Long) -> Unit = {},
        isFromMapScreen: Boolean = false,
        backToUserLocationScreen: () -> Unit = {}
    ) {
        val threshold = 0.0001 // ~10 meters, adjust as needed
        val current = _selectedAddress.value
        if (current == null ||
            kotlin.math.abs(current.latitude - coordinates.latitude) > threshold ||
            kotlin.math.abs(current.longitude - coordinates.longitude) > threshold
        ) {
            // Only update if location is different
            viewModelScope.launch {
                _isLoadingAddress.value = true
                // Create address with coordinates only (no reverse geocoding)
                val address = UserAddress(
                    id = Long.MAX_VALUE,
                    latitude = coordinates.latitude,
                    longitude = coordinates.longitude,
                    pinAddress = EMPTY_STRING,
                    streetAddress = EMPTY_STRING,
                    area = EMPTY_STRING,
                    city = EMPTY_STRING,
                    country = EMPTY_STRING,
                )

                _isLoadingAddress.value = false
                _selectedAddress.value = address
                getUserLocationWarehouse(
                    lat = address.latitude,
                    lng = address.longitude,
                    city = "karachi",
                    isFromMapScreen = isFromMapScreen,
                    successCallback = { successCallback() },
                    warehouseIdCallback = {
                        warehouseIdCallback(it)
                    }, backToUserLocationScreen = backToUserLocationScreen
                )
                listenerCallback.invoke()
            }
        }
    }


    private fun openLocationSettings() {
        openUserLocationSettings()
    }

    private fun updateWarehouseData(
        lat: Double,
        lng: Double,
        city: String,
        addressDialogSuccessCallback: () -> Unit,
        warehouseId: Long,
        isWarehouseChanged: Boolean = true
    ) {

        sharedPreferencesManager.setSelectedWarehouseId(warehouseId)
        sharedPreferencesManager.setSelectedCity(city)
        sharedPreferencesManager.setLongitude(lng.toString())
        sharedPreferencesManager.setLatitude(lat.toString())
        if (sharedPreferencesManager.isLoggedIn()) {
            homeViewModel.getAddresses()
        }
        addressDialogSuccessCallback()

        if (isWarehouseChanged) {
            homeViewModel.observeCategories()
            homeViewModel.loadBanners(
                BannerUseCaseParams(
                    belongsTo = "home",
                    bannerCacheVersion = "1"
                )
            )

            homeViewModel.refreshSwimlanes()
            homeViewModel.resetScrollState()
            productListViewModel.clearCache()
            homeViewModel.resetData()
            homeViewModel.updateSelectedSuperParentCategory(null)
            if (homeViewModel.homeState.value.selectedSuperParentCategory?.id.getValueOrDefault() == SuperParent.FOOD.id)
                homeViewModel.getRestaurants()
        }
    }

    private fun updateWarehouseDataWithLocationDialog(
        lat: Double,
        lng: Double,
        city: String,
        warehouseId: Long,
        isWarehouseChanged: Boolean = true,
        showLocationDialog: Boolean = true,
        addressDialogSuccessCallback: () -> Unit,
        backToUserLocationScreen: () -> Unit
    ) {
        if (isWarehouseChanged && showLocationDialog) {
            if (warehouseId != 1L)
                GlobalDialogManager.showDialog(
                    isAddressDialog = true,
                    icon = Res.drawable.ic_dealcart_white,
                    title = "Location Alert!",
                    message = "Alert! We do not deliver grocery in this area",
                    confirmButton = "Okay",
                    cancelButton = "Cancel",
                    onConfirm = {
                        updateWarehouseData(
                            lat = lat,
                            lng = lng,
                            city = city,
                            warehouseId = warehouseId,
                            addressDialogSuccessCallback = addressDialogSuccessCallback
                        )
                    },
                    onCancel = {
                        GlobalDialogManager.hideDialog()
                        if (sharedPreferencesManager.getSelectedCity()
                                .isEmpty() && !sharedPreferencesManager.isLoggedIn()
                        ) {
                            backToUserLocationScreen()
                        }
                    }
                )
            else {
                updateWarehouseData(
                    lat = lat,
                    lng = lng,
                    city = city,
                    warehouseId = warehouseId,
                    addressDialogSuccessCallback = addressDialogSuccessCallback
                )
            }
        } else {
            updateWarehouseData(
                lat = lat,
                lng = lng,
                city = city,
                warehouseId = warehouseId,
                isWarehouseChanged = isWarehouseChanged,
                addressDialogSuccessCallback = addressDialogSuccessCallback
            )
        }
    }
}