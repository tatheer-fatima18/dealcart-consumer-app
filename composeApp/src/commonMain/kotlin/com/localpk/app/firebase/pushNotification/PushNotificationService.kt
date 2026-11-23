package com.localpk.app.firebase.pushNotification

interface PushNotificationService {
    suspend fun getToken(): String?
    suspend fun areNotificationsEnabled(): Boolean
    suspend fun requestNotificationPermissions(): Boolean
    suspend fun deleteToken(): Boolean
    fun isAppInForeground(): Boolean
    fun setAppForegroundState(isForeground: Boolean)
}

data class NotificationData(
    val title: String,
    val body: String,
    val data: Map<String, String> = emptyMap(),
    val imageUrl: String? = null,
    val notificationId: String? = null,
    val isForeground: Boolean = false,
)

expect class PushNotificationServiceImpl() : PushNotificationService