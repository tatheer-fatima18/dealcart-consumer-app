package com.localpk.app.features.address.data

import com.localpk.app.utils.EMPTY_STRING
import dev.jordond.compass.Coordinates
import kotlinx.serialization.Serializable

@Serializable
class UserAddress(
    val id: Long?,
    var latitude: Double,
    var longitude: Double,
    var streetAddress: String,
    var area: String,
    var city: String,
    var country: String,
    var default: Boolean = false,
    var pinAddress: String = EMPTY_STRING,
) {

    var label: String? = ""
        get() = if (field.isNullOrEmpty()) "Other" else field

    fun isSelected() = default

    fun getLatLng() = Coordinates(latitude, longitude)
}