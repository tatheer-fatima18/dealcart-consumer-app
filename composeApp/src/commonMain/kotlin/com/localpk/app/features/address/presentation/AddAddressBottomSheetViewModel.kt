package com.localpk.app.features.address.presentation

import com.localpk.app.base.BaseViewModel
import com.localpk.app.common_components.GlobalToastManager
import com.localpk.app.common_components.SnackbarType
import com.localpk.app.features.address.data.AddAddressRequest
import com.localpk.app.features.address.data.AddAddressUiData
import com.localpk.app.features.address.data.AdditionalAddressDetails
import com.localpk.app.features.address.data.AddressType
import com.localpk.app.features.address.data.UserAddress
import com.localpk.app.features.address.domain.AddAddressUseCase
import com.localpk.app.network.GeneralAddressResponse
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.KARACHI
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.SingleEventFlow
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddAddressBottomSheetViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val addAddressUseCase: AddAddressUseCase,
    private val eventLogger: EventLogger
) : BaseViewModel<Any>() {

    private val _addAddressButton = MutableStateFlow(false)
    val addAddressButton = _addAddressButton.asStateFlow()

    private val _argsData = MutableStateFlow<AddAddressUiData?>(null)
    val argsData = _argsData.asStateFlow()

    private val _address = MutableStateFlow<AddAddressUiData?>(null)
    val address = _address.asStateFlow()

    var city = MutableStateFlow(EMPTY_STRING)

    var addressType = MutableStateFlow(argsData.value?.addressType)

    private val _addressDetails = MutableStateFlow<AddAddressRequest?>(null)
    val addressDetails = _addressDetails.asStateFlow()

    private val _isOutsideKarachi = MutableStateFlow(false)
    val isOutsideKarachi = _isOutsideKarachi.asStateFlow()

    val actionDismissBottomSheet = SingleEventFlow<Unit>()

    fun onAddressTextChange(
        city: String,
        area: String,
        street: String,
        building: String,
        floor: String,
        flat: String,
        house: String,
        shopName: String,
        shopNumber: String
    ) {
        if (city.isNotEmpty() && area.isNotEmpty() && street.isNotEmpty()) {
            when (addressType.value) {
                AddressType.FLAT.type, AddressType.OFFICE.type -> {
                    _addAddressButton.value =
                        building.isNotEmpty() && flat.isNotEmpty() && floor.isNotEmpty()
                }

                AddressType.HOUSE.type -> {
                    _addAddressButton.value = false
                    _addAddressButton.value = house.isNotEmpty()
                }

                AddressType.SHOP.type -> {
                    _addAddressButton.value = false
                    _addAddressButton.value = shopName.isNotEmpty() && shopNumber.isNotEmpty()
                }
            }
        } else
            _addAddressButton.value = false
    }

    fun onActionClickConfirm(
        city: String,
        area: String,
        street: String,
        building: String,
        floor: String,
        flat: String,
        house: String,
        shopName: String,
        shopNumber: String,
        landmark: String,
        successCallback: (Double, Double, String) -> Unit
    ) {
        val floorNumber: Int = floor.toIntOrNull() ?: 0
        addAddress(
            addressType.value.default(),
            city,
            area,
            street,
            building,
            floorNumber,
            flat,
            house,
            shopName,
            shopNumber,
            landmark
        ) {
            if (sharedPreferencesManager.getSelectedCity().lowercase() != KARACHI) {
                _isOutsideKarachi.value = true
            }
            actionDismissBottomSheet.tryEmit(Unit)
            successCallback(
                argsData.value?.lat.getValueOrDefault(),
                argsData.value?.lng.getValueOrDefault(),
                city
            )

        }

    }

    private fun addAddress(
        addressType: String,
        city: String,
        area: String,
        street: String,
        building: String,
        floor: Int,
        flat: String,
        house: String,
        shopName: String,
        shopNumber: String,
        landmark: String,
        successCallback: () -> Unit
    ) {
        val additionalAddressDetails = AdditionalAddressDetails(
            flatNo = if (addressType == AddressType.FLAT.type || addressType == AddressType.OFFICE.type) flat else null,
            buildingName = if (addressType == AddressType.FLAT.type || addressType == AddressType.OFFICE.type) building else null,
            houseNumber = if (addressType == AddressType.HOUSE.type) house else null,
            floorNo = if (addressType == AddressType.FLAT.type || addressType == AddressType.OFFICE.type) floor else null,
            shopName = if (addressType == AddressType.SHOP.type) shopName else null,
            shopNumber = if (addressType == AddressType.SHOP.type) shopNumber else null
        )
        val request = AddAddressRequest(
            id = argsData.value?.id.getValueOrDefault(),
            city = city,
            addressType = addressType,
            lat = address.value?.lat.getValueOrDefault(),
            lng = address.value?.lng.getValueOrDefault(),
            area = area,
            nearestLandmark = landmark,
            streetName = street,
            additionalAddressDetails = additionalAddressDetails,
            phoneNumber = argsData.value?.phoneNumber?.ifEmpty { null }
        )
        _addAddressButton.value = false
        unZipViewStateFlow(addAddressUseCase(request), onSuccess = {
            val response = it as GeneralAddressResponse
            GlobalToastManager.showToast(
                message = "Address Added Successfully!",
                type = SnackbarType.SUCCESS
            )
            _addAddressButton.value = true
            eventLogger.confirmAddAddress(
                UserAddress(
                    id = 0,
                    latitude = request.lat,
                    longitude = request.lng,
                    streetAddress = request.streetName,
                    area = request.area,
                    city = request.city,
                    country = request.country,
                    default = request.isDefault,
                )
            )

            sharedPreferencesManager.setDefaultAddress(response.body.concatenatedAddress.default())

            if (argsData.value?.id.isNotZero()) {
                sharedPreferencesManager.setDefaultAddressId(argsData.value?.id.getValueOrDefault())
            } else {
                sharedPreferencesManager.setDefaultAddressId(response.body.Id)
            }
            _addressDetails.value = request
            successCallback()
        }, onError = {
            GlobalToastManager.showToast(
                message = it.message,
                type = SnackbarType.ERROR
            )
            _addAddressButton.value = true
        })
    }

    fun onActionCheckedChange(id: Int) {
        addressType.value = when (id) {
            1 -> AddressType.FLAT.type
            2 -> AddressType.HOUSE.type
            3 -> AddressType.SHOP.type
            4 -> AddressType.OFFICE.type
            else -> "other"
        }
    }

    fun updateAddress(argsData: AddAddressUiData) {
        _argsData.value = argsData
        _address.value = argsData
        city.value = argsData.city
        addressType.value = argsData.addressType.default().ifEmpty { AddressType.FLAT.type }
    }

}