package com.localpk.app.otp

actual class OtpCodeProvider actual constructor() {
    actual fun startListening(onCode: (String) -> Unit) {
        // No-op on iOS; use UITextField/SwiftUI TextField with .oneTimeCode
    }

    actual fun stopListening() {
        // No-op
    }
}

private val sharedProvider = OtpCodeProvider()

actual fun getSharedOtpProvider(): OtpCodeProvider = sharedProvider


