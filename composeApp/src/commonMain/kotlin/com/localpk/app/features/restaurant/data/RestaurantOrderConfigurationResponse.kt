package com.localpk.app.features.restaurant.data

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantOrderConfiguration(
    val deliveryTime: String?,
    val longitude: Double?,
    val latitude: Double?,
    val phoneNumber: String?,
    val name: String?,
    val logo: String?
)