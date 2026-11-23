package com.localpk.app.features.recommendation_engine.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.recommendation_engine.data.RecommendationEngineRequest
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecommendationEngineUseCase(
    private val appRepository: AppRepository,
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<RecommendationEngineRequest, ViewState<List<String>>>(dispatcher) {

    override fun execute(parameters: RecommendationEngineRequest): Flow<ViewState<List<String>>> =
        flow {
            emit(ViewState.Loading)

            appRepository.getRecommendedProductIds(parameters.swimlane, parameters.screenType).run {
                when (this) {
                    is ResponseResult.Success -> {
                        emit(ViewState.Success(data.body.results?.items.orEmptyArrayList()))
                    }

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }

        }

}