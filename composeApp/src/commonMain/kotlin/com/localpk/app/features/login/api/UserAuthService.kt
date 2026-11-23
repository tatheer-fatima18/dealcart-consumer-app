package com.localpk.app.features.login.api

import com.localpk.app.base.data.BaseApiService
import com.localpk.app.features.login.data.AuthResponse
import com.localpk.app.features.login.data.GenerateOtpRequest
import com.localpk.app.features.login.data.GenerateOtpResponse
import com.localpk.app.features.login.data.VerifyOtpRequest
import com.localpk.app.utils.SharedPreferencesManager
import io.ktor.client.HttpClient

interface UserAuthService {
    suspend fun verifyOTP(body: VerifyOtpRequest): AuthResponse
    suspend fun getRefreshToken(): AuthResponse
    suspend fun generateOTP(body: GenerateOtpRequest): GenerateOtpResponse
    suspend fun resendOTP(body: GenerateOtpRequest): GenerateOtpResponse
}

class UserAuthServiceImpl(
    httpClient: HttpClient,
    baseUrl: String,
    sharedPreferencesManager: SharedPreferencesManager
) : BaseApiService(httpClient, baseUrl, sharedPreferencesManager), UserAuthService {

    override suspend fun verifyOTP(body: VerifyOtpRequest): AuthResponse {
        return post("/api/otp/verify", body)
    }

    override suspend fun getRefreshToken(): AuthResponse {
        return post("/api/otp/refresh-token")
    }

    override suspend fun generateOTP(
        body: GenerateOtpRequest
    ): GenerateOtpResponse {
        return post("/api/otp/send", body)
    }

    override suspend fun resendOTP(
        body: GenerateOtpRequest
    ): GenerateOtpResponse {
        return post("/api/otp/resend", body)
    }
}