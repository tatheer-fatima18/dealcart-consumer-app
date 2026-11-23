package com.localpk.app.features.cricket_live.domain

import com.localpk.app.api.SportAppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.cricket_live.data.ScoreCardResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ScoreCardUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: SportAppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<String, ViewState<ScoreCardResponse>>(dispatcher) {
    override fun execute(parameters: String): Flow<ViewState<ScoreCardResponse>> = flow {
        emit(ViewState.Loading)
        appRepository.getMatchScoreCard(parameters).run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data))
                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }

    }
}