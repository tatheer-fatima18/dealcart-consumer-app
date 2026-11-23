package com.localpk.app.features.restaurant.data

import com.localpk.app.features.cart.data.PaymentDetails
import com.localpk.app.utils.default
import com.localpk.app.utils.formatConfirmTimelineDate
import com.localpk.app.utils.formatConfirmTimelineTime
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import kotlinx.serialization.Serializable
import cafe.adriel.voyager.core.lifecycle.JavaSerializable

@Serializable
data class FoodOrdersList(val results: List<FoodOrder>?) : JavaSerializable

@Serializable
data class FoodOrder(
    val id: Long,
    val createdAt: String? = null,
    val orderStatus: String?,
    val totalAmount: Double?,
    val orderItemsCount: Int?,
    val acceptedAt: String? = null,
    val dispatchedAt: String? = null,
    val deliveredAt: String? = null,
    val cancelledAt: String? = null,
    val orderTotal: Double?,
    val paymentType: String?,
    val itemProperties: List<ItemsProperties>?,
    val paymentDetails: PaymentDetails? = null,
    val deliveryTime: String?,
    val restaurantId: Long?,
    val shippingAddressText: String?
) : JavaSerializable {
    fun map(): FoodOrderUIData {
        return FoodOrderUIData(
            id = id.getValueOrDefault(),
            createdAt = createdAt.default(),
            orderStatus = orderStatus.default(),
            totalAmount = totalAmount.getValueOrDefault(),
            orderItemsCount = orderItemsCount.getValueOrDefault(),
            acceptedAt = acceptedAt.default(),
            dispatchedAt = dispatchedAt.default(),
            deliveredAt = deliveredAt.default(),
            cancelledAt = cancelledAt.default(),
            orderTotal = orderTotal.getValueOrDefault(),
            paymentType = paymentType.default(),
            paymentDetails = paymentDetails,
            itemProperties = itemProperties,
            deliveryTime = deliveryTime.default(),
            restaurantId = restaurantId.getValueOrDefault(),
            shippingAddressText = shippingAddressText.default(),
            deliveredDate = deliveredAt?.formatConfirmTimelineDate().default(),
            deliveredTime = deliveredAt?.formatConfirmTimelineTime().default(),
            cancelledDate = cancelledAt?.formatConfirmTimelineDate().default(),
            cancelledTime = cancelledAt?.formatConfirmTimelineTime().default(),
            fromTrackingScreen = false,
            fromCheckout = false
        )
    }


}

@Serializable
data class FoodOrderUIData(
    val id: Long,
    val createdAt: String,
    val orderStatus: String,
    val totalAmount: Double,
    val orderItemsCount: Int,
    val acceptedAt: String,
    val dispatchedAt: String,
    val deliveredAt: String,
    val cancelledAt: String,
    val orderTotal: Double,
    val paymentType: String,
    val itemProperties: List<ItemsProperties>? = null,
    val paymentDetails: PaymentDetails? = null,
    val deliveryTime: String,
    val restaurantId: Long,
    val shippingAddressText: String,
    val deliveredDate: String,
    val deliveredTime: String,
    val cancelledDate: String,
    val cancelledTime: String,
    val fromTrackingScreen: Boolean,
    val fromCheckout: Boolean
) : JavaSerializable {
    fun formattedOrderTotal(): String = orderTotal.formatPKRCurrency()
    fun formattedTotalAmount(): String = totalAmount.formatPKRCurrency()
}

@Serializable
data class ItemsProperties(
    val itemQuantity: Int?,
    val itemImage: String?
) : JavaSerializable
