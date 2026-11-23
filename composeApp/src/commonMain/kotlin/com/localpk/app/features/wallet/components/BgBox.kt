package com.localpk.app.features.wallet.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.login.presentation.PhoneInputScreen
import com.localpk.app.features.wallet.view.TopUpScreen
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.available_balance
import dealcart.composeapp.generated.resources.dc_pay
import dealcart.composeapp.generated.resources.login_to_continue
import dealcart.composeapp.generated.resources.top_up
import dealcart.composeapp.generated.resources.wallet_bg
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BgBox(coins: String, isLoggedIn: Boolean) {
    val navigator = LocalNavigator.currentOrThrow
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .wrapContentHeight().fillMaxWidth()
            .clip(shape = RoundedCornerShape(12.dp, 12.dp, 12.dp, 12.dp)),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            modifier = Modifier.matchParentSize(),
            painter = painterResource(Res.drawable.wallet_bg),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 4.dp),
                painter = painterResource(Res.drawable.dc_pay),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Column(
                modifier = Modifier.padding(vertical = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.available_balance),
                    color = Color.White,
                    fontSize = 14.sp,
                    lineHeight = 16.8.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFontFamily()
                )

                Text(
                    text = coins,
                    color = Color.White,
                    fontSize = 44.sp,
                    lineHeight = 44.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFontFamily()
                )
            }

            if (!isLoggedIn) {
                TopUpButton(
                    textColor = TextSubdued,
                    text = stringResource(Res.string.login_to_continue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            color = Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            navigator.push(
                                PhoneInputScreen(
                                    navSource = ScreenSource.WALLET.key
                                )
                            )
                        })
            } else if (RemoteConfigs.isTopUpEnabled()) {
                TopUpButton(
                    textColor = TextSubdued,
                    text = stringResource(Res.string.top_up),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            color = Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            //  eventLogger.initiateTopUp(TopUpSource.WALLET.source)
                            navigator.push(TopUpScreen())
                        }
                )
            }
        }
    }
}