package com.localpk.app.features.restaurant.data

import com.localpk.app.utils.EMPTY_STRING
import kotlinx.serialization.Serializable

@Serializable
data class AddItemRequestBody(
    val itemId: Long? = null,
    val quantity: Int? = null,
    val instructions: String? = EMPTY_STRING,
    val variationId: Long? = null,
    val choiceIds: List<Long>? = emptyList()
)

@Serializable
data class UpdateQuantityBody(
    val cartQuantities: List<CartQuantity>
)

@Serializable
data class CartQuantity(
    val cartItemId: Long,
    val newQuantity: Int
)