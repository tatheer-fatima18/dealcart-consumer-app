package com.localpk.app.features.checkout.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.localpk.app.features.cart.data.CardDetails
import com.localpk.app.features.cart.data.CardPaymentScreenArgs
import com.localpk.app.features.checkout.components.CheckoutPaymentBottomSheetContent
import com.localpk.app.features.checkout.components.getPaymentMethodId
import org.koin.compose.viewmodel.koinViewModel

class CheckoutPaymentBottomSheetScreen(
    private val encryptionKey: String?,
    private val clientSecret: String?,
    val paymentType: String
) : Screen {

    @Composable
    override fun Content() {
        val checkoutViewModel = koinViewModel<CartCheckoutViewModel>()

        val args = CardPaymentScreenArgs(
            cardDetails = CardDetails(encryptionKey, clientSecret),
            paymentType = paymentType
        )

        val wasHandledByPaymentFlow = remember { mutableStateOf(false) }

        // Handle iOS swipe back and other disposal scenarios
        DisposableEffect(Unit) {
            onDispose {
                if (!wasHandledByPaymentFlow.value) {
                    checkoutViewModel.triggerRedirect(
                        redirectToPaymentLoadingScreen = false,
                        getPaymentMethodId(args.paymentType),
                        args.paymentType
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight().statusBarsPadding()
        ) {
            CheckoutPaymentBottomSheetContent(
                args = args,
                triggerRedirect = {
                    wasHandledByPaymentFlow.value = true
                    checkoutViewModel.triggerRedirect(
                        redirectToPaymentLoadingScreen = false,
                        getPaymentMethodId(args.paymentType),
                        args.paymentType
                    )
                },
                onNavigateToResponse = {
                    wasHandledByPaymentFlow.value = true
                    checkoutViewModel.triggerRedirect(
                        redirectToPaymentLoadingScreen = true,
                        getPaymentMethodId(args.paymentType),
                        args.paymentType
                    )
                },
                onUserInitiatedBack = {
                    wasHandledByPaymentFlow.value = true
                    checkoutViewModel.triggerRedirect(
                        redirectToPaymentLoadingScreen = false,
                        getPaymentMethodId(args.paymentType),
                        args.paymentType
                    )
                }
            )
        }
    }
}