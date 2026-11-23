package com.localpk.app.network

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

sealed class ResponseResult<out T> {
    data class Success<out T>(val data: T) : ResponseResult<T>() {
        fun getSuccessData(): T = data // Renamed function
    }

    data class Failure(val error: ErrorResponse) : ResponseResult<Nothing>() {
        fun getFailureError(): ErrorResponse = error // Renamed function
    }

    data object NetworkError : ResponseResult<Nothing>()

}

@Serializable
data class ErrorResponse(
    val statusCode: Int? = 0,
    val errorCode: Int? = 0,
    val path: String? = null,
    val error: String? = null,
    val message: String? = null,
    val userBlocked: Boolean? = false
) : JavaSerializable {
    fun getDisplayMessage(): String {
        return message ?: error ?: "An unexpected error occurred"
    }
}

@Serializable
data class GeneralResponse(
    val message: String? = null,
    val success: Boolean
) : JavaSerializable

@Serializable
data class GeneralStringResponse(
    val message: String,
    val success: Boolean
) : JavaSerializable

@Serializable
data class GeneralResponseBodyLong(
    val body: Long,
    val success: Boolean
) : JavaSerializable

@Serializable
data class GeneralBodyResponse(
    val body: BodyMessage,
    val success: Boolean
) : JavaSerializable

@Serializable
data class BodyMessage(
    val message: String,
) : JavaSerializable

@Serializable
data class GeneralAddressResponse(val success: Boolean, val body: AddressBody) : JavaSerializable

@Serializable
data class AddressBody(val Id: Long, val concatenatedAddress: String?) : JavaSerializable