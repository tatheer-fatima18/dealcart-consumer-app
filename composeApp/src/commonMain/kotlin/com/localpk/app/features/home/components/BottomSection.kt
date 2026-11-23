package com.localpk.app.features.home.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.DIVIDER_COLOR
import com.localpk.app.theming.SurfaceBackground

@Composable
fun BottomSection(
    size: Int,
    index: Int,
    color: Color,
    modifier: Modifier = Modifier,
    isMatchIndicator: Boolean = false,
    onButtonClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        Indicators(size, index, color, isMatchIndicator)
    }
}

@Composable
fun BoxScope.Indicators(size: Int, index: Int, color: Color, isMatchIndicator: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(if (isMatchIndicator) 10.dp else 0.dp),
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        if (isMatchIndicator) {
            repeat(size) {
                MatchIndicator(isSelected = it == index)
            }

        } else {
            repeat(size) {
                CategoryIndicator(isSelected = it == index, color)
            }
        }
    }
}

@Composable
fun CategoryIndicator(isSelected: Boolean, color: Color) {
    val height = animateDpAsState(
        targetValue = 5.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = ""
    )
    Box(
        modifier = Modifier
            .height(height.value)
            .fillMaxWidth(0.2f)
            .background(
                color = if (isSelected) color else SurfaceBackground , shape = RoundedCornerShape(6.dp)
            )
    ) {}
}


@Composable
fun MatchIndicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 10.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = ""
    )
    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) ColorPrimary else DIVIDER_COLOR
            )
    ) {}
}
