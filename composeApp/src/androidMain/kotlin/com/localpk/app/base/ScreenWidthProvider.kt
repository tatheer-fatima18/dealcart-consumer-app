package com.localpk.app.base

import android.content.res.Resources

actual fun getScreenWidthDp(): Int {
    val metrics = Resources.getSystem().displayMetrics
    return (metrics.widthPixels / metrics.density).toInt()
}