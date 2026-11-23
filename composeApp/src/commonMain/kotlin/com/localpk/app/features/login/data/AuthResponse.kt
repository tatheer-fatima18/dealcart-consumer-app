package com.localpk.app.features.login.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(val accessToken: String, val id: Long, val referralCode: String?) :
    JavaSerializable

@Serializable
data class VerifyOTPUseCaseParams(
    val firstName: String?,
    val lastName: String?,
    val otp: String,
    val phone: String,
    val hash: String,
    val fcm: String,
    val referrerCode: String,
) : JavaSerializable