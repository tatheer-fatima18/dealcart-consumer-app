package com.localpk.app.features.dynamicLinksService.data

import com.localpk.app.utils.ZERO_LONG
import kotlinx.serialization.Serializable

@Serializable
data class DynamicLinkRequest(
    val referralCode: String,
    val platform: String = "firebase"
)

@Serializable
data class DynamicLinkRequestFlashDeal(
    val productId: Long,
    val dealId: Long,
    val productImage: String,
    val userId: Long = ZERO_LONG,
    val platform: String = "firebase",
)

