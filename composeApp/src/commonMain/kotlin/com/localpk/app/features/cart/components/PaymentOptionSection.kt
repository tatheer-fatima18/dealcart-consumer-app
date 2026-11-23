package com.localpk.app.features.cart.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.PaymentMethod
import com.localpk.app.features.wallet.data.WalletBalance
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackgroundGhosted
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.cash_on_delivery
import dealcart.composeapp.generated.resources.easy_paisa
import dealcart.composeapp.generated.resources.easypaisa_icon
import dealcart.composeapp.generated.resources.ic_cod
import dealcart.composeapp.generated.resources.ic_jazzcash
import dealcart.composeapp.generated.resources.jazz_cash
import dealcart.composeapp.generated.resources.master_card
import dealcart.composeapp.generated.resources.visa
import dealcart.composeapp.generated.resources.visaa
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PaymentOptionSection(
    paymentMethod: String,
    jazzPhoneNumber: String,
    paymentId: Double,
    discountedCartTotal: Int?,
    isWalletEnabled: Boolean?,
    walletAmount: WalletBalance?
) {
    var paymentMethodName = Res.string.cash_on_delivery
    var paymentIcon = Res.drawable.ic_cod

    when (paymentMethod) {
        PaymentMethod.CASH_ON_DELIVERY.method -> {
            paymentMethodName = Res.string.cash_on_delivery
            paymentIcon = Res.drawable.ic_cod
        }

        PaymentMethod.CARD.method -> {
            paymentMethodName = Res.string.visa
            paymentIcon = Res.drawable.master_card

        }

        PaymentMethod.JAZZ_CASH.method -> {
            paymentMethodName = Res.string.jazz_cash
            paymentIcon = Res.drawable.ic_jazzcash
        }

        PaymentMethod.EASYPAISA.method -> {
            paymentMethodName = Res.string.easy_paisa
            paymentIcon = Res.drawable.easypaisa_icon
        }

        else -> {}
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                BorderStroke(1.dp, BorderPrimaryDefault),
                shape = RoundedCornerShape(8.dp)
            ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            if (walletAmount != null) {
                CustomRadioButton(
                    selected = !((walletAmount.Balance.getValueOrDefault() >= discountedCartTotal.getValueOrDefault()) && (isWalletEnabled.default())),
                    onClick = {}
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 5.dp),
                        text = stringResource(paymentMethodName),
                        style = TextStyle(
                            color = TextSubdued,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    if (jazzPhoneNumber.isNotEmpty() && paymentId == 1.1) {
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = "Ending with ${jazzPhoneNumber.takeLast(4)}",
                            color = TextSubdued,
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = PoppinsFontFamily()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(.1f))
            if (paymentMethod == PaymentMethod.CARD.method) {
                Image(
                    painter = painterResource(Res.drawable.visaa),
                    contentDescription = null,
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))
            }
            Image(
                painter = painterResource(paymentIcon),
                contentDescription = null,
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
            )
        }
    }
}

@Composable
fun CustomRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color = ColorPrimary,
    unselectedColor: Color = SurfaceBackgroundGhosted
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Use remember to avoid unnecessary recompositions
        val outerColor = remember(selected, selectedColor, unselectedColor) {
            if (selected) selectedColor else unselectedColor
        }
        val innerColor = remember(selected, selectedColor, unselectedColor) {
            if (selected) selectedColor else unselectedColor
        }

        // Use optimized Canvas components to prevent priority inversions
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                color = outerColor,
                style = Stroke(width = 2.5f)
            )
        }

        Canvas(modifier = Modifier.size(15.dp)) {
            drawCircle(
                color = innerColor
            )
        }
    }
}