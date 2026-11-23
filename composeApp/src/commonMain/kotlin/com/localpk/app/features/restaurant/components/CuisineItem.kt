package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.restaurant.data.CuisineUI
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.capitalizeFirst
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.fallback

@Composable
fun CuisineItem(
    data: CuisineUI,
    selectedCuisines: List<CuisineUI>,
    onCuisineClick: (CuisineUI) -> Unit
) {
    val isSelected = selectedCuisines.any { it.id == data.id }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(74.dp)
                .clip(CircleShape)
                .clickable {
                    onCuisineClick(data)
                }
                .background(
                    if (isSelected) Color(0xFFFFF7E1) else Color.LightGray.copy(
                        alpha = 0.3f
                    )
                )
                .border(
                    width = 2.dp,
                    color = if (isSelected) Color(0xFFE9A804) else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            loadImagePainterWithValidation(
                imageUrl = data.image?.medium,
                fallbackRes = Res.drawable.fallback,
                contentDescription = null,
                modifier = Modifier
                    .size(74.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = data.name.capitalizeFirst(),
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                color = TextSubduedHalf,
                textAlign = TextAlign.Center
            )
        )
    }
}