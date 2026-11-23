package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.PaymentMethod
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextPrimary
import com.localpk.app.utils.formatPKRCurrency
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.card
import dealcart.composeapp.generated.resources.cash_on_delivery_type
import dealcart.composeapp.generated.resources.dc_pay
import dealcart.composeapp.generated.resources.ic_cod
import dealcart.composeapp.generated.resources.jazz_cash
import dealcart.composeapp.generated.resources.jc_new_logo
import dealcart.composeapp.generated.resources.master_card
import dealcart.composeapp.generated.resources.paid
import dealcart.composeapp.generated.resources.pay_wallet
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoodPaymentCard(paymentType: String, amount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BorderPrimaryDefault,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(Res.string.paid),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        lineHeight = 15.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    when (paymentType) {
                        PaymentMethod.WALLET.method -> PaymentMethodRow(
                            amount = amount.toDouble().formatPKRCurrency(),
                            iconRes = Res.drawable.pay_wallet,
                            textRes = Res.string.dc_pay,
                            isCardPayment = false
                        )

                        PaymentMethod.CASH_ON_DELIVERY.method-> PaymentMethodRow(
                            amount = amount.toDouble().formatPKRCurrency(),
                            iconRes = Res.drawable.ic_cod,
                            textRes = Res.string.cash_on_delivery_type,
                            isCardPayment = false
                        )

                        PaymentMethod.JAZZ_CASH.method -> PaymentMethodRow(
                            amount = amount.toDouble().formatPKRCurrency(),
                            iconRes = Res.drawable.jc_new_logo,
                            textRes = Res.string.jazz_cash,
                            isCardPayment = false
                        )

                        PaymentMethod.CARD.method -> PaymentMethodRow(
                            amount = amount.toDouble().formatPKRCurrency(),
                            iconRes = Res.drawable.master_card,
                            textRes = Res.string.card,
                            isCardPayment = true
                        )
                    }
                }
        }
    }
}
