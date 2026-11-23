package com.localpk.app.features.search.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.search.data.TrendingSearchResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrendingSearchUseCase(
    private val appRepository: AppRepository,
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, ViewState<TrendingSearchResponse>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<ViewState<TrendingSearchResponse>> =
        flow {
            emit(ViewState.Loading)
            appRepository.trendingSearch().run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(data))
                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
} 