package com.localpk.app.features.promo.domain

import com.localpk.app.api.DolatAppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPromosUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: DolatAppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, ViewState<PromoResponse>>(dispatcher) {

    override fun execute(parameter: Unit): Flow<ViewState<PromoResponse>> = flow {
        emit(ViewState.Loading)
        appRepository.getPromos().run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data))
                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }
}
