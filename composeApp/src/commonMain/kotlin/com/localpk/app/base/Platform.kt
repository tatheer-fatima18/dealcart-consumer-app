package com.localpk.app.base

expect object Platform {
    fun isAndroid(): Boolean
    fun isIos(): Boolean
}