@file:OptIn(ExperimentalFoundationApi::class)

package com.localpk.app.features.product_detail.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.productlist.data.ProductImage
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_image

@Composable
fun ProductImageComponent(
    productDetailImages: List<ProductImage>?
) {
    val imageCount = productDetailImages.orEmpty().size.getValueOrDefault()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(
                BorderStroke(width = 1.dp, color = BorderPrimaryDefault),
                RoundedCornerShape(8.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        if (imageCount > 1) {
            val pagerState = rememberPagerState(pageCount = { imageCount })
            val indicatorScrollState = rememberLazyListState()

            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(396.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Transparent),
                state = pagerState
            ) { page ->
                loadImagePainterWithValidation(
                    imageUrl = productDetailImages.orEmpty()[page].path,
                    fallbackRes = Res.drawable.ic_image,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            LazyRow(
                state = indicatorScrollState,
                modifier = Modifier
                    .height(50.dp)
                    .width((26 * 5).dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) ButtonSurfaceBrand else Color.White
                    item(key = "item$iteration") {
                        val currentPage = pagerState.currentPage
                        val firstVisibleIndex by remember { derivedStateOf { indicatorScrollState.firstVisibleItemIndex } }
                        val lastVisibleIndex =
                            indicatorScrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                                ?: 0
                        val size by animateDpAsState(
                            targetValue = if (iteration == currentPage) {
                                10.dp
                            } else if (iteration in firstVisibleIndex + 1..lastVisibleIndex - 1) {
                                10.dp
                            } else {
                                6.dp
                            }
                        )
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .border(
                                    BorderStroke(1.dp, BorderPrimaryDefault),
                                    CircleShape
                                )
                                .background(color)
                                .padding(1.dp)
                                .size(size)
                        )
                    }
                }
            }
        } else {
            loadImagePainterWithValidation(
                imageUrl = productDetailImages.orEmpty().firstOrNull()?.path,
                fallbackRes = Res.drawable.ic_image,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}
