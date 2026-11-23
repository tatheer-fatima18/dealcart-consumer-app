package com.localpk.app.features.orders.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.orders.data.OrderDetailModel
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class OrderDetailUseCase (
    private val appRepository: AppRepository,
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Long, ViewState<OrderDetailModel>>(dispatcher) {

    override fun execute(parameters: Long): Flow<ViewState<OrderDetailModel>> =
        flow {

            emit(ViewState.Loading)

            appRepository.getOrderDetail(parameters).run {
                when (this) {
                    is ResponseResult.Success -> emit(ViewState.Success(data.body))
                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}