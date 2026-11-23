package com.localpk.app.features.orders.viewModel

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.orders.data.DeliveryReceiptBody
import com.localpk.app.features.restaurant.data.FoodOrderDetailUIData
import com.localpk.app.features.restaurant.domain.FoodOrderDetailsUseCase
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent

// State for FoodOrderDetailViewModel

data class FoodOrderDetailState(
    val orderDetails: FoodOrderDetailUIData? = null,
    val deliveredReceiptData: DeliveryReceiptBody? = null,
    val isInvoiceVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val userPhoneNumber: String? = null
)

class FoodOrderDetailViewModel(
    private val orderId: Long,
    private val orderStatus: String,
    private val foodOrderDetailsUseCase: FoodOrderDetailsUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel<Any>(), KoinComponent {

    private val _state = MutableStateFlow(FoodOrderDetailState())
    val state: StateFlow<FoodOrderDetailState> = _state

    init {
        fetchOrderDetail(orderId)
        _state.update { it.copy(userPhoneNumber = sharedPreferencesManager.getPhoneNumber()) }
    }

    private fun fetchOrderDetail(orderId: Long) {
        launch {
            _state.update { it.copy(isLoading = true) }
            unZipViewStateFlow(
                foodOrderDetailsUseCase(orderId),
                onSuccess = { result ->
                    val details = result as? FoodOrderDetailUIData
                    if (details != null) {
                        processOrderDetails(details)
                    }
                    _state.update { it.copy(isLoading = false) }
                },
                onError = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.toString()
                        )
                    }
                }
            )
        }
    }

    private fun processOrderDetails(details: FoodOrderDetailUIData) {
        val deliveryReceipt = DeliveryReceiptBody(
            dcCoins = details.dcCoinDiscount,
            deliveryFee = details.deliveryFee.toLong(),
            packagingFee = ZERO_INT,
            platformFee = details.platformFee,
            optionalContributionFee = ZERO_INT,
            promoCodeDiscount = details.promoCodeDiscount,
            promoCodeUsed = details.promoCodeUsed,
            shippingAddressText = details.address?.completeAddress().default(),
            subTotal = details.grandTotal().toLong(),
            total = details.totalAmount,
            totalSavings = details.salesOrderItems?.sumOf { it.discount.getValueOrDefault() * it.quantity!! }
                ?: 0.0,
            orderDate = details.createdAt,
            salesOrderItems = details.salesOrderItems?.map { it -> it.map() }
        )
        _state.update {
            it.copy(
                orderDetails = details,
                isInvoiceVisible = details.isNotNull(),
                deliveredReceiptData = deliveryReceipt
            )
        }
    }

    fun onDownloadReceiptClicked() {
        val deliveryReceipt = _state.value.deliveredReceiptData?.mapToDeliveryReceipt(
            customerName = sharedPreferencesManager.getUserName(),
            customerNumber = sharedPreferencesManager.getPhoneNumber(),
            id = orderId,
            orderStatus = orderStatus
        )
        // TODO: Implement navigation to web view with receipt if needed
    }

    fun invoiceVisible(): Boolean {
        val orderStatus = _state.value.orderDetails?.orderStatus
        return orderStatus != null && orderStatus != "cancelled" && orderStatus != "returned"
    }

    fun refreshOrderDetails() {
        fetchOrderDetail(orderId)
    }
} 