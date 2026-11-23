package com.localpk.app.features.product_detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.theming.AppTypography
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder

@Composable
fun StoreNameAndImageComponent(
    storeName: String,
    storeImageUrl: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(38.dp)
                .height(30.dp)
                .border(
                    BorderStroke(
                        width = 0.5.dp,
                        color = BorderPrimaryDefault
                    ),
                    RoundedCornerShape(4.dp)
                )
                .clip(shape = RoundedCornerShape(4.dp)),
        ) {
            loadImagePainterWithValidation(
                imageUrl = storeImageUrl,
                fallbackRes = Res.drawable.ic_dealcart_placeholder,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
        Text(
            text = storeName.split("_").joinToString(" "),
            style = AppTypography().bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextSubdued
            )
        )
    }
}