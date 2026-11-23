package com.localpk.app.features.address.presentation

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.address.data.AddAddressRequest
import com.localpk.app.features.address.data.AddressType
import com.localpk.app.features.address.domain.AddAddressUseCase
import com.localpk.app.network.GeneralAddressResponse
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.default
import com.localpk.app.utils.isNotZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddPhoneNumberBottomSheetViewModel(
    private val addAddressUseCase: AddAddressUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel<Any>() {

    private val _addButton = MutableStateFlow(false)
    val addButton: StateFlow<Boolean> = _addButton.asStateFlow()

    private val _argsData = MutableStateFlow<AddAddressRequest?>(null)
    val argsData: StateFlow<AddAddressRequest?> = _argsData.asStateFlow()

    private val _address = MutableStateFlow<AddAddressRequest?>(null)
    val address: StateFlow<AddAddressRequest?> = _address.asStateFlow()

    var city = MutableStateFlow(EMPTY_STRING)
    var addressType = MutableStateFlow(AddressType.FLAT.type)


    private fun buttonEnabled() {
        _addButton.value = !_address.value?.phoneNumber.isNullOrEmpty()
    }

    fun onAddressTextChange(phoneNumber: String) {
        _addButton.value = phoneNumber.isNotEmpty() &&
                (if (phoneNumber.startsWith("0"))
                    phoneNumber.length == 11
                else
                    phoneNumber.length == 10)

    }

    fun onActionClickConfirm(
        phoneNumber: String,
        dismissBottomSheet: () -> Unit
    ) {
        addAddress(phoneNumber) {
            dismissBottomSheet()
        }
    }

    private fun addAddress(
        phoneNumber: String,
        successCallback: () -> Unit
    ) {
        val argsData = argsData.value ?: return
        val request =
            AddAddressRequest(
                id = argsData.id,
                city = argsData.city,
                addressType = argsData.addressType,
                lat = argsData.lat,
                lng = argsData.lng,
                area = argsData.area,
                nearestLandmark = argsData.nearestLandmark,
                streetName = argsData.streetName,
                additionalAddressDetails = argsData.additionalAddressDetails,
                phoneNumber = if (phoneNumber.isEmpty()) {
                    null
                } else if (phoneNumber.startsWith("0")) {
                    phoneNumber
                } else {
                    "0$phoneNumber"
                }
            )

        unZipViewStateFlow(addAddressUseCase(request), onSuccess = {
            val response = it as GeneralAddressResponse
            sharedPreferencesManager.setDefaultAddress(response.body.concatenatedAddress.default())

            if (argsData.id.isNotZero()) {
                sharedPreferencesManager.setDefaultAddressId(argsData.id)
            } else {
                sharedPreferencesManager.setDefaultAddressId(response.body.Id)
            }
            successCallback()
        }, onError = {
//            toastManager.showError(it)
        })
    }

    fun onActionClickSkip(dismissBottomSheet: () -> Unit) {
//        eventLogger.clickSkip()
        addAddress(argsData.value?.phoneNumber.default()) {
            dismissBottomSheet()
        }
    }

    fun updateArgsData(data: AddAddressRequest) {
        _argsData.value = data
        _address.value = argsData.value
        buttonEnabled()
    }

}