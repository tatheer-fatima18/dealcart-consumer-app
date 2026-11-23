package com.localpk.app.features.wallet.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.rewards.data.CoinsHistoryListUiData
import com.localpk.app.features.wallet.view.WalletHistoryScreen
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubduedHalf
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.dc_pay
import dealcart.composeapp.generated.resources.find_image
import dealcart.composeapp.generated.resources.navbutton_black
import dealcart.composeapp.generated.resources.recent_transactions
import dealcart.composeapp.generated.resources.see_all
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WalletScreenContent(
    walletHistoryList: List<CoinsHistoryListUiData>,
    totalCoins: String,
    isLoggedIn: Boolean
) {

    val navigator = LocalNavigator.currentOrThrow

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding())
    {
        TopNav(
            title = stringResource(Res.string.dc_pay),
            backButton = Res.drawable.navbutton_black,
            onBackClick = { navigator.pop() })

        BgBox(
            totalCoins,
            isLoggedIn
        )

        if (walletHistoryList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 44.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .width(84.dp)
                        .height(84.dp),
                    painter = painterResource(Res.drawable.find_image),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )

                Text(
                    text = if (isLoggedIn) "It’s quiet in here \uD83D\uDCA4" else "Please log in to avail your wallet services \uD83D\uDCA4 ",
                    color = ColorPrimaryText,
                    fontSize = 16.sp,
                    lineHeight = 19.2.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFontFamily(),
                    textAlign = TextAlign.Center
                )

                Text(
                    modifier = Modifier.width(233.dp),
                    text = if (isLoggedIn) "Make your first top-up to see your history here" else "Make your first payment to see your history here",
                    color = TextSubduedHalf,
                    fontSize = 12.sp,
                    lineHeight = 14.4.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = PoppinsFontFamily(),
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        } else {
            TransactionList(
                title = stringResource(Res.string.recent_transactions),
                subTitle = stringResource(Res.string.see_all),
                onClick = {
                    navigator.push(WalletHistoryScreen())
                },
                walletHistory = walletHistoryList,
                onLoadMore = {}
            )
        }
    }
}