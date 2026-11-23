package com.localpk.app.features.wallet.view

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.cart.data.CardPaymentDetails
import com.localpk.app.features.cart.data.PaymentMethod
import com.localpk.app.features.cart.data.defaultPaymentMethod
import com.localpk.app.features.personal.data.defaultProfile
import com.localpk.app.features.personal.domain.UpdateProfileDetailUseCase
import com.localpk.app.features.wallet.data.CardTopUpRequest
import com.localpk.app.features.wallet.domain.CardTopUpUseCase
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.default
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaymentViewModel(
    val argsData: CardTopUpRequest,
    private val updateProfileDetailUseCase: UpdateProfileDetailUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val cardTopUpUseCase: CardTopUpUseCase
) : BaseViewModel<Any>() {

    // Phone number
    val phoneNumber = MutableStateFlow(sharedPreferencesManager.getPhoneNumber())

    private val _cardPaymentDetails = MutableStateFlow<CardPaymentDetails?>(null)
    val cardPaymentDetails: StateFlow<CardPaymentDetails?> = _cardPaymentDetails.asStateFlow()

    // Selected payment method
    val selectedPaymentMethod = MutableStateFlow(defaultPaymentMethod(PaymentMethod.CARD.id))

    fun cardTopUpRequest(
        amount: Double,
        paymentType: String
    ) {
        unZipViewStateFlow(
            cardTopUpUseCase(
                CardTopUpRequest(amount, paymentType)
            ),
            onSuccess = {
                val data = it as CardPaymentDetails
                _cardPaymentDetails.value = data
            }
        )
    }

    fun updatePersonalDetails(name: String) {
        if (sharedPreferencesManager.getUserName().isEmpty()) {
            unZipViewStateFlow(
                updateProfileDetailUseCase(defaultProfile(name)),
                onSuccess = {
                    sharedPreferencesManager.setUserName(name)
                    cardTopUpRequest(
                        argsData.amount,
                        selectedPaymentMethod.value.paymentType.default()
                    )
                }
            )
        }
    }
    
}