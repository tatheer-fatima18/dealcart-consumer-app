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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.orders.data.OrderItemStatus
import com.localpk.app.features.orders.data.OrderStatus
import com.localpk.app.features.restaurant.data.FoodOrderUIData
import com.localpk.app.features.restaurant.data.OrderTrackingStatuses
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.theming.TextSuccess
import com.localpk.app.theming.indigo
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.capitalizeFirstLetter
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.cancelled_on
import dealcart.composeapp.generated.resources.delivered_on
import dealcart.composeapp.generated.resources.delivery_in
import dealcart.composeapp.generated.resources.four_add_on
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder
import dealcart.composeapp.generated.resources.out_for_Delivery
import dealcart.composeapp.generated.resources.track_order
import org.jetbrains.compose.resources.stringResource


@Composable
fun FoodOrderItemLayout(
    orderData: FoodOrderUIData,
    orderStatus: String?,
    onOrderClick: () -> Unit,
    onTrackClick: () -> Unit = {},
) {
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
                    OrderStatus.DELIVERED.status ->TextSuccess
                    OrderStatus.CANCELLED.status -> IconDanger
                    else -> ButtonSurfaceBrand
                }
                Text(
                    text = if (orderData.orderStatus == OrderTrackingStatuses.OUT_FOR_DELIVERY.status) stringResource(Res.string.out_for_Delivery) else orderData.orderStatus.capitalizeFirstLetter(),
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
                    text = "${orderData.orderItemsCount} item(s)",
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
                    text = orderData.formattedTotalAmount(),
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
            orderData.deliveredAt.isNotEmpty() || orderData.cancelledAt.isNotEmpty() || orderData.deliveryTime.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(if (orderData.orderStatus == OrderStatus.DELIVERED.status) Res.string.delivered_on else if (orderData.orderStatus == OrderStatus.CANCELLED.status) Res.string.cancelled_on else Res.string.delivery_in),
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = TextSubdued,
                )
                Text(
                    text = if (orderData.orderStatus == OrderStatus.DELIVERED.status) orderData.deliveredDate.default() else if (orderData.orderStatus == OrderStatus.CANCELLED.status) orderData.cancelledDate.default() else orderData.deliveryTime,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    color = TextSubdued,
                )
                if ((orderData.deliveredTime.isNotEmpty()) || orderData.cancelledTime.isNotEmpty()) {
                    Text(
                        text = " .  ${if (orderData.orderStatus == OrderStatus.DELIVERED.status) orderData.deliveredTime else if (orderData.orderStatus == OrderStatus.CANCELLED.status) orderData.cancelledTime else EMPTY_STRING}",
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        color = TextSubdued,
                    )
                }
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            orderData.itemProperties?.let {
                items(it.take(4)) {
                    Box(
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        loadImagePainterWithValidation(
                            imageUrl = if (it.itemImage.isNullOrEmpty()
                                    .not()
                            ) it.itemImage else "",
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
                                .clip(RoundedCornerShape(8.dp))
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
            }
            item {
                if (orderData.itemProperties?.size.getValueOrDefault() > 4) {
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

        if (orderData.orderStatus != OrderTrackingStatuses.DELIVERED.status && orderData.orderStatus != OrderTrackingStatuses.CANCELLED.status) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = indigo,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onTrackClick() }
                    .padding(vertical = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.track_order).uppercase(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = ButtonSurfaceBrand,

                    )
                )
            }
        }
    }
}


