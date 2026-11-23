package com.localpk.app.features.rewards.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.login_rewards
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginButton() {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(color = Color.Transparent, shape = RoundedCornerShape(10.dp))
            .border(
                1.dp,
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
            //    navigate(RewardsFragmentDirections.actionNavigationRewardsToAuthPhoneInputFragment(Listener(source = "rewards", listener = {})))
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.login_rewards),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}