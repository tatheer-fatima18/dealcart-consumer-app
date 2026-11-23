package com.localpk.app.features.wallet.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
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
import com.localpk.app.features.rewards.data.CoinsHistoryListUiData
import com.localpk.app.features.wallet.view.TopUpScreen
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.EMPTY_STRING
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.all_transactions
import dealcart.composeapp.generated.resources.available_balance
import dealcart.composeapp.generated.resources.dc_pay_icon
import dealcart.composeapp.generated.resources.navbutton_black
import dealcart.composeapp.generated.resources.top_up
import dealcart.composeapp.generated.resources.wallet_bg
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WalletHistoryScreenContent(
    walletHistoryList: List<CoinsHistoryListUiData>,
    totalCoins: String,
    onLoadMore: () -> Unit
) {

    val navigator = LocalNavigator.currentOrThrow

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding())
    {
        TopNav(
            title = EMPTY_STRING,
            backButton = Res.drawable.navbutton_black,
            onBackClick = { navigator.pop() })

        Box(
            modifier = Modifier
                .then(
                    if (RemoteConfigs.isTopUpEnabled()
                    ) Modifier.wrapContentWidth() else Modifier.fillMaxWidth()
                )
                .wrapContentHeight()
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .clip(shape = RoundedCornerShape(12.dp, 12.dp, 12.dp, 12.dp)),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                modifier = Modifier.matchParentSize(),
                painter = painterResource(Res.drawable.wallet_bg),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = 8.dp),
                    painter = painterResource(Res.drawable.dc_pay_icon),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
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
                        text = totalCoins,
                        color = Color.White,
                        fontSize = 18.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamily()
                    )
                }

                if (RemoteConfigs.isTopUpEnabled()
                ) {
                    Spacer(modifier = Modifier.weight(.1f))

                    TopUpButton(
                        textColor = TextSubdued,
                        text = stringResource(Res.string.top_up),
                        modifier = Modifier
                            .width(98.dp)
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
                                navigator.push(TopUpScreen())
//                                eventLogger.initiateTopUp(TopUpSource.WALLET.source)
                            },
                    )
                }
            }
        }

        if (walletHistoryList.isNotEmpty()) {
            TransactionList(
                title = stringResource(Res.string.all_transactions),
                subTitle = EMPTY_STRING,
                onClick = {},
                walletHistory = walletHistoryList,
                onLoadMore = { onLoadMore() }
            )
        }
    }
}