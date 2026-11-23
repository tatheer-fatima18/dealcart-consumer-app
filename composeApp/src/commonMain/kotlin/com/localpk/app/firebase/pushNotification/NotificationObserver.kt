package com.localpk.app.firebase.pushNotification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.localpk.app.features.productlist.data.ProductUiData
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NotificationObserver(
    onNavigateToProductDetails: (ProductUiData) -> Unit = {},
    onNavigateToFlashDeals: (Boolean, Boolean, Boolean) -> Unit = { isExpired: Boolean, isPending: Boolean, isPN: Boolean -> },
    onNavigateToOrders: (Int) -> Unit = {},
    onNavigateToOrderDetail: (Long, String) -> Unit = { l: Long, s: String -> },
    onOpenUrl: (String?) -> Unit = {},
    onNavigateToWallet: () -> Unit = {},
    onNavigateToOrderTracking: (String?, String?) -> Unit = { orderId: String?, restaurantId: String? -> },
    onNavigateToHome: () -> Unit = {},
    onRefreshCart: () -> Unit = {},
    onPaymentSuccess: () -> Unit = {},
    onPaymentFailure: () -> Unit = {},
    onNotificationReceived: (NotificationEvent) -> Unit = {}
) {
    var currentEvent by remember { mutableStateOf<NotificationEvent?>(null) }

    LaunchedEffect(Unit) {
        NotificationHandler.notificationEvents.collectLatest { event ->
            currentEvent = event
        }
    }

    LaunchedEffect(Unit) {
        NotificationHandler.notificationReceiveEvents.collectLatest { event ->
            onNotificationReceived(event)
        }
    }

    LaunchedEffect(currentEvent) {
        currentEvent?.let { event ->
            when (event) {
                is NotificationEvent.NavigateToOrders -> {
                    onNavigateToOrders(event.tabIndex)
                }

                is NotificationEvent.NavigateToFlashDeals -> {
                    onNavigateToFlashDeals(event.isExpired, event.isPending, event.isPN)
                }

                is NotificationEvent.NavigateToOrderDetail -> {
                    onNavigateToOrderDetail(event.id, event.status)
                }

                is NotificationEvent.NavigateToProductDetails -> {
                    onNavigateToProductDetails(event.product)
                }

                is NotificationEvent.NavigateToWallet -> {
                    onNavigateToWallet()
                }

                is NotificationEvent.NavigateToOrderTracking -> {
                    onNavigateToOrderTracking(event.orderId, event.restaurantId)
                }

                is NotificationEvent.NavigateToHome -> {
                    onNavigateToHome()
                }

                is NotificationEvent.OpenUrl -> {
                    onOpenUrl(event.url)
                }

                is NotificationEvent.RefreshCart -> {
                    onRefreshCart()
                }

                is NotificationEvent.PaymentSuccess -> {
                    onPaymentSuccess()
                }

                is NotificationEvent.PaymentFailure -> {
                    onPaymentFailure()
                }
            }
            currentEvent = null
        }
    }
}