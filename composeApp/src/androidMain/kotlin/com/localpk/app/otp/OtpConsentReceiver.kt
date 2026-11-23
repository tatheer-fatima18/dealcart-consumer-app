package com.localpk.app.otp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever

/** Forwards the consent intent to the OtpCodeProvider launcher. */
class OtpConsentReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras ?: return
            val consentIntent: Intent? = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT)
            if (consentIntent != null) {
                // Forward to provider which will launch StartActivityForResult
                OtpCodeProviderHolder.provider?.onReceiveConsentIntent(consentIntent)
            }
        }
    }
}

/** Simple holder to access the current OtpCodeProvider instance. */
object OtpCodeProviderHolder {
    var provider: OtpCodeProvider? = null
}


