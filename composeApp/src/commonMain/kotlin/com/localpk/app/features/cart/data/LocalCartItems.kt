package com.localpk.app.features.cart.data

data class LocalCartItem(
    val productId: Long,
    val cartProductId: Long?,
    val quantity: Int,
    val actualPrice: Double,
    val discountedPrice: Double,
    val productName: String,
    val productImage: String,
    val isTotal: Boolean = false,
    val isProductToBeDeliveredByThirdParty: Boolean = false
)

data class CartTotals(
    val cartTotal: Double,
    val cartDiscount: Double,
    val itemCount: Int,
    val dcCityCartTotal: Double? = 0.0
)

data class ProductInfo(
    val productId: Long,
    val cartProductId: Long,
    val quantity: Int,
    val price: Double,
    val productName: String,
)