package com.localpk.app.features.orders.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.cart.data.CartOrderType
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.features.orders.data.OrderDeliveryStatus
import com.localpk.app.features.orders.data.OrderItemStatus
import com.localpk.app.features.orders.data.OrderStatus
import com.localpk.app.features.orders.data.OrderUiData
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackgroundWaning
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.theming.TextSuccess
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.capitalizeFirstLetter
import com.localpk.app.utils.default
import com.localpk.app.utils.formatCartDeliveryDate
import com.localpk.app.utils.formatPKRCurrency
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.apkay_order_mai_kuch_tabdeeli_ayi_hai_order_details_check_karein
import dealcart.composeapp.generated.resources.cancelled_on
import dealcart.composeapp.generated.resources.delayed
import dealcart.composeapp.generated.resources.deliver_by
import dealcart.composeapp.generated.resources.delivered_on
import dealcart.composeapp.generated.resources.four_add_on
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder
import dealcart.composeapp.generated.resources.out_for_Delivery
import dealcart.composeapp.generated.resources.returned_on
import dealcart.composeapp.generated.resources.your_order_redeliver
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringResource


@Composable
fun OrderItemLayout(
    orderData: OrderUiData,
    orderStatus: String?,
    onOrderClick: () -> Unit,
    isOrderDelivered: Boolean,
    isOrderRated: Boolean,
    onReviewClick: () -> Unit,
    rating: Float?,
) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = Color.White)
            .border(
                BorderStroke(1.dp, color = BorderPrimaryDefault),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onOrderClick() }
            .padding(all = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = orderData.id.toString(),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = TextSubduedHalf,
                )
            )
            if (orderStatus == OrderItemStatus.ALL.itemStatus || orderStatus == EMPTY_STRING) {
                val textColor = when (orderData.orderStatus) {
                    OrderStatus.DELIVERED.status -> TextSuccess
                    OrderStatus.CANCELLED.status -> IconDanger
                    OrderStatus.RETURNED.status -> TextSubdued
                    else -> ButtonSurfaceBrand

                }
                Text(
                    text = if (!orderData.deliveredAt.isNullOrEmpty()) OrderStatus.DELIVERED.status.capitalizeFirstLetter() else if (!orderData.cancelledAt.isNullOrEmpty()) OrderStatus.CANCELLED.status.capitalizeFirstLetter()
                    else if (!orderData.dispatchedAt.isNullOrEmpty()) stringResource(Res.string.out_for_Delivery) else if (!orderData.packedAt.isNullOrEmpty()) OrderStatus.PACKED.status.capitalizeFirstLetter() else if (!orderData.confirmedAt.isNullOrEmpty()) OrderStatus.CONFIRMED.status.capitalizeFirstLetter() else orderData.orderStatus.capitalizeFirstLetter(),
                    style = TextStyle(
                        color = textColor,
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                    )
                )
            }
            Spacer(modifier = Modifier.weight(.1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "${orderData.totalItems} item(s)",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubduedHalf,
                    )
                )
                VerticalDivider(
                    modifier = Modifier
                        .height(11.dp)
                        .align(Alignment.CenterVertically),
                    thickness = 1.dp,
                    color = BorderPrimaryDefault
                )
                Text(
                    text = orderData.orderTotal.formatPKRCurrency(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = TextSubdued,
                    )
                )
            }
        }

        if (
            orderData.deliveredAt?.isNotEmpty() == true || orderData.deliveryStartRange.isNotEmpty() || orderData.cancelledAt?.isNotEmpty() == true || orderData.returnedAt?.isNotEmpty() == true || orderData.deliveryDate.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(if (orderData.orderStatus == OrderStatus.DELIVERED.status) Res.string.delivered_on else if (orderData.orderStatus == OrderStatus.CANCELLED.status) Res.string.cancelled_on else if (orderData.orderStatus == OrderStatus.RETURNED.status) Res.string.returned_on else Res.string.deliver_by),
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = TextSubdued,
                )
                if (orderData.deliveryType == DeliveryType.STANDARD.key) {
                    Text(
                        text = orderData.deliveryDate.ifEmpty { if (orderData.orderStatus == OrderStatus.DELIVERED.status) orderData.deliveredAt.default() else if (orderData.orderStatus == OrderStatus.CANCELLED.status) orderData.cancelledAt.default() else if (orderData.orderStatus == OrderStatus.RETURNED.status) orderData.returnedAt.default() else orderData.formattedDeliveryDate() },
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        color = TextSubdued,
                    )
                } else {
                    Text(
                        text = today.formatCartDeliveryDate(),
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        color = TextSubdued,
                    )
                }
                if ((orderData.deliveryStartTime.isNotEmpty() && orderData.deliveryEndTime.isNotEmpty() && RemoteConfigs.enableDisplayOfDelivery() && orderData.deliverBy == CartOrderType.DEAL_CART.key) || orderData.deliveredTime.isNotEmpty() || orderData.cancelledTime.isNotEmpty() || orderData.returnedTime.isNotEmpty()) {
                    Text(
                        text = " .  ${if (orderData.orderStatus == OrderStatus.DELIVERED.status) orderData.deliveredTime else if (orderData.orderStatus == OrderStatus.CANCELLED.status) orderData.cancelledTime else if (orderData.orderStatus == OrderStatus.RETURNED.status) orderData.returnedTime else if (orderData.deliverBy == CartOrderType.DEAL_CART.key) orderData.formattedDeliveryTime() else EMPTY_STRING}",
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        color = TextSubdued,
                    )
                }

                if (orderData.orderDeliveryStatus == OrderDeliveryStatus.DELAYED.status && RemoteConfigs.enableDisplayOfParcel()) {
                    Text(
                        modifier = Modifier
                            .background(
                                color = SurfaceBackgroundWaning,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 2.dp),
                        text = stringResource(Res.string.delayed),
                        color = TextSubdued,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp,
                        fontFamily = PoppinsFontFamily(),
                    )

                }
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(orderData.products.take(4)) {
                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {

                    loadImagePainterWithValidation(
                        imageUrl = if (it.itemImage.isNullOrEmpty()
                                .not()
                        ) it.itemImage else EMPTY_STRING,
                        fallbackRes = Res.drawable.ic_dealcart_placeholder,
                        modifier = Modifier
                            .width(64.dp)
                            .height(64.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = BorderPrimaryDefault
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                    Box(
                        modifier = Modifier.background(
                            color = BorderPrimaryDefault,
                            shape = RoundedCornerShape(topStart = 6.4.dp, bottomEnd = 6.4.dp)
                        )
                    ) {
                        Text(
                            text = "x${it.itemQuantity}",
                            style = TextStyle(
                                color = TextSubduedHalf,
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(vertical = 3.2.dp, horizontal = 4.dp)
                        )
                    }
                }
            }
            item {
                if (orderData.products.size > 4) {
                    Box(
                        modifier = Modifier
                            .border(
                                BorderStroke(
                                    1.dp,
                                    BorderPrimaryDefault
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .size(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.four_add_on),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily(),
                                color = TextSubduedHalf,
                            )
                        )
                    }
                }
            }
        }

        if (orderData.isReAttempt) {
            DisclaimerCard(
                stringResource(
                    Res.string.your_order_redeliver,
                    orderData.deliveryDate
                )
            )
        }

        if (!orderData.isReAttempt && orderData.isQcDone) {
            if (orderData.originalOrderItemsCount > orderData.totalItems) {
                DisclaimerCard(
                    stringResource(
                        Res.string.apkay_order_mai_kuch_tabdeeli_ayi_hai_order_details_check_karein
                    )
                )
            }
        }

        if (isOrderDelivered && rating != null) {
            RatingCard(
                isOrderDelivered = isOrderDelivered,
                isOrderRated = isOrderRated,
                onReviewClick = onReviewClick,
                rating = rating
            )
        }
    }
}


