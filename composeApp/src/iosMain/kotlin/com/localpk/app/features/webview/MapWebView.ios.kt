package com.localpk.app.features.webview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.darwin.NSObject
import com.localpk.app.utils.MAP_WEB_VIEW_URL
import platform.Foundation.NSDictionary
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKPreferences
import platform.WebKit.WKScriptMessage
import platform.WebKit.WKScriptMessageHandlerProtocol
import platform.WebKit.WKUserContentController
import platform.WebKit.WKUserScript
import platform.WebKit.WKUserScriptInjectionTime.WKUserScriptInjectionTimeAtDocumentStart
import platform.Foundation.NSLog
import platform.Foundation.NSNumber
import com.localpk.app.utils.TAG


private const val CONSOLE_HANDLER_NAME = "mapWebViewConsole"

/** Forwards WebView console.log/warn/error to Xcode console (NSLog). */
private class ConsoleMessageHandler : NSObject(), WKScriptMessageHandlerProtocol {
    override fun userContentController(
        userContentController: WKUserContentController,
        didReceiveScriptMessage: WKScriptMessage
    ) {
        val body = didReceiveScriptMessage.body
        NSLog("$TAG [JS]: $body")
    }
}

class MapLoadedHandler(
    private val getLatestCoords: () -> Pair<Double, Double>?,
    private val getWebView: () -> WKWebView?,
    private val onMapLoaded: () -> Unit = { }
) : NSObject(), WKScriptMessageHandlerProtocol {

    override fun userContentController(
        userContentController: WKUserContentController,
        didReceiveScriptMessage: WKScriptMessage
    ) {
        if (didReceiveScriptMessage.name == "mapLoaded") {

            onMapLoaded()

            val coords = getLatestCoords()
            val webView = getWebView()

            if (coords != null && webView != null) {
                val (lat, lng) = coords
                sendLocationToWebView(webView, lat, lng)
            }
        }
    }
}

class LocationHandler(
    private val onLocationReceived: (Double, Double) -> Unit
) : NSObject(), WKScriptMessageHandlerProtocol {

    override fun userContentController(
        userContentController: WKUserContentController,
        didReceiveScriptMessage: WKScriptMessage
    ) {
        if (didReceiveScriptMessage.name == "location") {
            val body = didReceiveScriptMessage.body as? NSDictionary
            val lat = (body?.objectForKey("lat") as? NSNumber)?.doubleValue
            val lng = (body?.objectForKey("lng") as? NSNumber)?.doubleValue

            if (lat != null && lng != null) {
                onLocationReceived(lat, lng)
            }
        }
    }
}

private val consoleBridgeScript = """
(function() {
    var h = window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers['$CONSOLE_HANDLER_NAME'];
    if (!h) return;
    var send = function(level, args) {
        try {
            h.postMessage(level + ': ' + Array.prototype.slice.call(args).map(function(a) { return a === null ? 'null' : (typeof a === 'object' ? JSON.stringify(a) : String(a)); }).join(' '));
        } catch(e) {}
    };
    var origLog = console.log, origWarn = console.warn, origError = console.error;
    console.log = function() { origLog.apply(console, arguments); send('log', arguments); };
    console.warn = function() { origWarn.apply(console, arguments); send('warn', arguments); };
    console.error = function() { origError.apply(console, arguments); send('error', arguments); };
})();
""".trimIndent()

private fun logD(message: String) {
    NSLog("$TAG: $message")
}

/**
 * Sends location to the map WebView by calling window.setLocation(lat, lng).
 * Use from a ViewModel or composable when you have the user's location (GPS, saved address, etc.).
 */
fun sendLocationToWebView(webView: WKWebView?, lat: Double, lng: Double) {
    val javascript = "window.setLocation($lat, $lng);"
    webView?.evaluateJavaScript(javascript) { _, error ->
        if (error != null) {
            logD("Error sending location: ${error.localizedDescription}")
        }
    }
}

/**
 * Gets the current marker position from the map WebView by calling window.getCurrentLocation().
 * Use from a ViewModel or composable when you need the selected location (e.g. on Confirm).
 */
