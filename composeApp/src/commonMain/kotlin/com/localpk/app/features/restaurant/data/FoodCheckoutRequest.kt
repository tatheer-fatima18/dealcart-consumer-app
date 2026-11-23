package com.localpk.app.features.restaurant.data

import com.localpk.app.utils.EMPTY_STRING
import kotlinx.serialization.Serializable

@Serializable
data class FoodCheckoutRequest(
    val addressId: Long,
    val latitude: Double,
    val longitude: Double,
    val secondaryPhoneNumber: String? = null,
    val addressLine1: String? = EMPTY_STRING,
    val addressLine2: String? = EMPTY_STRING,
    val city: String?,
    val zip: String? = EMPTY_STRING,
    val state: String? = EMPTY_STRING,
    val customerName: String,
    val phoneNumber: String,
    val customerId: Long,
    val platformType: String? = EMPTY_STRING,
)
