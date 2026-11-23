package com.localpk.app.features.orders.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.orders.data.PastOrderUiData
import com.localpk.app.features.orders.data.PastOrdersRequest
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PastOrdersUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<PastOrdersRequest, ViewState<List<PastOrderUiData>>>(dispatcher) {
    override fun execute(parameters: PastOrdersRequest): Flow<ViewState<List<PastOrderUiData>>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getPastOrders(parameters.orderStatus, parameters.month).run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(data.body?.map { it.map() }.orEmptyArrayList()))
                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}