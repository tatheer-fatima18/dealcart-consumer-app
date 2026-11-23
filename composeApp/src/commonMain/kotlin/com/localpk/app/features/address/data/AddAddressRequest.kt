package com.localpk.app.features.address.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class AddAddressRequest(
    val lng: Double,
    val lat: Double,
    val city: String,
    val addressType: String,
    val country: String = "Pakistan",
    val isDefault: Boolean = true,
    val status: Boolean = true,
    val id: Long = 0,
    val area: String,
    val streetName: String,
    val additionalAddressDetails: AdditionalAddressDetails?,
    val nearestLandmark: String,
    val phoneNumber: String?,
    val navigationFrom: String? = null
) {
    @Transient
    var listener: () -> Unit = { }

    @Transient
    var onDismiss: () -> Unit = { }
}

@Serializable
data class AdditionalAddressDetails(
    val flatNo: String? = null,
    val floorNo: Int? = null,
    val buildingName: String? = null,
    val houseNumber: String? = null,
    val shopName: String? = null,
    val shopNumber: String? = null
)

enum class AddressType(val type: String) {
    FLAT("flat"),
    HOUSE("house"),
    OFFICE("office"),
    SHOP("shop")
}