fun getCurrentLocationFromWebView(
    webView: WKWebView?,
    callback: (Double, Double) -> Unit
) {
    if (webView == null) return
    webView.evaluateJavaScript("window.getCurrentLocation();") { result, error ->
        if (error != null) {
            logD("Error getting location: ${error.localizedDescription}")
            return@evaluateJavaScript
        }
        when (result) {
            is Map<*, *> -> {
                val lat = (result["lat"] as? Number)?.toDouble()
                val lng = (result["lng"] as? Number)?.toDouble()
                if (lat != null && lng != null) callback(lat, lng)
            }

            is String -> {
                val clean = result.removeSurrounding("\"")
                val latMatch = Regex("""\"lat\"\s*:\s*([\d.-]+)""").find(clean)?.groupValues?.get(1)
                    ?.toDoubleOrNull()
                val lngMatch = Regex("""\"lng\"\s*:\s*([\d.-]+)""").find(clean)?.groupValues?.get(1)
                    ?.toDoubleOrNull()
                if (latMatch != null && lngMatch != null) callback(latMatch, lngMatch)
            }

            else -> {}
        }
    }
}

private fun buildMapUrl(queryString: String?): String {
    val base = MAP_WEB_VIEW_URL
    return if (!queryString.isNullOrBlank()) "$base?$queryString" else base
}

@OptIn(ExperimentalForeignApi::class)
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
    var webViewRef by mutableStateOf<WKWebView?>(null)
    // Hold latest coords so navigation delegate (created once in remember) always sees current values
    var latestCoords by mutableStateOf<Pair<Double, Double>?>(null)

    var mapLoaded by remember { mutableStateOf(false) }

    logD("MapWebView initial coordinates: lat=$initialLat, lng=$initialLng")

    LaunchedEffect(requestCurrentLocationTrigger) {
        if (requestCurrentLocationTrigger > 0 && onCurrentLocation != null) {
            getCurrentLocationFromWebView(webViewRef) { lat, lng ->
                onCurrentLocation(lat, lng)
            }
        }
    }

    val webView = remember {
        val config = WKWebViewConfiguration().apply {
            preferences = WKPreferences().apply {
                setJavaScriptCanOpenWindowsAutomatically(true)
            }
            allowsInlineMediaPlayback = true

            userContentController.addScriptMessageHandler(
                MapLoadedHandler(
                    getLatestCoords = { latestCoords },
                    getWebView = { webViewRef },
                    onMapLoaded = {
                        mapLoaded = true
                        onLocationSet(mapLoaded)
                    }
                ),
                "mapLoaded"
            )

            userContentController.addScriptMessageHandler(
                LocationHandler(
                    onLocationReceived = { lat, lng ->
                        logD("Location received via message handler: lat=$lat, lng=$lng")
                        onLocationReceived(lat, lng)
                    }
                ),
                "location"
            )

            // Forward WebView console.log/warn/error to Xcode console
            userContentController.addScriptMessageHandler(
                ConsoleMessageHandler(),
                CONSOLE_HANDLER_NAME
            )
            userContentController.addUserScript(
                WKUserScript(
                    source = consoleBridgeScript,
                    injectionTime = WKUserScriptInjectionTimeAtDocumentStart,
                    forMainFrameOnly = true
                )
            )
        }
        val mapUrl = buildMapUrl(urlQueryString)
        WKWebView(frame = CGRectZero.readValue(), configuration = config).apply {
            webViewRef = this
            navigationDelegate = object : NSObject(), WKNavigationDelegateProtocol {}
            val nsUrl = NSURL.URLWithString(mapUrl)
            if (nsUrl != null) {
                loadRequest(NSURLRequest(nsUrl))
            }
        }
    }

    UIKitView(
        modifier = modifier,
        factory = { webView },
        update = { view ->
            latestCoords = Pair(initialLat, initialLng)
            logD("update: latestCoords updated (url=${view.URL?.absoluteString})")
            if (mapLoaded)
                sendLocationToWebView(webView, initialLat, initialLng)
        }
    )
}
