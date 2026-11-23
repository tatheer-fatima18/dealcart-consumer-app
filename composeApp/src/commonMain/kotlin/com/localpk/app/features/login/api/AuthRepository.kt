package com.localpk.app.features.login.api

import com.localpk.app.features.login.data.AuthResponse
import com.localpk.app.features.login.data.GenerateOtpRequest
import com.localpk.app.features.login.data.GenerateOtpResponse
import com.localpk.app.features.login.data.VerifyOtpRequest
import com.localpk.app.network.ResponseResult
import com.localpk.app.network.callApi

interface AuthRepository {
    suspend fun verifyOTP(request: VerifyOtpRequest): ResponseResult<AuthResponse>
    suspend fun generateOTP(request: GenerateOtpRequest): ResponseResult<GenerateOtpResponse>
    suspend fun getRefreshToken(): ResponseResult<AuthResponse>
    suspend fun resendOTP(request: GenerateOtpRequest): ResponseResult<GenerateOtpResponse>
}

class AuthRepositoryImpl(
    private val authService: UserAuthService,
) : AuthRepository {

    override suspend fun verifyOTP(request: VerifyOtpRequest): ResponseResult<AuthResponse> {
        return callApi {
            authService.verifyOTP(request)
        }
    }

    override suspend fun getRefreshToken(): ResponseResult<AuthResponse> {
        return callApi {
            authService.getRefreshToken()
        }
    }

    override suspend fun generateOTP(request: GenerateOtpRequest): ResponseResult<GenerateOtpResponse> {
        return callApi {
            authService.generateOTP(request)
        }
    }

    override suspend fun resendOTP(request: GenerateOtpRequest): ResponseResult<GenerateOtpResponse> {
        return callApi {
            authService.resendOTP(request)
        }
    }

}