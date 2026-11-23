package com.localpk.app.base

actual object Platform {
    actual fun isAndroid() = false

    actual fun isIos() = true
}