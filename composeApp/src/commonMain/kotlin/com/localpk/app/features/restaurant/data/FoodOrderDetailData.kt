package com.localpk.app.features.restaurant.data

import com.localpk.app.features.orders.data.DeliveredReceiptOrderItems
import com.localpk.app.features.orders.data.DeliveredReceiptOrderProduct
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.default
import com.localpk.app.utils.formatConfirmTimelineDate
import com.localpk.app.utils.formatConfirmTimelineTime
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import kotlinx.serialization.Serializable
import kotlin.math.ceil

@Serializable
data class FoodOrderDetailData(
    val cancelledAt: String? = null,
    val confirmedAt: String? = null,
    val createdAt: String? = null,
    val dcCoinDiscount: Long,
    val deliveredAt: String? = null,
    val deliveryFee: Int?,
    val dispatchedAt: String? = null,
    val id: Long,
    val orderStatus: String? = null,
    val originalOrderItemsCount: Int? = null,
    val paymentType: String? = null,
    val platformFee: Int? = null,
    val promoCodeDiscount: Long? = null,
    val promoCodeUsed: String? = EMPTY_STRING,
    val totalAmount: Double? = null,
    val restaurantLogo: String? = EMPTY_STRING,
    val restaurantName: String? = EMPTY_STRING,
    val taxes: Double? = null,
    val secondaryPhoneNumber: String? = EMPTY_STRING,
    val orderItems: List<FoodOrderItemDetails>? = arrayListOf(),
    val address: RestaurantAddress? = null
) {
    fun map(): FoodOrderDetailUIData {
        return FoodOrderDetailUIData(
            id = id.getValueOrDefault(),
            orderStatus = orderStatus.default(),
            cancelledAt = cancelledAt.default(),
            confirmedAt = confirmedAt.default(),
            createdAt = createdAt.default(),
            dcCoinDiscount = dcCoinDiscount.getValueOrDefault(),
            deliveredAt = deliveredAt.default(),
            deliveryFee = deliveryFee.getValueOrDefault(),
            dispatchedAt = dispatchedAt.default(),
            originalOrderItemsCount = originalOrderItemsCount.getValueOrDefault(),
            paymentType = paymentType.default(),
            platformFee = platformFee.getValueOrDefault(),
            promoCodeDiscount = promoCodeDiscount.getValueOrDefault(),
            totalAmount = totalAmount.getValueOrDefault(),
            restaurantLogo = restaurantLogo.default(),
            restaurantName = restaurantName.default(),
            taxes = taxes.getValueOrDefault(),
            secondaryPhoneNumber = secondaryPhoneNumber.default(),
            salesOrderItems = orderItems,
            address = address,
            promoCodeUsed = promoCodeUsed.default(),
            deliveredDate = deliveredAt?.formatConfirmTimelineDate().default(),
            deliveredTime = deliveredAt?.formatConfirmTimelineTime().default(),
            cancelledDate = cancelledAt?.formatConfirmTimelineDate().default(),
            cancelledTime = cancelledAt?.formatConfirmTimelineTime().default()
        )
    }
}

@Serializable
data class FoodOrderItemDetails(
    val id: Long?,
    val quantity: Int?,
    val discount: Double?,
    val totalAmount: Double?,
    val orderItemChoices: List<SalesOrderItemChoices>? = arrayListOf(),
    val item: OrderItem?
) {
    fun formattedDiscountAmount(): String =
        (item?.discountPrice.getValueOrDefault() * quantity.getValueOrDefault()).formatPKRCurrency()

    fun formattedAmount(): String =
        (item?.listPrice.getValueOrDefault() * quantity.getValueOrDefault()).formatPKRCurrency()

    fun map(): DeliveredReceiptOrderItems {
        return DeliveredReceiptOrderItems(
            totalUnitsPrice = item?.listPrice.getValueOrDefault() * quantity.getValueOrDefault(),
            quantity = quantity.getValueOrDefault(),
            totalAmount = totalAmount.getValueOrDefault() * quantity.getValueOrDefault(),
            totalDiscount = discount.getValueOrDefault() * quantity.getValueOrDefault(),
            product = DeliveredReceiptOrderProduct(
                name = item?.name.default()
            )
        )
    }
}

@Serializable
data class SalesOrderItemChoices(
    val id: Long?,
    val choice: Choice?
)

@Serializable
data class Choice(
    val id: Long?,
    val name: String?
)

@Serializable
data class OrderItem(
    val id: Long?,
    val name: String?,
    val image: String? = null,
    val listPrice: Double?,
    val discountPrice: Double?,
) {
    fun percentageOff(): String {
        return if ((((listPrice.getValueOrDefault() - discountPrice.getValueOrDefault()) / listPrice.getValueOrDefault()) * 100) <= 0)
            "1"
        else ceil(((listPrice.getValueOrDefault() - discountPrice.getValueOrDefault()) / listPrice.getValueOrDefault()) * 100).toInt()
            .toString() + "% OFF"
    }
}

@Serializable
data class FoodOrderDetailUIData(
    val cancelledAt: String,
    val confirmedAt: String,
    val createdAt: String,
    val dcCoinDiscount: Long,
    val deliveredAt: String,
    val deliveryFee: Int,
    val dispatchedAt: String,
    val id: Long,
    val orderStatus: String,
    val originalOrderItemsCount: Int,
    val paymentType: String,
    val platformFee: Int,
    val promoCodeDiscount: Long,
    val totalAmount: Double,
    val restaurantLogo: String,
    val restaurantName: String,
    val taxes: Double,
    val secondaryPhoneNumber: String,
    val salesOrderItems: List<FoodOrderItemDetails>? = arrayListOf(),
    val address: RestaurantAddress?,
    val promoCodeUsed: String,
    val deliveredDate: String,
    val deliveredTime: String,
    val cancelledDate: String,
    val cancelledTime: String
) {
    fun grandTotal(): Double =
        (totalAmount + platformFee + deliveryFee + taxes) - (promoCodeDiscount + dcCoinDiscount)

    fun formattedGrandTotal(): String =
        (grandTotal()).formatPKRCurrency()

    fun formattedPromoCodeDiscount(): String =
        (promoCodeDiscount.getValueOrDefault().toDouble()).formatPKRCurrency()

    fun formattedDcCoinDiscount(): String =
        (dcCoinDiscount.getValueOrDefault().toDouble()).formatPKRCurrency()

    fun formattedTax(): String = (taxes).formatPKRCurrency()

    fun formattedOrderTotal(): String = (totalAmount).formatPKRCurrency()

}
