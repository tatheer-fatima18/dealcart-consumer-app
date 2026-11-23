package com.localpk.app.features.cart.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class PromoCodeRequest(
    val isEnabled: Boolean,
    val promoCode: String?,
    val itemIds: List<Long>
) : JavaSerializable


@Serializable
data class PromoNavigationArgs(
    val isBucketEnabled: Boolean,
    val itemIds: List<Long>,
) : JavaSerializable
