package com.localpk.app.utils



fun String?.default(other: String? = null) = this ?: other ?: EMPTY_STRING
fun Boolean?.default(other: Boolean? = null) = this ?: other ?: false

fun String.isNullToEmpty(): String {
    return if (equals("null")) {
        EMPTY_STRING
    } else {
        this
    }
}

fun String.capitalizeFirstLetter(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}
