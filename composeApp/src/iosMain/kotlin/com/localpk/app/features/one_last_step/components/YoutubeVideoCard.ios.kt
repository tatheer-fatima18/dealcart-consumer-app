package com.localpk.app.features.one_last_step.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.localpk.app.features.cart.data.OneLastStepUiData
import androidx.compose.ui.viewinterop.UIKitView
import com.localpk.app.youTubePlayerController

@Composable
actual fun VideoPlayer(videoData: OneLastStepUiData) {
    UIKitView(
        factory = { youTubePlayerController(videoData.youTubeId).view },
        modifier = Modifier.fillMaxSize()
    )
}
