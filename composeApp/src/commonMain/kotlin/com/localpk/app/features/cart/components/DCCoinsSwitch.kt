package com.localpk.app.features.cart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.check_mark
import dealcart.composeapp.generated.resources.unchecked_mark
import org.jetbrains.compose.resources.painterResource

@Composable
fun DCCoinsSwitch(
    isDCCoinEnabled: Boolean,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    trackWidth: Dp = 50.dp,
    trackHeight: Dp = 30.dp,
    thumbSize: Dp = 20.dp,
    colors: SwitchColors = SwitchDefaults.colors()
) {
    val thumbOffset = if (checked) trackWidth - thumbSize - 4.dp else 4.dp

    Box(
        modifier = modifier
            .size(trackWidth, trackHeight)
            .clip(RoundedCornerShape(50)) // Rounded track
            .background(
                if (checked) colors.checkedTrackColor
                else colors.uncheckedTrackColor
            )
            .clickable(enabled = isDCCoinEnabled) { onCheckedChange(checked.not()) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset(x = thumbOffset)
                .clip(CircleShape) // Round thumb
                .background(
                    if (checked) colors.checkedThumbColor
                    else colors.uncheckedThumbColor
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Image(
                    painter = painterResource(Res.drawable.check_mark),
                    contentDescription = null,
                    modifier = Modifier
                )
            } else {
                Image(
                    painter = painterResource(Res.drawable.unchecked_mark),
                    contentDescription = null,
                    modifier = Modifier
                )
            }
        }
    }
}