package com.localpk.app.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.localpk.app.common_components.loadComposableAsyncImage
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.bat_logo
import dealcart.composeapp.generated.resources.pakistan_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun TeamScoreItem(
    flag: String,
    showBat: Boolean,
    isLive: Boolean,
) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        loadComposableAsyncImage(
            imageUrl = flag,
            fallbackRes = Res.drawable.pakistan_logo,
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
        )
        if (showBat && isLive) {
            Spacer(modifier = Modifier.width(5.dp))
            Image(
                painter = painterResource(Res.drawable.bat_logo),
                contentDescription = "Bat",
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
            )
        }
    }
}