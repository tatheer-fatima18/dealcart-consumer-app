package com.localpk.app.features.rewards.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.localpk.app.features.rewards.presentation.CoinHistoryScreen
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.arrow_right_blue
import dealcart.composeapp.generated.resources.history
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HistoryButton() {
    val navigator = LocalNavigator.current
    Box(
        modifier = Modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                1.dp,
                color = Color.White,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
              //  eventLogger.clickCoinsHistory()
                navigator?.push(CoinHistoryScreen())
            }
    ) {
        Row(
            modifier = Modifier.padding(1.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = stringResource(Res.string.history),
                style = TextStyle(
                    color = ColorPrimary,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
            Icon(
                painter = painterResource(Res.drawable.arrow_right_blue),
                contentDescription = null,
                tint = ColorPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))

        }
    }
}