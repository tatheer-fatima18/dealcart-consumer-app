package com.localpk.app.features.login.presentation

import com.localpk.app.base.BaseViewModel
import com.localpk.app.common_components.GlobalToastManager
import com.localpk.app.common_components.SnackbarType
import com.localpk.app.features.login.data.AuthUiModel
import com.localpk.app.features.login.domain.GenerateOTPUseCase
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PhoneInputViewModel(
    private val generateOTPUseCase: GenerateOTPUseCase,
    private val eventLogger: EventLogger
) : BaseViewModel<Any>() {
    companion object {
        private val PHONE_REGEX = "^3\\d{9}\$".toRegex()
        private val FULL_PHONE_NUMBER_REGEX = "^03\\d{9}\$".toRegex()
    }

    private val _phone = MutableStateFlow(EMPTY_STRING)
    val phone: StateFlow<String> = _phone

    private val _isContinueEnabled = MutableStateFlow(false)
    val isContinueEnabled: StateFlow<Boolean> = _isContinueEnabled

    private val _isOtpFetchingInProgress = MutableStateFlow(false)
    val isOtpFetchingInProgress: StateFlow<Boolean> = _isOtpFetchingInProgress

    private fun validatePhoneNumber(phone: String): Boolean {
        return when {
            phone.length == 10 && phone.matches(PHONE_REGEX) -> true
            phone.length == 11 && phone.matches(FULL_PHONE_NUMBER_REGEX) -> true
            else -> false
        }
    }

    // Update phone number
    fun updatePhone(newPhone: String) {
        _phone.value = newPhone
        _isContinueEnabled.value = validatePhoneNumber(newPhone)
    }

    fun resetPhone() {
        _phone.value = EMPTY_STRING
        _isContinueEnabled.value = false
        _isOtpFetchingInProgress.value = false
    }

    fun onActionSignUp(
        phone: String,
        screenSource: String,
        successCallback: (AuthUiModel) -> Unit = {}
    ) {
        eventLogger.signupButton(phone)
        val phoneNumber = if (!phone.startsWith("0")) {
            "0$phone"
        } else {
            phone
        }
        generateOtp(phoneNumber, screenSource, successCallback)
    }

    private fun generateOtp(
        phone: String,
        screenSource: String,
        successCallback: (AuthUiModel) -> Unit = {}
    ) {
        _isContinueEnabled.value = false
        _isOtpFetchingInProgress.value = true
        unZipViewStateFlow(
            generateOTPUseCase(phone),
            onSuccess = { authUiModel ->
                val authData = authUiModel as AuthUiModel
                val otpArgs = AuthUiModel(
                    phone = phone,
                    hash = authData.hash,
                    user = authData.user,
                    fullName = authData.fullName,
                    otp = authData.otp,
                    source = screenSource
                )

                successCallback.invoke(otpArgs)

                _isOtpFetchingInProgress.value = false
                _isContinueEnabled.value = validatePhoneNumber(_phone.value)
            },
            onError = {
                GlobalToastManager.showToast(
                    "Unable to send OTP. Please try again.",
                    type = SnackbarType.ERROR
                )
                _isOtpFetchingInProgress.value = false
                _isContinueEnabled.value = validatePhoneNumber(_phone.value)
            }
        )
    }
}