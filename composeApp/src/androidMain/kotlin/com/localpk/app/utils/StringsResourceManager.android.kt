package com.localpk.app.utils

import android.content.Context
import org.jetbrains.compose.resources.StringResource

actual class StringsResourceManagerImpl(private val context: Context) : StringsResourceManager {

    actual override fun getString(resource: StringResource): String {
        return context.getString(getResId(resource))
    }

    actual override fun getString(resource: StringResource, vararg formatArgs: Any): String {
        return context.getString(getResId(resource), *formatArgs)
    }

    actual override fun getStringArray(resource: StringResource): List<String> {
        // For now, returning a single string as list
        // You might want to implement proper array resource handling
        return listOf(getString(resource))
    }

    private fun getResId(resource: StringResource): Int {
        // Extract the resource name from the StringResource using reflection
        val resourceName = try {
            val field = resource::class.java.getDeclaredField("name")
            field.isAccessible = true
            field.get(resource) as String
        } catch (e: Exception) {
            // Fallback to toString parsing
            resource.toString().substringAfterLast(".").substringBefore(")")
        }
        return context.resources.getIdentifier(resourceName, "string", context.packageName)
    }
} 