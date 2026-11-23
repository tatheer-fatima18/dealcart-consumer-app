package com.localpk.app.base

import com.localpk.app.utils.EMPTY_STRING
import platform.Foundation.NSBundle

actual val LOCATIONIQ_API_KEY: String
    get() {
        return NSBundle.mainBundle.objectForInfoDictionaryKey("LOCATIONIQ_API_KEY") as? String
            ?: EMPTY_STRING
    }
