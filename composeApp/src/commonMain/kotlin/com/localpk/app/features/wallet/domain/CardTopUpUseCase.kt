package com.localpk.app.features.wallet.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.cart.data.CardPaymentDetails
import com.localpk.app.features.wallet.data.CardTopUpRequest
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.default
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CardTopUpUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<CardTopUpRequest, ViewState<CardPaymentDetails>>(dispatcher) {

    override fun execute(parameters: CardTopUpRequest): Flow<ViewState<CardPaymentDetails>> =
        flow {
            emit(ViewState.Loading)
            appRepository.cardTopUpRequest(parameters.amount, parameters.paymentType.default())
                .run {
                    when (this) {
                        is ResponseResult.Success ->
                            emit(ViewState.Success(data))

                        else -> emit(errorResponseViewStateMapper.map(this))
                    }
                }
        }
}