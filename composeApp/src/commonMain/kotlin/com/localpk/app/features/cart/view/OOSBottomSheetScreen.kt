package com.localpk.app.features.cart.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.localpk.app.features.cart.components.OOSBottomSheetContent
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.orders.data.GlobalEvents
import org.koin.compose.viewmodel.koinViewModel

class OOSBottomSheetScreen : Screen {

    @Composable
    override fun Content() {
        val cartViewModel = koinViewModel<CartViewModel>()
        val argsOOSBottomSheet by cartViewModel.argsOOSBottomSheet.collectAsState()
        Surface(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
        ) {
            if (argsOOSBottomSheet != null) {
                OOSBottomSheetContent(
                    argsData = argsOOSBottomSheet!!
                )
            }
        }
    }
}