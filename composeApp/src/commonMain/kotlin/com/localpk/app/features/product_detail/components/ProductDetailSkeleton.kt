package com.localpk.app.features.product_detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.utils.shimmerEffect

@Composable
fun ProductDetailSkeleton(titleWidth: Float = 0.4f) {
    Column(modifier = Modifier.statusBarsPadding()) {
        HeaderShimmer(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, end = 8.dp)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        )
        ProductImageShimmerPlaceholder()
        ProductNameAndPriceShimmerPlaceholder(titleWidth)
        ProductDescriptionShimmerPlaceholder()
    }
}

@Composable
fun HeaderShimmer(modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Back Button Placeholder
        Box(
            modifier = Modifier
                .size(40.dp)
                .shimmerEffect()
                .clip(RoundedCornerShape(8.dp))
        )

        // Search Bar Placeholder
        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .shimmerEffect()
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

@Composable
fun ProductImageShimmerPlaceholder() {
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
        // Large image shimmer box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(396.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )

        // Fake pager indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .height(50.dp)
                .width((26 * 5).dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) { // You can adjust the count to match real loading design
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(
                            BorderStroke(1.dp, BorderPrimaryDefault),
                            CircleShape
                        )
                        .shimmerEffect()
                )
            }
        }
    }
}





