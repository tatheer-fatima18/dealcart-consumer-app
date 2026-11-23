package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackgroundSecondary
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_baseline_keyboard_arrow_down_24
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SummaryGroupItem(
    icon: DrawableResource,
    value: String,
    title: StringResource
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            modifier = Modifier.size(14.dp),
            painter = painterResource(icon),
            contentDescription = null,
        )
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(title),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    color = TextSubdued,
                    textDecoration = TextDecoration.Underline
                )
            )
            Icon(
                modifier = Modifier
                    .background(
                        color = SurfaceBackgroundSecondary,
                        shape = CircleShape
                    )
                    .size(14.dp),
                painter = painterResource(Res.drawable.ic_baseline_keyboard_arrow_down_24),
                contentDescription = null,
                tint = TextSubdued
            )
        }
        Text(
            text = value,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = TextSubdued
            )
        )
    }
}