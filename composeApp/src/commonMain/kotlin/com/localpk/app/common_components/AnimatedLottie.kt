package com.localpk.app.common_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.alexzhirkevich.compottie.LottieAnimation
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieConstants
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter

@Composable
fun AnimatedLottie(
    modifier: Modifier = Modifier,
    lottieSpec: LottieCompositionSpec,
    iterations: Int = LottieConstants.IterateForever,
    isPlaying: Boolean = true,
    useImage: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val composition by rememberLottieComposition(lottieSpec)
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = iterations,
        isPlaying = isPlaying
    )

    if (useImage) {
        Image(
            painter = rememberLottiePainter(
                composition = composition,
                progress = { progress }
            ),
            contentDescription = "Lottie Image",
            modifier = modifier.clickable(enabled = onClick != null) {
                onClick?.invoke()
            }
        )
    } else {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier
        )
    }
}
