package com.localpk.app.features.share_to_friend.data

import kotlinx.serialization.Serializable


@Serializable
data class LinkResponse(
    val success: Boolean,
    val body: LinkBody,
)

@Serializable
data class LinkBody(
    val shortLink: String,
    val newUserReward: Double
)

@Serializable
data class CreateAppLinkRequest(
    val userId: Long,
    val platform: String = "firebase"
)
