package com.localpk.app.features.rewards.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.BlackText
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.learn_earn_coins
import dealcart.composeapp.generated.resources.promo_codes
import dealcart.composeapp.generated.resources.share_earn
import org.jetbrains.compose.resources.stringResource

@Composable
fun StoriesSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.learn_earn_coins),
            modifier = Modifier.padding(start = 31.dp),
            style = TextStyle(
                color = BlackText,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 31.dp, vertical = 6.dp),
        ) {
            QuestStory(
                caption = stringResource(Res.string.share_earn),
                onClick = {
                   // rewardsViewModel.onHowItWorksClick()
                          },
                onEventClick = {}
            )
            QuestStory(
                caption = stringResource(Res.string.promo_codes),
                onClick = {
                  //  rewardsViewModel.onHowItWorksClick("3111")
                    }
                ,
                onEventClick = {}
            )
        }
    }
}