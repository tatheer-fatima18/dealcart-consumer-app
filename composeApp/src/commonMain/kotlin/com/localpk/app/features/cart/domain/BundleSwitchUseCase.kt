package com.localpk.app.features.cart.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.network.GeneralStringResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BundleSwitchUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, ViewState<GeneralStringResponse>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<ViewState<GeneralStringResponse>> = flow {
        emit(ViewState.Loading)

        appRepository.bundleSwitch().run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data))

                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }

}