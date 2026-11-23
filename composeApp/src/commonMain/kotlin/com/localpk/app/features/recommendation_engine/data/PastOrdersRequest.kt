package com.localpk.app.features.recommendation_engine.data

data class RecommendationEngineRequest(
    val swimlane: String,
    val screenType: String,
)

data class RecommendationEngineProductsRequest(
    val swimlane: String,
    val screenType: String,
    val productId: Number
)