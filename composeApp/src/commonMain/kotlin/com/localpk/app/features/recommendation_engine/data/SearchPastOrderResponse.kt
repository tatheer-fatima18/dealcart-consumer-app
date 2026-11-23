package com.localpk.app.features.recommendation_engine.data

import kotlinx.serialization.Serializable

@Serializable
data class SearchPastOrderResponse(val body: SearchPastOrder)

@Serializable
data class SearchPastOrder(
    val pastPurchaseItems: List<String>?,
    val recommendedItems: List<String>?,
    val items: List<String>?
)