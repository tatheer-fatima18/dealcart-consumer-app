package com.localpk.app.base.domain

import com.localpk.app.data.ViewError
import com.localpk.app.data.ViewErrorType
import com.localpk.app.base.ViewState
import com.localpk.app.network.ResponseResult

class DefaultErrorResponseViewStateMapper(
    private val networkErrorBuilder: NetworkErrorBuilder
) : ErrorResponseViewStateMapper {

    override fun map(from: ResponseResult<Any>): ViewState.Error {
        return when (from) {
            is ResponseResult.Failure ->
                ViewState.Error(
                    ViewError(
                        ViewErrorType.Toast,
                        message = from.error.message ?: from.error.error
                        ?: networkErrorBuilder.getDefaultError(),
                        code = from.error.statusCode ?: 0,
                        errorCode = from.error.errorCode ?: 0,
                        userBlocked = from.error.userBlocked ?: false
                    )
                )
            is ResponseResult.NetworkError ->
                ViewState.Error(networkErrorBuilder.buildNetworkViewError())
            else -> error("Invalid response type for error mapping")
        }
    }
} 