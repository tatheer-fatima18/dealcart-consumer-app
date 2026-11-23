package com.localpk.app.features.cricket_live.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.cricket_live.data.PoweredByDetailUiData
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PoweredByUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<String, ViewState<List<PoweredByDetailUiData>>>(dispatcher) {

    override fun execute(parameters: String): Flow<ViewState<List<PoweredByDetailUiData>>> = flow {
        emit(ViewState.Loading)
        appRepository.getPoweredByBanners(parameters).run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data.body.results?.map { it.map() }.orEmptyArrayList()))

                else -> emit(errorResponseViewStateMapper.map(this as ResponseResult<Any>))
            }
        }
    }
}