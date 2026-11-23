package com.localpk.app.features.one_last_step.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.PurpleNew
import com.localpk.app.theming.PurpleShade
import com.localpk.app.theming.StrokeHomeScreenShareEarn
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.share_earn
import dealcart.composeapp.generated.resources.share_earn_gift
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SupportButton(onActionShareAndWin: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .height(46.dp)
            .fillMaxWidth()
            .background(
                color = PurpleShade,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                BorderStroke(1.dp, StrokeHomeScreenShareEarn),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onActionShareAndWin() }
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(Res.drawable.share_earn_gift),
                contentDescription = null,
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
            )
            Text(
                text = stringResource(Res.string.share_earn),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 8.dp),
                style = TextStyle(
                    color = PurpleNew,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Bold
                ),
            )

        }
    }
}