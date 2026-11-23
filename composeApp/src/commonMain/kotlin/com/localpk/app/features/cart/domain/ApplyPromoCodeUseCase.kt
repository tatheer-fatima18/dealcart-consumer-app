package com.localpk.app.features.cart.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.cart.data.PromoCodeRequest
import com.localpk.app.network.GeneralResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApplyPromoCodeUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<PromoCodeRequest, ViewState<GeneralResponse>>(dispatcher) {

    override fun execute(parameters: PromoCodeRequest): Flow<ViewState<GeneralResponse>> = flow {
        emit(ViewState.Loading)
        appRepository.applyPromoCode(parameters).run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data))

                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }
}
