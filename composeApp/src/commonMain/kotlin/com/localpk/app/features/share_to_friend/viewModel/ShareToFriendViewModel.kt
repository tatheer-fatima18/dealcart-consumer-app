package com.localpk.app.features.share_to_friend.viewModel

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.share_to_friend.data.CreateAppLinkRequest
import com.localpk.app.features.share_to_friend.data.LinkBody
import com.localpk.app.features.share_to_friend.data.LinkResponse
import com.localpk.app.features.share_to_friend.domain.AppLinkUseCase
import com.localpk.app.utils.SharedPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent

class ShareToFriendViewModel(
    private val appLinkUseCase: AppLinkUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel<Any>(), KoinComponent {

    private val _appLinkData = MutableStateFlow<LinkBody?>(null)
    val appLinkData: StateFlow<LinkBody?> = _appLinkData

    var gender: StateFlow<String> = MutableStateFlow("male")

    init {
        createLink()
    }

    fun createLink() {
        val request = CreateAppLinkRequest(
            userId = sharedPreferencesManager.getUserId(),
            platform = "firebase"
        )
        unZipViewStateFlow(appLinkUseCase(request), onSuccess = {
            val response = it as LinkResponse
            if (response.success) {
                _appLinkData.value = response.body
            }
        })
    }
} 