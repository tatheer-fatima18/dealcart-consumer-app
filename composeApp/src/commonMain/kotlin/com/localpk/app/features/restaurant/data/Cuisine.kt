package com.localpk.app.features.restaurant.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.default
import kotlinx.serialization.Serializable

@Serializable
data class Cuisine(
    val id: Long,
    val name: String?,
    val image: Image? = null
) : JavaSerializable {
    fun map(): CuisineUI {
        return CuisineUI(
            id = id,
            name = name.default(),
            image = image
        )
    }
}

@Serializable
data class CuisineUI(
    val id: Long,
    val name: String,
    val image: Image?
) : JavaSerializable

@Serializable
data class Image(
    val id: Long,
    val full: String? = EMPTY_STRING,
    val medium: String? = EMPTY_STRING,
    val path: String? = EMPTY_STRING,
    val thumbnail: String? = EMPTY_STRING,
) : JavaSerializable