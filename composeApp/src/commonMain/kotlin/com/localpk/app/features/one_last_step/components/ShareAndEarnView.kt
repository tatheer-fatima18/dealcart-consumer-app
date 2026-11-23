package com.localpk.app.features.one_last_step.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.rewards.data.MilestoneRewardsInfo
import com.localpk.app.theming.DarkGrayPrimary
import com.localpk.app.theming.GreyLight
import com.localpk.app.theming.Lilac
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.ShareEarnPointerOne
import com.localpk.app.theming.ShareEarnPointerTwo
import com.localpk.app.theming.StrokeHomeScreenButtons
import com.localpk.app.theming.TextGray
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_ols_coin_first
import dealcart.composeapp.generated.resources.ic_ols_coin_second
import dealcart.composeapp.generated.resources.ic_ols_coin_third
import dealcart.composeapp.generated.resources.share_earn
import dealcart.composeapp.generated.resources.share_earn_method
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ShareAndEarnSection(
    milestoneData: MilestoneRewardsInfo?,
    onActionShareAndWin: () -> Unit = {},
    onHowItWorksClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(top = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Lilac,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.share_earn),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        lineHeight = 19.sp
                    ),
                    color = TextGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Steps
                if (milestoneData != null) {
                    StepIndicator(
                        1,
                        "Dost ke signup pe kamayen ${milestoneData.toMilestoneRewardsInfoUIData().formattedCCoinsForAppInstall} DC coins.",
                        color = ShareEarnPointerOne
                    )

                    VerticalLineSteps()
                    StepIndicator(
                        2,
                        "Signup pe apkay dost kay liye ${milestoneData.toMilestoneRewardsInfoUIData().formattedCoinsForAppSignup} DC coins.",
                        color = ShareEarnPointerTwo
                    )
                    VerticalLineSteps()
                    StepIndicator(
                        3,
                        "Dost ke pehlay order delivery pe \nkamayein ${milestoneData.toMilestoneRewardsInfoUIData().formattedCoinsForFirstOrder} DC coins.",
                        color = DarkGrayPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                SupportButton(onActionShareAndWin)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 8.dp)
                        .clickable { onHowItWorksClick() },
                    text = stringResource(Res.string.share_earn_method),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily(),
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline
                    ),
                    color = GreyLight
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Image(
            painter = painterResource(Res.drawable.ic_ols_coin_first),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = (-24).dp, x = (8).dp)

        )

        Image(
            painter = painterResource(Res.drawable.ic_ols_coin_second),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = (-15).dp, x = (-40).dp)

        )

        Image(
            painter = painterResource(Res.drawable.ic_ols_coin_third),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(y = (8).dp, x = (0).dp)

        )
    }


}

@Composable
fun VerticalLineSteps() {
    Box(
        modifier = Modifier
            .padding(start = 12.dp)
            .width(2.dp)
            .height(8.dp)
            .background(color = StrokeHomeScreenButtons)
    )
}

@Composable
fun StepIndicator(step: Int, text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .background(color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(step.toString(), color = Color.White)
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            fontFamily = PoppinsFontFamily(),
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .offset(y = (-1).dp),
            color = TextGray,
            lineHeight = 13.sp
        )
    }
}