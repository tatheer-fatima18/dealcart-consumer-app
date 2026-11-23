package com.localpk.app.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val httpClient = HttpClient {
    expectSuccess = true
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
                explicitNulls = false
            }
        )
    }

    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.ALL
    }
}

suspend inline fun <T> callApi(apiCall: () -> T): ResponseResult<T> {
    return try {
        val result = apiCall()
        ResponseResult.Success(result)
    } catch (e: Exception) {
        when (e) {
            is ClientRequestException -> {
                val errorResponse = try {
                    e.response.body<ErrorResponse>()
                } catch (ex: Exception) {
                    ErrorResponse(
                        statusCode = e.response.status.value,
                        message = e.message
                    )
                }
                ResponseResult.Failure(errorResponse)
            }

            is ServerResponseException -> {
                ResponseResult.Failure(
                    ErrorResponse(
                        statusCode = e.response.status.value,
                        message = "Server error: ${e.message}"
                    )
                )
            }

            else -> ResponseResult.NetworkError
        }
    }
}