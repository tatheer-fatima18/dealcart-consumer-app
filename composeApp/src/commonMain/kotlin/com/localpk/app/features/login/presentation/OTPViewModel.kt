package com.localpk.app.features.login.presentation

import androidx.lifecycle.viewModelScope
import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.login.data.AuthResponse
import com.localpk.app.features.login.data.AuthUiModel
import com.localpk.app.features.login.data.CheckIfCustomerExistsResponse
import com.localpk.app.features.login.data.VerifyOTPUseCaseParams
import com.localpk.app.features.login.domain.CheckIfNewCustomerUseCase
import com.localpk.app.features.login.domain.ResendOTPUseCase
import com.localpk.app.features.login.domain.VerifyOTPUseCase
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.SPACE
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.SingleEventFlow
import com.localpk.app.utils.default
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.isNotZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OTPViewModel(
    private val checkIfNewCustomerUseCase: CheckIfNewCustomerUseCase,
    private val verifyOTPUseCase: VerifyOTPUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val resendOTPUseCase: ResendOTPUseCase,
    private val eventLogger: EventLogger
) : BaseViewModel<Any>() {
    private val _resendButtonState = MutableStateFlow(
        Pair(false, "Resend OTP in")
    )
    val resendButtonState: StateFlow<Pair<Boolean, String>> = _resendButtonState

    private val _otpArgs = MutableStateFlow<AuthUiModel?>(null)
    val otpArgs: StateFlow<AuthUiModel?> = _otpArgs

    private var isNewUser = false

    private val dolatReferredById = 0

    val fullName = MutableStateFlow(EMPTY_STRING)
    var OTP = MutableStateFlow(EMPTY_STRING)
    val isOTPIncorrect = MutableStateFlow(false)
    val verificationSuccess = MutableStateFlow(false)
    val actionDismissKeyboard = SingleEventFlow<Unit>()

    // Timer job for resend functionality
    private var timerJob: Job? = null

    val errorMessage = MutableStateFlow(EMPTY_STRING)

    init {
        val firstName = otpArgs.value?.user?.firstName ?: EMPTY_STRING
        val lastName = otpArgs.value?.user?.lastName ?: EMPTY_STRING
        _otpArgs.value?.fullName = (firstName + SPACE + lastName).trim()
        fullName.value = (firstName + SPACE + lastName).trim()

        eventLogger.otpScreen(otpArgs.value?.phone.default())

        if (dolatReferredById.isNotZero()) {
            unZipViewStateFlow(
                checkIfNewCustomerUseCase(
                    otpArgs.value?.phone.default()
                ),
                onSuccess = {
                    val res = it as CheckIfCustomerExistsResponse
                    isNewUser = !res.body
                },
                onError = {
                    isNewUser = false
                }
            )
        }
    }

    fun verifyOtp(
        successCallback: () -> Unit?
    ) {
        val names = fullName.value.default().split(SPACE)

        val params = VerifyOTPUseCaseParams(
            firstName = names.getOrNull(0),
            lastName = names.getOrNull(1),
            otp = OTP.value.default(),
            phone = (if (otpArgs.value?.phone?.contains("+92") == true) otpArgs.value?.phone?.replace(
                "+92",
                "0"
            ) else otpArgs.value?.phone).default(),
            hash = otpArgs.value?.hash.default(),
            fcm = sharedPreferencesManager.getFCMToken(),
            referrerCode = sharedPreferencesManager.getReferralUserCode()
        )
        eventLogger.otpSubmitButton(otpArgs.value?.fullName.default(), otpArgs.value?.otp.default())
        unZipViewStateFlow(
            verifyOTPUseCase(params),
            onSuccess = {
                val response = it as AuthResponse
                sharedPreferencesManager.setUserName(fullName.value.default())
                sharedPreferencesManager.setPhoneNumber(otpArgs.value?.phone.default())
                sharedPreferencesManager.setToken(response.accessToken)
                sharedPreferencesManager.setLogin(true)
                sharedPreferencesManager.setUserId(response.id)
                sharedPreferencesManager.setMyReferralCode(response.referralCode.default())
                sharedPreferencesManager.setReferralUserCode(EMPTY_STRING)
                sharedPreferencesManager.setIsCommunityLeader(otpArgs.value?.user?.isCL.default())
                sharedPreferencesManager.setGender(otpArgs.value?.user?.gender.default())

                verificationSuccess.value = true
                eventLogger.otpSuccess()
                eventLogger.logUserInfo(
                    response.id.toString(),
                    fullName.value.default(),
                    otpArgs.value?.phone.default()
                )
                if (otpArgs.value?.user?.firstName.isNullOrEmpty())
                    eventLogger.onUserRegistration(true)
                else eventLogger.onUserLogin(true)
                actionDismissKeyboard.tryEmit(Unit)
                successCallback()
            },
            onError = {
                isOTPIncorrect.value = true
                errorMessage.value = it.message
                if (otpArgs.value?.user?.firstName.isNullOrEmpty())
                    eventLogger.onUserRegistration(false)
                else eventLogger.onUserLogin(false)
                eventLogger.otpFailure(message = it.message)
            }
        )
    }

    fun regenerateOtpWithSMS() {
        if (otpArgs.value.isNotNull()) {
            unZipViewStateFlow(
                resendOTPUseCase(otpArgs.value?.phone.default()),
                onSuccess = { authUiModel ->
                    val model = (authUiModel as AuthUiModel)
                    _otpArgs.value?.hash = model.hash
                    _otpArgs.value?.user = model.user
//                resendButtonState()
                }, onError = {}
            )
        }

    }

    //60 sec timer for resend
    fun resendButtonState(otpTimer: Long = 60000L) {
        // Cancel any existing timer
        cancelTimer()

        timerJob = viewModelScope.launch {
            val totalSeconds = otpTimer / 1000

            for (second in totalSeconds downTo 0) {
                val timeLeftFormatted = if (second < 10) "0$second" else "$second"

                _resendButtonState.value = Pair(
                    false,
                    "Resend OTP in $timeLeftFormatted" +
                            " Seconds"
                )

                if (second > 0) {
                    delay(1000) // Wait for 1 second
                }
            }

            // Timer finished
            _resendButtonState.value = Pair(
                true,
                "Resend OTP in"
            )
        }
    }

    fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun setOTPArgs(args: AuthUiModel) {
        _otpArgs.value = args
    }

    fun resetOTP() {
        OTP.value = EMPTY_STRING
        isOTPIncorrect.value = false
        errorMessage.value = EMPTY_STRING
    }
}