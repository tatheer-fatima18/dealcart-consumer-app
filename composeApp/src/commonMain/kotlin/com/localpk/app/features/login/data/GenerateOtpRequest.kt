package com.localpk.app.features.login.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class GenerateOtpRequest(
    val phoneNumber: String,
) : JavaSerializable