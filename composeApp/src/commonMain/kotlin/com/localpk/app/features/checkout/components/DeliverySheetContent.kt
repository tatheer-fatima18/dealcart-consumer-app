package com.localpk.app.features.checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextPrimary
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.formatCartDeliveryDate
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.express_del
import dealcart.composeapp.generated.resources.standard_del
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Composable
fun DeliverySheetContent(
    formattedDeliveryDate: String,
    deliveryType: DeliveryType,
    formattedSameDayDeliveryFee: String = EMPTY_STRING,
    onClose: (DeliveryType) -> Unit
) {
    val selectedOption = remember { mutableStateOf(deliveryType) }
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "Choose delivery option",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = TextPrimary,
            fontFamily = PoppinsFontFamily(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 12.dp)
        )

        Spacer(Modifier.height(8.dp))

        // Express Delivery
        DeliveryOptionItem(
            icon = Res.drawable.express_del,
            title = DeliveryType.EXPRESS.displayName,
            subtext = today.formatCartDeliveryDate(),
            price = formattedSameDayDeliveryFee,
            selected = selectedOption.value == DeliveryType.EXPRESS,
            onClick = {
                selectedOption.value = DeliveryType.EXPRESS
                onClose(DeliveryType.EXPRESS)
            }
        )

        Spacer(Modifier.height(12.dp))

        // Standard Delivery
        DeliveryOptionItem(
            icon = Res.drawable.standard_del,
            title = DeliveryType.STANDARD.displayName,
            subtext = formattedDeliveryDate,
            price = "Free",
            selected = selectedOption.value == DeliveryType.STANDARD,
            onClick = {
                selectedOption.value = DeliveryType.STANDARD
                onClose(DeliveryType.STANDARD)
            }
        )
    }
}
