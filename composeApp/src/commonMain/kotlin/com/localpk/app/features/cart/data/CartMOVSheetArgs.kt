package com.localpk.app.features.cart.data

import androidx.compose.runtime.NoLiveLiterals
import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
@NoLiveLiterals
data class CartMOVSheetArgs(
    val feeNeeded: Int,
    val nonGroceryCheck: Boolean? = false,
    val placeOrderCheck: Boolean
) : JavaSerializable