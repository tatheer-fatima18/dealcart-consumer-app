package com.localpk.app.features.cart.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.restaurant.api.FoodAppRepository
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ClearCartUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    private val foodAppRepository: FoodAppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Boolean, ViewState<Unit>>(dispatcher) {

    override fun execute(parameters: Boolean): Flow<ViewState<Unit>> = flow {
        emit(ViewState.Loading)
        val result = if (parameters) foodAppRepository.clearCart() else appRepository.clearCart()
        emit(handleCartClearResult(result))
    }

    private fun handleCartClearResult(result: ResponseResult<Unit>): ViewState<Unit> =
        when (result) {
            is ResponseResult.Success -> ViewState.Success(result.data)
            else -> errorResponseViewStateMapper.map(result)
        }
}