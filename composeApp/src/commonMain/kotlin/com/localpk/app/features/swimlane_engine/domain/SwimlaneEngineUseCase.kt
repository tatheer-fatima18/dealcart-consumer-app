package com.localpk.app.features.swimlane_engine.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.swimlane_engine.data.SwimlaneRequestParams
import com.localpk.app.features.swimlane_engine.data.SwimlaneUI
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SwimlaneEngineUseCase(
    private val appRepository: AppRepository,
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<SwimlaneRequestParams, ViewState<List<SwimlaneUI>>>(dispatcher) {

    override fun execute(parameters: SwimlaneRequestParams): Flow<ViewState<List<SwimlaneUI>>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getSwimlaneEngine(
                parameters.landingPageType,
                parameters.userType,
                parameters.superParentCategoryId
            ).run {
                when (this) {
                    is ResponseResult.Success -> {
                        emit(
                            ViewState.Success(
                                data.body.map { it.map() }.orEmptyArrayList()
                            )
                        )
                    }

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
} 