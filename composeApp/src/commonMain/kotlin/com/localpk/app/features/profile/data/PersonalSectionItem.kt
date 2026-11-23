package com.localpk.app.features.profile.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource

@Serializable
data class PersonalSectionItem(
    @Contextual
    val iconSrc: IconSource,
    val label: String,
    var notificationIcon: Boolean = false,
    val isAnimation: Boolean = false,
)

sealed class IconSource {
    @Serializable
    data class Drawable(@Contextual val res: DrawableResource) : IconSource()

    @Serializable
    data class Animation(@Contextual val json: ByteArray) : IconSource()
}

