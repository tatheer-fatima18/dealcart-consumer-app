package com.localpk.app.features.orders.data


import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.formatDate
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.serialization.Serializable

@Serializable
data class DeliveredReceiptResponse(
    val success: Boolean, val body: DeliveryReceiptBody
) : JavaSerializable

@Serializable
data class DeliveryReceiptBody(
    val dcCoins: Long,
    val deliveryFee: Long,
    val promoCodeDiscount: Long,
    val promoCodeUsed: String,
    val salesOrderItems: List<DeliveredReceiptOrderItems>?,
    val subTotal: Long,
    val total: Double,
    val totalSavings: Double,
    val orderDate: String,
    val shippingAddressText: String,
    val optionalContributionFee: Int,
    val packagingFee: Int,
    val platformFee: Int
) : JavaSerializable {
    fun mapToDeliveryReceipt(
        customerName: String,
        customerNumber: String,
        id: Long,
        orderStatus: String
    ) =
        DeliveryReceipt(
            serialNo = id,
            deliveryDate = orderDate.formatDate(),
            customerName = customerName,
            customerNumber = customerNumber,
            address = shippingAddressText,
            items = salesOrderItems?.mapIndexed { index, shipmentItem ->
                shipmentItem.mapToDeliveryReceiptItem(index + 1)
            }.orEmptyArrayList(),
            totalBachat = totalSavings,
            totalAmount = total,
            subTotal = subTotal,
            deliveryFee = deliveryFee,
            promoCodeDiscount = promoCodeDiscount,
            dcCoins = dcCoins,
            total = total,
            promoCodeUsed = promoCodeUsed,
            totalItems = salesOrderItems?.size ?: 0,
            optionalContributionFee = optionalContributionFee,
            packagingFee = packagingFee,
            platformFee = platformFee,
            orderStatus = orderStatus
        )
}


@Serializable
data class DeliveredReceiptOrderItems(
    val totalUnitsPrice: Double,
    val quantity: Int,
    val totalAmount: Double,
    val totalDiscount: Double,
    val product: DeliveredReceiptOrderProduct
) : JavaSerializable {
    fun mapToDeliveryReceiptItem(serialNo: Int) = DeliveryReceiptItems(
        serialNo = serialNo,
        name = product.name,
        quantity = quantity,
        price = totalUnitsPrice,
        discount = totalDiscount,
        discountedPrice = totalAmount,
        size = "${product.unitValue}${product.unit}"
    )
}

@Serializable
data class DeliveredReceiptOrderProduct(
    val name: String,
    val unit: String? = EMPTY_STRING,
    val unitValue: String? = EMPTY_STRING
) : JavaSerializable
