package com.localpk.app.features.product_detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.productlist.data.DeliveryDateRange
import com.localpk.app.theming.AppTypography
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.delivery_days
import dealcart.composeapp.generated.resources.ic_delivery_cart_summary
import dealcart.composeapp.generated.resources.partner
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeliveryInfoComponent(
    deliveryDateRange: DeliveryDateRange?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val deliveryDateRangeText = stringResource(
            Res.string.delivery_days,
            deliveryDateRange?.deliveryStartRange.getValueOrDefault(),
            deliveryDateRange?.deliveryEndRange.getValueOrDefault()
        )
        val deliveryBgColor = TextSubduedHalf
        val deliveryText = stringResource(Res.string.partner)
        val deliverIcon = painterResource(Res.drawable.ic_delivery_cart_summary)

        Image(
            modifier = Modifier.size(22.dp),
            painter = deliverIcon,
            contentDescription = null
        )
        Text(
            modifier = Modifier.weight(1f),
            text = deliveryDateRangeText,
            style = AppTypography().bodyMedium.copy(
                fontSize = 12.sp,
                color = TextSubdued,
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            modifier = Modifier
                .background(
                    color = deliveryBgColor,
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(8.dp),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic,
                    )
                ) {
                    append("Fulfilled by")
                }
                append(" ")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append(deliveryText)
                }
            },
            style = AppTypography().bodyMedium.copy(fontSize = 12.sp, color = Color.White)
        )
    }
}