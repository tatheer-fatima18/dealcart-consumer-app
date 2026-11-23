package com.localpk.app.features.rewards.domain

import com.localpk.app.api.DolatAppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.rewards.data.RewardsMilestoneResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class RewardsMilestoneUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: DolatAppRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Unit, ViewState<RewardsMilestoneResponse>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<ViewState<RewardsMilestoneResponse>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getRewardsMilestones().run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(data))
                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}