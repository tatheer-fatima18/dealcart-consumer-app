package com.localpk.app.base.domain

import com.localpk.app.data.ViewError
import com.localpk.app.data.ViewErrorType
import android.content.Intent
import android.net.Uri
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.net.toUri

class AndroidNetworkErrorBuilder : NetworkErrorBuilder {

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

class SocialSharingPlatformAndroid(private val context: Context) : SocialSharingPlatform {
    
    override fun shareToWhatsApp(message: String, link: String) {
        val fullMessage = "$message $link"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            setPackage("com.whatsapp")
            putExtra(Intent.EXTRA_TEXT, fullMessage)
        }
        
        if (isAppInstalled("com.whatsapp")) {
            context.startActivity(intent)
        } else {
            // Fallback to general share
            shareToGeneral(message, link)
        }
    }
    
    override fun shareToFacebook(message: String, link: String) {
        val fullMessage = "$message $link"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            setPackage("com.facebook.katana")
            putExtra(Intent.EXTRA_TEXT, fullMessage)
        }
        
        if (isAppInstalled("com.facebook.katana")) {
            context.startActivity(intent)
        } else {
            shareToGeneral(message, link)
        }
    }
    
    override fun shareToMessenger(message: String, link: String) {
        val fullMessage = "$message $link"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            setPackage("com.facebook.orca")
            putExtra(Intent.EXTRA_TEXT, fullMessage)
        }
        
        if (isAppInstalled("com.facebook.orca")) {
            context.startActivity(intent)
        } else {
            shareToGeneral(message, link)
        }
    }
    
    override fun shareToSMS(message: String, link: String) {
        val fullMessage = "$message $link"
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:")
            putExtra("sms_body", fullMessage)
        }
        
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            shareToGeneral(message, link)
        }
    }
    
    override fun shareToTwitter(message: String, link: String) {
        val fullMessage = "$message $link"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            setPackage("com.twitter.android")
            putExtra(Intent.EXTRA_TEXT, fullMessage)
        }
        
        if (isAppInstalled("com.twitter.android")) {
            context.startActivity(intent)
        } else {
            shareToGeneral(message, link)
        }
    }
    
    override fun shareToGeneral(message: String, link: String) {
        val fullMessage = "$message $link"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, fullMessage)
        }
        
        val chooser = Intent.createChooser(intent, "Share via")
        context.startActivity(chooser)
    }
    
    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}

class ExternalLinkHandlerAndroid(private val context: Context) : ExternalLinkHandler {
    override fun openExternalLink(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the case where no app can handle the intent
        }
    }
}