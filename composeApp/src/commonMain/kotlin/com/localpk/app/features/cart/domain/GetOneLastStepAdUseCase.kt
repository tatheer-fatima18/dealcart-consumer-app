package com.localpk.app.features.cart.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.cart.data.OneLastStepUiData
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOneLastStepAdUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, ViewState<List<OneLastStepUiData>>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<ViewState<List<OneLastStepUiData>>> = flow {
        emit(ViewState.Loading)
        appRepository.getOneLastStepAd().run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data.map { it.map() }))

                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }
}
