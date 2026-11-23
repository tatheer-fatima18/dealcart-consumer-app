package com.localpk.app.features.cart.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.cart.data.CartRequest
import com.localpk.app.features.cart.data.CartUiData
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchCartUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<CartRequest, ViewState<CartUiData>>(dispatcher) {

    override fun execute(parameters: CartRequest): Flow<ViewState<CartUiData>> = flow {
        emit(ViewState.Loading)
        appRepository.fetchCart(
            parameters.detailed
        ).run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data.body.mapCartUiData()))

                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }
}
