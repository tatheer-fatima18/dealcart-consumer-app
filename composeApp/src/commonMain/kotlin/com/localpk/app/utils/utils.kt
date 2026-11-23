package com.localpk.app.utils


expect fun copyToClipboard(text: String)

fun copyTextToClipboardAndShowToast(textToCopy: String, message: String) {
    copyToClipboard(textToCopy)
}
