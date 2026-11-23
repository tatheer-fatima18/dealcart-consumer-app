package com.localpk.app.features.webview

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.localpk.app.base.PlatformContext
import com.localpk.app.utils.CONTACTS_PERMISSION_CODE

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun PlatformWebView(
    modifier: Modifier,
    url: String,
    htmlContent: String?,
    isWebChat: Boolean?,
    cookies: Map<String, String>? // cookieName to cookieValue
) {
    var filePathCallbackVar: ValueCallback<Array<Uri>>? = null
    val activity = LocalActivity.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var webView: WebView? by remember { mutableStateOf(null) }

    val multipleFileChooserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            filePathCallbackVar?.onReceiveValue(uris.toTypedArray())
            filePathCallbackVar = null
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    webView?.evaluateJavascript("pauseGame();", null)
                }

                Lifecycle.Event.ON_RESUME -> {
                    webView?.evaluateJavascript("resumeGame();", null)
                }

                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            webView?.apply {
                loadUrl("about:blank") // stop loading anything
                clearHistory()
                removeAllViews()
                destroy() // fully release WebView resources
            }
            webView = null
        }
    }

    val chatModifier = if (isWebChat == true) modifier
        .imePadding()
    else modifier

    AndroidView(
        modifier = chatModifier,
        factory = { context ->
            WebView(context).apply {
                webView = this
                settings.javaScriptEnabled = true
                webChromeClient = object : WebChromeClient() {}
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                settings.domStorageEnabled = true
                settings.allowFileAccess = true
                settings.mediaPlaybackRequiresUserGesture = true
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                if (isWebChat == true) {
                    if (ContextCompat.checkSelfPermission(
                            PlatformContext.appContext,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        activity?.let { act ->
                            requestPermissions(
                                act,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                CONTACTS_PERMISSION_CODE
                            )
                        }
                    }
                    // Enable storage permissions for web chat
                    settings.allowContentAccess = true
                    settings.databaseEnabled = true


                    CookieManager.getInstance().setAcceptCookie(true)
                    CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
                    CookieManager.getInstance().setCookie(url, "your_saved_cookie_string")
                    CookieManager.getInstance().flush()
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView,
                            request: WebResourceRequest
                        ): Boolean = true

                        override fun onPageFinished(view: WebView, url: String) {
                            super.onPageFinished(view, url)
                            view.evaluateJavascript(
                                "try { _Click2ConnectWidgetMaximise(); } catch(e) { console.error('Error maximizing widget:', e); }",
                                null
                            )
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                        }
                    }
                    webChromeClient = object : WebChromeClient() {
                        override fun onConsoleMessage(message: ConsoleMessage): Boolean {
                            return true
                        }

                        override fun onShowFileChooser(
                            webView: WebView?,
                            filePathCallback: ValueCallback<Array<Uri>>?,
                            fileChooserParams: FileChooserParams?
                        ): Boolean {
                            filePathCallbackVar = filePathCallback

                            return try {
                                // Launch the file chooser using the modern API
                                multipleFileChooserLauncher.launch("*/*")
                                true
                            } catch (e: Exception) {
                                filePathCallback?.onReceiveValue(null)
                                filePathCallbackVar = null
                                false
                            }
                        }
                    }
                } else {
                    webViewClient = WebViewClient()
                }
                if (!htmlContent.isNullOrBlank()) {
                    loadDataWithBaseURL(url, htmlContent, "text/html", "UTF-8", null)
                } else {
                    loadUrl(url)
                }
            }
        },
        update = { webViewInstance ->
            webView = webViewInstance
            if (!htmlContent.isNullOrBlank()) {
                webViewInstance.loadDataWithBaseURL(url, htmlContent, "text/html", "UTF-8", null)
            } else {
                webViewInstance.loadUrl(url)
            }
        }
    )
}
