package com.localpk.app.features.address.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AreaData(
    val id: Long,
    val name: String,
    val city: String
) : JavaSerializable

@Serializable
data class MapPolygonResponse(
    val success: Boolean,
    val body: Polygon
) : JavaSerializable

@Serializable
data class Polygon(
    val type: String,
    val features: List<Feature>
) : JavaSerializable

@Serializable
data class Feature(
    val type: String,
    val geometry: Geometry?,
    val properties: Properties
) : JavaSerializable

@Serializable
data class Geometry(
    val type: String,
    val coordinates: List<List<List<Double>>>?,
    val geometries: List<Geometry>?
) : JavaSerializable

@Serializable
data class Properties(
    val name: String,
    val description: String,
    val styleUrl: String,
    @SerialName("fill-opacity")
    val fillOpacity: Double? = 0.302,
    val fill: String,
    val strokeOpacity: Int?,
    val stroke: String,
    val strokeWidth: Double?
) : JavaSerializable

