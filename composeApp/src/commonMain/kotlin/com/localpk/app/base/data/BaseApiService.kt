package com.localpk.app.base.data

import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.ANDROID
import com.localpk.app.utils.ANDROID_ID
import com.localpk.app.utils.DEVICE_ID
import com.localpk.app.utils.PLATFORM
import com.localpk.app.utils.SELECTED_WAREHOUSE_ID
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.USER_AGENT
import com.localpk.app.utils.VERSION_CODE
import com.localpk.app.utils.VERSION_CODE_INT
import com.localpk.app.utils.WAREHOUSE
import com.localpk.app.utils.WAREHOUSE_ID
import com.localpk.app.utils.getDeviceId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

abstract class BaseApiService(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val sharedPreferencesManager: SharedPreferencesManager
) {

    internal suspend inline fun <reified T> get(
        endpoint: String,
        parameters: Map<String, Any> = emptyMap(),
        additionalHeaders: Map<String, String> = emptyMap(),
        mockUrl: String? = null
    ): T {
        val url = mockUrl ?: "$baseUrl$endpoint"
        val response: HttpResponse = httpClient.get(url) {
            configureRequest(parameters, additionalHeaders)
        }
        if ((response.status.value == 201 || response.status.value == 204)) {
            val content = response.bodyAsText()
            if (content.isBlank()) {
                return Json.decodeFromString<T>("{}")
            }
        }
        return response.body()
    }

    internal suspend inline fun <reified T> post(
        endpoint: String,
        requestBody: Any? = null,
        additionalHeaders: Map<String, String> = emptyMap(),
    ): T {
        val response: HttpResponse = httpClient.post("$baseUrl$endpoint") {
            configureRequest(emptyMap(), additionalHeaders)
            if (requestBody != null) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
        }
        if ((response.status.value == 201 || response.status.value == 204)) {
            val content = response.bodyAsText()
            if (content.isBlank()) {
                return Json.decodeFromString<T>("{}")
            }
        }
        return response.body()
    }

    internal suspend inline fun <reified T> patch(
        endpoint: String,
        requestBody: Any? = null,
        additionalHeaders: Map<String, String> = emptyMap(),
    ): T {
        val response: HttpResponse = httpClient.patch("$baseUrl$endpoint") {
            configureRequest(emptyMap(), additionalHeaders)
            if (requestBody != null) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
        }
        if ((response.status.value == 201 || response.status.value == 204)) {
            val content = response.bodyAsText()
            if (content.isBlank()) {
                return Json.decodeFromString<T>("{}")
            }
        }
        return response.body()
    }

    internal suspend inline fun <reified T> delete(
        endpoint: String,
        parameters: Map<String, Any> = emptyMap(),
        additionalHeaders: Map<String, String> = emptyMap()
    ): T {
        val response: HttpResponse = httpClient.delete("$baseUrl$endpoint") {
            configureRequest(parameters, additionalHeaders)
        }
        if ((response.status.value == 201 || response.status.value == 204)) {
            val content = response.bodyAsText()
            if (content.isBlank()) {
                return Json.decodeFromString<T>("{}")
            }
        }
        return response.body()
    }

    private fun HttpRequestBuilder.configureRequest(
        parameters: Map<String, Any>,
        additionalHeaders: Map<String, String>
    ) {
        addAuthHeader()
        addWarehouseHeaders()
        addCommonHeaders()
        addAdditionalHeaders(additionalHeaders)
        parameters.forEach { (key, value) ->
            parameter(key, value)
        }
    }

    private fun HttpRequestBuilder.addCommonHeaders() {
        headers {
            append(PLATFORM, ANDROID)
        }
        headers {
            append(DEVICE_ID, getDeviceId())
        }
        headers {
            append(ANDROID_ID, getDeviceId())
        }
        headers{
            append(USER_AGENT, RemoteConfigs.getUserAgent())
        }
        headers{
            append(VERSION_CODE, VERSION_CODE_INT.toString())
        }
    }

    private fun HttpRequestBuilder.addWarehouseHeaders() {
        headers {
            append(WAREHOUSE, sharedPreferencesManager.getWarehouseId().toString())
            append(WAREHOUSE_ID, sharedPreferencesManager.getWarehouseId().toString())
            append(
                SELECTED_WAREHOUSE_ID,
                sharedPreferencesManager.getSelectedWarehouseId().toString()
            )
        }
    }

    private fun HttpRequestBuilder.addAuthHeader() {
        headers {
            append(HttpHeaders.Authorization, "Bearer ${sharedPreferencesManager.getToken()}")
        }
    }

    private fun HttpRequestBuilder.addAdditionalHeaders(additionalHeaders: Map<String, String>) {
        additionalHeaders.forEach { (key, value) ->
            headers {
                append(key, value)
            }
        }
    }

    internal suspend inline fun <reified T> put(
        endpoint: String,
        requestBody: Any? = null,
        additionalHeaders: Map<String, String> = emptyMap(),
    ): T {
        return httpClient.put("$baseUrl$endpoint") {
            configureRequest(emptyMap(), additionalHeaders)
            if (requestBody != null) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
        }.body()
    }
}
