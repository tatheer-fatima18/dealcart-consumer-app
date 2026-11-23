package com.localpk.app.features.banner.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class CarousalBanner(
    val body: BannerBody,
    val success: Boolean,
) : JavaSerializable

@Serializable
data class BannerBody(
    val results: List<Banner>?,
) : JavaSerializable

@Serializable
data class Banner(
    val bannerType: String,
    val id: Long,
    val image: BannerImage?,
    val order: Long,
    val redirectType: String?,
    val redirectTypeId: Long?,
    val redirectURL: String,
    val status: Boolean,
    val bannerTypeId: Long?,
) : JavaSerializable

@Serializable
data class BannerImage(
    val path: String,
    val medium: String?,
) : JavaSerializable

@Serializable
data class BannerUseCaseParams(
    val belongsTo: String,
    val bannerCacheVersion: String,
) : JavaSerializable