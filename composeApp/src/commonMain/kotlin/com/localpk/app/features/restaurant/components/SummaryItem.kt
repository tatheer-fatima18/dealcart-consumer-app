package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun SummaryItem(
    modifier: Modifier = Modifier,
    icon: DrawableResource,
    value: String,
    title: String,
    iconSize: Dp = 14.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            modifier = Modifier.size(iconSize),
            painter = painterResource(icon),
            contentDescription = null,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                color = TextSubdued,
            )
        )
        Text(
            text = value,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                color = TextSubdued
            )
        )
    }
}