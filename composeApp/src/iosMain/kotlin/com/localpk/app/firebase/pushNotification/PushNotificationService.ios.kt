package com.localpk.app.firebase.pushNotification

import cocoapods.FirebaseMessaging.FIRMessaging
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
actual class PushNotificationServiceImpl : PushNotificationService {

    private var isAppForeground: Boolean = false

    override suspend fun getToken(): String? {
        return try {
            suspendCancellableCoroutine { continuation ->
                // Check if APNs token is set
                
                FIRMessaging.messaging().tokenWithCompletion { token, error ->
                    if (error != null) {
                        continuation.resume(null)
                    } else {
                        continuation.resume(token)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    override suspend fun areNotificationsEnabled(): Boolean {
        return true
    }

    override suspend fun requestNotificationPermissions(): Boolean {
        return true
    }

    override suspend fun deleteToken(): Boolean {
        return try {
            FIRMessaging.messaging().deleteTokenWithCompletion {}
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun isAppInForeground(): Boolean {
        return isAppForeground
    }

    override fun setAppForegroundState(isForeground: Boolean) {
        isAppForeground = isForeground
    }
}