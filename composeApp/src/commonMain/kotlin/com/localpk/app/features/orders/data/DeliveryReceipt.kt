package com.localpk.app.features.orders.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable


@Serializable
data class DeliveryReceipt(
    val serialNo: Long,
    val deliveryDate: String,
    val customerName: String,
    val customerNumber: String,
    val address: String,
    val items: List<DeliveryReceiptItems>,
    val totalBachat: Double,
    val totalAmount: Double,
    val subTotal: Long,
    val deliveryFee: Long,
    val promoCodeDiscount: Long,
    val dcCoins: Long,
    val total: Double,
    val promoCodeUsed: String?,
    val totalItems: Int,
    val optionalContributionFee: Int,
    val packagingFee: Int,
    val platformFee: Int,
    val orderStatus: String,
) : JavaSerializable

@Serializable
data class DeliveryReceiptItems(
    val serialNo: Int,
    val name: String,
    val quantity: Int,
    val price: Double,
    val discount: Double,
    val discountedPrice: Double,
    val size: String
) : JavaSerializable