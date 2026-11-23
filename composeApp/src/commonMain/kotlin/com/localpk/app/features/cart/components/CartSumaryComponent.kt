package com.localpk.app.features.cart.components

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
import com.localpk.app.features.cart.data.CartUiData
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.ZERO_FLOAT
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.default
import com.localpk.app.utils.formatPKRCurrency
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.bill_summary
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
import dealcart.composeapp.generated.resources.subtotal
import org.jetbrains.compose.resources.stringResource

@Composable
fun CartSummaryComponent(
    cart: CartUiData,
    packagingFeeName: String?,
    platformFeeName: String?,
    discountedCartTotal: String,
    platformFee: Int,
    packagingFee: Int,
    dcCoinDiscount: Double,
    isDiscountSwitchChecked: Boolean,
    formattedDcCoinDiscount: String,
    selectedDeliveryType: DeliveryType,
) {
    var isHandlingFeeOpen by remember { mutableStateOf(false) }
    var isDiscountsOpen by remember { mutableStateOf(false) }
    val totalHandlingFee =
        (platformFee + packagingFee + cart.dcDeliveryFee + cart.multiCityDeliveryFee + if (selectedDeliveryType == DeliveryType.EXPRESS) cart.sameDayDeliveryFee else 0.0).formatPKRCurrency()
    val totalDeliveryFee =
        cart.dcDeliveryFee + cart.multiCityDeliveryFee + if (selectedDeliveryType == DeliveryType.EXPRESS) cart.sameDayDeliveryFee else 0.0
    val totalDiscount =
        (cart.totalPromoDiscount(
            cart.dcCityCart?.isEnabled == true,
            cart.multiCityCart?.isEnabled == true
        ) + dcCoinDiscount).formatPKRCurrency()
    val isDCCityPromoApplied =
        cart.dcCityCart?.PromoDetails?.isValidated == true
    val isMultiCityPromoApplied =
        cart.multiCityCart?.PromoDetails?.isValidated == true
    val isSellerPromoApplied =
        cart.sellerCarts?.any { it.PromoDetails?.isValidated == true } == true
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(
                color = Color.White,
                RoundedCornerShape(8.dp)
            )
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
            value = cart.formattedCartTotal,
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
                value = totalHandlingFee,
                title = Res.string.handling_fee
            )
            if (platformFee > ZERO_INT && isHandlingFeeOpen) {
                SummaryItem(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    icon = Res.drawable.platform,
                    value = platformFee.toDouble().formatPKRCurrency(),
                    title = platformFeeName.default()
                )
            }
            if (packagingFee > ZERO_INT && isHandlingFeeOpen) {
                SummaryItem(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    icon = Res.drawable.packaging,
                    value = packagingFee.toDouble().formatPKRCurrency(),
                    title = packagingFeeName.default()
                )
            }
            if (isHandlingFeeOpen && totalDeliveryFee > ZERO_FLOAT) {
                SummaryItem(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    icon = Res.drawable.ic_delivery_cart_summary,
                    value = totalDeliveryFee.formatPKRCurrency(),
                    title = "Delivery"
                )
            }
        }
        if (isDCCityPromoApplied || isMultiCityPromoApplied || isSellerPromoApplied || isDiscountSwitchChecked)
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
                if ((isDCCityPromoApplied || isMultiCityPromoApplied || isSellerPromoApplied) && isDiscountsOpen) {
                    SummaryItem(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        icon = Res.drawable.ic_promo_cart_summary,
                        value = cart.totalPromoDiscount(
                            cart.dcCityCart?.isEnabled == true,
                            cart.multiCityCart?.isEnabled == true
                        ).formatPKRCurrency(),
                        title = "Promos"
                    )
                }
                if (isDiscountSwitchChecked && isDiscountsOpen) {
                    SummaryItem(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        icon = Res.drawable.ic_coins_summary,
                        value = formattedDcCoinDiscount,
                        title = "Coins"
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
                text = discountedCartTotal,
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