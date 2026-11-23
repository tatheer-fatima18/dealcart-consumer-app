package com.localpk.app.features.login.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    val firstName: String,
    val lastName: String,
    val hash: String,
    val phoneNumber: String,
    val otp: String,
    val fcm: String,
    val referrerCode: String,
) : JavaSerializable