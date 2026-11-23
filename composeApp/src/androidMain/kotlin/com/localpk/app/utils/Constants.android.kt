package com.localpk.app.utils

import android.provider.Settings
import android.util.Log
import com.localpk.app.base.PlatformContext

actual fun getDeviceId(): String {
    return try {
        Settings.Secure.getString(PlatformContext.appContext.contentResolver, Settings.Secure.ANDROID_ID) ?: EMPTY_STRING
    } catch (e: Exception) {
        println(Log.getStackTraceString(e))
        ""
    }
}



