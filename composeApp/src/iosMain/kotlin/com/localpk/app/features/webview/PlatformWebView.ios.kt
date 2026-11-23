package com.localpk.app.features.webview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.WebKit.WKPreferences
import platform.WebKit.WKWebsiteDataStore
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.CoreGraphics.CGRectZero

// Singleton holder for persistent WKWebView
object WebViewHolder {
    var chatWebView: WKWebView? = null
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformWebView(
    modifier: Modifier,
    url: String,
    htmlContent: String?,
    isWebChat: Boolean?,
    cookies: Map<String, String>?
) {
    val webView = remember {
        if (isWebChat == true) {
            // ✅ Persist chat WKWebView
            WebViewHolder.chatWebView ?: run {
                createWKWebView(url, htmlContent, isWebChat)
                    .also { WebViewHolder.chatWebView = it }
            }
        } else {
            createWKWebView(url, htmlContent, isWebChat)
        }
    }

    UIKitView(
        modifier = modifier,
        factory = { webView },
        update = { view ->
            // Don’t reload if already loaded
            if (view.URL == null) {
                if (!htmlContent.isNullOrBlank()) {
                    view.loadHTMLString(htmlContent, baseURL = NSURL.URLWithString(url))
                } else {
                    val nsUrl = NSURL.URLWithString(url)
                    if (nsUrl != null) {
                        val request = NSURLRequest(nsUrl)
                        view.loadRequest(request)
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createWKWebView(
    url: String,
    htmlContent: String?,
    isWebChat: Boolean?
): WKWebView {
    val config = WKWebViewConfiguration().apply {
        preferences = WKPreferences().apply {
            setJavaScriptCanOpenWindowsAutomatically(true)
        }
        allowsInlineMediaPlayback = true
        mediaTypesRequiringUserActionForPlayback = 0uL

        if (isWebChat == true) {
            websiteDataStore = WKWebsiteDataStore.defaultDataStore()
            allowsAirPlayForMediaPlayback = true
            allowsPictureInPictureMediaPlayback = true
            suppressesIncrementalRendering = false
        }
    }

    return WKWebView(frame = CGRectZero.readValue(), configuration = config).apply {
        if (!htmlContent.isNullOrBlank()) {
            loadHTMLString(htmlContent, baseURL = NSURL.URLWithString(url))
        } else {
            val nsUrl = NSURL.URLWithString(url)
            if (nsUrl != null) {
                val request = NSURLRequest(nsUrl)
                loadRequest(request)
            }
        }
    }
}