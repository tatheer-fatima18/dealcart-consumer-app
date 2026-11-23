package com.localpk.app.features.flash_deals.data

import com.localpk.app.features.productlist.data.ProductImage
import com.localpk.app.utils.EMPTY_STRING
import kotlinx.serialization.Serializable

@Serializable
data class FlashDealDetailResponse(val body: FlashDealDetail?, val success: Boolean)

@Serializable
data class FlashDealDetail(
    val productId: Long,
    val productName: String,
    val productImage: ProductImage,
    val description: String? = EMPTY_STRING,
    val originalPrice: Double?,
    val discountedPercentage: Double,
    val dealPrice: Double,
    val timeLeft: Long,
    val totalPersonsRequired: Int,
    val totalPersonsJoined: Int,
    val isAvailed: Boolean? = false,
    val dealStatus: String,
    val dealPresence: String,
    val maxPurchasableQuantity: Int,
    val expiryEndTime: Long
)

enum class DealPresenceConstants(val key: String) {
    CART("cart"),
    NONE("none"),
    ORDER("order")
}

enum class FlashDealScreenName(val key: String) {
    HOME_SCREEN("homescreen"),
    DEAL_DETAILS("dealdetails"),
    CART("cart"),
    OLS("OLS"),
    ORDER_DETAILS("orderdetails"),
    PENDING_FLASH("abouttoexpirepn")
}