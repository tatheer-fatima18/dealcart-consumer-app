package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.background
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
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotZero
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.bill_summary
import dealcart.composeapp.generated.resources.coins
import dealcart.composeapp.generated.resources.discounts
import dealcart.composeapp.generated.resources.grand_total
import dealcart.composeapp.generated.resources.handling_fee
import dealcart.composeapp.generated.resources.ic_bill_line
import dealcart.composeapp.generated.resources.ic_coins_summary
import dealcart.composeapp.generated.resources.ic_delivery_cart_summary
import dealcart.composeapp.generated.resources.ic_handling_fee
import dealcart.composeapp.generated.resources.ic_promo_cart_summary
import dealcart.composeapp.generated.resources.percentage_circle
import dealcart.composeapp.generated.resources.platform
import dealcart.composeapp.generated.resources.platform_fee
import dealcart.composeapp.generated.resources.promos
import dealcart.composeapp.generated.resources.subtotal
import dealcart.composeapp.generated.resources.vat
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoodSummaryComponent(
    modifier: Modifier,
    formattedOrderTotal: String,
    platformFee: Int,
    deliveryFee: Int,
    promoCodeDiscount: Long,
    dcCoinDiscount: Long,
    formattedPromoCodeDiscount: String,
    formattedGrandTotal: String,
    formattedDcCoinDiscount: String,
    taxes: Double,
    formattedTax: String
) {
    var isHandlingFeeOpen by remember { mutableStateOf(false) }
    var isDiscountsOpen by remember { mutableStateOf(false) }
    val totalHandlingFee = platformFee + deliveryFee
    val totalDiscount = (promoCodeDiscount.toDouble() + dcCoinDiscount.toDouble()).formatPKRCurrency()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                RoundedCornerShape(8.dp)
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
                value = formattedOrderTotal,
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
                if (platformFee > ZERO_INT && isHandlingFeeOpen) {
                    SummaryItem(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        icon = Res.drawable.platform,
                        value = "Rs. $platformFee",
                        title = stringResource(Res.string.platform_fee)
                    )
                }

                if (isHandlingFeeOpen && deliveryFee > ZERO_INT) {
                    SummaryItem(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        icon = Res.drawable.ic_delivery_cart_summary,
                        value = "Rs. $deliveryFee",
                        title = "Delivery"
                    )
                }
            }
            if (taxes.getValueOrDefault().isNotZero()) {
                SummaryItem(
                    icon = Res.drawable.ic_bill_line,
                    value = formattedTax,
                    title = stringResource(Res.string.vat)
                )
            }
            if (dcCoinDiscount > ZERO_LONG || promoCodeDiscount > ZERO_LONG)
                Column(
                    modifier = Modifier.clickable {
                        isDiscountsOpen = isDiscountsOpen.not()
                    },
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SummaryGroupItem(
                        icon = Res.drawable.percentage_circle,
                        value = totalDiscount,
                        title = Res.string.discounts
                    )
                    if (promoCodeDiscount > ZERO_LONG && isDiscountsOpen) {
                        SummaryItem(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            icon = Res.drawable.ic_promo_cart_summary,
                            value = formattedPromoCodeDiscount,
                            title = stringResource(Res.string.promos)
                        )
                    }
                    if (dcCoinDiscount > ZERO_LONG && isDiscountsOpen) {
                        SummaryItem(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            icon = Res.drawable.ic_coins_summary,
                            value = formattedDcCoinDiscount,
                            title = stringResource(Res.string.coins)
                        )
                    }
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
                    text = formattedGrandTotal,
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