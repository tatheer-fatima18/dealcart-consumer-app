package com.localpk.app.api

import com.localpk.app.features.restaurant.data.ORSRequestBody
import com.localpk.app.features.restaurant.data.ORSResponse
import com.localpk.app.features.user_location.data.Geolocation
import com.localpk.app.network.ErrorResponse
import com.localpk.app.network.ResponseResult

interface RouteRepository {
    suspend fun fetchRouteRaw(
        apiKey: String,
        body: ORSRequestBody
    ): ResponseResult<ORSResponse>

    suspend fun getRoutePath(
        start: Geolocation,
        end: Geolocation,
        apiKey: String
    ): List<Geolocation>
}

class RouteRepositoryImpl(
    private val orsService: ORSService
) : RouteRepository {

    override suspend fun getRoutePath(
        start: Geolocation,
        end: Geolocation,
        apiKey: String
    ): List<Geolocation> {
        return try {
            val req = ORSRequestBody(
                coordinates = listOf(
                    listOf(start.longitude, start.latitude),
                    listOf(end.longitude, end.latitude)
                )
            )

            val res = orsService.getRoute(apiKey, req)
            val geometry = res.routes?.firstOrNull()?.geometry
            val decoded = geometry?.let { decodePolyline(it) } ?: emptyList()

            buildList {
                add(start)
                addAll(decoded)
                add(end)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun fetchRouteRaw(
        apiKey: String,
        body: ORSRequestBody
    ): ResponseResult<ORSResponse> {
        return try {
            val response = orsService.getRoute(apiKey, body)
            ResponseResult.Success(response)
        } catch (e: Exception) {
            ResponseResult.Failure(
                ErrorResponse(
                    statusCode = 500,
                    message = "Server error: ${e.message}"
                )
            )
        }
    }

    private fun decodePolyline(encoded: String): List<Geolocation> {
        val poly = mutableListOf<Geolocation>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dLat = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
            lat += dLat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dLng = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
            lng += dLng

            poly.add(
                Geolocation(
                    latitude = lat / 1E5,
                    longitude = lng / 1E5
                )
            )
        }

        return poly
    }
}


