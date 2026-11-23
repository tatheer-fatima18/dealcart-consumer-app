package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.visaa
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PaymentMethodRow(
    amount: String,
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
            fontSize = 13.sp,
            lineHeight = 13.sp,
            fontFamily = PoppinsFontFamily(),
            color = TextSubdued
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = amount,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 13.sp,
            fontFamily = PoppinsFontFamily(),
            color = TextSubdued
        )
    }
}

fun PaymentDetails.hasAnyPayments(): Boolean {
    return cod != null || wallet != null || jazzcash != null || card != null
}