package com.localpk.app.firebase.pushNotification

import com.localpk.app.features.orders.data.OrderStatus
import com.localpk.app.utils.CANCELLED_ORDERS_REDIRECT
import com.localpk.app.utils.CONFIRMED_ORDERS_REDIRECT
import com.localpk.app.utils.DELIVERED_ORDERS_REDIRECT
import com.localpk.app.utils.EXPIRED_FLASH_DEAL
import com.localpk.app.utils.EXPIRING_FLASH_DEAL
import com.localpk.app.utils.ORDER_TRACKING
import com.localpk.app.utils.PENDING_ORDERS_REDIRECT
import com.localpk.app.utils.PRODUCT_REDIRECT
import com.localpk.app.utils.RETURNED_ORDERS_REDIRECT
import com.localpk.app.utils.WALLET
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.productlist.data.getProduct
import com.localpk.app.utils.PAYMENT_FAILURE
import com.localpk.app.utils.PAYMENT_SUCCESS
import com.localpk.app.utils.REFRESH_CART
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object NotificationHandler {

    private val _notificationEvents = MutableSharedFlow<NotificationEvent>()
    val notificationEvents = _notificationEvents.asSharedFlow()

    private val _notificationReceiveEvents = MutableSharedFlow<NotificationEvent>()
    val notificationReceiveEvents = _notificationReceiveEvents.asSharedFlow()

    suspend fun handleNotificationTap(
        data: Map<String, String>
    ) {
        val event = data.toNotificationEvent()
        _notificationEvents.emit(event)
    }

    suspend fun handleNotificationReceive(
        data: Map<String, String>
    ) {
        val event = data.toNotificationEvent()
        _notificationReceiveEvents.emit(event)
    }
}

sealed class NotificationEvent {
    data class NavigateToOrders(val tabIndex: Int) : NotificationEvent()
    data class NavigateToFlashDeals(
        val isExpired: Boolean,
        val isPending: Boolean,
        val isPN: Boolean
    ) : NotificationEvent()

    data class NavigateToOrderDetail(
        val id: Long,
        val status: String
    ) : NotificationEvent()

    data class NavigateToProductDetails(val product: ProductUiData) : NotificationEvent()
    data object NavigateToWallet : NotificationEvent()
    data class NavigateToOrderTracking(
        val orderId: String?,
        val restaurantId: String?
    ) : NotificationEvent()

    data object NavigateToHome : NotificationEvent()
    data class OpenUrl(val url: String?) : NotificationEvent()

    // Broadcast events (left empty for now)
    data object RefreshCart : NotificationEvent()
    data object PaymentSuccess : NotificationEvent()
    data object PaymentFailure : NotificationEvent()
}

// Utility functions for notification data handling
fun Map<String, String>.getOrderId(): String? = this["orderId"] ?: this["order_id"]
fun Map<String, String>.getRedirection(): String? = this["redirection"]
fun Map<String, String>.getTitle(): String =
    this["title"] ?: this["notification_title"] ?: "DealCart"

fun Map<String, String>.getContentId(): String? =
    this["id"] // Using 'id' field for product/restaurant ID

fun Map<String, String>.getSalesOrderId(): String? = this["salesOrderId"]
suspend fun Map<String, String>.handleNotificationTap() {
    NotificationHandler.handleNotificationTap(this)
}

suspend fun Map<String, String>.handleNotificationReceive() {
    NotificationHandler.handleNotificationReceive(this)
}

// Platform-specific function for extracting notification data from intent
expect fun extractNotificationData(intent: Any): Map<String, String>

private fun Map<String, String>.toNotificationEvent(): NotificationEvent {
    return when (getRedirection()) {
        PENDING_ORDERS_REDIRECT, CONFIRMED_ORDERS_REDIRECT -> {
            NotificationEvent.NavigateToOrders(0)
        }

        EXPIRED_FLASH_DEAL -> {
            NotificationEvent.NavigateToFlashDeals(
                isExpired = true,
                isPending = false,
                isPN = true
            )
        }

        EXPIRING_FLASH_DEAL -> {
            NotificationEvent.NavigateToFlashDeals(
                isExpired = false,
                isPending = true,
                isPN = true
            )
        }

        DELIVERED_ORDERS_REDIRECT -> {
            val salesOrderId = getOrderId()?.toLongOrNull()
            if (salesOrderId != null && salesOrderId != 0L) {
                NotificationEvent.NavigateToOrderDetail(
                    id = salesOrderId,
                    status = OrderStatus.DELIVERED.status
                )
            } else {
                NotificationEvent.NavigateToOrders(2)
            }
        }

        RETURNED_ORDERS_REDIRECT -> {
            NotificationEvent.NavigateToOrders(3)
        }

        CANCELLED_ORDERS_REDIRECT -> {
            NotificationEvent.NavigateToOrders(4)
        }

        PRODUCT_REDIRECT -> {
            val productId = getContentId()?.toLongOrNull() ?: 0L
            val product = getProduct(productId)
            NotificationEvent.NavigateToProductDetails(product)
        }

        WALLET -> {
            NotificationEvent.NavigateToWallet
        }

        ORDER_TRACKING -> {
            val orderId = getSalesOrderId()
            val restaurantId = getContentId()
            NotificationEvent.NavigateToOrderTracking(orderId, restaurantId)
        }

        REFRESH_CART -> {
            NotificationEvent.RefreshCart
        }

        PAYMENT_SUCCESS -> {
            NotificationEvent.PaymentSuccess
        }

        PAYMENT_FAILURE -> {
            NotificationEvent.PaymentFailure
        }

        else -> {
            NotificationEvent.NavigateToHome
        }
    }
}