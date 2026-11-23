package com.localpk.app.features.rewards.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.utils.EMPTY_STRING
import dealcart.composeapp.generated.resources.Res
import androidx.compose.runtime.getValue
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder

@Composable
fun AnimatedImage(isSelected: Boolean, coinsImageUrl: String?, savingsImageUrl: String?) {
    val transition = updateTransition(targetState = isSelected, label = "")
    val alpha by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 1000)
        },
        label = ""
    )
    { isSelected ->
        if (isSelected) 1f else 1f
    }

    val selectedImage = if (isSelected) coinsImageUrl else savingsImageUrl
    val unselectedImage = if (isSelected) coinsImageUrl else savingsImageUrl

    Crossfade(
        targetState = isSelected,
        animationSpec = tween(durationMillis = 1000),
        label = ""
    ) { targetState ->
        val imageResource = if (targetState) selectedImage else unselectedImage

        loadImagePainterWithValidation(
            imageUrl = if (imageResource.isNullOrEmpty()
                    .not()
            ) imageResource else EMPTY_STRING,
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        topStart = 15.dp,
                        topEnd = 15.dp,
                        bottomStart = 27.dp,
                        bottomEnd = 27.dp
                    )
                )
                .alpha(alpha),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            fallbackRes = Res.drawable.ic_dealcart_placeholder,

            )
    }
}
