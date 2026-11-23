package com.localpk.app.features.cart.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper

import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DecreaseItemQuantityUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Long, ViewState<Unit>>(dispatcher) {

    override fun execute(parameters: Long): Flow<ViewState<Unit>> = flow {
        emit(ViewState.Loading)

        appRepository.decreaseCartItemQuantity(parameters).run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(Unit))

                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }

}