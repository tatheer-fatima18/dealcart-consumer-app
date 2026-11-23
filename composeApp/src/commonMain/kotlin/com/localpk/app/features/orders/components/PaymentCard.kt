package com.localpk.app.features.orders.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.PaymentDetails
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued
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
import dealcart.composeapp.generated.resources.visaa
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PaymentCard(
    paymentDetails: PaymentDetails,
) {
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
            if (paymentDetails.hasAnyPayments()) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(Res.string.paid),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubdued
                    )

                    val paymentRows: List<Triple<Double?, DrawableResource, StringResource>> = listOf(
                        Triple(paymentDetails.wallet, Res.drawable.pay_wallet, Res.string.dc_pay),
                        Triple(paymentDetails.cod, Res.drawable.ic_cod, Res.string.cash_on_delivery_type),
                        Triple(paymentDetails.jazzcash, Res.drawable.jc_new_logo, Res.string.jazz_cash),
                        Triple(paymentDetails.card, Res.drawable.master_card, Res.string.card)
                    )

                    paymentRows.forEach { (amount, iconRes, textRes) ->
                        amount?.let {
                            PaymentMethodRow(
                                amount = it.toInt(),
                                iconRes = iconRes,
                                textRes = textRes,
                                isCardPayment = textRes == Res.string.card
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentMethodRow(
    amount: Int,
    iconRes: DrawableResource,
    textRes: StringResource,
    isCardPayment: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        if (isCardPayment) {
            Spacer(modifier = Modifier.width(7.dp))
            Image(
                painter = painterResource(
                    Res.drawable.visaa
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = stringResource(textRes),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            fontFamily = PoppinsFontFamily(),
            color = TextSubdued
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = amount.toDouble().formatPKRCurrency(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            fontFamily = PoppinsFontFamily(),
            color = TextSubdued
        )
    }
}

fun PaymentDetails.hasAnyPayments(): Boolean {
    return cod != null || wallet != null || jazzcash != null || card != null
}