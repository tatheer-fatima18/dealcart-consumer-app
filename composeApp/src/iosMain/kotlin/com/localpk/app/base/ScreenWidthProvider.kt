package com.localpk.app.base

import platform.UIKit.UIScreen
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents

@OptIn(ExperimentalForeignApi::class)
actual fun getScreenWidthDp(): Int {
    val screen = UIScreen.mainScreen
    val widthPoints = screen.bounds.useContents { size.width }
    return widthPoints.toInt()
}