package com.localpk.app.features.wallet.view

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import com.localpk.app.features.wallet.components.ResponseScreenContent
import com.localpk.app.features.wallet.data.ResponseArgs
import com.localpk.app.utils.StatusBarIconColor

class ResponseScreen(val amount: Double, val success: Boolean) : Screen {

    @Composable
    override fun Content() {
        val args = ResponseArgs(success, amount)
        StatusBarIconColor(backgroundColor = Color.White)
        Surface {
            ResponseScreenContent(args)
        }
    }
}