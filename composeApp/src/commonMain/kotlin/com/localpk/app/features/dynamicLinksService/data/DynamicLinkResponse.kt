package com.localpk.app.features.dynamicLinksService.data

import kotlinx.serialization.Serializable

@Serializable
data class DynamicLinkResponse(
    val success: Boolean,
    val body: DynamicLinkBody,
)

@Serializable
data class DynamicLinkBody(
    val shortLink: String,
)
