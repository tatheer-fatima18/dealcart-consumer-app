package com.localpk.app.base.domain

import com.localpk.app.base.ViewState
import com.localpk.app.data.ViewError
import com.localpk.app.network.ResponseResult

interface NetworkErrorBuilder {
    fun getDefaultError(): String
    fun buildNetworkViewError(): ViewError
}

interface ErrorResponseViewStateMapper {
    fun map(from: ResponseResult<Any>): ViewState.Error
}

interface SocialSharingPlatform {
    fun shareToWhatsApp(message: String, link: String)
    fun shareToFacebook(message: String, link: String)
    fun shareToMessenger(message: String, link: String)
    fun shareToSMS(message: String, link: String)
    fun shareToTwitter(message: String, link: String)
    fun shareToGeneral(message: String, link: String)
}

interface ExternalLinkHandler {
    fun openExternalLink(url: String)
} 