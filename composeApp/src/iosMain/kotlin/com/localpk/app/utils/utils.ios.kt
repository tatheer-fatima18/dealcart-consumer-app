package com.localpk.app.utils


actual fun copyToClipboard(text: String) {

    if (text.isBlank()) {
        return
    }

    try {
        // Use UIPasteboard to copy text to clipboard
        val pasteboard = platform.UIKit.UIPasteboard.generalPasteboard
        pasteboard.string = text

    } catch (e: Exception) {
    }
} 