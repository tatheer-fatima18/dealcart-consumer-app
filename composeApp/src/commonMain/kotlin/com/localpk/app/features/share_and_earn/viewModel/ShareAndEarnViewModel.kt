package com.localpk.app.features.share_and_earn.viewModel

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.rewards.data.MilestoneRewardsInfo
import com.localpk.app.features.rewards.data.MilestoneRewardsInfoResponse
import com.localpk.app.features.rewards.domain.MilestoneRewardsInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent

class ShareAndEarnViewModel(
    private val milestoneRewardsInfoUseCase: MilestoneRewardsInfoUseCase
) : BaseViewModel<Any>(), KoinComponent {

    private val _milestoneInfoStateFlow = MutableStateFlow<MilestoneRewardsInfo?>(null)
    val milestoneInfoStateFlow: StateFlow<MilestoneRewardsInfo?> = _milestoneInfoStateFlow

    init {
        getMileStoneInfo()
    }

    fun getMileStoneInfo() {
        unZipViewStateFlow(
            milestoneRewardsInfoUseCase(Unit),
            onSuccess = {
                val response = it as MilestoneRewardsInfoResponse
                _milestoneInfoStateFlow.value = response.body
            }
        )
    }
} 