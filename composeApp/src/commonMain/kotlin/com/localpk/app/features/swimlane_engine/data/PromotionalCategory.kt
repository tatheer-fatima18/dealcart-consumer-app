package com.localpk.app.features.swimlane_engine.data

import kotlinx.serialization.Serializable

@Serializable
data class PromotionalCategory(
    val categoryType: String,
    val id: Long,
    val name: String,
    val status: Boolean,
    val image: Image? = null
)

@Serializable
data class Image(
    val medium: String,
    val id: Int,
) 