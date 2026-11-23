package com.localpk.app.features.wallet.view

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import com.localpk.app.features.rewards.viewModel.CoinsHistoryViewModel
import com.localpk.app.features.wallet.components.WalletHistoryScreenContent
import com.localpk.app.utils.FIRST_PAGE
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.WALLET_HISTORY_LIMIT
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class WalletHistoryScreen : Screen {

    @Composable
    override fun Content() {

        val coinsHistoryViewModel = koinViewModel<CoinsHistoryViewModel>()
        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()

        val walletHistoryList by coinsHistoryViewModel.coinsHistory.collectAsState(initial = emptyList())
        val totalCoins by coinsHistoryViewModel.totalCoins.collectAsState()

        LaunchedEffect(Unit) {
            if (sharedPreferencesManager.isLoggedIn())
                coinsHistoryViewModel.currentPage = FIRST_PAGE
            coinsHistoryViewModel.fetchCoinsHistory(
                source = "dcpay",
                limit = WALLET_HISTORY_LIMIT,
                page = coinsHistoryViewModel.currentPage
            )
        }

        fun onLoadMore() {
            coinsHistoryViewModel.currentPage++
            coinsHistoryViewModel.fetchCoinsHistory(
                source = "dcpay",
                limit = WALLET_HISTORY_LIMIT,
                page = coinsHistoryViewModel.currentPage
            )
        }
        StatusBarIconColor(backgroundColor = Color.White)
        Surface {
            WalletHistoryScreenContent(
                walletHistoryList = walletHistoryList,
                totalCoins = totalCoins,
                onLoadMore = { onLoadMore() }
            )
        }
    }
}