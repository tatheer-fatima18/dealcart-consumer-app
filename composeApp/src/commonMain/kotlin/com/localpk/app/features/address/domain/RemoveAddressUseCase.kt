package com.localpk.app.features.address.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.network.GeneralResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoveAddressUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Long, ViewState<GeneralResponse>>(dispatcher) {

    override fun execute(parameter: Long): Flow<ViewState<GeneralResponse>> = flow {
        emit(ViewState.Loading)
        appRepository.removeAddress(parameter).run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data))
                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }
}