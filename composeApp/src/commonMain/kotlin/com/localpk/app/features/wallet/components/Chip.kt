package com.localpk.app.features.wallet.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued

@Composable
fun Chip(title: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = SurfaceBackground
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(110.dp)
            .wrapContentHeight()
    ) {
        Text(
            text = title,
            color = TextSubdued,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            fontStyle = FontStyle.Normal,
            fontFamily = PoppinsFontFamily()
        )
    }
}