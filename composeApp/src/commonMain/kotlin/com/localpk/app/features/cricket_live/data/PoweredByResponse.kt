package com.localpk.app.features.cricket_live.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.utils.default
import kotlinx.serialization.Serializable


@Serializable
data class PoweredByResponse(val success: Boolean, val body: PoweredByResults) : JavaSerializable

@Serializable
data class PoweredByResults(val results: List<PoweredByDetailData>?) : JavaSerializable

@Serializable
data class PoweredByDetailData(
    val sponsorshipRedirectLink: String?,
    val image: ProductImage?,
    val name: String,
    val redirectType: String?,
    val redirectTypeId: Long?,
    val id: Int
) : JavaSerializable {
    fun map(): PoweredByDetailUiData = PoweredByDetailUiData(
        sponsorshipRedirectLink.default(),
        image,
        name,
        redirectType,
        redirectTypeId,
        id
    )
}

@Serializable
data class PoweredByDetailUiData(
    val sponsorshipRedirectLink: String,
    val image: ProductImage?,
    val name: String,
    val redirectType: String?,
    val redirectTypeId: Long?,
    val id: Int
) : JavaSerializable

@Serializable
data class ProductImage(val path: String?, val thumbnail: String?, val medium: String?) : JavaSerializable