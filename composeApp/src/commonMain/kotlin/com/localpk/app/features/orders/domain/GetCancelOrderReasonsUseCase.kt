package com.localpk.app.features.orders.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.orders.data.CancelOrderReason
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetCancelOrderReasonsUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Unit, ViewState<List<CancelOrderReason>>>(dispatcher) {
    override fun execute(parameters: Unit): Flow<ViewState<List<CancelOrderReason>>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getCancelOrderReasons().run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(convertToCancelOrderReasonList(data.body)))
                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}

fun convertToCancelOrderReasonList(stringList: List<String>): List<CancelOrderReason> {
    return stringList.map { CancelOrderReason(it) }
}