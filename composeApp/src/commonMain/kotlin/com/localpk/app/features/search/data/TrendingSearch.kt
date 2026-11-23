package com.localpk.app.features.search.data

import kotlinx.serialization.Serializable


@Serializable
data class TrendingSearchResponse(val body: List<String>)

@Serializable
data class SearchProductName(val productName: String) 