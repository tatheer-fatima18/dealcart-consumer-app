package com.localpk.app.features.orders.data


import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.features.productlist.data.ProductImage
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.DC_ORDER_CANCEL
import com.localpk.app.utils.EMPTY_STRING

import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.default
import com.localpk.app.utils.formatConfirmTimelineDate
import com.localpk.app.utils.formatConfirmTimelineTime
import com.localpk.app.utils.formatDate
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.toInteger
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.math.ceil

@Serializable
data class OrderDetailModel(
    val id: Long,
    val totalAmount: Double,
    val orderTotal: Double,
    val orderTotalAmountPacked: Double,
    val orderStatus: String,
    val createdAt: String?,
    val confirmedAt: String?,
    val commentHistory: List<CancelOrderReasonRequestBody>? = arrayListOf(),
    val packedAt: String?,
    val dispatchedAt: String?,
    val deliveredAt: String?,
    val cancelledAt: String?,
    val returnedAt: String?,
    val totalDiscount: Double,
    val pickedOrderItemsCount: Int,
    val isQcDone: Boolean = false,
    val isShipmentCreated: Boolean,
    val originalOrderItemsCount: Int,
    val shippingAddressText: String,
    val dcCoinDiscount: Long,
    val deliveryFee: Int?,
    val promoCodeDiscount: Long?,
    val promoCodeUsed: String?,
    val salesOrderRating: OrderDetailSalesOrderRating?,
    val salesOrderItems: List<OrderItemDetail>? = arrayListOf(),
    val consumerBundle: OrderBundles?,
    val isReAttempt: Boolean,
    val paymentStatus: String?,
    val optionalContributionFee: Int?,
    val deliveryDate: String? = null,
    val deliveryStartRange: String? = null,
    val deliveryEndRange: String? = null,
    val deliverBy: String?,
    val packagingFee: Int?,
    val platformFee: Int?,
    val secondaryPhoneNumber: String?,
    val shippingCity: String?,
    val storeImage: String? = null,
    val storeName: String?,
    val isDeliveryTimeEnabled: Boolean = RemoteConfigs.enableDisplayOfDelivery(),
    val paymentType: String?,
    val deliveryType: String? = DeliveryType.STANDARD.key
) {
    fun getTotalItems() = salesOrderItems?.size
    fun getPromoLabel() = promoCodeUsed ?: EMPTY_STRING
    fun timeLine() = OrderTimeLineModel(
        placedAtDate = createdAt?.formatDate(),
        placedAtTime = createdAt?.formatConfirmTimelineTime(),
        confirmedAtDate = confirmedAt?.formatDate(),
        confirmedAtTime = confirmedAt?.formatConfirmTimelineTime(),
        packedAtDate = packedAt?.formatDate(),
        packedAtTime = packedAt?.formatConfirmTimelineTime(),
        deliveryAtDate = dispatchedAt?.formatDate(),
        deliveryAtTime = dispatchedAt?.formatConfirmTimelineTime(),
        deliveredAtDate = deliveredAt?.formatConfirmTimelineDate(),
        deliveredAtTime = deliveredAt?.formatConfirmTimelineTime(),
        cancelledAtDate = cancelledAt?.formatDate(),
        cancelledAtTime = cancelledAt?.formatConfirmTimelineTime(),
        returnedAtDate = returnedAt?.formatDate(),
        returnedAtTime = returnedAt?.formatConfirmTimelineTime(),
    )

    fun formattedOrderTotalAmountPacked(): String = orderTotalAmountPacked.formatPKRCurrency()

    fun getCancellationComment(): String {
        return commentHistory?.firstOrNull()?.let { firstComment ->
            if (firstComment.cancelledByRef == "customer") {
                if (firstComment.commentType.lowercase() != "others") {
                    firstComment.commentType
                } else {
                    firstComment.commentDescription ?: EMPTY_STRING
                }
            } else {
                DC_ORDER_CANCEL
            }
        } ?: DC_ORDER_CANCEL
    }

    fun isOrderCompleted(): Boolean {
        return when {
            isReAttempt -> false
            orderStatus in listOf(
                OrderStatus.DELIVERED.status,
                OrderStatus.PARTIAL_RETURNED.status,
                OrderStatus.PARTIAL_DELIVERED.status,
                OrderStatus.RETURNED.status,
                OrderStatus.CANCELLED.status
            ) -> true

            else -> false
        }
    }

    fun formattedDcCoinDiscount(): String = dcCoinDiscount.toDouble().formatPKRCurrency()
    fun formattedPromoCodeDiscount(): String =
        promoCodeDiscount?.let { it.toDouble().formatPKRCurrency() } ?: EMPTY_STRING

    fun formattedOrderTotal(): String = orderTotal.formatPKRCurrency()

    fun getIsOrderCancellable(): Boolean {
        return when (orderStatus) {
            OrderStatus.DELIVERED.status,
            OrderStatus.PARTIAL_RETURNED.status,
            OrderStatus.PARTIAL_DELIVERED.status,
            OrderStatus.RETURNED.status,
            OrderStatus.CANCELLED.status -> false

            else -> !isShipmentCreated
        }
    }

    fun formattedDeliveryTime(): String = "${
        deliveryStartRange?.formatConfirmTimelineTime().default()
    } - ${deliveryEndRange?.formatConfirmTimelineTime().default()}"
}

