package com.localpk.app.features.one_last_step.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.padding
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.Yellow
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.sponsored
import org.jetbrains.compose.resources.stringResource

@Composable
fun SponsoredText() {
    Row(
        modifier = Modifier
            .border(
                BorderStroke(
                    1.dp,
                    Yellow
                ), shape = RoundedCornerShape(6.dp)
            )
            .background(
                Yellow,
                shape = RoundedCornerShape(6.dp)
            ),
    ) {
        Text(
            text = stringResource(Res.string.sponsored).uppercase(),
            color = Color.White,
            fontSize = 8.sp,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 5.dp),
            fontFamily = PoppinsFontFamily()
        )
    }
}