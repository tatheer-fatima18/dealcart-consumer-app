package com.localpk.app.features.login.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class Body(
    val hash: String,
    val user: User? = null,
) : JavaSerializable

@Serializable
data class GenerateOtpResponse(val success: Boolean, val body: Body) : JavaSerializable

