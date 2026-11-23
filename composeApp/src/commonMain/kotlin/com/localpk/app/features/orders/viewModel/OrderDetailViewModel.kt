package com.localpk.app.features.orders.viewModel

import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.cart.data.CartOrderType
import com.localpk.app.features.flash_deals.data.FlashDealUiData
import com.localpk.app.features.order_review.data.AddOrderReviewRequest
import com.localpk.app.features.order_review.data.OrderReviewActionContinueArgs
import com.localpk.app.features.order_review.data.OrderReviewBottomSheetUIData
import com.localpk.app.features.order_review.data.OrderReviewCategoryArgs
import com.localpk.app.features.order_review.data.OrderReviewData
import com.localpk.app.features.order_review.data.OrderReviewSuccessArgs
import com.localpk.app.features.order_review.domain.AddOrderReviewUseCase
import com.localpk.app.features.order_review.presentation.OrderReviewBottomSheetScreen
import com.localpk.app.features.order_review.presentation.OrderReviewCategoriesListingScreen
import com.localpk.app.features.order_review.presentation.OrderReviewSuccessBottomSheet
import com.localpk.app.features.orders.data.DeliveryReceiptBody
import com.localpk.app.features.orders.data.OrderDetailModel
import com.localpk.app.features.orders.data.OrderItemDetailUiData
import com.localpk.app.features.orders.data.OrderItemStatus
import com.localpk.app.features.orders.data.OrderStatus
import com.localpk.app.features.orders.data.OrderSupportArgs
import com.localpk.app.features.orders.data.OrderUiData
import com.localpk.app.features.orders.domain.DeliveredReceiptUseCase
import com.localpk.app.features.orders.domain.OrderDetailUseCase
import com.localpk.app.utils.BIT_DELAY
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.ZERO_FLOAT
import com.localpk.app.utils.default
import com.localpk.app.utils.formatCartDeliveryDate
import com.localpk.app.utils.formatDateWithOne
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.isNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class OrderDetailViewModel(
    private val orderId: Long,
    private val orderDetailUseCase: OrderDetailUseCase,
    private val deliveredReceiptUseCase: DeliveredReceiptUseCase,
    private val addOrderReviewUseCase: AddOrderReviewUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val navigator: Navigator? = null,
    private val bottomSheetNavigator: BottomSheetNavigator? = null,
) : BaseViewModel<Any>() {

    private val _state = MutableStateFlow(OrderDetailState())
    val state: StateFlow<OrderDetailState> = _state

    val _argsData = MutableStateFlow<OrderUiData?>(null)
    val argsData: StateFlow<OrderUiData?> = _argsData

    init {
        fetchOrderDetails()
        if (invoiceVisible()) {
            getDeliveredInvoice()
        }
        _state.update { it.copy(userPhoneNumber = sharedPreferencesManager.getPhoneNumber()) }
    }

    private fun fetchOrderDetails() {
        fetchOrderDetail(orderId)
    }

    private fun getDeliveredInvoice() {
        launch {
            unZipViewStateFlow(
                deliveredReceiptUseCase(orderId),
                onSuccess = { result ->
                    val receiptData = result as? DeliveryReceiptBody
                    _state.update { it.copy(deliveredReceiptData = receiptData) }
                },
                onError = {
                    // Handle error silently for invoice
                }
            )
        }
    }

    fun fetchOrderDetail(orderId: Long) {
        launch {
            _state.update { it.copy(isLoading = true) }
            unZipViewStateFlow(
                orderDetailUseCase(orderId),
                onSuccess = { result ->
                    val confirmedOrderDetails = result as? OrderDetailModel
                    if (confirmedOrderDetails != null) {
                        processOrderDetails(confirmedOrderDetails)
                    }
                    _state.update { it.copy(isLoading = false) }
                },
                onError = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
            )
        }
    }

    private fun processOrderDetails(confirmedOrderDetails: OrderDetailModel) {
        val list = mutableListOf<OrderItemDetailUiData>()

        argsData.value?.let { args ->
            _state.update {
                it.copy(
                    confirmedOrderDetails = confirmedOrderDetails,
                    isOrderCancelled = confirmedOrderDetails.orderStatus == OrderStatus.CANCELLED.status,
                    timeline = confirmedOrderDetails.timeLine(),
                    rashanBundleItems = confirmedOrderDetails.consumerBundle?.salesOrderItems?.map { rashanItem ->
                        rashanItem.bundleMap()
                    } ?: emptyList(),
                    cancellationComment = confirmedOrderDetails.getCancellationComment(),
                    isReAttempt = false, // TODO: Get from args
                    deliveryDate = if (confirmedOrderDetails.deliverBy == CartOrderType.POSTEX.key) {
                        "${confirmedOrderDetails.deliveryStartRange?.formatCartDeliveryDate()} - ${confirmedOrderDetails.deliveryEndRange?.formatCartDeliveryDate()}"
                    } else {
                        confirmedOrderDetails.createdAt?.formatDateWithOne() ?: ""
                    },
                    paymentDetails = args.paymentDetails
                )
            }
        }

        val salesOrderItems =
            confirmedOrderDetails.salesOrderItems?.map { slo -> slo.map() } ?: emptyList()
        val filteredItems = salesOrderItems.filter { single -> !single.isDeal && !single.isGroup }

        _state.update {
            it.copy(
                salesOrderItems = filteredItems
            )
        }

        // Get first 2 items for filtered list
        for ((ind, elem) in filteredItems.withIndex()) {
            if (ind < 2) {
                list.add(elem)
            } else {
                break
            }
        }

        state.value.confirmedOrderDetails?.apply {
            if (navigator != null && bottomSheetNavigator != null) {
                startOrderReview(
                    orderForReviewData = OrderReviewData(
                        shipmentId = state.value.confirmedOrderDetails?.salesOrderItems?.firstOrNull()?.shipmentItem?.shipment?.id.getValueOrDefault(),
                        salesOrderId = id.getValueOrDefault(),
                        orderTotal = orderTotal.toLong().getValueOrDefault(),
                        createdAt = createdAt.default(),
                        deliveredAt = deliveredAt.default(),
                        salesOrderRating = salesOrderRating,
                        orderStatus = orderStatus.default()
                    ),
                    bottomSheetNavigator = bottomSheetNavigator,
                )
            }
        }


        _state.update {
            it.copy(
                filteredSalesOrderItems = list
            )
        }

        // Process flash orders
        val flashOrders = salesOrderItems
            .filter { deal ->
                deal.isDeal && (deal.itemStatus == OrderItemStatus.PENDING.itemStatus ||
                        deal.itemStatus == OrderItemStatus.CANCELLED.itemStatus ||
                        deal.itemStatus == OrderItemStatus.CONFIRMED.itemStatus ||
                        deal.itemStatus == OrderItemStatus.DELIVERED.itemStatus ||
                        deal.itemStatus == OrderItemStatus.OUT_OF_STOCK.itemStatus ||
                        deal.itemStatus == OrderItemStatus.RETURNED.itemStatus)
            }
            .map { dealMapData ->
                FlashDealUiData(
                    productName = dealMapData.productName,
                    productImage = dealMapData.productImage,
                    productId = dealMapData.id,
                    originalPrice = dealMapData.totalUnitsPrice,
                    dealPrice = dealMapData.totalAmount,
                    timeLeft = dealMapData.deal?.timeLeft.getValueOrDefault(),
                    totalPersonsJoined = dealMapData.deal?.totalPersonsJoined.getValueOrDefault(),
                    totalPersonsRequired = dealMapData.deal?.totalPersonsRequired.getValueOrDefault(),
                    dealStatus = if (dealMapData.deal?.timeLeft.getValueOrDefault() > 0) "active" else "expired",
                    id = dealMapData.deal?.id.getValueOrDefault(),
                    quantityBought = dealMapData.quantity,
                    sloID = dealMapData.id,
                    discountPercentage = ((dealMapData.totalUnitsPrice - dealMapData.totalAmount) / dealMapData.totalUnitsPrice) * 100,
                    expiryEndTime = dealMapData.deal?.expiryEndTime.getValueOrDefault(),
                    categoryName = EMPTY_STRING,
                    subCategoryName = EMPTY_STRING,
                    isAvailed = false,
                    isExpired = dealMapData.remainingTime <= 0,
                    status = if (dealMapData.remainingTime > 0) "active" else "expired",
                    paymentStatus = dealMapData.paymentStatus,
                    itemQuantity = 0
                )
            }

        _state.update {
            it.copy(
                flashDealsItems = flashOrders
            )
        }

    }


    fun startOrderReview(
        orderForReviewData: OrderReviewData,
        bottomSheetNavigator: BottomSheetNavigator,
    ) {
        orderForReviewData.apply {
            val isDelivered =
                (orderStatus == OrderStatus.DELIVERED.status || orderStatus == OrderStatus.PARTIAL_RETURNED.status)

            _state.update {
                it.copy(
                    isOrderDelivered = isDelivered
                )
            }

            if (salesOrderRating.isNull() && isDelivered) {
                bottomSheetNavigator.show(
                    OrderReviewBottomSheetScreen(
                        salesOrderId = salesOrderId,
                        deliveryDate = deliveredAt,
                        orderTotal = orderTotal,
                        shipmentId = shipmentId
                    )
                )
            } else {
                _state.update {
                    it.copy(
                        isOrderRated = true,
                        rating = salesOrderRating?.rating?.toFloat() ?: ZERO_FLOAT
                    )
                }
            }
        }
    }

    fun onDownloadReceiptClicked() {
        val deliveryReceipt = state.value.deliveredReceiptData?.mapToDeliveryReceipt(
            customerName = "", // TODO: Get from SharedPreferencesManager
            customerNumber = "", // TODO: Get from SharedPreferencesManager
            orderId,
            state.value.confirmedOrderDetails?.orderStatus ?: ""
        )

        // TODO: Navigate to web view with receipt
        // val args = WebViewFragmentArgs(
        //     title = "Delivery Receipt",
        //     htmlPath = "file:///android_asset/delivery_receipt.html",
        //     htmlContent = Gson().toJson(deliveryReceipt),
        //     webViewType = WebViewType.DELIVERY_RECIEPT
        // )
        // navigate(navigationProvider.getPushNavigator(...))
    }

    fun navigateToOrderCancellationBottomSheet() {
        val currentOrderDetails = state.value.confirmedOrderDetails
        if (currentOrderDetails.isNotNull() && currentOrderDetails?.getIsOrderCancellable() == true) {
            val orderSupportArgs = OrderSupportArgs(
                orderId = currentOrderDetails.id.getValueOrDefault(),
                orderStatus = currentOrderDetails.orderStatus,
                deliveryDate = if (currentOrderDetails.deliverBy == CartOrderType.POSTEX.key) {
                    "${currentOrderDetails.deliveryStartRange?.formatCartDeliveryDate()} - ${currentOrderDetails.deliveryEndRange?.formatCartDeliveryDate()}"
                } else {
                    currentOrderDetails.createdAt?.formatDateWithOne() ?: ""
                },
                shippingAddress = currentOrderDetails.shippingAddressText,
                isOrderCancellable = currentOrderDetails.getIsOrderCancellable(),
                orderCreatedAt = currentOrderDetails.createdAt ?: ""
            )
            // TODO: Navigate to cancel order bottom sheet
            // navigate(navigationProvider.getPushNavigator(...))
        } else {
            // TODO: Show success message
            // toastManager.showSuccess("You can not cancel order now")
        }
    }



    fun invoiceVisible(): Boolean {
        val orderStatus = state.value.confirmedOrderDetails?.orderStatus
        return orderStatus !in listOf(OrderStatus.CANCELLED.status, OrderStatus.RETURNED.status)
    }



} 