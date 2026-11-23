package com.localpk.app.utils

import org.jetbrains.compose.resources.StringResource
import platform.Foundation.NSBundle

actual class StringsResourceManagerImpl : StringsResourceManager {

    actual override fun getString(resource: StringResource): String {
        val resourceName = extractResourceName(resource)
        return NSBundle.mainBundle.localizedStringForKey(resourceName, value = null, table = null)
    }

    actual override fun getString(resource: StringResource, vararg formatArgs: Any): String {
        val baseString = getString(resource)
        return if (formatArgs.isNotEmpty()) {
            // For iOS, we'll use a simple string replacement approach
            // since NSString.stringWithFormat has C interop issues
            var result = baseString
            formatArgs.forEachIndexed { index, arg ->
                result = result.replace("%${index + 1}\$s", arg.toString())
            }
            result
        } else {
            baseString
        }
    }

    actual override fun getStringArray(resource: StringResource): List<String> {
        // For now, returning a single string as list
        // You might want to implement proper array resource handling
        return listOf(getString(resource))
    }

    private fun extractResourceName(resource: StringResource): String {
        // Extract the resource name from the StringResource using toString parsing
        // since reflection is not available in Kotlin Native
        return resource.toString().substringAfterLast(".").substringBefore(")")
    }
} 