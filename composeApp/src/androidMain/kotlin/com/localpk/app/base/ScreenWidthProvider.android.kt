package com.localpk.app.base

import android.content.res.Resources

actual fun getScreenHeightDp(): Float {
    val configuration = Resources.getSystem().configuration
    return configuration.screenHeightDp.toFloat()
}