package com.localpk.app.features.restaurant.data

import kotlinx.serialization.Serializable

@Serializable
data class FoodCheckoutResponse(
    val id: Long?,
    val restaurantId: Long?
)