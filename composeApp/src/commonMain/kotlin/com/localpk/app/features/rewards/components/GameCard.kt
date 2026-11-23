package com.localpk.app.features.rewards.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun GamesCard(
    backgroundImageResource: DrawableResource,
    onClick: () -> Unit,
    onEventClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clickable {
                onEventClick.invoke()
                onClick.invoke()
            }
            .background(
                shape = RoundedCornerShape(size = 12.dp),
                color = Color.Transparent,
            )
            .border(BorderStroke(1.dp, Color.Transparent))
            .size(width = 105.dp, height = 107.dp)
    ) {

        Image(
            painter = painterResource(backgroundImageResource),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
    }
}