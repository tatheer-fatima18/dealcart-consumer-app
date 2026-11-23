package com.localpk.app.utils

object JsonUtils {
    
    /**
     * Converts a list of strings to a JSON array string
     */
    fun listToJsonArray(list: List<String>?): String {
        if (list.isNullOrEmpty()) return "[]"
        return "[${list.joinToString(",") { "\"$it\"" }}]"
    }

} 