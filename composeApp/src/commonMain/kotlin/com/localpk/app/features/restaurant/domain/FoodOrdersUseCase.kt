package com.localpk.app.features.restaurant.domain

import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.restaurant.api.FoodAppRepository
import com.localpk.app.features.restaurant.data.FoodOrderUIData
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

@Serializable
data class RequestData(
    val limit: Int,
    val page: Int,
    val source: String? = null
)

class FoodOrdersUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: FoodAppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<RequestData, ViewState<List<FoodOrderUIData>>>(dispatcher) {
    override fun execute(parameters: RequestData): Flow<ViewState<List<FoodOrderUIData>>> = flow {
        emit(ViewState.Loading)
        appRepository.getFoodOrders(page = parameters.page, limit = parameters.limit).run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data.results?.map { it.map() }.orEmptyArrayList()))

                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }

    }
}