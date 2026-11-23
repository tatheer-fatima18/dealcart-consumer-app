package com.localpk.app.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.shimmerEffect
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.categories
import org.jetbrains.compose.resources.stringResource
import kotlin.math.max

@Composable
fun BannerShimmer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 8.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(8.dp))
            .shimmerEffect()
    )
}

@Composable
fun CategoriesShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .padding(start = 2.dp)
    ) {
        // Title shimmer
        Text(
            text = stringResource(Res.string.categories),
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = TextSubdued,
            )
        )
        Spacer(Modifier.height(8.dp))

        // Categories grid shimmer
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val itemMinWidth = 81.dp
            val spacing = 8.dp
            val itemsPerRow = max(1, ((maxWidth + spacing) / (itemMinWidth + spacing)).toInt())
            val rowsPerPage = 2

            Column(
                verticalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier.padding(top = spacing)
            ) {
                repeat(rowsPerPage) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        repeat(itemsPerRow) {
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Category icon shimmer
                                Box(
                                    modifier = Modifier
                                        .height(82.dp)
                                        .width(92.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .shimmerEffect()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                // Category name shimmer
                                Box(
                                    modifier = Modifier
                                        .width(82.dp)
                                        .height(12.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .shimmerEffect()
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun SuperParentCategoriesShimmer(
    tileBg: Color,
    animatedHeight: Dp,
    topColor: Color
) {
    Column(
        modifier = Modifier
            .background(color = topColor)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(start = 2.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // "All" button shimmer
            item {
                SuperParentCategoryAllComponent(
                    selectedSuperParentCategory = null,
                    tileBg = tileBg,
                    animatedHeight = animatedHeight
                )
            }
            // Category items shimmer (show 4-5 items)
            items(5) {
                Box(
                    modifier = Modifier
                        .width(96.dp)
                        .height(82.dp)
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun ReOrderShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .padding(start = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Title shimmer
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            // View all shimmer
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
        Spacer(modifier = Modifier.height(15.dp))

        // Products row shimmer
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(4) {
                Column(
                    modifier = Modifier
                        .width(140.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    // Product image shimmer
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Product name shimmer
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Product price shimmer
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}
