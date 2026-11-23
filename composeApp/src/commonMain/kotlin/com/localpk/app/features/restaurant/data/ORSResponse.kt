package com.localpk.app.features.restaurant.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ORSResponse(
    @SerialName("routes")
    val routes: List<Route>? = null
)

@Serializable
data class Route(
    @SerialName("geometry")
    val geometry: String? = null
)

@Serializable
data class ORSRequestBody(
    @SerialName("coordinates")
    val coordinates: List<List<Double>>
)