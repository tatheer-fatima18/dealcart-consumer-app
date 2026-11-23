package com.localpk.app.api

import com.localpk.app.features.restaurant.data.ORSRequestBody
import com.localpk.app.features.restaurant.data.ORSResponse
import com.localpk.app.network.httpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders

interface ORSService {
    suspend fun getRoute(
        apiKey: String,
        body: ORSRequestBody
    ): ORSResponse
}

class ORSServiceImpl : ORSService {

    private fun HttpRequestBuilder.configureRequest(
        parameter: String
    ) {
        addAuthHeader(parameter)
    }

    private fun HttpRequestBuilder.addAuthHeader(parameter: String) {
        headers {
            append(HttpHeaders.Authorization, parameter)
            append(HttpHeaders.ContentType, ContentType.Application.Json.contentType)
        }
    }

    override suspend fun getRoute(apiKey: String, body: ORSRequestBody): ORSResponse {
        return httpClient.post("v2/directions/driving-car") {
            configureRequest(apiKey)
            setBody(body)
        }.body()
    }

}