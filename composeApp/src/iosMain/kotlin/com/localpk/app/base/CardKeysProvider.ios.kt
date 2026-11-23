package com.localpk.app.base

import com.localpk.app.utils.EMPTY_STRING
import platform.Foundation.NSBundle
actual val CARD_ACCOUNT_ID: String
    get() {
        val url = NSBundle.mainBundle.objectForInfoDictionaryKey("CARD_ACCOUNT_ID") as? String
        return url ?: EMPTY_STRING
    }
actual val CARD_HMAC_SECRET_KEY: String
    get() {
        val url = NSBundle.mainBundle.objectForInfoDictionaryKey("CARD_HMAC_SECRET_KEY") as? String
        return url ?: EMPTY_STRING
    }
actual val CARD_PUBLIC_KEY: String
    get() {
        val url = NSBundle.mainBundle.objectForInfoDictionaryKey("CARD_PUBLIC_KEY") as? String
        return url ?: EMPTY_STRING
    }