package com.localpk.app.features.wallet.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.disclaimer_alert
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopUMessageCard(
    title: StringResource
) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .height(30.dp)
            .background(
                color = SurfaceBackground,
                shape = RoundedCornerShape(size = 34.5.dp)
            )
            .padding(7.dp),
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(Res.drawable.disclaimer_alert),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))

            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = stringResource(title),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                style = TextStyle(
                    lineHeight = 14.sp
                ),
                color = TextSubdued
            )

        }
    }
}
