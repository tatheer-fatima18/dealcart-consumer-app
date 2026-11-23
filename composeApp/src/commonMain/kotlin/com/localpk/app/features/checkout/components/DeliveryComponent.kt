package com.localpk.app.features.checkout.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.formatCartDeliveryDate
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.express_del
import dealcart.composeapp.generated.resources.standard_del
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.painterResource

@Composable
fun DeliveryComponent(
    formattedDeliveryDate: String,
    deliveryType: DeliveryType,
    formattedSameDayDeliveryFee: String,
    onClick: (DeliveryType) -> Unit
) {
    val selectedOption = remember { mutableStateOf(deliveryType) }
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(
                color = Color.White,
                RoundedCornerShape(8.dp)
            )
            .padding(all = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Delivery Options",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    color = TextSubdued,
                )
            )

            Spacer(modifier = Modifier.width(3.dp))

            Image(
                painter = painterResource(Res.drawable.express_del),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
            )

            Image(
                painter = painterResource(Res.drawable.standard_del),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
            )
        }

        Spacer(Modifier.height(4.dp))

        DeliveryOptionItem(
            icon = Res.drawable.express_del,
            title = DeliveryType.EXPRESS.displayName,
            subtext = today.formatCartDeliveryDate(),
            price = formattedSameDayDeliveryFee,
            selected = selectedOption.value == DeliveryType.EXPRESS,
            onClick = {
                selectedOption.value = DeliveryType.EXPRESS
                onClick(DeliveryType.EXPRESS)
            },
            isIconNeeded = false
        )

        Spacer(Modifier.height(4.dp))

        // Standard Delivery
        DeliveryOptionItem(
            icon = Res.drawable.standard_del,
            title = DeliveryType.STANDARD.displayName,
            subtext = formattedDeliveryDate,
            price = "Free",
            selected = selectedOption.value == DeliveryType.STANDARD,
            onClick = {
                selectedOption.value = DeliveryType.STANDARD
                onClick(DeliveryType.STANDARD)
            },
            isIconNeeded = false
        )
    }
}
