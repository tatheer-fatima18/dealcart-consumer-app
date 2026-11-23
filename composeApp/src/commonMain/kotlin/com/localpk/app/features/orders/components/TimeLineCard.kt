package com.localpk.app.features.orders.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.CartOrderType
import com.localpk.app.features.orders.data.OrderStatus
import com.localpk.app.features.orders.data.OrderTimeLineModel
import com.localpk.app.features.orders.data.PaymentStatus
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.DarkGrayPrimary
import com.localpk.app.theming.ErrorRedColor
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.default
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.badge
import dealcart.composeapp.generated.resources.cancelled
import dealcart.composeapp.generated.resources.confirm
import dealcart.composeapp.generated.resources.date_time
import dealcart.composeapp.generated.resources.deliver_by
import dealcart.composeapp.generated.resources.deliveredchecked
import dealcart.composeapp.generated.resources.deliveredunchecked
import dealcart.composeapp.generated.resources.order_Placed
import dealcart.composeapp.generated.resources.order_confirmed
import dealcart.composeapp.generated.resources.order_delivered_
import dealcart.composeapp.generated.resources.order_packed
import dealcart.composeapp.generated.resources.order_refunded
import dealcart.composeapp.generated.resources.out_for_delivery
import dealcart.composeapp.generated.resources.outunchecked
import dealcart.composeapp.generated.resources.packedchecked
import dealcart.composeapp.generated.resources.packedunchecked
import dealcart.composeapp.generated.resources.returned
import dealcart.composeapp.generated.resources.unckeck_order_placed
import dealcart.composeapp.generated.resources.outchecked
import dealcart.composeapp.generated.resources.refund_icon_timeline
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun TimelineCard(
    timeLineModel: OrderTimeLineModel,
    deliverBy: String?,
    orderStatus: String,
    deliveryStartRange: String?,
    deliveryDate: String,
    paymentStatus: String?,
    deliveryTimeEnabled: Boolean,
    formattedDeliveryTime: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = BorderPrimaryDefault,
                shape = RoundedCornerShape(8.dp)
            )
    )
    {
        Timeline(
            timeLineModel,
            deliverBy,
            orderStatus,
            deliveryStartRange,
            deliveryDate,
            paymentStatus,
            deliveryTimeEnabled,
            formattedDeliveryTime
        )
    }
}

