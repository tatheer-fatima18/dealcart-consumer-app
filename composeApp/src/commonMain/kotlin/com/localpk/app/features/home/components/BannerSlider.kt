package com.localpk.app.features.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.localpk.app.features.banner.data.Banner
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerSlider(
    banners: List<Banner>,
    onBannerClick: (Banner) -> Unit
) {
    if (banners.isNotEmpty()) {
        val lazyListState = rememberLazyListState()
        val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState)
        val currentPage by remember {
            derivedStateOf { lazyListState.firstVisibleItemIndex }
        }
        var isAutoScrolling by remember { mutableStateOf(true) }

        val bannerWidth = 360.dp
        val bannerHeight = 160.dp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .fillMaxHeight()
        ) {
            LazyRow(
                state = lazyListState,
                flingBehavior = snapFlingBehavior,
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(banners.size) { index ->
                    BannerItemView(
                        banner = banners[index],
                        onClick = {
                            isAutoScrolling = false
                            onBannerClick(banners[index])
                        },
                        modifier = Modifier
                            .width(bannerWidth)
                            .height(bannerHeight)
                    )
                }
            }
        }

        // Auto-scroll every 7 seconds
        LaunchedEffect(isAutoScrolling, banners.size) {
            if (banners.size > 1 && isAutoScrolling) {
                while (true) {
                    delay(7000)
                    if (!isAutoScrolling) break

                    val currentPage = lazyListState.firstVisibleItemIndex
                    val nextPage = if (banners.isNotEmpty()) (currentPage + 1) % banners.size else 0

                    if (nextPage in 0 until banners.size) {
                        lazyListState.animateScrollToItem(nextPage)
                    }
                }
            }
        }

        // Pause scrolling on manual swipe, resume after 10 seconds
        LaunchedEffect(currentPage) {
            isAutoScrolling = false
            delay(10000)
            isAutoScrolling = true
        }
    }
} 