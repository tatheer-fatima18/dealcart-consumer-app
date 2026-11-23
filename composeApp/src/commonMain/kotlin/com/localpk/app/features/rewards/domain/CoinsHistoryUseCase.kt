package com.localpk.app.features.rewards.domain

import com.localpk.app.api.DolatAppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.restaurant.domain.RequestData
import com.localpk.app.features.rewards.data.CoinsHistory
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.default
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CoinsHistoryUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: DolatAppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<RequestData, ViewState<CoinsHistory>>(dispatcher) {

    override fun execute(parameters: RequestData): Flow<ViewState<CoinsHistory>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getUserCoinsHistory(
                parameters.limit,
                parameters.page,
                parameters.source.default()
            ).run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(data.body.data))

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}