package com.localpk.app.utils

import org.jetbrains.compose.resources.StringResource

interface StringsResourceManager {
    fun getString(resource: StringResource): String
    fun getString(resource: StringResource, vararg formatArgs: Any): String
    fun getStringArray(resource: StringResource): List<String>
}

expect class StringsResourceManagerImpl : StringsResourceManager {
    override fun getString(resource: StringResource): String
    override fun getString(
        resource: StringResource,
        vararg formatArgs: Any
    ): String

    override fun getStringArray(resource: StringResource): List<String>
}