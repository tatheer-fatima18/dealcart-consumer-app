package com.localpk.app.features.address.data

import com.localpk.app.utils.EMPTY_STRING
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class AddAddressUiData(
    val id: Long,
    val address: String = EMPTY_STRING,
    val lng: Double,
    val lat: Double,
    val street: String,
    val city: String,
    val cities: List<String>,
    val state: List<AreaData>,
    val area: String,
    val addressType: String?,
    val additionalAddressDetails: AdditionalAddressDetails?,
    val navigationFrom: String,
    val nearestLandmark: String,
    val phoneNumber: String?
) {
    @Transient
    var listener: (AddAddressRequest) -> Unit = { }

    @Transient
    var editListener: () -> Unit = { }
}
