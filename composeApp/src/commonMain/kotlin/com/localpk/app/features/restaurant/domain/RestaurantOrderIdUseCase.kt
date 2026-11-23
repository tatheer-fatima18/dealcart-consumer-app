package com.localpk.app.features.restaurant.domain

import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.restaurant.api.FoodAppRepository
import com.localpk.app.features.restaurant.data.RestaurantOrderId
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RestaurantOrderIdUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: FoodAppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Unit, ViewState<List<RestaurantOrderId>>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<ViewState<List<RestaurantOrderId>>> =
        flow {
            emit(ViewState.Loading)
            appRepository.restaurantOrderInProgress().run {
                when (this) {
                    is ResponseResult.Success -> {
                        emit(
                            ViewState.Success(
                                data
                            )
                        )
                    }

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}