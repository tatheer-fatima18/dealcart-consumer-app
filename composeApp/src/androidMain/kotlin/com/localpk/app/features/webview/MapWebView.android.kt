package com.localpk.app.features.webview

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.localpk.app.utils.LOCATION_URL_SCHEME
import com.localpk.app.utils.MAP_LOADED_URL_SCHEME
import com.localpk.app.utils.MAP_WEB_VIEW_URL
import com.localpk.app.utils.TAG
import org.json.JSONObject

/**
 * Calls window.getCurrentLocation() in the WebView and passes the result to [callback].
 */
private fun getCurrentLocationFromWebView(webView: WebView?, callback: (Double, Double) -> Unit) {
    webView?.evaluateJavascript(
        "window.getCurrentLocation();",
        ValueCallback { result ->
            try {
                val cleanResult = result?.removeSurrounding("\"") ?: return@ValueCallback
                val json = JSONObject(cleanResult)
                val lat = json.getDouble("lat")
                val lng = json.getDouble("lng")
                callback(lat, lng)
            } catch (e: Exception) {
                Log.e(TAG, "getCurrentLocationFromWebView parse error", e)
            }
        }
    )
}

/**
 * Calls window.setLocation(lat, lng) in the WebView. Logs whether the function existed and was called.
 */
private fun WebView.setLocation(lat: Double, lng: Double, onLocationSet: (Boolean) -> Unit) {
    val script = """
        (function() {
            if (typeof window.setLocation === 'function') {
                window.setLocation($lat, $lng);
                return 'called';
            }
            return 'not_ready';
        })();
    """.trimIndent()
    evaluateJavascript(script) { result ->
        Log.d(TAG, "setLocation($lat, $lng) result=$result")
    }
    onLocationSet(true)
}

private fun buildMapUrl(queryString: String?): String {
    val base = MAP_WEB_VIEW_URL
    return if (!queryString.isNullOrBlank()) "$base?$queryString" else base
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun MapWebView(
    modifier: Modifier,
    initialLat: Double,
    initialLng: Double,
    onLocationReceived: (Double, Double) -> Unit,
    onCurrentLocation: ((Double, Double) -> Unit)?,
    requestCurrentLocationTrigger: Int,
    urlQueryString: String?,
    onLocationSet: (Boolean) -> Unit,
) {
    var webViewRef by mutableStateOf<WebView?>(null)
    // Hold latest coords so WebViewClient (created once in factory) always sees current values
    var latestCoords by mutableStateOf<Pair<Double, Double>?>(null)

    Log.d(TAG, "MapWebView initial coordinates: lat=$initialLat, lng=$initialLng")

    DisposableEffect(Unit) {
        onDispose {
            webViewRef?.apply {
                loadUrl("about:blank")
                destroy()
            }
            webViewRef = null
        }
    }

    LaunchedEffect(requestCurrentLocationTrigger) {
        if (requestCurrentLocationTrigger > 0 && onCurrentLocation != null) {
            getCurrentLocationFromWebView(webViewRef) { lat, lng ->
                onCurrentLocation(lat, lng)
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                webViewRef = this
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d(TAG, "onPageFinished url=$url")
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val url = request?.url?.toString() ?: return false
                        if (url.startsWith(MAP_LOADED_URL_SCHEME)) {
                            Log.d(TAG, "Map loaded signal received from webview")
                            val coords = latestCoords
                            if (coords != null && view != null) {
                                val (lat, lng) = coords
                                Log.d(
                                    TAG,
                                    "Calling setLocation(lat=$lat, lng=$lng) after map loaded"
                                )
                                view.setLocation(
                                    lat = lat,
                                    lng = lng,
                                    onLocationSet = onLocationSet
                                )
                            } else {
                                Log.w(
                                    TAG,
                                    "Map loaded but no coords or view: coords=$coords, view=$view"
                                )
                            }
                            return true
                        }
                        if (url.startsWith(LOCATION_URL_SCHEME)) {
                            val uri = Uri.parse(url)
                            val lat = uri.getQueryParameter("lat")?.toDoubleOrNull()
                            val lng = uri.getQueryParameter("lng")?.toDoubleOrNull()
                            if (lat != null && lng != null) {
                                Log.d(TAG, "Location received from webview: lat=$lat, lng=$lng")
                                onLocationReceived(lat, lng)
                            }
                            return true
                        }
                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }
                postDelayed({ loadUrl(buildMapUrl(urlQueryString)) }, 100L)
            }
        },
        update = { webViewInstance ->
            latestCoords = Pair(initialLat, initialLng)
            // Only run when we're actually on the map page. Update runs before load finishes,
            // so we must not inject into about:blank or the script is lost on navigation.
            if (webViewInstance.url?.startsWith(MAP_WEB_VIEW_URL) == true) {
                webViewInstance.setLocation(
                    lat = initialLat,
                    lng = initialLng,
                    onLocationSet = onLocationSet
                )
            }
        }
    )
}
