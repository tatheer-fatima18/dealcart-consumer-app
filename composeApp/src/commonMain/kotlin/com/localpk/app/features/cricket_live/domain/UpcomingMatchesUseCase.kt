package com.localpk.app.features.cricket_live.domain

import com.localpk.app.api.SportAppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.cricket_live.data.UpcomingMatchesUiData
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpcomingMatchesUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: SportAppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, ViewState<List<UpcomingMatchesUiData>>>(dispatcher) {
    override fun execute(parameters: Unit): Flow<ViewState<List<UpcomingMatchesUiData>>> = flow {
        emit(ViewState.Loading)
        appRepository.getUpcomingMatches().run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data.body?.map { it.map() }.orEmptyArrayList()))

                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }

    }
}