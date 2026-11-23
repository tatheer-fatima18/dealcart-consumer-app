package com.localpk.app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.localpk.app.base.BaseUrls
import com.localpk.app.firebase.pushNotification.extractNotificationData
import com.localpk.app.firebase.pushNotification.handleNotificationTap
import com.localpk.app.otp.OtpCodeProvider
import com.localpk.app.otp.OtpCodeProviderHolder
import com.localpk.app.otp.OtpConsentReceiver
import com.localpk.app.otp.getSharedOtpProvider
import com.localpk.app.utils.BUILD_FLAVOUR_PROD
import com.localpk.app.utils.SharedPreferencesManager
import com.mergn.insights.classes.AttributeManager
import com.mergn.insights.classes.EventManager
import com.mergn.insights.classes.MergnSDK
import com.singular.sdk.Singular
import com.singular.sdk.SingularConfig
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.context.GlobalContext
import java.lang.ref.WeakReference


class MainActivity : ComponentActivity() {
    private val activityScope = CoroutineScope(Dispatchers.Main)
    private val sharedPreferencesManager: SharedPreferencesManager by inject()
    private var otpConsentReceiver: OtpConsentReceiver? = null
    private var isOtpReceiverRegistered = false
    private var keepSplashScreen by mutableStateOf(true)

    companion object {
        private var eventManager: EventManager? = null
        private var attributeManager: AttributeManager? = null
        private var appContext: Context? = null
        private var activityContextRef: WeakReference<Context>? = null

        private fun currentContext(): Context? {
            val activityContext = activityContextRef?.get()
            return activityContext ?: appContext
        }

        fun sendEvent(eventName: String, properties: Map<String, String>) {
            val context = currentContext() ?: return
            eventManager?.sendEvent(eventName, properties.toMutableMap(), context)
        }

        fun sendAttribute(attributeName: String, attributeValue: String) {
            val context = currentContext() ?: return
            attributeManager?.sendAttribute(context = context, attributeName, attributeValue)
        }

        fun postIdentification(identity: String) {
            val context = currentContext() ?: return
            eventManager?.login(identity, context)
        }

        fun updateActivityContext(context: Context) {
            activityContextRef = WeakReference(context)
        }
    }

