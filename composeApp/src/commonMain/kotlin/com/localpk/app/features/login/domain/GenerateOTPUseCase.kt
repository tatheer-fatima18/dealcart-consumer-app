package com.localpk.app.features.login.domain

import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.data.ViewError
import com.localpk.app.data.ViewErrorType
import com.localpk.app.features.login.api.AuthRepository
import com.localpk.app.features.login.data.AuthUiModel
import com.localpk.app.features.login.data.GenerateOtpRequest
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.StringsResourceManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GenerateOTPUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val authRepository: AuthRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val stringResourceManager: StringsResourceManager
) : FlowUseCase<String, ViewState<AuthUiModel>>(dispatcher) {
    override fun execute(parameters: String): Flow<ViewState<AuthUiModel>> =
        flow {
            val phoneNumberRegex = Regex("^03\\d{9}\$")

            val matcher = phoneNumberRegex.matches(parameters)

            if (parameters.length < 11 || matcher.not()) {
                emit(
                    ViewState.Error(
                        ViewError(
                            ViewErrorType.Toast,
                            ""
//                            stringResourceManager.getString(Res.string.invalid_phone),
                        )
                    )
                )
                return@flow
            }

            emit(ViewState.Loading)

            authRepository.generateOTP(GenerateOtpRequest(parameters)).run {
                when (this) {
                    is ResponseResult.Success -> {
                        val uiModel = AuthUiModel(parameters, data.body.hash, data.body.user)
                        emit(ViewState.Success(uiModel))
                    }

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}
