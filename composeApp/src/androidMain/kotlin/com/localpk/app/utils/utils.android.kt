package com.localpk.app.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import com.localpk.app.base.PlatformContext


actual fun copyToClipboard(text: String) {
    val context = PlatformContext.appContext ?: return

    try {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("copied_text", text)
        clipboard.setPrimaryClip(clip)
    } catch (e: Exception) {
    }
} 