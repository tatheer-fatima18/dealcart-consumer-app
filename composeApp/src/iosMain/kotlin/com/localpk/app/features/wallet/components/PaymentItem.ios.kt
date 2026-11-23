package com.localpk.app.features.wallet.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.localpk.app.features.cart.data.PaymentMethod

@Composable
actual fun PaymentElementView(
    paymentElement: Any?,
    modifier: Modifier
) {
    val uiView = UIViewHelper.getPaymentElementView()
    UIKitView(
        uiView = uiView,
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        paymentType = PaymentMethod.CARD.method
    )
}

@Composable
actual fun EasyPaisaPaymentElementView(
    paymentElement: Any?,
    modifier: Modifier
) {
    val uiView = UIViewHelper.getEasyPaisaElementView()
    UIKitView(
        uiView = uiView,
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        paymentType = PaymentMethod.EASYPAISA.method
    )
} 