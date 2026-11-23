package com.localpk.app.features.cart.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class AddToCartRequest(
    val product: Long,
    val deal: Long? = null,
    val quantity: Int,
    val dealPurchase: Boolean = false
) : JavaSerializable