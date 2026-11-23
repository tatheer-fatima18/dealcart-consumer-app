package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.orders.data.OrderStatus
import com.localpk.app.features.restaurant.data.FoodOrderDetailUIData
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
import com.localpk.app.utils.capitalizeFirst
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.cancelled_on
import dealcart.composeapp.generated.resources.delivered_on
import dealcart.composeapp.generated.resources.delivery_in
import dealcart.composeapp.generated.resources.fallback_logo
import dealcart.composeapp.generated.resources.out_for_Delivery
import dealcart.composeapp.generated.resources.track_order
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoodOrderInfoComponent(
    restaurantName: String,
    restaurantLogo: String,
    onTrackClick: () -> Unit,
    orderDetails: FoodOrderDetailUIData?,
    deliveryTime: String
) {
    val textColor = when (orderDetails?.orderStatus.default()) {
        OrderStatus.DELIVERED.status -> TextSuccess
        OrderStatus.CANCELLED.status -> IconDanger
        else -> ButtonSurfaceBrand
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .background(color = Color.White, RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = BorderPrimaryDefault,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = orderDetails?.id.getValueOrDefault().toString(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubduedHalf
                    )
                )
                Text(
                    text = if (orderDetails?.orderStatus.default() == OrderTrackingStatuses.OUT_FOR_DELIVERY.status) stringResource(
                        Res.string.out_for_Delivery
                    ) else orderDetails?.orderStatus.default().capitalizeFirst(),
                    style = TextStyle(
                        color = textColor,
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.weight(.1f))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${orderDetails?.originalOrderItemsCount.getValueOrDefault()} item(s)",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = TextSubduedHalf
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
                        text = orderDetails?.formattedGrandTotal().default(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            color = TextSubdued
                        )
                    )
                }
            }

            if (
                orderDetails?.deliveredAt.default()
                    .isNotEmpty() || orderDetails?.cancelledAt.default()
                    .isNotEmpty() || deliveryTime.isNotEmpty()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(if (orderDetails?.orderStatus.default() == OrderStatus.DELIVERED.status) Res.string.delivered_on else if (orderDetails?.orderStatus.default() == OrderStatus.CANCELLED.status) Res.string.cancelled_on else Res.string.delivery_in),
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        color = TextSubdued
                    )
                    Text(
                        text = if (orderDetails?.orderStatus.default() == OrderStatus.DELIVERED.status) orderDetails?.deliveredDate.default() else if (orderDetails?.orderStatus.default() == OrderStatus.CANCELLED.status) orderDetails?.cancelledDate.default() else deliveryTime,
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        color = TextSubdued
                    )
                    if ((orderDetails?.deliveredTime.default()
                            .isNotEmpty()) || orderDetails?.cancelledTime.default().isNotEmpty()
                    ) {
                        Text(
                            text = " .  ${if (orderDetails?.orderStatus.default() == OrderStatus.DELIVERED.status) orderDetails?.deliveredTime.default() else if (orderDetails?.orderStatus.default() == OrderStatus.CANCELLED.status) orderDetails?.cancelledTime.default() else EMPTY_STRING}",
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            color = TextSubdued
                        )
                    }
                }
            }
            if (orderDetails?.orderStatus.default() != OrderTrackingStatuses.DELIVERED.status && orderDetails?.orderStatus.default() != OrderTrackingStatuses.CANCELLED.status) {
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
                            color = ButtonSurfaceBrand
                        )
                    )
                }
            }

            if (restaurantName.isNotEmpty() && restaurantLogo.isNotEmpty()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(45.dp)
                            .height(37.dp)
                            .border(
                                BorderStroke(
                                    width = 0.5.dp,
                                    color = BorderPrimaryDefault
                                ),
                                RoundedCornerShape(4.dp)
                            )
                            .clip(shape = RoundedCornerShape(4.dp)),
                    ) {
                        loadImagePainterWithValidation(
                            imageUrl = restaurantLogo,
                            fallbackRes = Res.drawable.fallback_logo,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .matchParentSize()
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                    Text(
                        text = restaurantName,
                        modifier = Modifier.width(180.dp),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            color = TextSubdued
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}