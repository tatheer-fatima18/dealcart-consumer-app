package com.localpk.app.features.share_and_earn.data

import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource


data class ShareAndEarnUiData(
    val shareSteps: List<ShareStepsAndCoins>,
    val icons: List<ShareIcon>
)


data class ShareIcon(
    val icon: DrawableResource,
    val onClick: () -> Unit

)

@Serializable
data class ShareStepsAndCoins(
    val step: String,
    val coins: String,
)