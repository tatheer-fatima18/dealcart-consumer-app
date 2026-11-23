package com.localpk.app.features.orders.data


import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.features.cart.data.PaymentDetails
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.default
import com.localpk.app.utils.formatConfirmTimelineDate
import com.localpk.app.utils.formatConfirmTimelineTime
import com.localpk.app.utils.formatDate
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import kotlinx.serialization.Serializable

@Serializable
data class CurrentOrdersResponse(val success: Boolean, val body: OrdersList)

@Serializable
data class OrdersList(val results: List<Order>?)

@Serializable
data class Order(
    val id: Long,
    val createdAt: String? = EMPTY_STRING,
    val orderStatus: String,
    val totalAmount: Double,
    val orderItemsCount: Int,
    val shippingAddressText: String?,
    val packedAt: String?,
    val confirmedAt: String?,
    val dispatchedAt: String?,
    val deliveredAt: String?,
    val cancelledAt: String?,
    val orderTotal: Double,
    val originalOrderItemsCount: Int,
    val isQcDone: Boolean = false,
    val isReAttempt: Boolean = false,
    val paymentType: String,
    val deliveryDate: String? = EMPTY_STRING,
    val deliveryStartRange: String? = null,
    val deliveryEndRange: String? = null,
    val deliverBy: String? = EMPTY_STRING,
    val orderDeliveryStatus: String? = EMPTY_STRING,
    val itemProperties: List<ItemsProperties>,
    val paymentDetails: PaymentDetails,
    val deliveryType: String? = DeliveryType.STANDARD.key
) : JavaSerializable {
    fun map() = OrderUiData(
        id = id,
        createdAt = createdAt?.formatDate(),
//        deliveryDate = createdAt?.formatDate()!!.replace(',',' ').split(' ')[1],
        // getExpectedDeliveryDate(createdAt!!),
        orderDeliveryDate = createdAt,
        orderStatus = orderStatus,
        totalAmount = totalAmount,
        totalItems = orderItemsCount,
        packedAt = packedAt,
        confirmedAt = confirmedAt,
        dispatchedAt = dispatchedAt,
        deliveredAt = deliveredAt,
        cancelledAt = cancelledAt,
        orderTotal = orderTotal,
        originalOrderItemsCount = originalOrderItemsCount,
        isQcDone = isQcDone,
        isReAttempt = isReAttempt,
        orderStatusBackground = when (orderStatus) {
            OrderStatus.PENDING.status -> 1
            OrderStatus.CONFIRMED.status -> 1
            else -> 1
        },
        deliveryDate = deliveryDate?.formatConfirmTimelineDate().default(),
        deliveryStartRange = deliveryStartRange?.formatConfirmTimelineDate().default(),
        deliveryEndRange = deliveryEndRange?.formatConfirmTimelineDate().default(),
        orderPlacedTime = createdAt?.formatConfirmTimelineTime().default(),
        shippingAddressText = shippingAddressText.default(),
        formattedOrderTotal = orderTotal.formatPKRCurrency(),
        paymentType = paymentType.default(),
        deliveryStartTime = deliveryStartRange?.formatConfirmTimelineTime().default(),
        deliverBy = deliverBy.default(),
        deliveredTime = EMPTY_STRING,
        cancelledTime = EMPTY_STRING,
        orderDeliveryStatus = orderDeliveryStatus.default(),
        deliveryEndTime = deliveryEndRange?.formatConfirmTimelineTime().default(),
        returnedAt = EMPTY_STRING,
        returnedTime = EMPTY_STRING,
        products = itemProperties.map {
            ItemsProperties(
                it.itemQuantity.getValueOrDefault(),
                it.itemImage.default(),
            )
        },
        paymentDetails = paymentDetails,
        salesOrderRating = null,
        shipmentId = ZERO_LONG,
        deliveryType = deliveryType.default()
    )
}

@Serializable
data class ItemsProperties(
    val itemQuantity: Int?,
    val itemImage: String?,
) : JavaSerializable

@Serializable
data class OrderUiData(
    val id: Long,
    val createdAt: String?,
    val orderStatus: String,
    val orderDeliveryDate: String? = EMPTY_STRING,
    val totalAmount: Double,
    val totalItems: Int,
    val packedAt: String?,
    val confirmedAt: String?,
    val cancelledAt: String?,
    val dispatchedAt: String?,
    val deliveredAt: String?,
    val orderTotal: Double,
    val originalOrderItemsCount: Int,
    val isQcDone: Boolean = false,
    val isReAttempt: Boolean = false,
    val isFromNotification: Boolean = false,
    val orderStatusBackground: Int = ZERO_INT,
    val formattedOrderTotal: String = EMPTY_STRING,
    val paymentType: String,
    val deliveryDate: String,
    val deliveryStartRange: String,
    val deliveryEndRange: String,
    val orderPlacedTime: String,
    val shippingAddressText: String,
    val deliveryStartTime: String,
    val deliveryEndTime: String,
    val deliverBy: String,
    val deliveredTime: String,
    val cancelledTime: String,
    val orderDeliveryStatus: String,
    val returnedAt: String?,
    val returnedTime: String,
    val products: List<ItemsProperties>,
    val paymentDetails: PaymentDetails,
    val salesOrderRating: OrderDetailSalesOrderRating?,
    val shipmentId: Long,
    val deliveryType: String
) : JavaSerializable {
    fun formattedDeliveryDate(): String = "$deliveryStartRange - $deliveryEndRange"

    fun formattedDeliveryTime(): String = "$deliveryStartTime - $deliveryEndTime"
}

@Serializable
enum class OrderStatus(val status: String) : JavaSerializable {
    PENDING("pending"),
    PACKED("packed"),
    OUT_FOR_DELIVERY("out_for_delivery"),
    DELIVERED("delivered"),
    PARTIAL_RETURNED("partial_returned"),
    PARTIAL_DELIVERED("delivered*"),
    RETURNED("returned"),
    CONFIRMED("confirmed"),
    CANCELLED("cancelled"),
    OUT_OF_STOCK("out of stock")
}

@Serializable
enum class OrderDeliveryStatus(val status: String) : JavaSerializable {
    ON_TIME("on_time"),
    DELAYED("delayed")
}