@Serializable
data class OrderTimeLineModel(
    val placedAtDate: String?,
    val placedAtTime: String?,
    val confirmedAtDate: String?,
    val confirmedAtTime: String?,
    val packedAtDate: String?,
    val packedAtTime: String?,
    val deliveryAtDate: String?,
    val deliveryAtTime: String?,
    val deliveredAtDate: String?,
    val deliveredAtTime: String?,
    val cancelledAtDate: String?,
    val cancelledAtTime: String?,
    val returnedAtDate: String?,
    val returnedAtTime: String?,
)

@Serializable
data class OrderDetailResponse(val body: OrderDetailModel)

@Serializable
data class OrderStatusResponse(val body: OrderStatusBody, val success: Boolean)

@Serializable
data class OrderStatusBody(val orderStatus: String?, val message: String?)

@Serializable
data class CancelOrderComment(
    val comm: OrderDetailModel
)

@Serializable
data class OrderBundles(
    val id: Long?,
    val name: String,
    val totalAmount: Double,
    val totalUnitPrice: Double,
    val quantity: Long,
    val salesOrderItems: List<OrderItemDetail>? = arrayListOf()
) {
    fun formattedTotalAmount(): String = totalAmount.formatPKRCurrency()
    fun formattedTotalUnitPrice(): String = totalUnitPrice.formatPKRCurrency()

    fun percentageOff(): String {

        return if ((((totalUnitPrice - totalAmount) / totalUnitPrice) * 100) <= 0)
            "1" // this is by design, as recommended by product team.
        else ceil(((totalUnitPrice - totalAmount) / totalUnitPrice) * 100).toInt()
            .toString() + "% OFF"
    }
}

@Serializable
data class OrderItemDetail(
    val id: Long,
    val quantity: Int,
    val totalUnitsPrice: Double,
    val totalAmount: Double,
    val itemStatus: String,
    val outOfStock: Boolean,
    val product: CartProduct,
    val shipmentItem: ShipmentDetails?,
    val unitPrice: Double,
    val discount: Double,
    val group: Group?,
    val deal: Deal?,
    val isQcDone: Boolean = false,
    val paymentStatus: String?
) {
    fun map() = OrderItemDetailUiData(
        id = id,
        quantity = quantity,
        totalUnitsPrice = totalUnitsPrice,
        totalAmount = totalAmount,
        itemStatus = if (isQcDone && outOfStock && !(itemStatus == OrderItemStatus.RETURNED.itemStatus || itemStatus == OrderItemStatus.CANCELLED.itemStatus))
            OrderItemStatus.OUT_OF_STOCK.itemStatus
        else itemStatus,
        productName = product.name,
        variant = "${product.unitValue}${product.unit}",
        productImage = product.images.firstOrNull()?.path ?: EMPTY_STRING,
        isGroup = group.isNotNull(),
        showTimer = group?.remainingTime.getValueOrDefault() < (1000 * 60 * 60),
        groupJoinedQuantity = group?.joinedCount ?: 0,
        remainingPerson = (group?.quantity ?: 0) - (group?.joinedCount ?: 0),
        remainingTime = group?.remainingTime ?: 0L,
        groupId = group?.id.getValueOrDefault(),
        customerName = group?.customer?.firstName ?: EMPTY_STRING,
        outOfStock = isQcDone && outOfStock,
        pickedQuantity = (shipmentItem?.quantityPicked ?: quantity).toString(),
        isQcDone = isQcDone,
        isDeal = deal.isNotNull(),
        deal = deal,
        showMrp = product.isMRPRestricted == true || totalUnitsPrice <= totalAmount,
        dealCardBackground = if (deal.isNotNull()) 123 else 123,
        formattedTotalUnitsPrice = totalUnitsPrice,
        formattedTotalAmount = totalAmount,
        paymentStatus = paymentStatus.default()
    )

    fun bundleMap() = RashanItemDetailUiData(
        id = id,
        quantity = quantity,
        totalUnitsPrice = totalUnitsPrice,
        totalAmount = totalAmount,
        productName = product.name,
        productImage = product.images.firstOrNull()?.path ?: EMPTY_STRING,
        formattedTotalAmount = totalAmount.formatPKRCurrency(),
        formattedTotalUnitsPrice = totalUnitsPrice.formatPKRCurrency(),
        percentageOff = (((totalUnitsPrice.getValueOrDefault() - totalAmount.getValueOrDefault()) / totalUnitsPrice.getValueOrDefault()) * 100).toInteger()
            .toString(),
    )
}

