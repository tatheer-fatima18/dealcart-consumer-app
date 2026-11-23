package com.localpk.app.features.restaurant.presentation

import platform.Foundation.*
import platform.UIKit.*
import platform.darwin.*

actual fun openDialPad(phoneNumber: String) {
    val cleanedPhone = phoneNumber.filter { it.isDigit() }
    val url = NSURL.URLWithString("tel:$cleanedPhone") ?: return

    if (UIApplication.sharedApplication.canOpenURL(url)) {
        // Required to call from the main thread
        dispatch_async(dispatch_get_main_queue()) {
            UIApplication.sharedApplication.openURL(
                url,
                options = emptyMap<Any?, Any?>().toNSDictionary(),
                completionHandler = null
            )
        }
    }
}

fun Map<Any?, Any?>.toNSDictionary(): Map<Any?, *> =
    NSDictionary.dictionaryWithDictionary(this)