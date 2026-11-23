package com.localpk.app.common_components

import com.localpk.app.base.LOCATIONIQ_API_KEY
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class LocationIQAutocompleteResult(
    val place_id: String? = null,
    val display_name: String? = null,
    val display_place: String? = null,
    val display_address: String? = null,
    val lat: String? = null,
    val lon: String? = null,
    val address: LocationIQAddress? = null
)

@Serializable
data class LocationIQAddress(
    val name: String? = null,
    val road: String? = null,
    val neighbourhood: String? = null,
    val suburb: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postcode: String? = null,
    val country: String? = null,
    val country_code: String? = null
)

suspend fun searchPlacesWebAPI(query: String): List<GooglePlace> =
    withContext(Dispatchers.IO) {
        try {
            if (LOCATIONIQ_API_KEY.isBlank()) {
                return@withContext emptyList()
            }

            val httpClient = HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    })
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 30000
                }
            }

            kotlinx.coroutines.delay(300) // Debounce delay

            val results = httpClient.get("https://api.locationiq.com/v1/autocomplete") {
                parameter("key", LOCATIONIQ_API_KEY)
                parameter("q", query)
                parameter("countrycodes", "pk")
                parameter("limit", 5)
                parameter("accept-language", "en")
            }.body<List<LocationIQAutocompleteResult>>()

            results.mapNotNull { result ->
                val lat = result.lat?.toDoubleOrNull() ?: return@mapNotNull null
                val lon = result.lon?.toDoubleOrNull() ?: return@mapNotNull null

                GooglePlace(
                    name = result.display_place ?: result.display_name ?: "",
                    address = result.display_address ?: result.display_name ?: "",
                    latitude = lat,
                    longitude = lon
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
