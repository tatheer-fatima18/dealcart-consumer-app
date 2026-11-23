package com.localpk.app.features.orders.data


import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.utils.EMPTY_STRING
import kotlinx.serialization.Serializable


@Serializable
data class CancelOrderReasonsResponse(
    val success: Boolean,
    val body: List<String>
) : JavaSerializable

@Serializable
data class CancelOrderReasonRequest(
    val orderId: Long,
    val cancelOrderReasonRequestBody: CancelOrderReasonRequestBody
) : JavaSerializable


@Serializable
data class CancelOrderReasonRequestBody(
    val commentDescription: String? = EMPTY_STRING,
    val commentType: String,
    val cancelledByRef: String? = EMPTY_STRING
) : JavaSerializable

@Serializable
data class CancelOrderReason(
    val reason: String,
    var isSelected: Boolean? = false
) : JavaSerializable
