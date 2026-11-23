package com.localpk.app.firebase.pushNotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.localpk.app.MainActivity
import com.localpk.app.R
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.MultiplatformSettingsFactory
import com.localpk.app.utils.SharedPreferencesManagerImpl
import com.localpk.app.utils.isNotNull
import com.mergn.insights.classes.EventManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DealCartFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "DealCart"
        private const val CHANNEL_NAME = "DealCart"
        private const val CHANNEL_DESCRIPTION = "Notifications from DealCart app"
        private const val CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("New FCM token: $token")
        serviceScope.launch {
            sendRegistrationToServer(token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Handle data payload
        if (remoteMessage.data.isNotEmpty()) {
            serviceScope.launch {
                remoteMessage.data.handleNotificationReceive()
            }
            if (remoteMessage.data["type"].equals("app_sdk")) {
                EventManager().mergnNotification(remoteMessage, this)
                return
            }
            handleDataPayload(remoteMessage)
        } else if (remoteMessage.notification.isNotNull()) {
            val notificationData = NotificationData(
                title = remoteMessage.notification?.title ?: "DealCart",
                body = remoteMessage.notification?.body ?: "",
                data = remoteMessage.data,
                imageUrl = remoteMessage.notification?.imageUrl?.toString(),
                notificationId = remoteMessage.messageId,
            )

            // Show system notification
            showNotification(notificationData)
        }
    }

    private fun handleDataPayload(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        when (val redirection = data["redirection"]) {
            "refresh_cart" -> {
                val intent = Intent("refresh_cart_event")
                sendBroadcast(intent)
            }

            "confirmed_orders" -> {
                if (data["title"] == "Your order is on the way!" &&
                    !RemoteConfigs.enableDisplayOfDelivery()
                ) {
                    val intent = Intent("confirmed_orders_event")
                    sendBroadcast(intent)
                }
            }

            else -> {
                when (redirection) {
                    "payment_success" -> {
                        val intent = Intent("payment_success_event")
                        sendBroadcast(intent)
                    }

                    "payment_failure" -> {
                        val intent = Intent("payment_failure_event")
                        sendBroadcast(intent)
                    }

                    "wallet" -> {
                        val intent = Intent("wallet_event")
                        sendBroadcast(intent)
                    }

                    "order_tracking" -> {
                        val intent = Intent("order_tracking_event")
                        sendBroadcast(intent)
                    }
                }

                // Handle other notification types
                handleCustomNotification(remoteMessage)
            }
        }
    }

    private fun handleCustomNotification(remoteMessage: RemoteMessage) {
        val notificationData = NotificationData(
            title = remoteMessage.data["title"] ?: "DealCart",
            body = remoteMessage.data["body"] ?: "",
            data = remoteMessage.data,
            imageUrl = remoteMessage.data["imageUrl"],
            notificationId = remoteMessage.messageId,
        )
        showNotification(notificationData)
    }


    private fun sendRegistrationToServer(token: String) {
        val sharedPreferencesManager =
            SharedPreferencesManagerImpl(MultiplatformSettingsFactory(applicationContext))
        if (token.isNotEmpty()) {
            // TODO: Send token to your backend server
            println("FCM token saved and ready to send to server")
        }
    }

    private fun showNotification(notificationData: NotificationData) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create intent for notification tap with proper action
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            action = "NOTIFICATION_TAP"

            // Add notification data as extras
            notificationData.data.forEach { (key, value) ->
                putExtra(key, value)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationData.notificationId?.hashCode() ?: 0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build notification
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(notificationData.title)
            .setContentText(notificationData.body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        // Add image if available
        notificationData.imageUrl?.let { imageUrl ->
            // TODO: Load and set notification image
            // You can use Glide or Coil to load the image
        }

        // Show notification
        val notificationId =
            notificationData.notificationId?.hashCode() ?: System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                CHANNEL_IMPORTANCE
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
} 