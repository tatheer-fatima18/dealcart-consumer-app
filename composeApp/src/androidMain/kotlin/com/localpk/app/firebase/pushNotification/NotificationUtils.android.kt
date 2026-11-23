package com.localpk.app.firebase.pushNotification

import android.content.Intent

actual fun extractNotificationData(intent: Any): Map<String, String> {
    val androidIntent = intent as Intent
    val data = mutableMapOf<String, String>()
    
    // Extract all string extras
    androidIntent.extras?.keySet()?.forEach { key ->
        androidIntent.extras?.getString(key)?.let { value ->
            data[key] = value
        }
    }
    
    return data
} 