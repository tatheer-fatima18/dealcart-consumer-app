package com.localpk.app.features.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.utils.shimmerEffect

@Composable
fun OrderDetailShimmerLoading() {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()).statusBarsPadding()
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Top Header Shimmer
            TopHeaderShimmer()
            
            // Order Info Component Shimmer
            OrderInfoShimmer()
            
            // Location and Contact Component Shimmer
            LocationAndContactShimmer()
            
            // Timeline Card Shimmer
            TimelineCardShimmer()
            
            // Items Card Shimmer
            ItemsCardShimmer()
            
            // Summary Component Shimmer
            SummaryComponentShimmer()
            
            // Payment Card Shimmer
            PaymentCardShimmer()
        }
    }
}

@Composable
private fun TopHeaderShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button shimmer
        Box(
            modifier = Modifier
                .size(24.dp)
                .shimmerEffect()
        )
        
        // Title shimmer
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(20.dp)
                .shimmerEffect()
        )
        
        // Action buttons shimmer
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun OrderInfoShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .border(
                width = 1.dp,
                color = BorderPrimaryDefault,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Order ID and Status row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Order ID shimmer
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(12.dp)
                        .shimmerEffect()
                )
                
                // Order status shimmer
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(12.dp)
                        .shimmerEffect()
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Items count and total shimmer
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(12.dp)
                            .shimmerEffect()
                    )
                    
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(11.dp)
                            .background(BorderPrimaryDefault)
                    )
                    
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(14.dp)
                            .shimmerEffect()
                    )
                }
            }
            
            HorizontalDivider(
                thickness = 1.dp,
                color = BorderPrimaryDefault
            )
            
            // Store info and delivery fulfillment row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Store image shimmer
                Box(
                    modifier = Modifier
                        .width(38.dp)
                        .height(30.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
                
                // Store name shimmer
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(14.dp)
                        .shimmerEffect()
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Fulfilled by shimmer
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(30.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
private fun LocationAndContactShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // City and address shimmer
        Column {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(16.dp)
                    .shimmerEffect()
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .shimmerEffect()
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(14.dp)
                    .shimmerEffect()
            )
        }
        
        // Map placeholder shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )
        
        // Phone and default address row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(14.dp)
                    .shimmerEffect()
            )
            
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(14.dp)
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun TimelineCardShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Timeline items
        repeat(5) { index ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Timeline icon shimmer
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                )
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Timeline text shimmer
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(14.dp)
                            .shimmerEffect()
                    )
                    
                    // Timeline date shimmer
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemsCardShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Items header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(16.dp)
                    .shimmerEffect()
            )
            
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(14.dp)
                    .shimmerEffect()
            )
        }
        
        // Items list shimmer
        repeat(3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Product image shimmer
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
                
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Product name shimmer
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .shimmerEffect()
                    )
                    
                    // Product variant shimmer
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(12.dp)
                            .shimmerEffect()
                    )
                    
                    // Product price shimmer
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(14.dp)
                            .shimmerEffect()
                    )
                }
                
                // Quantity shimmer
                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(20.dp)
                        .shimmerEffect()
                )
            }
            
            if (it < 2) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SummaryComponentShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Summary header
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(16.dp)
                .shimmerEffect()
        )
        
        // Summary items
        repeat(4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(14.dp)
                        .shimmerEffect()
                )
                
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(14.dp)
                        .shimmerEffect()
                )
            }
        }
        
        HorizontalDivider(
            thickness = 1.dp,
            color = BorderPrimaryDefault
        )
        
        // Total row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(16.dp)
                    .shimmerEffect()
            )
            
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(16.dp)
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun PaymentCardShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Payment header
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(16.dp)
                .shimmerEffect()
        )
        
        // Payment details
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(14.dp)
                        .shimmerEffect()
                )
                
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(12.dp)
                        .shimmerEffect()
                )
            }
            
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(14.dp)
                    .shimmerEffect()
            )
        }
    }
}
