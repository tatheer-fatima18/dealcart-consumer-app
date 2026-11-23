package com.localpk.app.base

import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    operator fun invoke(parameters: P): Flow<R> = execute(parameters)
        .flowOn(coroutineDispatcher)

    protected abstract fun execute(parameters: P): Flow<R>

    protected fun handleResponse(
        response: ResponseResult<R>,
        onSuccess: (R) -> Unit = {},
        onError: (String) -> Unit = {}
    ): ViewState<R> {
        return when (response) {
            is ResponseResult.Success -> {
                onSuccess(response.data)
                ViewState.Success(response.data)
            }
            is ResponseResult.Failure -> {
                val errorMessage = response.error.getDisplayMessage()
                onError(errorMessage)
                ViewState.Error(
                    com.localpk.app.data.ViewError(
                        message = errorMessage,
                        code = response.error.statusCode ?: 0,
                        errorCode = response.error.errorCode ?: 0,
                        userBlocked = response.error.userBlocked ?: false
                    )
                )
            }
            is ResponseResult.NetworkError -> {
                val errorMessage = "Network error. Please check your connection."
                onError(errorMessage)
                ViewState.Error(
                    com.localpk.app.data.ViewError(
                        message = errorMessage
                    )
                )
            }
        }
    }
}

abstract class SimpleFlowUseCase<R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<R> = execute()
        .flowOn(coroutineDispatcher)

    protected abstract fun execute(): Flow<R>
}

abstract class SuspendUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(parameters: P): ResponseResult<R> {
        return try {
            val result = execute(parameters)
            ResponseResult.Success(result)
        } catch (e: Exception) {
            ResponseResult.Failure(
                com.localpk.app.network.ErrorResponse(
                    message = e.message ?: "An error occurred"
                )
            )
        }
    }

    protected abstract suspend fun execute(parameters: P): R
} 