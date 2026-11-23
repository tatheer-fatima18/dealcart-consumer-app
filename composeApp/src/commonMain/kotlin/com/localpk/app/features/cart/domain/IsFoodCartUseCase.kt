package com.localpk.app.features.cart.domain

import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.restaurant.api.FoodAppRepository
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IsFoodCartUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: FoodAppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, ViewState<Boolean>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<ViewState<Boolean>> =
        flow {
            emit(ViewState.Loading)
            appRepository.isFoodCart().run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(data))

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}