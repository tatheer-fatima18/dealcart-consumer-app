package com.localpk.app.firebase.pushNotification

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.localpk.app.base.PlatformContext
import kotlinx.coroutines.tasks.await

actual class PushNotificationServiceImpl : PushNotificationService, FirebaseMessagingService() {

    private var isAppForeground: Boolean = false
    private val context: Context = PlatformContext.appContext

    override suspend fun getToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            println("Failed to get FCM token: ${e.message}")
            null
        }
    }

    override suspend fun areNotificationsEnabled(): Boolean {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationManager.areNotificationsEnabled()
        } else {
            true // For older versions, assume enabled
        }
    }

    override suspend fun requestNotificationPermissions(): Boolean {
        // Check if notifications are enabled
        return areNotificationsEnabled()
    }

    override suspend fun deleteToken(): Boolean {
        return try {
            FirebaseMessaging.getInstance().deleteToken().await()
            true
        } catch (e: Exception) {
            println("Failed to delete FCM token: ${e.message}")
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