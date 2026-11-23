package com.localpk.app.base

actual object Platform {
    actual fun isAndroid() = true

    actual fun isIos() = false
}