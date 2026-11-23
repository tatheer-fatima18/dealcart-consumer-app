package com.localpk.app.features.cricket_live.domain

import com.localpk.app.api.SportAppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.cricket_live.data.PointsTableUiData
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class PointsTableUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: SportAppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, ViewState<PointsTableUiData>>(dispatcher) {
    override fun execute(parameters: Unit): Flow<ViewState<PointsTableUiData>> = flow {
        emit(ViewState.Loading)
        appRepository.getPointsTableData().run {
            when (this) {
                is ResponseResult.Success ->
                    data.body?.let { ViewState.Success(it.map()) }?.let { emit(it) }

                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }
}