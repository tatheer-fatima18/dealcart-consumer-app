package com.localpk.app.features.checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.localpk.app.features.cart.data.PaymentMethodParams
import com.localpk.app.theming.DIVIDER_COLOR

@Composable
fun PaymentMethodListing(
    paymentMethods: List<PaymentMethodParams>,
    onPaymentMethodSelected: (PaymentMethodParams) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(12.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            LazyColumn {
                itemsIndexed(paymentMethods) { index, paymentMethod ->
                    PaymentMethodItem(paymentMethodItem = paymentMethod) { selectedPaymentMethod ->
                        onPaymentMethodSelected(selectedPaymentMethod)

                        paymentMethods.forEach {
                            it.isSelected.value = it == selectedPaymentMethod
                        }

                    }
                    if (index < paymentMethods.size - 1) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = DIVIDER_COLOR
                        )
                    }
                }
            }
        }
    }
}