@Serializable
data class Group(
    val id: Long,
    val quantity: Int,
    val groupRangeType: String,
    val joinedCount: Int,
    val remainingTime: Long,
    val isCompleted: Boolean,
    val customer: Customer?
)

@Serializable
data class Deal(
    val id: Long,
    val dealPrice: Double,
    val timeLeft: Long,
    val totalPersonsJoined: Int,
    val totalPersonsRequired: Int,
    val expiryEndTime: Long,
)

@Serializable
data class Customer(
    val firstName: String
)

@Serializable
data class OrderItemDetailUiData(
    val id: Long,
    val totalUnitsPrice: Double,
    val quantity: Int,
    val totalAmount: Double,
    val itemStatus: String,
    val productName: String,
    val productImage: String,
    val isGroup: Boolean,
    val showTimer: Boolean,
    val variant: String,
    val groupJoinedQuantity: Int,
    val remainingPerson: Int,
    val remainingTime: Long,
    val groupId: Long,
    val customerName: String,
    val outOfStock: Boolean,
    val pickedQuantity: String,
    val isQcDone: Boolean = false,
    val isDeal: Boolean,
    val deal: Deal?,
    val dealCardBackground: Int? = ZERO_INT,
    val formattedTotalUnitsPrice: Double,
    val formattedTotalAmount: Double,
    val showMrp: Boolean,
    val paymentStatus: String
) {
    fun formattedTotalUnitsPrice(): String = formattedTotalUnitsPrice.formatPKRCurrency()
    fun formattedTotalAmount(): String = formattedTotalAmount.formatPKRCurrency()
    fun percentageOff(): String {
        return if ((((formattedTotalUnitsPrice - formattedTotalAmount) / formattedTotalUnitsPrice) * 100) <= 0)
            "1" // this is by design, as recommended by product team.
        else ceil((((formattedTotalUnitsPrice - formattedTotalAmount) / formattedTotalUnitsPrice) * 100)).toInt()
            .toString() + "%"
    }
}

@Serializable
data class RashanItemDetailUiData(
    val id: Long,
    val totalUnitsPrice: Double,
    val quantity: Int,
    val totalAmount: Double,
    val productName: String,
    val productImage: String,
    val formattedTotalUnitsPrice: String,
    val formattedTotalAmount: String,
    val percentageOff: String
)

@Serializable
data class CartProduct(
    val id: Long,
    val name: String,
    val images: List<ProductImage>,
    val unitValue: Int,
    val unit: String,
    val isMRPRestricted: Boolean?
)

@Serializable
data class ShipmentDetails(
    val quantityPicked: Int?,
    val shipment: Shipment?
)

@Serializable
data class Shipment(
    val id: Long,
)

@Serializable
data class OrderSupportArgs(
    val orderId: Long,
    val orderStatus: String,
    val deliveryDate: String,
    val orderCreatedAt: String,
    val shippingAddress: String,
    val isOrderCancellable: Boolean
)

enum class OrderItemStatus(val itemStatus: String) {
    ALL("all"),
    PENDING("pending"),
    CONFIRMED("confirmed"),
    DELIVERED("delivered"),
    RETURNED("returned"),
    CANCELLED("cancelled"),
    OUT_OF_STOCK("out of stock"),
    UPCOMING("upcoming")
}

enum class PaymentStatus(val paymentStatus: String) {
    PAYMENT("payment"),
    PARTIAL_REFUND("partial_refund"),
    REFUNDED("refund")
}

data class StatusModel(val id: Long, val status: String, val title: String, val message: String)