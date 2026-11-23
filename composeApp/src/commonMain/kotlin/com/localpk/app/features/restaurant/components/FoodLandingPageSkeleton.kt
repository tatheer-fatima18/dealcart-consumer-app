package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.utils.shimmerEffect

@Composable
fun FoodLandingPageSkeleton() {
    Column(modifier = Modifier.padding(14.dp)) {
        repeat(1) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(5) {
                    Box(
                        modifier = Modifier
                            .width(72.dp)
                            .height(72.dp)
                            .clip(CircleShape)
                            .background(SurfaceBackground)
                            .shimmerEffect()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceBackground)
                            .shimmerEffect()
                            .padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
    return
}