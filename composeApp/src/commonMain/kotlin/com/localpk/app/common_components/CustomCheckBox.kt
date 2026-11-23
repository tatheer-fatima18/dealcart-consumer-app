package com.localpk.app.common_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.TextSubduedHalf

@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    checkedColor: Color = ButtonSurfaceBrand,
    borderColor: Color = TextSubduedHalf,
    checkmarkColor: Color = Color.White,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(4.dp))
            .background(if (checked) checkedColor else Color.Transparent)
            .border(
                width = 1.5.dp,
                color = if (checked) checkedColor else borderColor,
                shape = RoundedCornerShape(4.dp)
            )
            .then(
                if (enabled) Modifier.clickable { onCheckedChange(checked.not()) } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = checkmarkColor,
                modifier = Modifier.size(size * 0.8f)
            )
        }
    }
}