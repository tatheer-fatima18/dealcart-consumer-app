package com.localpk.app.features.wallet.components

import androidx.compose.runtime.Composable
import com.localpk.app.base.CARD_ACCOUNT_ID
import com.localpk.app.base.CARD_HMAC_SECRET_KEY
import com.localpk.app.base.CARD_PUBLIC_KEY
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import platform.UIKit.UIViewController

/**
 * iOS-specific actual interface declarations
 */
actual interface PaymentElement {
    actual fun onReady(callback: (Boolean) -> Unit)
    actual fun confirmPayment(
        customerName: String,
        clientSecret: String,
        encryptionKey: String,
        callback: (String) -> Unit
    )
    actual fun clear()
}

actual interface EasyPaisaPaymentElement {
    actual fun onReady(callback: (Boolean) -> Unit)
    actual fun confirmPayment(
        customerName: String,
        clientSecret: String,
        encryptionKey: String,
        callback: (String) -> Unit
    )
    actual fun clear()
}

// Global variables to store Swift functions (similar to YouTube implementation)
lateinit var xpayPaymentElementController: (String, String, String, String) -> UIViewController
lateinit var xpayEasyPaisaElementController: (String, String, String, String) -> UIViewController

// Payment confirmation function references
lateinit var confirmCardPayment: (String, String, String, (String) -> Unit) -> Unit
lateinit var confirmEasyPaisaPayment: (String, String, String, (String) -> Unit) -> Unit

lateinit var clearCard: () -> Unit
lateinit var clearEasyPaisa: () -> Unit

// onReady callback function references
lateinit var cardOnReady: ((Boolean) -> Unit) -> Unit
lateinit var easyPaisaOnReady: ((Boolean) -> Unit) -> Unit

actual fun createPaymentElement(context: Any?): Any? {
    return try {
        val paymentViewController = xpayPaymentElementController(
            RemoteConfigs.getCardPublicKey(),
            CARD_HMAC_SECRET_KEY,
            CARD_ACCOUNT_ID,
            "card"
        )

        UIViewHelper.setPaymentElementView(paymentViewController.view)

        object : PaymentElement {
            override fun onReady(callback: (Boolean) -> Unit) {
                // Store the callback to be called when Swift form is ready
                cardOnReady(callback)
            }

            override fun confirmPayment(
                customerName: String,
                clientSecret: String,
                encryptionKey: String,
                callback: (String) -> Unit
            ) {
                try {
                    // Use the passed function instead of external function
                    confirmCardPayment(customerName, clientSecret, encryptionKey, callback)
                } catch (e: Exception) {
                    e.printStackTrace()
//            toastManager.showError("Invalid response received")
                }
            }

            override fun clear() {
                clearCard()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
//            toastManager.showError("Invalid response received")
        null
    }
}

actual fun createEasyPaisaPaymentElement(context: Any?): Any? {
    return try {
        val easyPaisaViewController = xpayEasyPaisaElementController(
            RemoteConfigs.getCardPublicKey(),
            CARD_HMAC_SECRET_KEY,
            CARD_ACCOUNT_ID,
            "easypaisa"
        )

        UIViewHelper.setEasyPaisaElementView(easyPaisaViewController.view)

        object : EasyPaisaPaymentElement {
            override fun onReady(callback: (Boolean) -> Unit) {
                // Store the callback to be called when Swift form is ready
                easyPaisaOnReady(callback)
            }

            override fun confirmPayment(
                customerName: String,
                clientSecret: String,
                encryptionKey: String,
                callback: (String) -> Unit
            ) {
                try {
                    confirmEasyPaisaPayment(customerName, clientSecret, encryptionKey, callback)
                } catch (e: Exception) {
                    e.printStackTrace()
//            toastManager.showError("Invalid response received")
                }
            }

            override fun clear() {
                clearEasyPaisa()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
//            toastManager.showError("Invalid response received")
        null
    }
}

@Composable
actual fun getPlatformContext(): Any? {
    return null
}

