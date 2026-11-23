package com.localpk.app.features.rewards.viewModel

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.personal.data.ProfileData
import com.localpk.app.features.personal.domain.ProfileDetailUseCase
import com.localpk.app.features.profile.data.CustomerSavingDetailData
import com.localpk.app.features.profile.data.CustomerSavingDetailUseCase
import com.localpk.app.features.rewards.data.Milestones
import com.localpk.app.features.rewards.data.RewardsMilestoneResponse
import com.localpk.app.features.rewards.domain.RewardsMilestoneUseCase
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.default
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent

class RewardsViewModel(
    private val rewardsMilestoneUseCase: RewardsMilestoneUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val customerSavingDetailUseCase: CustomerSavingDetailUseCase,
    private val personalDetailUseCase: ProfileDetailUseCase
) : BaseViewModel<Any>(), KoinComponent {

    private val _milestonesData = MutableStateFlow<Milestones?>(null)
    val milestonesData: StateFlow<Milestones?> = _milestonesData

    private val _layoutVisibility = MutableStateFlow(true)
    val layoutVisibility: StateFlow<Boolean> = _layoutVisibility

    private val _isCommunityLeader = MutableStateFlow(false)
    val isCommunityLeader: StateFlow<Boolean> = _isCommunityLeader

    private val _customerSavingDetail = MutableStateFlow<CustomerSavingDetailData?>(null)
    val customerSavingDetail: StateFlow<CustomerSavingDetailData?> = _customerSavingDetail

    private val _gender = MutableStateFlow("male")
    val gender: StateFlow<String> = _gender

    init {
        if (!sharedPreferencesManager.isLoggedIn()) {
            _layoutVisibility.value = true
        } else {
            _layoutVisibility.value = true
            fetchRewardsMilestones()
            fetchCustomerDetails()
            getPersonalDetails()
        }
        //    eventLogger.onLoadRewardsScreen()
    }

    private fun fetchRewardsMilestones() {
        unZipViewStateFlow(rewardsMilestoneUseCase(Unit), onSuccess = {
            val response = it as RewardsMilestoneResponse
            if (response.success) {
                _milestonesData.value = response.body
            }
        }, onError = { error ->
           // showDcSuccess(error)
        })
    }

    fun onActionShareAndWin() {
       // eventLogger.ShareFromRewards()
        // Navigation logic for share and win can be implemented here if needed
    }

    fun onActionBack() {
        // Navigation logic for back action can be implemented here if needed
    }

    fun onHowItWorksClick(id: String = "1mzds6a") {
      //  eventLogger.HowItWorksFromRewards()
        // InAppStoryManager logic can be implemented here if needed
    }

    fun navigateToLogin() {
        // Navigation logic for login can be implemented here if needed
    }

  //  fun logRewardsClickEvent() = eventLogger.Rewards()

    private fun fetchCustomerDetails() {
        unZipViewStateFlow(customerSavingDetailUseCase(Unit), onSuccess = {
            val response = it as? CustomerSavingDetailData
            if (response != null) {
                _customerSavingDetail.value = response
            }
        })
    }

    private fun getPersonalDetails() {
        unZipViewStateFlow(
            personalDetailUseCase(Unit),
            onSuccess = { res ->
                val profileData = res as ProfileData
                profileData.profileDetails?.let { data ->
                    _gender.value = data.gender.default("male")
                }
            }
        )
    }
} 