package com.localpk.app.features.orders.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.orders.data.OrderUiData

import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.orEmptyArrayList

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



class CurrentOrdersUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Unit, ViewState<List<OrderUiData>>>(dispatcher) {
    override fun execute(parameters: Unit): Flow<ViewState<List<OrderUiData>>> = flow {
        emit(ViewState.Loading)
        appRepository.getCurrentOrders().run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data.body.results?.map { it.map() }.orEmptyArrayList()))
                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }

    }
}