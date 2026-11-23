package com.localpk.app.features.rewards.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.login.presentation.PhoneInputScreen
import com.localpk.app.features.promo.view.PromosScreen
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.promocodes

@Composable
fun UpperSection(isLoggedIn: Boolean) {
    val navigator = LocalNavigator.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
        ) {
            GamesCard(
                backgroundImageResource = Res.drawable.promocodes,
                onClick = {
                    if (isLoggedIn) {
                        navigator?.push(PromosScreen())
                    } else {
                        navigator?.push(
                            PhoneInputScreen(
                              navSource = ScreenSource.PROMO_LISTING.key
                            )
                        )
                    }
                },
                onEventClick = {
                    //  eventLogger.clickPromoCodesRewards()
                })
          /*  Spacer(modifier = Modifier.width(16.dp))
            GamesCard(
                backgroundImageResource = Res.drawable.sharearn,
                onClick = {
                    if (isLoggedIn) {
                        navigator?.push(ShareAndEarnScreen())
                    } else {
                        navigator?.push(
                            PhoneInputScreen(
                                navSource = ScreenSource.SHARE_AND_EARN.key
                            )
                        )
                    }
                },
                onEventClick = {
                    //   eventLogger.clickShareAndEarnRewards()
                })*/
        }

        HorizontalDivider(
            Modifier
                .padding(bottom = 16.dp)
                .padding(start = 25.dp)
                .padding(end = 16.dp)
                .fillMaxWidth()
                .height(1.dp),
                color = Color.LightGray
        )
    }
}