@Composable
fun Timeline(
    timeLineModel: OrderTimeLineModel,
    deliverBy: String?,
    orderStatus: String,
    deliveryStartRange: String?,
    deliveryDate: String,
    paymentStatus: String?,
    deliveryTimeEnabled: Boolean,
    formattedDeliveryTime: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        // Module 1: Placed Order
        TimelineModule(
            text = stringResource(Res.string.order_Placed),
            iconRes = Res.drawable.confirm,
            statusColor = DarkGrayPrimary,
            subStatus = timeLineModel.placedAtDate,
            isTrue = true
        )

        VerticalLine()

        if (orderStatus != OrderStatus.CANCELLED.status && orderStatus != OrderStatus.RETURNED.status) {
            // Module 2: Confirmed Order
            TimelineModule(
                text = stringResource(Res.string.order_confirmed),
                iconRes = if (timeLineModel.confirmedAtDate != null) Res.drawable.confirm else Res.drawable.unckeck_order_placed,
                statusColor = if (timeLineModel.confirmedAtDate != null) DarkGrayPrimary else TextSubdued,
                subStatus = timeLineModel.confirmedAtDate,
                isTrue = timeLineModel.confirmedAtDate != null
            )

            VerticalLine()

            // Module 3: Packed Order
            TimelineModule(
                text = stringResource(Res.string.order_packed),
                iconRes = if (timeLineModel.packedAtDate != null) Res.drawable.packedchecked else Res.drawable.packedunchecked,
                statusColor = if (timeLineModel.packedAtDate != null) DarkGrayPrimary else TextSubdued,
                subStatus = timeLineModel.packedAtDate,
                isTrue = timeLineModel.packedAtDate != null
            )

            VerticalLine()

            // Module 4: Out for Delivery
            TimelineModule(
                text = stringResource(Res.string.out_for_delivery),
                iconRes = if (timeLineModel.deliveryAtDate != null) Res.drawable.outchecked else Res.drawable.outunchecked,
                statusColor = if (timeLineModel.deliveryAtDate != null) DarkGrayPrimary else TextSubdued,
                subStatus = timeLineModel.deliveryAtDate,
                isTrue = timeLineModel.deliveryAtDate != null
            )

            VerticalLine()

            // Module 5: Delivered
            TimelineModule(
                text = if (timeLineModel.deliveredAtDate != null && timeLineModel.returnedAtDate == null && timeLineModel.cancelledAtDate == null) stringResource(
                    Res.string.order_delivered_
                ) else stringResource(Res.string.deliver_by),
                iconRes = if ((deliverBy == CartOrderType.DEAL_CART.key && deliveryStartRange != null) || (timeLineModel.deliveredAtDate != null && timeLineModel.returnedAtDate == null && timeLineModel.cancelledAtDate == null)) Res.drawable.deliveredchecked else Res.drawable.deliveredunchecked,
                statusColor = if ((deliverBy == CartOrderType.DEAL_CART.key && deliveryStartRange != null) || (timeLineModel.deliveredAtDate != null && timeLineModel.returnedAtDate == null && timeLineModel.cancelledAtDate == null)) DarkGrayPrimary else TextSubdued,
                subStatus = when {
                    orderStatus == OrderStatus.DELIVERED.status ->
                        "${timeLineModel.deliveredAtDate.default()}, ${timeLineModel.deliveredAtTime.default()}"

                    deliveryTimeEnabled && deliveryDate.isNotEmpty() ->
                        "${deliveryDate}, $formattedDeliveryTime"

                    else -> EMPTY_STRING
                },
                isTrue = deliverBy == CartOrderType.DEAL_CART.key && deliveryStartRange != null && orderStatus != OrderStatus.RETURNED.status,
                isDelivery = true
            )
        }

        // Module 6: Cancelled or Returned
        if (orderStatus == OrderStatus.RETURNED.status || orderStatus == OrderStatus.CANCELLED.status) {
            TimelineModule(
                text = if (timeLineModel.cancelledAtDate != null) stringResource(Res.string.cancelled) else stringResource(
                    Res.string.returned
                ),
                iconRes = Res.drawable.badge,
                statusColor = ErrorRedColor,
                subStatus = timeLineModel.cancelledAtDate ?: timeLineModel.returnedAtDate,
                isTrue = true
            )
        }

        // Module 7: Refunded
        if (paymentStatus == PaymentStatus.REFUNDED.paymentStatus) {
            VerticalLine()
            TimelineModule(
                text = stringResource(Res.string.order_refunded),
                iconRes = Res.drawable.refund_icon_timeline,
                statusColor = DarkGrayPrimary,
                subStatus = EMPTY_STRING,
                isTrue = true
            )
        }
    }
}

@Composable
fun TimelineModule(
    text: String,
    iconRes: DrawableResource,
    statusColor: Color,
    subStatus: String?,
    isTrue: Boolean,
    isDelivery: Boolean? = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier
                .width(31.dp)
                .height(31.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.padding(top = 4.dp)) {
            Text(
                text = text,
                style = TextStyle(
                    color = statusColor,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = if (isTrue) FontWeight.SemiBold else FontWeight.Medium,
                )
            )
            if (subStatus != null && isTrue) {
                Text(
                    text = subStatus,
                    style = TextStyle(
                        color = TextSubdued,
                        fontSize = 10.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = if (isDelivery == true) FontWeight.SemiBold else FontWeight.Normal,
                    ),
                )
            }
        }
    }
}


@Composable
fun VerticalLine() {
    Box(
        modifier = Modifier
            .padding(start = 14.dp)
            .width(2.dp)
            .height(15.dp)
            .background(color = SurfaceBackground)
    )
}