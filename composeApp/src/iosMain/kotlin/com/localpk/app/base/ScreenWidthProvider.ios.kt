package com.localpk.app.base

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIScreen

@OptIn(ExperimentalForeignApi::class)
actual fun getScreenHeightDp(): Float {
    val screenHeight = UIScreen.mainScreen.bounds.useContents { size.height }
    return screenHeight.toFloat()
}
