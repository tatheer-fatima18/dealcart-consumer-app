package com.localpk.app.features.checkout.view

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.cart.data.PaymentMethodUiData
import com.localpk.app.features.cart.data.defaultPaymentMethod
import com.localpk.app.features.personal.data.defaultProfile
import com.localpk.app.features.personal.domain.UpdateProfileDetailUseCase
import com.localpk.app.utils.SharedPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow

class SelectPaymentMethodViewModel(
    val argsData: PaymentMethodUiData,
    private val updateProfileDetailUseCase: UpdateProfileDetailUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel<Any>() {

    val phoneNumber = MutableStateFlow(sharedPreferencesManager.getPhoneNumber())

    var selectedPaymentMethod = MutableStateFlow(
        defaultPaymentMethod(argsData.paymentId)
    )

    fun updatePersonalDetails(name: String) {
        unZipViewStateFlow(
            updateProfileDetailUseCase(defaultProfile(name)),
            onSuccess = {
                sharedPreferencesManager.setUserName(name)
            }
        )
    }
}




