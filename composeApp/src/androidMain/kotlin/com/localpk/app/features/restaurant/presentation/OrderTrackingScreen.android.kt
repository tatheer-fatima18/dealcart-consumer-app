package com.localpk.app.features.restaurant.presentation

import android.content.Intent
import android.net.Uri
import com.localpk.app.base.PlatformContext

actual fun openDialPad(phoneNumber: String) {
    val context = PlatformContext.appContext
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}
