package com.localpk.app.features.wallet.domain

import com.localpk.app.api.DolatAppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.wallet.data.WalletBalanceResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WalletBalanceUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: DolatAppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<String, ViewState<WalletBalanceResponse>>(dispatcher) {

    override fun execute(parameters: String): Flow<ViewState<WalletBalanceResponse>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getWalletBalance(parameters).run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(data))
                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}