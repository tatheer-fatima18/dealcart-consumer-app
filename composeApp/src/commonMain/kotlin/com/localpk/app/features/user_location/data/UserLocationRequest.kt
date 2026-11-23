package com.localpk.app.features.user_location.data

import kotlinx.serialization.Serializable


@Serializable
data class UserLocationRequest(
    val latitude: Double,
    val longitude: Double,
    val city: String
)

