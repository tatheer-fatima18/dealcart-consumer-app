package com.localpk.app.common_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.localpk.app.features.user_location.data.Geolocation
import com.localpk.app.features.webview.MapWebView
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.address_pin
import org.jetbrains.compose.resources.painterResource

@Composable
fun StaticMap(
    modifier: Modifier = Modifier,
    location: Geolocation,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        MapWebView(
            modifier = Modifier.matchParentSize(),
            initialLat = location.latitude,
            initialLng = location.longitude,
            onLocationReceived = { _, _ -> },
            onCurrentLocation = null,
            requestCurrentLocationTrigger = 0,
            urlQueryString = "isPinDraggable=false"
        )
    }
}