    // Notification permission launcher
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("MainActivity", "Notification permission granted")
        } else {
            Log.d("MainActivity", "Notification permission denied")
        }
    }

    private var pendingDeepLink by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { keepSplashScreen }

        activityScope.launch {
            delay(2000)
            keepSplashScreen = false
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Set navigation bar to white background with grey icons
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = android.graphics.Color.WHITE
            // Set status bar to transparent (already set in theme, but ensuring it here)
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        }
        
        // Configure status bar and navigation bar icon colors using modern API
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Make status bar icons dark/grey (for light backgrounds)
        windowInsetsController?.isAppearanceLightStatusBars = true
        // Make navigation bar icons dark/grey (for light backgrounds)
        windowInsetsController?.isAppearanceLightNavigationBars = true
        
        Firebase.initialize(this)
        appContext = applicationContext
        updateActivityContext(this)
        if (!BuildConfig.DEBUG && BuildConfig.FLAVOR == BUILD_FLAVOUR_PROD) {
            initMergn()
            initSingularSDK()
        }

        // Request notification permission for Android 13+ (API 33+)
        requestNotificationPermission()

        // Handle notification tap if app was launched from notification
        handleNotificationIntent(intent)

        handleIntent(intent)

        // Provide current activity to OTP provider and register result launcher early
        OtpCodeProvider.setActivityProvider { this }
        OtpCodeProviderHolder.provider = getSharedOtpProvider().also {
            it.registerResultLauncher(this)
        }

        setContent {
            App(pendingDeepLink) { deepLink ->
                // Clear the pending deep link after it's been handled
                pendingDeepLink = null
            }
        }

        // Register FCM token after Koin is initialized
        // Post to main thread handler to ensure it runs after setContent and Koin initialization
        // This guarantees it runs after the current call stack, including Koin setup
        if (!BuildConfig.DEBUG && BuildConfig.FLAVOR == BUILD_FLAVOUR_PROD) {
            Handler(Looper.getMainLooper()).post {
                registerFCMTokenIfNeeded()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        updateActivityContext(this)
        registerOtpConsentReceiver()
    }

    override fun onResume() {
        super.onResume()
        updateActivityContext(this)
    }


    override fun onStop() {
        unregisterOtpConsentReceiver()
        super.onStop()
    }

    private fun handleNotificationIntent(intent: Intent) {
        // Check if this intent is from a notification tap
        if (intent.action == "NOTIFICATION_TAP") {
            val notificationData = extractNotificationData(intent)
            val deepLink = notificationData["notificationAction"]?.takeIf { it.isNotBlank() }
                ?: notificationData["url"]?.takeIf { it.isNotBlank() }

            if (deepLink != null) {
                pendingDeepLink = deepLink
                return
            }

            // Use the centralized notification handler with delay for killed state
            activityScope.launch {
                kotlinx.coroutines.delay(3000) // 3 second delay to ensure navigation is initialized
                notificationData.handleNotificationTap()
            }
        }
    }

    private fun initMergn() {
        eventManager = EventManager()
        attributeManager = AttributeManager()
        MergnSDK.initialize(application)
        val mergnApiKey = BaseUrls.mergnApiKey()
        eventManager?.registerApiKey(mergnApiKey, applicationContext)
        // FCM token registration will be done after Koin is initialized (see registerFCMTokenIfNeeded)
    }

    private fun registerFCMTokenIfNeeded() {
        // Verify Koin is initialized before accessing dependencies
        val koinInstance = GlobalContext.getOrNull()
        if (koinInstance == null) {
            Log.w("MainActivity", "Koin not initialized yet, FCM token registration skipped")
            return
        }

        try {
            val fcmToken = sharedPreferencesManager.getFCMToken()
            if (fcmToken.isNotEmpty()) {
                eventManager?.firebaseToken(fcmToken, context = applicationContext)
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to register FCM token: ${e.message}", e)
        }
    }

    private fun registerOtpConsentReceiver() {
        if (otpConsentReceiver == null) {
            otpConsentReceiver = OtpConsentReceiver()
        }

        if (!isOtpReceiverRegistered) {
            ContextCompat.registerReceiver(
                this,
                otpConsentReceiver,
                IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION),
                ContextCompat.RECEIVER_EXPORTED
            )
            isOtpReceiverRegistered = true
        }
    }

    private fun unregisterOtpConsentReceiver() {
        if (isOtpReceiverRegistered && otpConsentReceiver != null) {
            unregisterReceiver(otpConsentReceiver)
            isOtpReceiverRegistered = false
        }
    }


    private fun initSingularSDK() {
        val apiKey = BuildConfig.SINGULAR_API_KEY
        val secret = BuildConfig.SINGULAR_SECRET
        if (apiKey.isBlank() || secret.isBlank()) {
            Log.w("Singular", "SDK keys missing; skipping initialization")
            return
        }

        val config = SingularConfig(apiKey, secret)
        try {
            Singular.init(applicationContext, config)
            Log.d("Singular", "SDK initialized successfully")
        } catch (e: Exception) {
            Log.e("Singular", "SDK initialization failed: ${e.message}")
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("DeepLink", "onNewIntent called with: ${intent.data}")
        handleIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        Log.d("DeepLink", "handleIntent called with action: ${intent?.action}")
        Log.d("DeepLink", "handleIntent called with data: ${intent?.data}")

        if (intent?.action == Intent.ACTION_VIEW) {
            val data: Uri? = intent.data
            data?.let { uri ->
                val deepLink = uri.toString()
                Log.d("DeepLink", "Setting pending deep link: $deepLink")
                pendingDeepLink = deepLink
            }
        }
    }

    private fun requestNotificationPermission() {
        // Only request permission for Android 13+ (API level 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted
                    Log.d("MainActivity", "Notification permission already granted")
                }

                else -> {
                    // Request permission
                    Log.d("MainActivity", "Requesting notification permission")
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // For Android 12 and below, notifications are enabled by default
            Log.d("MainActivity", "Android version < 13, notifications enabled by default")
        }
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    App(null) {}
}