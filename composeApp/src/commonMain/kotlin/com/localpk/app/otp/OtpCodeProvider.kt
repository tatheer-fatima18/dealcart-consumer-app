package com.localpk.app.otp

/**
 * Platform OTP reader contract.
 */
expect class OtpCodeProvider() {
    fun startListening(onCode: (String) -> Unit)
    fun stopListening()
}

/** Obtain a shared provider instance suitable for the current platform. */
expect fun getSharedOtpProvider(): OtpCodeProvider

