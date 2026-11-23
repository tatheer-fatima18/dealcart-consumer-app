package com.localpk.app.features.checkout.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.PaymentMethod
import com.localpk.app.features.cart.data.PaymentMethodParams
import com.localpk.app.theming.PURPLE_COLOR
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.visaa
import org.jetbrains.compose.resources.painterResource

@Composable
fun PaymentMethodItem(
    paymentMethodItem: PaymentMethodParams,
    onItemSelected: (PaymentMethodParams) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable {
                onItemSelected(paymentMethodItem)
                paymentMethodItem.select()
            }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = paymentMethodItem.isSelected.value,
            onClick = {
                onItemSelected(paymentMethodItem)
                paymentMethodItem.select()
            },
            colors = RadioButtonDefaults.colors(selectedColor = PURPLE_COLOR),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .height(20.dp)
                .width(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))
        Image(
            painter = painterResource(paymentMethodItem.imageRes),
            contentDescription = null, modifier = Modifier.height(35.dp)
        )
        if (paymentMethodItem.paymentType == PaymentMethod.CARD.method) {
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(Res.drawable.visaa),
                contentDescription = null, modifier = Modifier.height(35.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center
        ) {
            Column {
                Text(
                    modifier = Modifier,
                    text = paymentMethodItem.name,
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFontFamily()
                )
            }
        }
    }
}