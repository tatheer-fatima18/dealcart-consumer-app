package com.localpk.app.features.user_location.data

import kotlinx.serialization.Serializable

@Serializable
data class UserLocationResponse(
    val success: Boolean,
    val body: Long
)

@Serializable
data class OperationalCities(
    val success: Boolean,
    val body: List<String>
)

fun doesMatchSearchQuery(city: String, query: String): Boolean {
    val matchingCombinations = listOf(
        city,
        "${city.first()}",
    )
    return matchingCombinations.any {
        it.contains(query, ignoreCase = true)
    }
}


