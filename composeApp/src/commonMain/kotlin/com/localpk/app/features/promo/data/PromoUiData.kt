package com.localpk.app.features.promo.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PromoUiData(
    val isFood: Boolean = false
)