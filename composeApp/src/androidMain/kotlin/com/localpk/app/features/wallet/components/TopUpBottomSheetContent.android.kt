package com.localpk.app.features.wallet.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.localpk.app.base.CARD_ACCOUNT_ID
import com.localpk.app.base.CARD_HMAC_SECRET_KEY
import com.localpk.app.common_components.GlobalToastManager
import com.localpk.app.common_components.SnackbarType
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.xstak.xpay_element.EasyPaisaPaymentElement as XPayEasyPaisaPaymentElement
import com.xstak.xpay_element.PaymentElement as XPayPaymentElement

/**
 * Android-specific actual interface declarations
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

/**
 * Android wrapper classes that implement our interfaces
 */
class AndroidPaymentElementWrapper(
    private val paymentElement: XPayPaymentElement
) : PaymentElement {
    override fun onReady(callback: (Boolean) -> Unit) {
        try {
            paymentElement.onReady { isReady ->
                callback(isReady)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GlobalToastManager.showToast(
                message = "Invalid response received",
                type = SnackbarType.ERROR
            )
        }
    }
    
    override fun confirmPayment(
        customerName: String,
        clientSecret: String,
        encryptionKey: String,
        callback: (String) -> Unit
    ) {
        try {
            paymentElement.confirmPayment(
                customerName = customerName,
                clientSecret = clientSecret,
                encryptionKey = encryptionKey
            ) { response ->
                callback(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GlobalToastManager.showToast(
                message = "Invalid response received",
                type = SnackbarType.ERROR
            )
        }
    }
    
    fun getView(): android.view.View {
        return paymentElement
    }

    override fun clear() {
        paymentElement.clear()
    }
}

class AndroidEasyPaisaPaymentElementWrapper(
    private val easyPaisaElement: XPayEasyPaisaPaymentElement
) : EasyPaisaPaymentElement {
    override fun onReady(callback: (Boolean) -> Unit) {
        try {
            easyPaisaElement.onReady { isReady ->
                callback(isReady)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GlobalToastManager.showToast(
                message = "Invalid response received",
                type = SnackbarType.ERROR
            )
        }
    }
    
    override fun confirmPayment(
        customerName: String,
        clientSecret: String,
        encryptionKey: String,
        callback: (String) -> Unit
    ) {
        try {
            easyPaisaElement.confirmPayment(
                customerName = customerName,
                clientSecret = clientSecret,
                encryptionKey = encryptionKey
            ) { response ->
                callback(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GlobalToastManager.showToast(
                message = "Invalid response received",
                type = SnackbarType.ERROR
            )
        }
    }
    
    fun getView(): android.view.View {
        return easyPaisaElement
    }

    override fun clear() {
        easyPaisaElement.clear()
    }
}

/**
 * Android-specific implementation for creating payment elements
 */
actual fun createPaymentElement(context: Any?): Any? {
    return try {
        val activityContext = context as? Activity ?: return null

        val paymentElement = XPayPaymentElement(
            context = activityContext,
            publicKey = RemoteConfigs.getCardPublicKey(),
            hmacSecretKey = CARD_HMAC_SECRET_KEY,
            accountId = CARD_ACCOUNT_ID
        )
        AndroidPaymentElementWrapper(paymentElement)
    } catch (e: Exception) {
        e.printStackTrace()
        GlobalToastManager.showToast(
            message = "Invalid response received",
            type = SnackbarType.ERROR
        )
        null
    }
}

actual fun createEasyPaisaPaymentElement(context: Any?): Any? {
    return try {
        val activityContext = context as? Activity ?: return null

        val easyPaisaElement = XPayEasyPaisaPaymentElement(
            context = activityContext,
            publicKey = RemoteConfigs.getCardPublicKey(),
            hmacSecretKey = CARD_HMAC_SECRET_KEY,
            accountId = CARD_ACCOUNT_ID
        )
        AndroidEasyPaisaPaymentElementWrapper(easyPaisaElement)
    } catch (e: Exception) {
        e.printStackTrace()
        GlobalToastManager.showToast(
            message = "Invalid response received",
            type = SnackbarType.ERROR
        )
        null
    }
}

@Composable
actual fun getPlatformContext(): Any? {
    val context = LocalContext.current
    val activity = context as? Activity
    return activity
}