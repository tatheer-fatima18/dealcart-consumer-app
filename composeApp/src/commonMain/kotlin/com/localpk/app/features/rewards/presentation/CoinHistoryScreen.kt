package com.localpk.app.features.rewards.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.login.presentation.PhoneInputScreen
import com.localpk.app.features.rewards.components.CoinsHistoryItem
import com.localpk.app.features.rewards.components.TopBar
import com.localpk.app.features.rewards.data.CoinsHistoryListUiData
import com.localpk.app.features.rewards.viewModel.CoinsHistoryViewModel
import com.localpk.app.features.share_to_friend.presentation.ShareToFriendScreen
import com.localpk.app.theming.ColorDividers
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.ProfileTextGray
import com.localpk.app.theming.Yellow
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.formatDate
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.coins_history
import dealcart.composeapp.generated.resources.no_coins_history_available
import dealcart.composeapp.generated.resources.no_promos_icon
import dealcart.composeapp.generated.resources.your_coins
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class CoinHistoryScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<CoinsHistoryViewModel>()
        val isLoaded by viewModel.isCoinsHistoryLoaded.collectAsState()
        val coinsHistoryList by viewModel.coinsHistory.collectAsState()
        val totalCoins by viewModel.totalCoins.collectAsState()
        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()
        val navigator = LocalNavigator.current
        val coinsDateList = remember(coinsHistoryList) {
            val dates = arrayListOf<String>()
            coinsHistoryList.forEach {
                if (dates.contains(it.coinsDate.formatDate()).not() && it.coinAmount > 0) {
                    dates.add(it.coinsDate.formatDate())
                }
            }
            dates
        }

        // Fetch on first composition
        LaunchedEffect(Unit) {
            viewModel.fetchCoinsHistory(
                source = EMPTY_STRING,
                limit = 20, // PAGE_LIMIT, replace with your constant if needed
                page = 1    // FIRST_PAGE, replace with your constant if needed
            )
        }
        StatusBarIconColor(backgroundColor = Color.Blue)
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Crossfade(
                targetState = isLoaded,
                label = EMPTY_STRING
            ) { target ->
                when (target) {
                    true -> {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            TopBar(
                                onBackClick = { navigator?.pop() },
                                title = stringResource(Res.string.coins_history),
                                onShareClick = {
                                    if (sharedPreferencesManager.isLoggedIn()) {
                                        navigator?.push(ShareToFriendScreen())
                                    } else {
                                        navigator?.push(
                                            PhoneInputScreen(
                                                navSource = ScreenSource.SHARE_TO_FRIEND.key
                                            )
                                        )
                                    }
                                }
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        modifier = Modifier.padding(start = 16.dp),
                                        text = stringResource(Res.string.your_coins),
                                        style = TextStyle(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            fontFamily = PoppinsFontFamily(),
                                            textAlign = TextAlign.Left,
                                            color = Color.White,
                                        ),
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 16.dp),
                                        text = totalCoins,
                                        style = TextStyle(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 34.sp,
                                            fontFamily = PoppinsFontFamily(),
                                            color = Yellow,
                                            textAlign = TextAlign.Left,
                                        ),
                                    )
                                }
                            }
                            if (coinsHistoryList.isNotEmpty()) {
                                CoinsHistoryList(coinsHistoryList, coinsDateList)
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.no_promos_icon),
                                        contentDescription = null
                                    )
                                    Text(
                                        text = stringResource(Res.string.no_coins_history_available),
                                        style = TextStyle(
                                            fontFamily = PoppinsFontFamily(),
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    )
                                }
                            }
                        }
                    }

                    false -> {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            TopBar(
                                onBackClick = { navigator?.pop() },
                                title = stringResource(Res.string.coins_history),
                                onShareClick = {
                                    if (sharedPreferencesManager.isLoggedIn()) {
                                        navigator?.push(ShareToFriendScreen())
                                    } else {
                                        navigator?.push(
                                            PhoneInputScreen(
                                                navSource = ScreenSource.SHARE_TO_FRIEND.key
                                            )
                                        )
                                    }
                                }
                            ) {}
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Loading! Please wait",
                                    style = TextStyle(
                                        fontFamily = PoppinsFontFamily(),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CoinsHistoryList(
    coinsHistory: List<CoinsHistoryListUiData>,
    coinsDateList: List<String>
) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp),
    ) {
        itemsIndexed(coinsDateList) { index, it ->
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = it,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFontFamily(),
                    textAlign = TextAlign.Center,
                    color = ProfileTextGray
                ),
            )
            coinsHistory.filter { item -> item.coinsDate.formatDate() == it && item.coinAmount > 0 }
                .forEach { data ->
                    CoinsHistoryItem(data)
                }
            if (coinsDateList.size > index + 1)
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    1.dp,
                    ColorDividers
                )
        }
    }
} 