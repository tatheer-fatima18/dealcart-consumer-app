package com.localpk.app.features.one_last_step.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_right_arrow
import dealcart.composeapp.generated.resources.one_last_step_video_button_bg
import org.jetbrains.compose.resources.painterResource

@Composable
fun NextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.one_last_step_video_button_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(32.dp)
                .height(32.dp)
        )
        Icon(
            painter = painterResource(Res.drawable.ic_right_arrow),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .width(8.dp)
                .height(13.dp)
        )
    }
}
