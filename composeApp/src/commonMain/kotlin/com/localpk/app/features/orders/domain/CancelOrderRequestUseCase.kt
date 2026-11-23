package com.localpk.app.features.orders.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase

import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.orders.data.CancelOrderReasonRequest
import com.localpk.app.network.GeneralResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



class CancelOrderRequestUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<CancelOrderReasonRequest, ViewState<GeneralResponse>>(dispatcher) {

    override fun execute(parameters: CancelOrderReasonRequest): Flow<ViewState<GeneralResponse>> = flow {
        emit(ViewState.Loading)
        appRepository.cancelOrder(parameters).run {
            when (this) {
                is ResponseResult.Success -> emit(ViewState.Success(data))
                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }
}