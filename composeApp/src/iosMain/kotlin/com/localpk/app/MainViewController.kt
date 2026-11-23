package com.localpk.app

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import com.localpk.app.features.wallet.components.cardOnReady
import com.localpk.app.features.wallet.components.clearCard
import com.localpk.app.features.wallet.components.clearEasyPaisa
import com.localpk.app.features.wallet.components.confirmCardPayment
import com.localpk.app.features.wallet.components.confirmEasyPaisaPayment
import com.localpk.app.features.wallet.components.easyPaisaOnReady
import com.localpk.app.features.wallet.components.xpayEasyPaisaElementController
import com.localpk.app.features.wallet.components.xpayPaymentElementController
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIViewController

lateinit var mapViewController: (((Double, Double) -> Unit)?, Double?, Double?, Double?) -> UIViewController
lateinit var youTubePlayerController: (String) -> UIViewController
lateinit var setMapCamera: (Double, Double, Double) -> Unit

lateinit var eventBridge: (String, Map<String, String>) -> Unit
lateinit var attributeBridge: (String, String) -> Unit
lateinit var identificationBridge: (String) -> Unit
lateinit var singularCustomUserIdBridge: (String) -> Unit
lateinit var singularEventBridge: (String, Map<String, Any?>) -> Unit
lateinit var singularUnsetCustomUserIdBridge: () -> Unit
lateinit var singularCustomRevenueBridge: (String, String, Double, Map<String, Any?>) -> Unit

// Global deep link observer setup
private fun setupDeepLinkObserver(): (String) -> Unit {

    val observer = { notification: NSNotification? ->

        // Try to get URL from object first (as posted in Swift)
        val urlFromObject = notification?.`object` as? String

        // Try to get URL from userInfo as fallback
        val urlFromUserInfo = notification?.userInfo?.get("object") as? String

        val url = urlFromObject ?: urlFromUserInfo

        if (url != null) {
            // Post a notification that the Compose UI can listen to
            NSNotificationCenter.defaultCenter.postNotificationName(
                aName = "deepLinkReceivedFromGlobal",
                `object` = url
            )
        }
    }

    try {
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = "deepLinkReceived",
            `object` = null,
            queue = null,
            usingBlock = observer
        )
    } catch (e: Exception) {

    }

    return { url ->
        // This function can be called directly if needed
        NSNotificationCenter.defaultCenter.postNotificationName(
            aName = "deepLinkReceivedFromGlobal",
            `object` = url
        )
    }
}

fun MainViewController(
    mapUIViewController: (((Double, Double) -> Unit)?, Double?, Double?, Double?) -> UIViewController,
    youTubePlayerUIViewController: (String) -> UIViewController,
    xpayPaymentElementUIViewController: (String, String, String, String) -> UIViewController,
    xpayEasyPaisaElementUIViewController: (String, String, String, String) -> UIViewController,
    // Add these new parameters
    confirmCardPaymentFunction: (String, String, String, (String) -> Unit) -> Unit,
    confirmEasyPaisaPaymentFunction: (String, String, String, (String) -> Unit) -> Unit,
    clearCardFunction: () -> Unit,
    clearEasyPaisaFunction: () -> Unit,
    onEvent: (String, Map<String, String>) -> Unit,
    onAttribute: (String, String) -> Unit,
    onIdentification: (String) -> Unit,
    onSingularCustomUserId: (String) -> Unit,
    onSingularUnsetCustomUserId: () -> Unit,
    onSingularEvent: (String, Map<String, Any?>) -> Unit,
    onSingularCustomRevenue: (String, String, Double, Map<String, Any?>) -> Unit,
    // Add onReady callback functions
    cardOnReadyFunction: ((Boolean) -> Unit) -> Unit,
    easyPaisaOnReadyFunction: ((Boolean) -> Unit) -> Unit,
) = ComposeUIViewController(
    configure = {
        initKoinIOS()
        // Setup global deep link observer immediately
        setupDeepLinkObserver()
    }
) {
    var deepLink by remember { mutableStateOf<String?>(null) }

    // Listen for deep link notifications from the global observer
    LaunchedEffect(Unit) {
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = "deepLinkReceivedFromGlobal",
            `object` = null,
            queue = null
        ) { notification ->
            val url = notification?.`object` as? String

            if (url != null) {
                deepLink = url
            }
        }
    }

    App(deepLink = deepLink) { handledDeepLink ->
        // Clear the deep link after it's been handled
        deepLink = null
    }

    mapViewController = mapUIViewController
    youTubePlayerController = youTubePlayerUIViewController
    xpayPaymentElementController = xpayPaymentElementUIViewController
    xpayEasyPaisaElementController = xpayEasyPaisaElementUIViewController

    // Store the payment confirmation functions
    confirmCardPayment = confirmCardPaymentFunction
    confirmEasyPaisaPayment = confirmEasyPaisaPaymentFunction
    clearCard = clearCardFunction
    clearEasyPaisa = clearEasyPaisaFunction
    eventBridge = onEvent
    attributeBridge = onAttribute
    identificationBridge = onIdentification
    singularCustomUserIdBridge = onSingularCustomUserId
    singularUnsetCustomUserIdBridge = onSingularUnsetCustomUserId
    singularEventBridge = onSingularEvent
    singularCustomRevenueBridge = onSingularCustomRevenue

    // Store the onReady callback functions
    cardOnReady = cardOnReadyFunction
    easyPaisaOnReady = easyPaisaOnReadyFunction
}

fun initialize() {
    // Initialize GitLive Firebase service
    Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)
}