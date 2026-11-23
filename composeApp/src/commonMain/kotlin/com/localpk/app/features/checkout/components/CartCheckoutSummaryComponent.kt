package com.localpk.app.features.checkout.components

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
import com.localpk.app.features.cart.components.SummaryGroupItem
import com.localpk.app.features.cart.components.SummaryItem
import com.localpk.app.features.cart.data.CartUiData
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.features.cart.data.PaymentMethod
import com.localpk.app.features.restaurant.data.FoodCartUIData
import com.localpk.app.features.wallet.data.WalletBalance
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.ZERO_FLOAT
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.default
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.isNotZero
import com.localpk.app.utils.isNull
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.bill_summary
import dealcart.composeapp.generated.resources.cash_on_delivery_type
import dealcart.composeapp.generated.resources.contribution
import dealcart.composeapp.generated.resources.dc_pay
import dealcart.composeapp.generated.resources.discounts
import dealcart.composeapp.generated.resources.easy_paisa
import dealcart.composeapp.generated.resources.easypaisa_icon
import dealcart.composeapp.generated.resources.grand_total
import dealcart.composeapp.generated.resources.handling_fee
import dealcart.composeapp.generated.resources.ic_bill_line
import dealcart.composeapp.generated.resources.ic_coins_summary
import dealcart.composeapp.generated.resources.ic_delivery_cart_summary
import dealcart.composeapp.generated.resources.ic_handling_fee
import dealcart.composeapp.generated.resources.ic_promo_cart_summary
import dealcart.composeapp.generated.resources.icon_card
import dealcart.composeapp.generated.resources.icon_cash
import dealcart.composeapp.generated.resources.icon_jazz
import dealcart.composeapp.generated.resources.jazz_cash
import dealcart.composeapp.generated.resources.packaging
import dealcart.composeapp.generated.resources.pay_icon
import dealcart.composeapp.generated.resources.percentage_circle
import dealcart.composeapp.generated.resources.platform
import dealcart.composeapp.generated.resources.subtotal
import dealcart.composeapp.generated.resources.support_icon
import dealcart.composeapp.generated.resources.vat
import dealcart.composeapp.generated.resources.visa
import org.jetbrains.compose.resources.stringResource

@Composable
fun CartCheckoutSummaryComponent(
    cart: CartUiData?,
    packagingFeeName: String?,
    platformFeeName: String?,
    optionalFee: Int?,
    isWalletEnabled: Boolean?,
    walletAmount: WalletBalance?,
    discountedCartTotal: Int?,
    paymentMethod: String?,
    platformFee: Int,
    packagingFee: Int,
    dcDeliveryFee: Double,
    multiCityDeliveryFee: Double,
    deliveryFee: Int,
    foodCart: FoodCartUIData?,
    totalBill: Int,
    isSellerPromoApplied: Boolean,
    isMultiCityPromoApplied: Boolean,
    isDCCityPromoApplied: Boolean,
    totalPromoDiscount: Double,
    dcCoinDiscount: Double,
    dcCoinEnabled: Boolean,
    calculatePayment: (String) -> Int?,
    formattedTotalWalletBalance: String,
    formattedDcCoinDiscount: String,
    paymentType: String,
    selectedDeliveryType: DeliveryType,
) {
    var isHandlingFeeOpen by remember { mutableStateOf(false) }
    var isDiscountsOpen by remember { mutableStateOf(false) }
    val totalHandlingFee =
        platformFee + packagingFee + dcDeliveryFee + multiCityDeliveryFee + deliveryFee + if (selectedDeliveryType == DeliveryType.EXPRESS) cart?.sameDayDeliveryFee.getValueOrDefault() else 0.0
    val totalDeliveryFee =
        dcDeliveryFee + multiCityDeliveryFee + deliveryFee + if (selectedDeliveryType == DeliveryType.EXPRESS) cart?.sameDayDeliveryFee.getValueOrDefault() else 0.0
    val totalDiscount =
        totalPromoDiscount + dcCoinDiscount
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
            value = if (cart.isNotNull())
                cart?.formattedCartTotal.default()
            else foodCart?.formattedOrderTotal().default(),
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
                value = totalHandlingFee.formatPKRCurrency(),
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
        if (foodCart?.additionalFees?.valueAddedTax.getValueOrDefault()
                .isNotZero()
        ) {
            SummaryItem(
                icon = Res.drawable.ic_bill_line,
                value = foodCart?.formattedTax().default(),
                title = stringResource(Res.string.vat)
            )
        }
        if (isDCCityPromoApplied || isMultiCityPromoApplied || isSellerPromoApplied || dcCoinEnabled || totalPromoDiscount
                .isNotZero()
        )
            Column(
                modifier = Modifier.clickable {
                    isDiscountsOpen = isDiscountsOpen.not()
                },
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SummaryGroupItem(
                    icon = Res.drawable.percentage_circle,
                    value = totalDiscount.formatPKRCurrency(),
                    title = Res.string.discounts
                )
                if ((isDCCityPromoApplied || isMultiCityPromoApplied || isSellerPromoApplied || totalPromoDiscount
                        .isNotZero()) && isDiscountsOpen
                ) {
                    SummaryItem(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        icon = Res.drawable.ic_promo_cart_summary,
                        value = totalPromoDiscount.formatPKRCurrency(),
                        title = "Promos"
                    )
                }
                if (dcCoinEnabled && isDiscountsOpen) {
                    SummaryItem(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        icon = Res.drawable.ic_coins_summary,
                        value = if (foodCart?.discounts?.coinDiscount.isNotZero()) foodCart?.formattedAmountInCurrency()
                            .default() else formattedDcCoinDiscount,
                        title = "Coins"
                    )
                }
            }

        if (optionalFee.getValueOrDefault() > 0 && foodCart.isNull()) {
            SummaryItem(
                icon = Res.drawable.support_icon,
                value = optionalFee.getValueOrDefault().toDouble().formatPKRCurrency(),
                title = stringResource(Res.string.contribution)
            )
        }
        if (isWalletEnabled == true && walletAmount?.Balance.getValueOrDefault() > 0) {
            SummaryItem(
                icon = Res.drawable.pay_icon,
                value = if (walletAmount?.Balance.getValueOrDefault() > discountedCartTotal.getValueOrDefault()) totalBill.toDouble()
                    .formatPKRCurrency()
                else formattedTotalWalletBalance,
                title = stringResource(Res.string.dc_pay)
            )
        }
        calculatePayment(paymentType)
            ?.toDouble()
            ?.let {
                if (isWalletEnabled == true && walletAmount?.Balance.getValueOrDefault() < discountedCartTotal.getValueOrDefault()) {
                    SummaryItem(
                        icon = if (paymentMethod == PaymentMethod.CARD.method) Res.drawable.icon_card else if (paymentMethod == PaymentMethod.JAZZ_CASH.method) Res.drawable.icon_jazz else if (paymentMethod == PaymentMethod.EASYPAISA.method) Res.drawable.easypaisa_icon else Res.drawable.icon_cash,
                        value = it.formatPKRCurrency(),
                        title = when (paymentMethod) {
                            PaymentMethod.CARD.method -> stringResource(Res.string.visa)
                            PaymentMethod.JAZZ_CASH.method -> stringResource(
                                Res.string.jazz_cash
                            )

                            PaymentMethod.EASYPAISA.method -> stringResource(Res.string.easy_paisa)
                            PaymentMethod.CASH_ON_DELIVERY.method -> stringResource(Res.string.cash_on_delivery_type)
                            else -> EMPTY_STRING
                        }
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
                text = totalBill.toDouble().formatPKRCurrency(),
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