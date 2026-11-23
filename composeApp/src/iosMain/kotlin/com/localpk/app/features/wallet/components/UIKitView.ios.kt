package com.localpk.app.features.wallet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import com.localpk.app.features.cart.data.PaymentMethod
import platform.UIKit.UIView

@Composable
fun UIKitView(
    uiView: UIView?,
    modifier: Modifier = Modifier,
    paymentType: String
) {
    if (uiView != null) {
        UIKitView(
            factory = { uiView },
            modifier = modifier
                .fillMaxWidth()
                .then(
                    if (paymentType == PaymentMethod.CARD.method) {
                        Modifier
                            .height(200.dp)
                    } else {
                        Modifier
                            .height(85.dp)
                    }
                )
                .background(Color.White)
        )
    }
} 