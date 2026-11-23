package com.localpk.app.features.address.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.common_components.AutocompleteSearchBar
import com.localpk.app.features.address.data.AdditionalAddressDetails
import com.localpk.app.features.address.data.AddressUiData
import com.localpk.app.features.address.data.Feature
import com.localpk.app.features.user_location.data.Geolocation
import com.localpk.app.features.user_location.presentation.UserLocationViewModel
import com.localpk.app.features.webview.MapWebView
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextPrimary
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotZero
import org.koin.compose.koinInject
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.alert_outside_selected_area
import dealcart.composeapp.generated.resources.back_button
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.localpk.app.features.address.data.toAddAddressBottomSheetScreen
import com.localpk.app.features.cart.view.CartScreen
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.ColorDivider
import com.localpk.app.utils.BIT_DELAY
import com.localpk.app.utils.default
import com.localpk.app.utils.isNotNull
import dealcart.composeapp.generated.resources.confirm_address
import dealcart.composeapp.generated.resources.edit_address
import dealcart.composeapp.generated.resources.pin
import openAppSettings
import org.koin.compose.viewmodel.koinViewModel

class MapScreen(
    private val id: Long = 0L,
    private val defaultAddress: String = EMPTY_STRING,
    private val address: String = EMPTY_STRING,
    private val addressDescription: String = EMPTY_STRING,
    private val lat: Double = 0.0,
    private val lng: Double = 0.0,
    private val selected: Boolean = false,
    private val selectedPosition: Int = -1,
    private val city: String = EMPTY_STRING,
    private val area: String = EMPTY_STRING,
    private val addressType: String = EMPTY_STRING,
    private val flatNo: String? = null,
    private val floorNo: Int? = null,
    private val buildingName: String? = null,
    private val houseNumber: String? = null,
    private val shopName: String? = null,
    private val shopNumber: String? = null,
    private val nearestLandmark: String = EMPTY_STRING,
    private val streetName: String = EMPTY_STRING,
    private val navigationFrom: String = EMPTY_STRING,
    private val isOrderReturned: Boolean = false,
    private val secondaryPhoneNumber: String? = null,
    private val isSecondaryContactNeeded: Boolean = false
) : Screen {

    @Composable
    override fun Content() {

        val userLocationViewModel = koinInject<UserLocationViewModel>()
        val addressesViewModel = koinViewModel<AddressListViewModel>()
        val viewModel = koinInject<MapViewModel>()
        val selectedAddress by userLocationViewModel.selectedAddress.collectAsState(null)
        val isLoadingAddress by userLocationViewModel.isLoadingAddress.collectAsState(null)
        val mapPolygon by viewModel.mapPolygon.collectAsState(null)
        val isPinLocationOutsideArea by viewModel.isPinLocationOutsideArea.collectAsState()
        var cameraCenter by remember { mutableStateOf<Geolocation?>(null) }
        var lastCameraCenter by remember { mutableStateOf<Geolocation?>(null) }
        var warehouseId by remember { mutableStateOf<Long?>(null) }
        val debounceMillis = 500L
        val threshold = 0.0001
        val coroutineScope = rememberCoroutineScope()
        val locationError by userLocationViewModel.locationError.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        var showPinOutsideAlert by remember { mutableStateOf(false) }

        // Create AddressUiData from primitive parameters
        val argsData = AddressUiData(
            id = id,
            defaultAddress = defaultAddress,
            address = address,
            addressDescription = addressDescription,
            lat = lat,
            lng = lng,
            selected = selected,
            selectedPosition = selectedPosition,
            city = city,
            area = area,
            addressType = addressType,
            additionalAddressDetails = AdditionalAddressDetails(
                flatNo = flatNo,
                floorNo = floorNo,
                buildingName = buildingName,
                houseNumber = houseNumber,
                shopName = shopName,
                shopNumber = shopNumber
            ),
            nearestLandmark = nearestLandmark,
            streetName = streetName,
            navigationFrom = navigationFrom,
            isOrderReturned = isOrderReturned,
            secondaryPhoneNumber = secondaryPhoneNumber,
            isSecondaryContactNeeded = isSecondaryContactNeeded
        )
        LaunchedEffect(Unit) {
            viewModel.fetchMapPolygon()
            if (selectedAddress == null) {
                if (argsData.lat.isNotZero() && argsData.lng.isNotZero()) {
                    userLocationViewModel.onLocationSelected(
                        coordinates = Geolocation(argsData.lat, argsData.lng),
                        isFromMapScreen = true,
                        warehouseIdCallback = { warehouseId = it }

                    )
                } else {
                    userLocationViewModel.getCurrentLocation(
                        isFromMapScreen = true,
                        warehouseIdCallback = { warehouseId = it })
                }
            } else {
                userLocationViewModel.getCurrentLocation(
                    isFromMapScreen = true,
                    warehouseIdCallback = { warehouseId = it })
            }
        }

        LaunchedEffect(mapPolygon, selectedAddress) {
            // Only check if both mapPolygon and selectedAddress are available and coordinates are valid
            val polygon = mapPolygon
            val address = selectedAddress
            if (polygon != null && address != null) {
                val lat = address.latitude.getValueOrDefault()
                val lng = address.longitude.getValueOrDefault()
                if (lat.isNotZero() && lng.isNotZero()) {
                    val location = Geolocation(latitude = lat, longitude = lng)
                    val features = polygon.features

                    val isInside = isLocationInsidePolygon(features, location)
                    val isAllowed = RemoteConfigs.isLocationOutsideServiceAreaAllowed()
                    val isOutside = !isInside && isAllowed

                    viewModel.updateIsPinLocationOutsideArea(isOutside)
                } else {
                    viewModel.updateIsPinLocationOutsideArea(false)
                }
            }
        }

        LaunchedEffect(cameraCenter) {
            cameraCenter?.let { location ->
                val shouldUpdate = lastCameraCenter == null ||
                        kotlin.math.abs(location.latitude - lastCameraCenter!!.latitude) > threshold ||
                        kotlin.math.abs(location.longitude - lastCameraCenter!!.longitude) > threshold

                if (shouldUpdate) {
                    lastCameraCenter = location
                    // Only update ViewModel, do NOT update selectedAddress or trigger a camera move
                    delay(debounceMillis)
                    userLocationViewModel.onLocationSelected(
                        coordinates = location,
                        isFromMapScreen = true,
                        warehouseIdCallback = { warehouseId = it }
                    )
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            if (selectedAddress != null) {
                MapWebView(
                    modifier = Modifier
                        .fillMaxSize()
                        .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                    initialLat = selectedAddress?.latitude.getValueOrDefault(),
                    initialLng = selectedAddress?.longitude.getValueOrDefault(),
                    onLocationReceived = { lat, lng ->
                        cameraCenter = Geolocation(latitude = lat, longitude = lng)
                    },
                    onCurrentLocation = null,
                    requestCurrentLocationTrigger = 0,
                    urlQueryString = "isPinDraggable=true",
                    onLocationSet = {
                        showPinOutsideAlert = it
                    }
                )
            } else if (isLoadingAddress == true || userLocationViewModel.isGettingCurrentLocation.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (locationError != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(locationError ?: "Unable to determine your location.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { openAppSettings() }) {
                        Text("Open Settings")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        coroutineScope.launch {
                            userLocationViewModel.getCurrentLocation(
                                isFromMapScreen = true,
                                warehouseIdCallback = { warehouseId = it })
                        }
                    }) {
                        Text("Retry")
                    }
                }
            }


            // Back Button
            IconButton(
                onClick = { navigator.pop() },
                modifier = Modifier.size(80.dp)
                    .padding(top = 16.dp)

            ) {
                Image(
                    painter = painterResource(Res.drawable.back_button),
                    contentDescription = "Back",
                )
            }


            // Top Bar with Search and Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .zIndex(9999f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Search Bar
                AutocompleteSearchBar(
                    onLocationSelected = { location ->
                        // Update map camera to selected location

                        coroutineScope.launch {
                            userLocationViewModel.onLocationSelected(
                                coordinates = location,
                                isFromMapScreen = true,
                                successCallback = {
//                                viewModel.updateIsPinLocationOutsideArea( !isLocationInsidePolygon(
//                                    mapPolygon?.features.orEmpty(),
//                                    Geolocation(
//                                        latitude = selectedAddress?.latitude.getValueOrDefault(),
//                                        longitude = selectedAddress?.longitude.getValueOrDefault()
//                                    )
//                                ) && RemoteConfigs.isLocationOutsideServiceAreaAllowed())
                                },
                                warehouseIdCallback = { warehouseId = it }
                            )

                            // Also update camera center to trigger map movement
                            //  userLocationViewModel.updateSelectedAddress(lat = location.latitude, lng = location.longitude)
                            cameraCenter = location
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp)
                )
            }
            if (isPinLocationOutsideArea && showPinOutsideAlert) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter).padding(bottom = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(5.20.dp)
                ) {
                    Text(
                        stringResource(Res.string.alert_outside_selected_area),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextPrimary,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier
                            .padding(horizontal = 13.dp)
                            .background(
                                shape = RoundedCornerShape(5.2.dp),
                                color = SurfaceBackground
                            )
                            .padding(7.8.dp)
                            .zIndex(1f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 13.dp, end = 13.dp, bottom = 30.dp)
                    .align(Alignment.BottomCenter)
            ) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(bottom = 16.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(25.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = ButtonSurfaceBrand,
                                shape = RoundedCornerShape(25.dp)
                            )
                            .clickable {
                                coroutineScope.launch {
                                    userLocationViewModel.getCurrentLocation(
                                        isFromMapScreen = true,
                                        warehouseIdCallback = { warehouseId = it })
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp).align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.pin),
                            modifier = Modifier.size(24.dp),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Text
                        Text(
                            text = "Go to my current location",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = ButtonSurfaceBrand,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                // Confirm Address Button
                Button(
                    enabled = isLoadingAddress == false,
                    onClick = {
                        if (selectedAddress?.latitude.getValueOrDefault()
                                .isNotZero() && selectedAddress?.longitude.getValueOrDefault()
                                .isNotZero() && warehouseId.isNotNull() && warehouseId != null
                        ) {
                            val arguments = argsData.copy(
                                lat = selectedAddress?.latitude.getValueOrDefault(),
                                lng = selectedAddress?.longitude.getValueOrDefault(),
                                streetName = argsData.streetName.ifEmpty { selectedAddress?.streetAddress.default() },
                                city = selectedAddress?.city.default(),
                                area = argsData.area.ifEmpty { selectedAddress?.area.default() }
                            ).apply {
                                listener = {
                                    coroutineScope.launch {
                                        delay(BIT_DELAY)

                                        if (navigationFrom == ScreenSource.CART.key) {
                                            navigator.popUntil { screen: Screen ->
                                                screen is CartScreen
                                            }
                                        } else {
                                            navigator.pop()
                                        }
                                    }
                                }
                            }
                            addressesViewModel.setAddressDataListener(arguments)
                            bottomSheetNavigator.show(
                                arguments.toAddAddressBottomSheetScreen(warehouseId = warehouseId.getValueOrDefault())
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonSurfaceBrand,
                        disabledContainerColor = ColorDivider
                    ),
                    shape = RoundedCornerShape(9.1.dp)
                ) {
                    Text(
                        text = if (argsData.id.isNotZero()) stringResource(
                            Res.string.edit_address
                        ) else stringResource(
                            Res.string.confirm_address
                        ),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }


    private fun isLocationInsidePolygon(
        features: List<Feature>,
        pinLocation: Geolocation
    ): Boolean {

        for (feature in features) {
            val geometry = feature.geometry
            if (feature.type == "Feature" && geometry != null) {
                if (geometry.type == "Polygon") {
                    val coordinates = geometry.coordinates
                    if (!coordinates.isNullOrEmpty()) {
                        val polygonLatLngList = createPolygonLatLngList(coordinates)
                        if (isPointInsidePolygon(pinLocation, polygonLatLngList)) {
                            return true
                        }
                    }
                } else if (geometry.type == "GeometryCollection") {
                    val geometries = geometry.geometries
                    for (subGeometry in geometries.orEmpty()) {
                        if (subGeometry.type == "Polygon") {
                            val coordinates = subGeometry.coordinates
                            if (!coordinates.isNullOrEmpty()) {
                                val polygonLatLngList = createPolygonLatLngList(coordinates)
                                if (isPointInsidePolygon(pinLocation, polygonLatLngList)) {
                                    return true
                                }
                            }
                        }
                    }
                }
            }
        }

        return false
    }

    private fun isPointInsidePolygon(point: Geolocation, polygon: List<Geolocation>): Boolean {
        var inside = false
        var j = polygon.size - 1

        for (i in polygon.indices) {
            val vertex1 = polygon[i]
            val vertex2 = polygon[j]

            if ((vertex1.latitude < point.latitude && vertex2.latitude >= point.latitude ||
                        vertex2.latitude < point.latitude && vertex1.latitude >= point.latitude) &&
                (vertex1.longitude + (point.latitude - vertex1.latitude) /
                        (vertex2.latitude - vertex1.latitude) * (vertex2.longitude - vertex1.longitude) < point.longitude)
            ) {
                inside = !inside
            }

            j = i
        }

        return inside
    }

    private fun createPolygonLatLngList(coordinates: List<List<List<Double>>>): List<Geolocation> {
        val polygonLatLngList = mutableListOf<Geolocation>()

        for (coordinateList in coordinates) {
            for (coordinate in coordinateList) {
                val latitude = coordinate[1]
                val longitude = coordinate[0]
                polygonLatLngList.add(Geolocation(latitude, longitude))
            }
        }

        return polygonLatLngList
    }
}
