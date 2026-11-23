package com.localpk.app.features.one_last_step.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.localpk.app.features.cart.data.OneLastStepUiData

@Composable
fun VideoCard(
    videoData: OneLastStepUiData? = null,
    onClick: () -> Unit
) {
    if (videoData != null) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                // YouTube Player
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(224.dp)
                ) {
                    VideoPlayer(videoData)
                }
                VideoInfoSection(videoName = videoData.name, onClick = onClick)
            }
        }
    }
}

@Composable
expect fun VideoPlayer(
    videoData: OneLastStepUiData
)

