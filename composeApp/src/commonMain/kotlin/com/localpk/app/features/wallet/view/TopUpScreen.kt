package com.localpk.app.features.wallet.view

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.wallet.components.TopUpScreenContent
import com.localpk.app.features.wallet.data.ResponseArgs
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TopUpScreen : Screen {

    companion object {
        private val _isTopUpCompleted = MutableStateFlow<ResponseArgs?>(null)
        val isTopUpCompleted: StateFlow<ResponseArgs?> = _isTopUpCompleted

        fun updateIsTopUpCompleted(responseArgs: ResponseArgs) {
            _isTopUpCompleted.value = responseArgs
        }
    }

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        // Use collectAsState to properly observe the StateFlow
        val topUpCompleted by isTopUpCompleted.collectAsState()

        LaunchedEffect(topUpCompleted) {
            if (topUpCompleted != null) {
                navigator.push(
                    ResponseScreen(
                        amount = topUpCompleted?.amount.getValueOrDefault(),
                        success = topUpCompleted?.success.default()
                    )
                )
                _isTopUpCompleted.value = null
            }
        }
        StatusBarIconColor(backgroundColor = Color.White)
        Surface {
            TopUpScreenContent()
        }
    }

}
