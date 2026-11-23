package com.localpk.app.features.orders.viewModel

import com.localpk.app.features.flash_deals.data.FlashDealUiData
import com.localpk.app.features.orders.data.DeliveryReceiptBody
import com.localpk.app.features.orders.data.OrderDetailModel
import com.localpk.app.features.orders.data.OrderItemDetailUiData
import com.localpk.app.features.orders.data.OrderTimeLineModel
import com.localpk.app.features.cart.data.PaymentDetails
import com.localpk.app.features.orders.data.RashanItemDetailUiData
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO_FLOAT

data class OrderDetailState(
    val confirmedOrderDetails: OrderDetailModel? = null,
    val salesOrderItems: List<OrderItemDetailUiData> = emptyList(),
    val rashanBundleItems: List<RashanItemDetailUiData> = emptyList(),
    val filteredSalesOrderItems: List<OrderItemDetailUiData> = emptyList(),
    val timeline: OrderTimeLineModel? = null,
    val deliveredReceiptData: DeliveryReceiptBody? = null,
    val isOrderDelivered: Boolean = false,
    val isOrderRated: Boolean = false,
    val isOrderCancelled: Boolean = false,
    val rating: Float? = ZERO_FLOAT,
    val flashOrders: List<FlashDealUiData> = emptyList(),
    val flashDealsItems: List<FlashDealUiData> = emptyList(),
    val platformFeeName: String = RemoteConfigs.getPlatformFeeName(),
    val packagingFeeName: String = RemoteConfigs.getPackagingFeeName(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val cancellationComment: String = EMPTY_STRING,
    val isReAttempt: Boolean = false,
    val deliveryDate: String = EMPTY_STRING,
    val paymentDetails: PaymentDetails = PaymentDetails(),
    val userPhoneNumber: String? = EMPTY_STRING
) 