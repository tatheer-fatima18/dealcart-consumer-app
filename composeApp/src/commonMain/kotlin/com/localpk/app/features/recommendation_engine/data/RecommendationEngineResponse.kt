package com.localpk.app.features.recommendation_engine.data

import com.localpk.app.features.productlist.data.Product
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationEngineResponse(
    val body: RecommendationEngineBody,
    val success: Boolean,
)

@Serializable
data class RecommendationEngineBody(
    val results: RecommendationEngineList?,
)

@Serializable
data class RecommendationEngineList(
    val items: List<String>
)

@Serializable
data class RecommendationEngineListProducts(
    val body: List<Product>?, val success: Boolean
)