package com.localpk.app.features.recommendation_engine.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.recommendation_engine.data.SearchPastOrderResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecommendationEngineSearchUseCase(
    private val appRepository: AppRepository,
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, ViewState<SearchPastOrderResponse>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<ViewState<SearchPastOrderResponse>> = flow {
        emit(ViewState.Loading)

        appRepository.searchPastOrderItems().run {
            when (this) {
                is ResponseResult.Success -> {
                    emit(ViewState.Success(data))
                }

                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }
}
