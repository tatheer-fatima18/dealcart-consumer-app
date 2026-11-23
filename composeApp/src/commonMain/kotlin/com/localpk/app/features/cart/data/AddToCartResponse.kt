package com.localpk.app.features.cart.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class AddToCartRawResponse(
    val body: AddToCartResponse,
    val success: Boolean
) : JavaSerializable

@Serializable
data class AddToCartResponse(
    val productId: Long,
    val isProductToBeDeliveredByThirdParty: Boolean
) : JavaSerializable


