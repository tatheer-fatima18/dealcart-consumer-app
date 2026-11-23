package com.localpk.app.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.category.data.CategoryUiModel
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder

@Composable
fun CategoryItemComponent(
    data: CategoryUiModel,
    onCategoryClick: (CategoryUiModel) -> Unit,
    categoryColour: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentHeight()
    ) {
        Box(
            modifier = Modifier
                .height(82.dp) // Keep the original 82dp height
                .width(92.dp)
                .background(
                    categoryColour,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    onCategoryClick(data)
                }
        ) {
            loadImagePainterWithValidation(
                imageUrl = data.imageUrl,
                fallbackRes = Res.drawable.ic_dealcart_placeholder,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp), // Add padding inside the box
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .width(82.dp),
            text = data.name,
            style = TextStyle(
                fontSize = 13.sp,
                lineHeight = 14.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                color = TextSubdued,
                textAlign = TextAlign.Center,
            )
        )
    }
} 