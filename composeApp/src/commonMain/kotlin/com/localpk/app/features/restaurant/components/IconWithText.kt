package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.RobotoCondensedFamily
import com.localpk.app.theming.TextSubdued
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun IconWithText(icon: DrawableResource, text: String, width: Dp, fontSize: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .width(width)
                .height(width)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontSize.sp,
                lineHeight = fontSize.sp,
                fontFamily = RobotoCondensedFamily(),
                fontWeight = FontWeight.Medium,
                color = TextSubdued,
            )
        )
    }
}

@Composable
fun VerticalSeparator(padding: Dp) {
    Box(
        modifier = Modifier
            .padding(padding)
            .width(1.dp)
            .height(12.dp)
            .background(color = BorderPrimaryDefault)
    )
}