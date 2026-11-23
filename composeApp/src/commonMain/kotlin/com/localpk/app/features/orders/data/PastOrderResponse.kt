package com.localpk.app.features.orders.data


import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.features.cart.data.PaymentDetails
import com.localpk.app.utils.default
import com.localpk.app.utils.formatConfirmTimelineDate
import com.localpk.app.utils.formatConfirmTimelineTime
import com.localpk.app.utils.formatDate
import com.localpk.app.utils.formatDateWithOne
import com.localpk.app.utils.getValueOrDefault
import kotlinx.serialization.Serializable


@Serializable
data class PastOrderResponse(
    val body: List<PastOrderItem>?
) : JavaSerializable

@Serializable
data class PastOrderItem(
    val orderId: Long,
    val orderStatus: String,
    val orderItemsCount: Int,
    val orderTotal: Double,
    val orderDate: String,
    val shippingAddressText: String,
    val originalOrderItemsCount: Int,
    val statusTimeStamp: String?,
    val paymentType: String,
    val deliverBy: String?,
    val salesOrderRating: OrderDetailSalesOrderRating?,
    val itemProperties: List<ItemsProperties>,
    val paymentDetails: PaymentDetails,
    val shipmentId: Long? = null,
    val deliveryType: String? = DeliveryType.STANDARD.key
) : JavaSerializable {
    fun map() = PastOrderUiData(
        orderId = orderId,
        orderStatus = orderStatus.replaceFirst(
            OrderStatus.PARTIAL_RETURNED.status,
            OrderStatus.PARTIAL_DELIVERED.status
        ),
        orderItemsCount = orderItemsCount,
        orderTotal = orderTotal,
        orderDate = orderDate.formatDate(),
        orderDeliveryDate = orderDate.formatDateWithOne(),
        shippingAddressText = shippingAddressText,
        originalOrderItemsCount = originalOrderItemsCount,
        statusTimeStampDate = statusTimeStamp.default().formatConfirmTimelineDate(),
        paymentType = paymentType.default(),
        orderTime = orderDate.formatConfirmTimelineTime(),
        deliverBy = deliverBy.default(),
        statusTimeStamp = statusTimeStamp.default().formatConfirmTimelineTime(),
        products = itemProperties.map {
            ItemsProperties(
                it.itemQuantity.getValueOrDefault(),
                it.itemImage.default(),
            )
        },
        paymentDetails = paymentDetails,
        salesOrderRating = salesOrderRating,
        shipmentId = shipmentId.getValueOrDefault(),
        deliveryType = deliveryType.default()
    )
}

@Serializable
data class PastOrderUiData(
    val orderId: Long,
    val orderStatus: String,
    val orderItemsCount: Int,
    val orderTotal: Double,
    val orderDate: String,
    val orderDeliveryDate: String,
    val shippingAddressText: String,
    val originalOrderItemsCount: Int,
    val statusTimeStampDate: String,
    val paymentType: String,
    val orderTime: String,
    val deliverBy: String,
    val statusTimeStamp: String,
    val products: List<ItemsProperties>,
    val paymentDetails: PaymentDetails,
    val salesOrderRating: OrderDetailSalesOrderRating?,
    val shipmentId: Long,
    val deliveryType: String
) : JavaSerializable


@Serializable
data class PastOrdersRequest(
    val orderStatus: String,
    val month: Int
) : JavaSerializable
