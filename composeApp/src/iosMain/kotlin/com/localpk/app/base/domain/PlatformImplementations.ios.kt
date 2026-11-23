package com.localpk.app.base.domain

import com.localpk.app.data.ViewError
import com.localpk.app.data.ViewErrorType

import platform.UIKit.UIApplication
import platform.Foundation.NSURL

class IosNetworkErrorBuilder : NetworkErrorBuilder {

    override fun getDefaultError(): String {
        return "An unexpected error occurred"
    }

    override fun buildNetworkViewError(): ViewError {
        return ViewError(
            type = ViewErrorType.Toast,
            message = "Network error occurred. Please check your connection.",
            isRetryable = true
        )
    }
}

class SocialSharingPlatformIOS : SocialSharingPlatform {
    
    override fun shareToWhatsApp(message: String, link: String) {
        val fullMessage = "$message $link"
        val whatsappURL = "whatsapp://send?text=${fullMessage.encodeURL()}"
        
        if (canOpenURL(whatsappURL)) {
            openURL(whatsappURL)
        } else {
            shareToGeneral(message, link)
        }
    }
    
    override fun shareToFacebook(message: String, link: String) {
        val fullMessage = "$message $link"
        val facebookURL = "fb://share?link=${link.encodeURL()}&quote=${message.encodeURL()}"
        
        if (canOpenURL(facebookURL)) {
            openURL(facebookURL)
        } else {
            shareToGeneral(message, link)
        }
    }
    
    override fun shareToMessenger(message: String, link: String) {
        val fullMessage = "$message $link"
        val messengerURL = "fb-messenger://share?link=${link.encodeURL()}&app_id=YOUR_FB_APP_ID"
        
        if (canOpenURL(messengerURL)) {
            openURL(messengerURL)
        } else {
            shareToGeneral(message, link)
        }
    }
    
    override fun shareToSMS(message: String, link: String) {
        val fullMessage = "$message $link"
        val smsURL = "sms:&body=${fullMessage.encodeURL()}"
        
        if (canOpenURL(smsURL)) {
            openURL(smsURL)
        } else {
            shareToGeneral(message, link)
        }
    }
    
    override fun shareToTwitter(message: String, link: String) {
        val fullMessage = "$message $link"
        val twitterURL = "twitter://post?message=${fullMessage.encodeURL()}"
        
        if (canOpenURL(twitterURL)) {
            openURL(twitterURL)
        } else {
            shareToGeneral(message, link)
        }
    }
    
    override fun shareToGeneral(message: String, link: String) {
        val fullMessage = "$message $link"
        // For general sharing, we'll use the system share URL scheme
        // This will open Safari with the content, allowing users to share from there
        val shareURL = "https://www.google.com/search?q=${fullMessage.encodeURL()}"
        openURL(shareURL)
    }
    
    private fun canOpenURL(urlString: String): Boolean {
        val url = NSURL.URLWithString(urlString)
        return url?.let { UIApplication.sharedApplication.canOpenURL(it) } ?: false
    }
    
    private fun openURL(urlString: String) {
        val url = NSURL.URLWithString(urlString)
        url?.let { UIApplication.sharedApplication.openURL(it) }
    }
    
    private fun String.encodeURL(): String {
        // For now, use a simple URL encoding approach
        // In a real implementation, you might want to use a proper URL encoding library
        return this.replace(" ", "%20")
            .replace("&", "%26")
            .replace("=", "%3D")
            .replace("?", "%3F")
            .replace("#", "%23")
            .replace("+", "%2B")
    }
}

class ExternalLinkHandlerIOS : ExternalLinkHandler {
    override fun openExternalLink(url: String) {
        try {
            val nsUrl = NSURL.URLWithString(url)
            if (nsUrl != null) {
                UIApplication.sharedApplication.openURL(
                    url = nsUrl,
                    options = emptyMap<Any?, Any>(),
                    completionHandler = {}
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}