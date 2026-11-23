package com.localpk.app.features.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.components.SummaryGroupItem
import com.localpk.app.features.cart.components.SummaryItem
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.features.orders.data.OrderDetailModel
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.default
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.bill_summary
import dealcart.composeapp.generated.resources.coins
import dealcart.composeapp.generated.resources.contribution
import dealcart.composeapp.generated.resources.discounts
import dealcart.composeapp.generated.resources.grand_total
import dealcart.composeapp.generated.resources.handling_fee
import dealcart.composeapp.generated.resources.ic_bill_line
import dealcart.composeapp.generated.resources.ic_coins_summary
import dealcart.composeapp.generated.resources.ic_delivery_cart_summary
import dealcart.composeapp.generated.resources.ic_handling_fee
import dealcart.composeapp.generated.resources.ic_promo_cart_summary
import dealcart.composeapp.generated.resources.packaging
import dealcart.composeapp.generated.resources.percentage_circle
import dealcart.composeapp.generated.resources.platform
import dealcart.composeapp.generated.resources.promos
import dealcart.composeapp.generated.resources.subtotal
import dealcart.composeapp.generated.resources.support_icon
import org.jetbrains.compose.resources.stringResource

@Composable
fun SummaryComponent(
    data: OrderDetailModel,
    platformFeeName: String,
    packagingFeeName: String
) {
    var isHandlingFeeOpen by remember { mutableStateOf(false) }
    var isDiscountsOpen by remember { mutableStateOf(false) }
    val totalHandlingFee =
        data.platformFee.getValueOrDefault() + data.packagingFee.getValueOrDefault() + data.deliveryFee.getValueOrDefault()
    val totalDiscount =
        data.promoCodeDiscount.getValueOrDefault()
            .toDouble() + data.dcCoinDiscount.getValueOrDefault().toDouble()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = BorderPrimaryDefault,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.bill_summary),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    color = TextSubdued
                )
            )
            SummaryItem(
                icon = Res.drawable.ic_bill_line,
                value = data.orderTotalAmountPacked.formatPKRCurrency(),
                title = stringResource(Res.string.subtotal)
            )
            Column(
                modifier = Modifier.clickable {
                    isHandlingFeeOpen = isHandlingFeeOpen.not()
                },
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SummaryGroupItem(
                    icon = Res.drawable.ic_handling_fee,
                    value = "Rs. $totalHandlingFee",
                    title = Res.string.handling_fee
                )
                if (data.platformFee.getValueOrDefault() > ZERO_INT && isHandlingFeeOpen) {
                    SummaryItem(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        icon = Res.drawable.platform,
                        value = "Rs. ${data.platformFee}",
                        title = platformFeeName.default()
                    )
                }
                if (data.packagingFee.getValueOrDefault() > ZERO_INT && isHandlingFeeOpen) {
                    SummaryItem(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        icon = Res.drawable.packaging,
                        value = "Rs. ${data.packagingFee}",
                        title = packagingFeeName.default()
                    )
                }
                if (isHandlingFeeOpen && data.deliveryFee.getValueOrDefault() > ZERO_INT) {
                    SummaryItem(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        icon = Res.drawable.ic_delivery_cart_summary,
                        value = "Rs. ${data.deliveryFee}",
                        title = if (data.deliveryType == DeliveryType.EXPRESS.key) DeliveryType.EXPRESS.displayName else "Delivery"
                    )
                }
            }
            if (data.dcCoinDiscount.getValueOrDefault() > ZERO_LONG || data.promoCodeDiscount.getValueOrDefault() > ZERO_LONG)
                Column(
                    modifier = Modifier.clickable {
                        isDiscountsOpen = isDiscountsOpen.not()
                    },
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SummaryGroupItem(
                        icon = Res.drawable.percentage_circle,
                        value = "Rs. ${data.totalDiscount}",
                        title = Res.string.discounts
                    )
                    if (data.promoCodeDiscount.getValueOrDefault() > ZERO_LONG && isDiscountsOpen) {
                        SummaryItem(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            icon = Res.drawable.ic_promo_cart_summary,
                            value = "Rs. ${data.promoCodeDiscount.getValueOrDefault()}",
                            title = stringResource(Res.string.promos)
                        )
                    }
                    if (data.dcCoinDiscount.getValueOrDefault() > ZERO_LONG && isDiscountsOpen) {
                        SummaryItem(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            icon = Res.drawable.ic_coins_summary,
                            value = "Rs. ${data.dcCoinDiscount}",
                            title = stringResource(Res.string.coins)
                        )
                    }
                }

            if (data.optionalContributionFee.getValueOrDefault() > 0) {
                SummaryItem(
                    icon = Res.drawable.support_icon,
                    value = "Rs. ${data.optionalContributionFee}",
                    title = stringResource(Res.string.contribution)
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 2.dp),
                color = BorderPrimaryDefault,
                thickness = 1.dp
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.grand_total),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = TextSubdued
                    )
                )
                Text(
                    text = data.orderTotal.formatPKRCurrency(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = TextSubdued
                    )
                )
            }
        }
    }
}