package com.localpk.app.features.cart.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class CheckoutResponse(val success: Boolean, val body: CheckoutData) : JavaSerializable

@Serializable
data class CheckoutData(
    val id: Long? = null,
    val isOrderAlreadyPlaced: Boolean? = false,
    val salesOrderItems: List<PlaceOrderSalesOrderItems>? = emptyList(),
    val cardPaymentDetails: CardPaymentDetails? = null,
    val oosItems: List<ProductOOSDetails>? = null
) : JavaSerializable

@Serializable
data class CardPaymentDetails(
    val encryptionKey: String?,
    val clientSecret: String?
) : JavaSerializable

@Serializable
data class PlaceOrderSalesOrderItems(
    val itemStatus: String?
) : JavaSerializable

@Serializable
data class OrderStatusResponse(val body: OrderStatusBody, val success: Boolean) : JavaSerializable

@Serializable
data class OrderStatusBody(val orderStatus: String?, val message: String?) : JavaSerializable