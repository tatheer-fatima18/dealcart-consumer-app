package com.localpk.app.otp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.auth.api.phone.SmsRetrieverStatusCodes
import com.google.android.gms.common.api.CommonStatusCodes
import java.util.regex.Pattern

/**
 * Android implementation using SMS User Consent API.
 * No RECEIVE_SMS permission needed; user approves reading a single SMS.
 */
actual class OtpCodeProvider actual constructor() {
    private var client: SmsRetrieverClient? = null
    private var callback: ((String) -> Unit)? = null

    // We don't keep a strong reference to an Activity; the app should supply one when wiring the launcher
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var pendingConsentIntent: Intent? = null

    fun registerResultLauncher(activity: ComponentActivity) {
        resultLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            val message = data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            if (result.resultCode == Activity.RESULT_OK && message != null) {
                parseAndEmitCode(message)
            }
        }
        pendingConsentIntent?.let {
            resultLauncher?.launch(it)
            pendingConsentIntent = null
        }
    }

    actual fun startListening(onCode: (String) -> Unit) {
        callback = onCode
        val context: Context? = activityProvider()
        if (context == null) return
        client = SmsRetriever.getClient(context)
        // Start user consent for any sender since message may come from varying numbers
        client?.startSmsUserConsent(null)
            ?.addOnSuccessListener {
                // No-op: waiting for onReceiveConsentIntent to be triggered by broadcast
            }
            ?.addOnFailureListener {
                // Best-effort; nothing to do here
            }
    }

    actual fun stopListening() {
        callback = null
        client = null
        // No explicit unregister needed; launcher is bound to Activity lifecycle
    }

    /** Call from MainActivity when the consent intent arrives via broadcast. */
    fun onReceiveConsentIntent(consentIntent: Intent) {
        val launcher = resultLauncher
        if (launcher != null) {
            launcher.launch(consentIntent)
        } else {
            pendingConsentIntent = consentIntent
        }
    }

    private fun parseAndEmitCode(message: String) {
        // Extract a 6-digit code from the message text
        val matcher = OTP_REGEX.matcher(message)
        if (matcher.find()) {
            val code = matcher.group(1)
            if (code != null) {
                callback?.invoke(code)
            }
        }
    }

    companion object {
        private val OTP_REGEX: Pattern = Pattern.compile("\\b(\\d{6})\\b")

        // Simple current activity provider hook; set by MainActivity
        private var activityProvider: () -> Activity? = { null }
        fun setActivityProvider(provider: () -> Activity?) {
            activityProvider = provider
        }
    }
}

private val sharedProvider: OtpCodeProvider by lazy { OtpCodeProvider() }

actual fun getSharedOtpProvider(): OtpCodeProvider = sharedProvider


