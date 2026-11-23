package com.localpk.app.features.cart.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.default
import com.localpk.app.utils.getQueryParameter
import kotlinx.serialization.Serializable

@Serializable
data class OneLastStepAdResponse(
    val adName: String,
    val description: String,
    val link: String,
    val externalLink: String? = null,
    val isExternalLink: Boolean? = false,
    val isActive: Boolean,
    val productId: Long?,
    val product: OneLastStepAdProduct?
) : JavaSerializable {
    fun map() = OneLastStepUiData(
        name = adName,
        youTubeId = link.getQueryParameter("v") ?: EMPTY_STRING,
        externalLink = externalLink ?: EMPTY_STRING,
        isExternalLink = isExternalLink.default(),
        productId = productId ?: 0,
        product = product
    )
}

@Serializable
data class OneLastStepUiData(
    val name: String,
    val youTubeId: String,
    val externalLink: String,
    val isExternalLink: Boolean,
    val productId: Long,
    val product: OneLastStepAdProduct?
) : JavaSerializable

@Serializable
data class OneLastStepAdProduct(
    val id: Long,
    val name: String,
) : JavaSerializable

