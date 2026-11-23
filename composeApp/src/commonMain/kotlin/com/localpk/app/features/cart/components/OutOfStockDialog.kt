package com.localpk.app.features.cart.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.localpk.app.features.cart.data.CartBundle

@Composable
fun OutOfStockDialog(
    outOfStockData: CartBundle? = null,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(shape = RoundedCornerShape(12.dp), color = Color.White) {
            BundleScreen(outOfStockData, onDismissRequest)
        }
    }
}