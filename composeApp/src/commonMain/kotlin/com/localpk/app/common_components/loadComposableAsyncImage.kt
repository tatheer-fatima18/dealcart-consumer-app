package com.localpk.app.common_components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest.Builder
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.placeholder_home_banner
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
expect fun getCoilPlatformContext(): PlatformContext?

@Composable
fun loadComposableAsyncImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    fallbackRes: DrawableResource = Res.drawable.placeholder_home_banner,
    contentScale: ContentScale = ContentScale.Crop
) {
    val platformContext = getCoilPlatformContext() ?: return
    val imageLoader = remember {
        ImageLoader.Builder(platformContext)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }
    val request = Builder(platformContext)
        .data(imageUrl)
        .crossfade(true)
        .build()
    AsyncImage(
        model = request,
        imageLoader = imageLoader,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
        placeholder = painterResource(fallbackRes),
        error = painterResource(fallbackRes),
        fallback = painterResource(fallbackRes)
    )

}
