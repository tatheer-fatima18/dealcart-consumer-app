package com.localpk.app.features.address.presentation

import androidx.lifecycle.viewModelScope
import com.localpk.app.base.BaseViewModel
import com.localpk.app.common_components.GlobalToastManager
import com.localpk.app.common_components.SnackbarType
import com.localpk.app.features.address.data.AddressUiData
import com.localpk.app.features.address.domain.FetchCustomerAddressUseCase
import com.localpk.app.features.address.domain.RemoveAddressUseCase
import com.localpk.app.features.user_location.data.doesMatchSearchQuery
import com.localpk.app.features.user_location.domain.OperationalCitiesUseCase
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.ZERO_LONG
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

class AddressListViewModel(
    private val operationalCitiesUseCase: OperationalCitiesUseCase,
    private val fetchCustomerAddressUseCase: FetchCustomerAddressUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val removeAddressUseCase: RemoveAddressUseCase,
) : BaseViewModel<Any>() {
    private val _addresses = MutableStateFlow<List<AddressUiData>>(emptyList())
    val addresses = _addresses.asStateFlow()

    private val _isAddressesLoaded = MutableStateFlow(false)
    val isAddressesLoaded = _isAddressesLoaded.asStateFlow()

    private val _cities = MutableStateFlow(listOf<String>())

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow(EMPTY_STRING)
    val searchText = _searchText.asStateFlow()

    private val _addressDataListener = MutableStateFlow<AddressUiData?>(null)
    val addressDataListener: StateFlow<AddressUiData?> = _addressDataListener

    @OptIn(FlowPreview::class)
    val cities = searchText
        .debounce(300L)
        .onEach { _isSearching.value = true }
        .combine(_cities) { text, cities ->
            if (text.isBlank()) {
                cities
            } else {
                delay(300L)
                cities.filter { doesMatchSearchQuery(it, text) }
            }
        }
        .onEach { _isSearching.value = false }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(2000),
            _cities.value
        )


    fun loadSavedAddresses() {
        unZipViewStateFlow(
            fetchCustomerAddressUseCase(Unit),
            onSuccess = {
                val result = it as List<AddressUiData>
                _addresses.value = result
                _isAddressesLoaded.value = true
                if (result.isEmpty()) {
                    sharedPreferencesManager.setDefaultAddress(EMPTY_STRING)
                    sharedPreferencesManager.setDefaultAddressId(ZERO_LONG)
                }
            }
        )
    }

    fun removeAddress(addressId: Long) {
        _isAddressesLoaded.value = false
//        eventLogger.logMoEngageGenericEvent(MO_PROFILE_DELETE_ADDRESS)
//        eventLogger.RemoveAddress(addressId)
        unZipViewStateFlow(removeAddressUseCase(addressId), onSuccess = {
            GlobalToastManager.showToast(
                message = "Your address has been deleted successfully",
                type = SnackbarType.SUCCESS
            )
            loadSavedAddresses()
        })
    }

    fun selectDefaultAddress(addressUiData: AddressUiData) {
        if (addressUiData.id != sharedPreferencesManager.getDefaultAddressId()) {
            _isAddressesLoaded.value = false
            sharedPreferencesManager.setDefaultAddressId(addressUiData.id)
            sharedPreferencesManager.setDefaultAddress(addressUiData.address)
            loadSavedAddresses()
        }
    }

    fun setAddressesEmpty() {
        _addresses.value = emptyList()
    }

    fun getOperationalCities() {
        unZipViewStateFlow(
            operationalCitiesUseCase(Unit),
            onSuccess = {
                val response = it as List<String>
                val priorityCities = listOf("karachi", "lahore", "islamabad")
                val topThree = priorityCities.mapNotNull { key ->
                    response.firstOrNull { it.equals(key, ignoreCase = true) }
                }
                val rest = response.filter { city ->
                    !priorityCities.any { city.equals(it, ignoreCase = true) }
                }
                _cities.value = topThree + rest
            }
        )
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun setAddressDataListener(data: AddressUiData) {
        _addressDataListener.value = data
    }
}