package com.localpk.app.features.user_location.presentation

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.localpk.app.base.PlatformContext

actual fun openUserLocationSettings() {
    val context = PlatformContext.appContext
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:" + context.packageName)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}
