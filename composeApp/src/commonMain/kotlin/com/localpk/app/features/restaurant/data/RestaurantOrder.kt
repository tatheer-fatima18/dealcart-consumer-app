package com.localpk.app.features.restaurant.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class RestaurantOrderId(
    val orderId: Long?,
    val restaurantId: Long?,
    val isCheckout: Boolean? = false,
    val fromDetails: Boolean? = false
) : JavaSerializable