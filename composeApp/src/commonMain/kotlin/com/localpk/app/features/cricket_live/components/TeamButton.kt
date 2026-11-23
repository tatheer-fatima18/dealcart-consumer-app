package com.localpk.app.features.cricket_live.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.ProfileTextGray
import com.localpk.app.theming.ScoreCardHeaderText
import com.localpk.app.theming.Yellow

@Composable
fun TeamButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Yellow else ProfileTextGray
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.wrapContentWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) Yellow else Color.Transparent
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) ScoreCardHeaderText else ProfileTextGray,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            fontFamily = PoppinsFontFamily()
        )
    }
}