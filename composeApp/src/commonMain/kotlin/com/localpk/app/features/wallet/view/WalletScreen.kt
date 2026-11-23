package com.localpk.app.features.wallet.view

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import com.localpk.app.features.rewards.viewModel.CoinsHistoryViewModel
import com.localpk.app.features.wallet.components.WalletScreenContent
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.FIRST_PAGE
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class WalletScreen : Screen {

    @Composable
    override fun Content() {

        val coinsHistoryViewModel = koinViewModel<CoinsHistoryViewModel>()
        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()
        val eventLogger = koinInject<EventLogger>()

        val walletHistoryList by coinsHistoryViewModel.coinsHistory.collectAsState(initial = emptyList())
        val totalCoins by coinsHistoryViewModel.totalCoins.collectAsState()
        val isLoggedIn = sharedPreferencesManager.isLoggedIn()

        LaunchedEffect(Unit) {
            eventLogger.viewWallet()
            if (sharedPreferencesManager.isLoggedIn())
                coinsHistoryViewModel.fetchCoinsHistory(
                    source = "dcpay",
                    limit = 3,
                    page = FIRST_PAGE
                )
        }

        StatusBarIconColor(backgroundColor = Color.White)
        Surface {
            WalletScreenContent(walletHistoryList, totalCoins, isLoggedIn)
        }
    }
}