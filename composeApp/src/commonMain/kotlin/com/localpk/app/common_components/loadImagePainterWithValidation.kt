package com.localpk.app.common_components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun loadImagePainterWithValidation(
    imageUrl: String?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    fallbackRes: DrawableResource,
    contentScale: ContentScale = ContentScale.Crop,
    loadingContent: @Composable () -> Unit = {
        Box(
            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            PulseAnimation(modifier = Modifier.size(48.dp))
        }
    }
) {
    var imageLoadResult by remember { mutableStateOf<Result<Painter>?>(null) }

    val fallbackPainter = painterResource(fallbackRes)

    // Don't start image load if URL is null or blank
    if (imageUrl.isNullOrBlank()) {
        loadingContent()
        return
    }

    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        onSuccess = {
            val size = it.painter.intrinsicSize
            imageLoadResult = if (
                size.isSpecified && size.width > 1f && size.height > 1f
            ) {
                Result.success(it.painter)
            } else {
                Result.failure(Exception("Invalid image dimensions"))
            }
        },
        onError = {
            it.result.throwable.printStackTrace()
            imageLoadResult = Result.failure(it.result.throwable)
        }
    )

    AnimatedContent(
        targetState = imageLoadResult,
        label = "ValidatedAsyncImage"
    ) { result ->
        when (result) {
            null -> loadingContent()

            else -> {
                Image(
                    painter = if (result.isSuccess) painter else fallbackPainter,
                    contentDescription = contentDescription,
                    contentScale = if (result.isSuccess) contentScale else ContentScale.Fit,
                    modifier = modifier.clip(shape)
                )
            }
        }
    }
}

