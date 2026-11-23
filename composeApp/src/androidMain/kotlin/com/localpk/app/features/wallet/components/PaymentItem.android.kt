package com.localpk.app.features.wallet.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun PaymentElementView(
    paymentElement: Any?,
    modifier: Modifier
) {
    if (paymentElement != null) {
        val wrapper = paymentElement as? AndroidPaymentElementWrapper
        if (wrapper != null) {
            val androidView = wrapper.getView()
            AndroidView(
                factory = { androidView },
                modifier = modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
}

@Composable
actual fun EasyPaisaPaymentElementView(
    paymentElement: Any?,
    modifier: Modifier
) {
    if (paymentElement != null) {
        val wrapper = paymentElement as? AndroidEasyPaisaPaymentElementWrapper
        if (wrapper != null) {
            val androidView = wrapper.getView()
            AndroidView(
                factory = { androidView },
                modifier = modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
} 