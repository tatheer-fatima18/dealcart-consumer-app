package com.localpk.app.firebase.pushNotification

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


actual fun extractNotificationData(intent: Any): Map<String, String> {
    val data = intent.toNotificationDataMap()

    if (data.isNotEmpty()) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                kotlinx.coroutines.delay(2000)
                data.handleNotificationTap()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    return data
}

fun handlePassiveNotificationData(intent: Any) {
    val data = intent.toNotificationDataMap()

    if (data.isNotEmpty()) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                data.handleNotificationReceive()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

private fun Any.toNotificationDataMap(): MutableMap<String, String> {
    val data = mutableMapOf<String, String>()

    when (this) {
        is Map<*, *> -> {
            forEach { (key, value) ->
                if (key is String && value is String) {
                    data[key] = value
                }
            }
        }

        is NotificationData -> {
            intentDataToMap(data)
        }

        else -> return mutableMapOf()
    }

    return data
}

private fun NotificationData.intentDataToMap(destination: MutableMap<String, String>) {
    data.forEach { (key, value) ->
        destination[key] = value
    }
    if (title.isNotEmpty()) destination["title"] = title
    if (body.isNotEmpty()) destination["body"] = body
    imageUrl?.let { destination["imageUrl"] = it }
}