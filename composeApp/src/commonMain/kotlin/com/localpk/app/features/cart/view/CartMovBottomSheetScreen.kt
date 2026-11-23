package com.localpk.app.features.cart.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.cart.components.CartMOVSection
import com.localpk.app.features.cart.data.CartMOVSheetArgs
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.checkout.view.CartCheckoutScreen
import com.localpk.app.features.home.presentation.HomeScreen
import org.koin.compose.koinInject

class CartMovBottomSheetScreen(
    private val feeNeeded: Int,
    private val nonGroceryCheck: Boolean = false,
    private val placeOrderCheck: Boolean
) : Screen {

    @Composable
    override fun Content() {
        val cartViewModel = koinInject<CartViewModel>()
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val navigator = LocalNavigator.currentOrThrow
        
        // Create CartMOVSheetArgs from primitive parameters
        val argsData = CartMOVSheetArgs(
            feeNeeded = feeNeeded,
            nonGroceryCheck = nonGroceryCheck,
            placeOrderCheck = placeOrderCheck
        )

        Surface(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
        ) {
            CartMOVSection(
                argsData = argsData,
                onConfirm = { 
                    navigator.push(HomeScreen()) 
                },
                onCancel = {
                    cartViewModel.handleMOVCancel(
                        navigateToCheckout = { navigator.push(CartCheckoutScreen()) },
                       bottomSheetNavigator = bottomSheetNavigator
                    )
                },
                onDismiss = { 
                    bottomSheetNavigator.hide() 
                }
            )
        }
    }
}