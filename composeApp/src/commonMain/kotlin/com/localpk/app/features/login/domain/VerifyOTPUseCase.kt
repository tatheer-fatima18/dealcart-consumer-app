package com.localpk.app.features.login.domain

import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.data.ViewError
import com.localpk.app.data.ViewErrorType
import com.localpk.app.features.login.api.AuthRepository
import com.localpk.app.features.login.data.AuthResponse
import com.localpk.app.features.login.data.VerifyOTPUseCaseParams
import com.localpk.app.features.login.data.VerifyOtpRequest
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.default
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VerifyOTPUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val authRepository: AuthRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<VerifyOTPUseCaseParams, ViewState<AuthResponse>>(dispatcher) {

    override fun execute(parameters: VerifyOTPUseCaseParams): Flow<ViewState<AuthResponse>> =
        flow {
            val phoneNumberRegex = Regex("^03\\d{9}\$")
//            val alphabetsAndSpacesRegex = Pattern.compile("^[a-zA-Z ]*\$")
            val matcher = phoneNumberRegex.matches(parameters.phone)
//            val firstNameMatcher = parameters.firstName?.let { alphabetsAndSpacesRegex.matcher(it) }
//            val lastNameMatcher = parameters.lastName?.let { alphabetsAndSpacesRegex.matcher(it) }
//            val isNameInvalid = firstNameMatcher?.find()?.not() == true || lastNameMatcher?.find()?.not() == true

            if (parameters.phone.length < 11 || matcher.not()) {
                emit(
                    ViewState.Error(
                        ViewError(
                            ViewErrorType.Toast,
                            "Invalid phone number"
                        )
                    )
                )
                return@flow
            }

//                if (isNameInvalid) {
//                    emit(
//                        ViewState.Error(
//                            ViewError(
//                                Toast,
//                                stringsResourceManager.getString(string.invalid_name)
//                            )
//                        )
//                    )
//                    return@flow
//                }

            emit(ViewState.Loading)

            authRepository.verifyOTP(
                VerifyOtpRequest(
                    parameters.firstName.default(),
                    parameters.lastName.default(),
                    parameters.hash,
                    parameters.phone,
                    parameters.otp,
                    parameters.fcm,
                    parameters.referrerCode
                )
            ).run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(data))

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}