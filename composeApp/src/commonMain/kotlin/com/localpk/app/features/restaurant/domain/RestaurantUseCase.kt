package com.localpk.app.features.restaurant.domain

import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.restaurant.api.FoodAppRepository
import com.localpk.app.features.restaurant.data.GetRestaurantsResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

@Serializable
data class RestaurantParameters(
    val query: String,
    val cuisines: List<String>? = null
)

class RestaurantUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: FoodAppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<RestaurantParameters, ViewState<GetRestaurantsResponse>>(dispatcher) {

    override fun execute(parameters: RestaurantParameters): Flow<ViewState<GetRestaurantsResponse>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getRestaurants(
                query = parameters.query,
                cuisines = parameters.cuisines
            ).run {
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