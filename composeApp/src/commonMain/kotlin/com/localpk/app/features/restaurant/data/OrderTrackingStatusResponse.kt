package com.localpk.app.features.restaurant.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class OrderTracking(
    val isPrepaid: Boolean?,
    val trackingStatus: String?,
    val deliveryTime: String?,
    val latitude: Double?,
    val longitude: Double?
) : JavaSerializable

enum class OrderTrackingStatuses(val status: String) {
    CANCELLED("cancelled"),
    DELAYED("delayed"),
    DELIVERED("delivered"),
    IN_PROGRESS("confirmed"),
    OUT_FOR_DELIVERY("out_for_delivery"),
    PENDING("pending")
}
