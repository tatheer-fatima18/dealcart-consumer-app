package com.localpk.app.features.restaurant.domain

import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.restaurant.api.FoodAppRepository
import com.localpk.app.features.restaurant.data.OrderTracking
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FoodOrderStatusUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: FoodAppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Long, ViewState<OrderTracking>>(dispatcher) {
    override fun execute(parameters: Long): Flow<ViewState<OrderTracking>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getTrackingStatus(parameters).run {
                when (this) {
                    is ResponseResult.Success -> {
                        emit(ViewState.Success(data))
                    }

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}
