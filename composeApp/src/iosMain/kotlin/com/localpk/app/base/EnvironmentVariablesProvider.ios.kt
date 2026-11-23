package com.localpk.app.base

import com.localpk.app.utils.EMPTY_STRING
import platform.Foundation.NSBundle

actual val BUILD_TYPE: String
    get() {
        val buildType = NSBundle.mainBundle.objectForInfoDictionaryKey("KOTLIN_FRAMEWORK_BUILD_TYPE") as? String
        return buildType ?: "Debug"
    }

actual val BUILD_FLAVOR: String
    get() {
        val buildFlavor = NSBundle.mainBundle.objectForInfoDictionaryKey("BUILD_ENVIRONMENT") as? String
        return buildFlavor ?: EMPTY_STRING
    } 