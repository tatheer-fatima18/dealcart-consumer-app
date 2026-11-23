package com.localpk.app.features.bottomsheet.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jetbrains.compose.resources.StringResource
import cafe.adriel.voyager.core.lifecycle.JavaSerializable

@Serializable
data class GeneralBottomSheetUiData(
    @Contextual val title: StringResource,
    @Contextual val description: StringResource,
    @Contextual val secondaryTitle: StringResource,
    @Contextual val secondaryDescription: StringResource,
    @Contextual val positiveButton: StringResource,
    @Contextual val negativeButton: StringResource,
) : JavaSerializable {
    @Transient
    var confirmListener: () -> Unit = { }

    @Transient
    var cancelListener: () -> Unit = { }

    @Transient
    var onDismiss: () -> Unit = { }
}