package com.localpk.app.features.rewards.domain

import com.localpk.app.api.DolatAppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.rewards.data.MilestoneRewardsInfoResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MilestoneRewardsInfoUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: DolatAppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, ViewState<MilestoneRewardsInfoResponse>>(dispatcher) {
    override fun execute(parameters: Unit): Flow<ViewState<MilestoneRewardsInfoResponse>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getMilestoneRewardsInfo().run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(data))

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}