package com.localpk.app.base

import com.localpk.app.BuildConfig
import com.localpk.app.utils.BUILD_TYPE_DEBUG
import com.localpk.app.utils.BUILD_TYPE_RELEASE

actual val BUILD_TYPE: String
    get() = if (BuildConfig.DEBUG) BUILD_TYPE_DEBUG else BUILD_TYPE_RELEASE

actual val BUILD_FLAVOR: String
    get() = BuildConfig.FLAVOR 