package com.localpk.app.features.restaurant.data

import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import kotlinx.serialization.Serializable

@Serializable
data class GetRestaurantsResponse(
    val cuisines: List<Cuisine>?,
    val restaurants: List<Restaurant>?
)

@Serializable
data class RestaurantUI(
    val id: Long,
    val name: String,
    val coverImage: Image? = null,
    val logoImage: Image? = null,
    val cuisines: List<String>? = null,
    val deliveryFee: Long,
    val prepTime: String,
    val minimumOrderValue: Int,
    val deliveryTime: String
)

@Serializable
data class Restaurant(
    val id: Long,
    val name: String?,
    val coverImage: Image? = null,
    val logoImage: Image? = null,
    val cuisines: List<RestaurantCuisines>? = null,
    val deliveryFee: Long?,
    val prepTime: Int?,
    val minimumOrderValue: Int?,
    val deliveryTime: String?
) {
    fun map(): RestaurantUI {
        return RestaurantUI(
            id = id,
            name = name.default(),
            coverImage = coverImage,
            logoImage = logoImage,
            cuisines = cuisines?.map { it.name.default() },
            deliveryFee = deliveryFee.getValueOrDefault(),
            prepTime = prepTime.getValueOrDefault().toString(),
            minimumOrderValue = minimumOrderValue.getValueOrDefault(),
            deliveryTime = deliveryTime.default()
        )
    }
}

@Serializable
data class RestaurantCuisines(
    val id: Long?,
    val name: String?,
)

