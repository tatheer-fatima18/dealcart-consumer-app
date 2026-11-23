package com.localpk.app.features.restaurant.data

import com.localpk.app.utils.EMPTY_STRING
import kotlinx.serialization.Serializable
import cafe.adriel.voyager.core.lifecycle.JavaSerializable

@Serializable
data class RestaurantDetailArgsData(
    val id: Long,
    val name: String,
    val logoImage: String? = EMPTY_STRING,
    val cuisines: List<String>? = null,
    val deliveryFee: Long,
    val prepTime: String,
    val minimumOrderValue: Int
) : JavaSerializable
