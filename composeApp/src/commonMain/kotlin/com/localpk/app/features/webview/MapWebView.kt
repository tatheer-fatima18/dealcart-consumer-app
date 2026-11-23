package com.localpk.app.features.webview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Minimal WebView that displays only the map at [MAP_WEB_VIEW_URL].
 * If [initialLat] and [initialLng] are non-null, calls window.setLocation(lat, lng) when the page loads to update the map pin.
 *
 * [urlQueryString] Optional query string to append to the map URL (e.g. "showZoomControls=false&isPinDraggable=false"). Do not include the leading "?".
 * [onLocationReceived] Called when the user drags the marker; the webview sends location via dealcart://location?lat=X&lng=Y.
 * [onCurrentLocation] Called with the current marker position when [requestCurrentLocationTrigger] is incremented (e.g. when user taps Confirm).
 */
@Composable
expect fun MapWebView(
    modifier: Modifier,
    initialLat: Double,
    initialLng: Double,
    onLocationReceived: (Double, Double) -> Unit = { d: Double, d1: Double -> },
    onCurrentLocation: ((Double, Double) -> Unit)? = null,
    requestCurrentLocationTrigger: Int = 0,
    urlQueryString: String? = null,
    onLocationSet: (Boolean) -> Unit = { _ -> },
)
