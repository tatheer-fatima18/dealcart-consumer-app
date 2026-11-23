package com.localpk.app.base

import com.localpk.app.utils.EMPTY_STRING
import platform.Foundation.NSBundle

actual val BASE_URL: String
    get() {
        val url = NSBundle.mainBundle.objectForInfoDictionaryKey("BASE_URL") as? String
        return url ?: EMPTY_STRING
    }

actual val BASE_FOOD_URL: String
    get() {
        val url = NSBundle.mainBundle.objectForInfoDictionaryKey("BASE_URL_FOOD") as? String
        return url ?: EMPTY_STRING
    }

actual val BASE_DOLAT_URL: String
    get() {
        val url = NSBundle.mainBundle.objectForInfoDictionaryKey("BASE_URL_DOLAT") as? String
        return url ?: EMPTY_STRING
    }

actual val BASE_LINKS_URL: String
    get() {
        val url = NSBundle.mainBundle.objectForInfoDictionaryKey("BASE_URL_LINKS") as? String
        return url ?: EMPTY_STRING
    }

actual val BASE_URL_AGENT: String
    get() {
        val url = NSBundle.mainBundle.objectForInfoDictionaryKey("BASE_URL_AGENT") as? String
        return url ?: EMPTY_STRING
    }

actual val BASE_URL_SPORTS: String
    get() {
        val url = NSBundle.mainBundle.objectForInfoDictionaryKey("BASE_URL_SPORTS") as? String
        return url ?: EMPTY_STRING
    }