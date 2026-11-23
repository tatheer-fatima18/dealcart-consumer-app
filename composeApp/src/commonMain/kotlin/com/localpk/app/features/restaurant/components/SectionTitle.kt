package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued

@Composable
fun SectionTitle(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = PoppinsFontFamily(),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSubdued
            ),
        )
        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                style = TextStyle(
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextSubdued
                ),
            )
        }
    }
}