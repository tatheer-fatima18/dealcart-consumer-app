package com.localpk.app.features.dynamicLinksService.presentation

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.dynamicLinksService.data.DynamicLinkRequest
import com.localpk.app.features.dynamicLinksService.data.DynamicLinkResponse
import com.localpk.app.features.dynamicLinksService.domain.DynamicLinkUseCase
import com.localpk.app.utils.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.localpk.app.utils.SharedPreferencesManager

class DynamicLinkViewModel(
    private val dynamicLinkUseCase: DynamicLinkUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager
) :
    BaseViewModel<Any>() {

    private val _dynamicLink = MutableStateFlow(EMPTY_STRING)
    val dynamicLink: StateFlow<String> = _dynamicLink.asStateFlow()


    fun createDynamicLink() {
        if (sharedPreferencesManager.getReferralLink().trim().isEmpty()) {
            val requestBody = DynamicLinkRequest(sharedPreferencesManager.getMyReferralCode())
            unZipViewStateFlow(
                dynamicLinkUseCase(requestBody),
                onSuccess = {
                    val response = it as DynamicLinkResponse
                    sharedPreferencesManager.setReferralLink(response.body.shortLink)
                    _dynamicLink.value = response.body.shortLink
                },
                onError = {}
            )
        } else {
            _dynamicLink.value = sharedPreferencesManager.getReferralLink()
        }
    }
}