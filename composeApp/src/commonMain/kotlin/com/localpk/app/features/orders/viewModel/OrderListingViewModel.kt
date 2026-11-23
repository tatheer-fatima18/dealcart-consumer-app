package com.localpk.app.features.orders.viewModel

import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.order_review.data.OrderReviewData
import com.localpk.app.features.order_review.domain.AddOrderReviewUseCase
import com.localpk.app.features.order_review.presentation.OrderReviewBottomSheetScreen
import com.localpk.app.features.orders.data.OrderItemStatus
import com.localpk.app.features.orders.data.OrderStatus
import com.localpk.app.features.orders.data.OrderUiData
import com.localpk.app.features.orders.data.PastOrderUiData
import com.localpk.app.features.orders.data.PastOrdersRequest
import com.localpk.app.features.orders.data.StatusModel
import com.localpk.app.features.orders.domain.CurrentOrdersUseCase
import com.localpk.app.features.orders.domain.PastOrdersUseCase
import com.localpk.app.features.restaurant.data.FoodOrderUIData
import com.localpk.app.features.restaurant.data.OrderTrackingStatuses
import com.localpk.app.features.restaurant.domain.FoodOrdersUseCase
import com.localpk.app.features.restaurant.domain.RequestData
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.FIRST_PAGE
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.VIEW_ALL_PRODUCT_LIMIT_MIN
import com.localpk.app.utils.isNull
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent

class OrderListingViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val currentOrdersUseCase: CurrentOrdersUseCase,
    private val pastOrdersUseCase: PastOrdersUseCase,
    private val addOrderReviewUseCase: AddOrderReviewUseCase,
    private val foodOrdersUseCase: FoodOrdersUseCase,
    private val eventLogger: EventLogger
) : BaseViewModel<Any>(), KoinComponent {

    private val _state = MutableStateFlow(OrderListingState())
    val state: StateFlow<OrderListingState> = _state

    var currentPage = FIRST_PAGE
    val pageSize = VIEW_ALL_PRODUCT_LIMIT_MIN

    val _orderUiData = MutableStateFlow<OrderUiData?>(null)
    val orderUiData: StateFlow<OrderUiData?> = _orderUiData

    // Job to track and cancel ongoing API requests
    private var currentOrdersJob: Job? = null
    private var pastOrdersJob: Job? = null
    private var foodOrdersJob: Job? = null

    // Job to handle debouncing of rapid tab switches
    private var debounceJob: Job? = null

    // Request ID to track which response belongs to which request
    private var currentRequestId = 0

    // Store the current tab status to verify response belongs to correct tab
    private var currentTabStatus: String? = null

    init {
        _state.update { it.copy(isUserLoggedIn = sharedPreferencesManager.isLoggedIn()) }
        if (!sharedPreferencesManager.isLoggedIn()) {
            eventLogger.MyOrdersPage()
        }
        // Initialize statusData
        _state.update { it.copy(statusData = buildStatusData()) }
    }

    private fun buildStatusData(): List<StatusModel> = listOf(
        StatusModel(
            2,
            OrderItemStatus.ALL.itemStatus,
            "All",
            "Ye Saray orders hain."
        ),
        StatusModel(
            1,
            OrderItemStatus.UPCOMING.itemStatus,
            "Upcoming",
            "Ye Saray orders hain."
        ),
        StatusModel(
            3,
            OrderItemStatus.DELIVERED.itemStatus,
            "Delivered",
            "Yeh orders aap ko deliver ho chukay hain."
        ),
        StatusModel(
            5,
            OrderItemStatus.CANCELLED.itemStatus,
            "Cancelled",
            "Ye woh orders hain jo members pooray na honay pe ya deegar masail ki wajah se cancel ho gayen hain."
        ),
        StatusModel(
            4,
            OrderItemStatus.RETURNED.itemStatus,
            "Returned",
            "Yeh orders wapis kar diye gaye hain."
        )
    )

    fun fetchCurrentOrders(callback: () -> Unit = {}) {
        // Cancel any ongoing current orders request
        _state.update {
            it.copy(
                currentOrders = emptyList(),
                currentOrdersFetched = false
            )
        }
        currentOrdersJob?.cancel()

        // Set loading state
        _state.update { it.copy(isLoading = true) }

        // Increment request ID for this new request
        val requestId = ++currentRequestId
        currentTabStatus = OrderItemStatus.UPCOMING.itemStatus

        currentOrdersJob = viewModelScope.launch {
            try {
                unZipViewStateFlow(
                    currentOrdersUseCase(Unit),
                    onSuccess = { result ->
                        // Only process response if this is still the current request
                        if (requestId == currentRequestId && currentTabStatus == OrderItemStatus.UPCOMING.itemStatus) {
                            val orders = result as? List<OrderUiData> ?: emptyList()
                            _state.update {
                                it.copy(
                                    currentOrders = orders,
                                    isLoading = false,
                                    currentOrdersFetched = true
                                )
                            }
                            callback()
                        }
                    },
                    onError = {
                        if (requestId == currentRequestId) {
                            _state.update { it.copy(isLoading = false) }
                            callback()
                        }
                    }
                )
            } catch (e: Exception) {
                // Handle cancellation or other errors
                if (requestId == currentRequestId) {
                    _state.update { it.copy(isLoading = false) }
                    callback()
                }
            }
        }
    }

    fun fetchPastOrders(
        title: String = state.value.statusData.firstOrNull()?.title ?: "",
        month: Int = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).monthNumber,
        callback: () -> Unit = {}
    ) {
        // Cancel any ongoing past orders request
        pastOrdersJob?.cancel()

        _state.update {
            it.copy(
                pastOrders = emptyList(),
                pastOrdersFetched = false
            )
        }

        // Set loading state
        _state.update { it.copy(isLoading = true) }

        // Increment request ID for this new request
        val requestId = ++currentRequestId
        currentTabStatus = title.lowercase()

        pastOrdersJob = viewModelScope.launch {
            try {
                unZipViewStateFlow(
                    pastOrdersUseCase(PastOrdersRequest(title.lowercase(), month)),
                    onSuccess = { result ->
                        // Only process response if this is still the current request
                        if (requestId == currentRequestId && currentTabStatus == title.lowercase()) {
                            val orders = result as? List<PastOrderUiData> ?: emptyList()
                            _state.update {
                                it.copy(
                                    pastOrders = orders,
                                    isLoading = false,
                                    pastOrdersFetched = true
                                )
                            }
                            callback()
                        }
                    },
                    onError = {
                        if (requestId == currentRequestId) {
                            _state.update { it.copy(isLoading = false) }
                            callback()
                        }
                    }
                )
            } catch (e: Exception) {
                // Handle cancellation or other errors
                if (requestId == currentRequestId) {
                    _state.update { it.copy(isLoading = false) }
                    callback()
                }
            }
        }
    }

    fun updateIsRefreshed(value: Boolean) {
        _state.update { it.copy(isRefreshed = value) }
    }

    fun fetchAllOrders(
        title: String = state.value.statusData.firstOrNull()?.title ?: "",
        month: Int = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).monthNumber,
        callback: () -> Unit = {}
    ) {
        // Cancel any ongoing requests
        currentOrdersJob?.cancel()
        pastOrdersJob?.cancel()

        // Clear all lists at the start
        _state.update {
            it.copy(
                currentOrders = emptyList(),
                pastOrders = emptyList(),
                currentOrdersFetched = false,
                pastOrdersFetched = false
            )
        }

        // Set loading state
        _state.update { it.copy(isLoading = true) }

        // Increment request ID for this new request
        val requestId = ++currentRequestId
        currentTabStatus = OrderItemStatus.ALL.itemStatus

        var currentOrdersCompleted = false
        var pastOrdersCompleted = false

        fun checkCompletion() {
            if (currentOrdersCompleted && pastOrdersCompleted) {
                _state.update { it.copy(isLoading = false) }
                callback()
            }
        }

        // Fetch current orders
        currentOrdersJob = viewModelScope.launch {
            try {
                unZipViewStateFlow(
                    currentOrdersUseCase(Unit),
                    onSuccess = { result ->
                        if (requestId == currentRequestId && currentTabStatus == OrderItemStatus.ALL.itemStatus) {
                            val orders = result as? List<OrderUiData> ?: emptyList()
                            _state.update { it.copy(currentOrders = orders, currentOrdersFetched = true) }
                            currentOrdersCompleted = true
                            checkCompletion()
                        }
                    },
                    onError = {
                        if (requestId == currentRequestId) {
                            currentOrdersCompleted = true
                            checkCompletion()
                        }
                    }
                )
            } catch (e: Exception) {
                if (requestId == currentRequestId) {
                    currentOrdersCompleted = true
                    checkCompletion()
                }
            }
        }

        // Fetch past orders
        pastOrdersJob = viewModelScope.launch {
            try {
                unZipViewStateFlow(
                    pastOrdersUseCase(PastOrdersRequest(title.lowercase(), month)),
                    onSuccess = { result ->
                        if (requestId == currentRequestId && currentTabStatus == OrderItemStatus.ALL.itemStatus) {
                            val orders = result as? List<PastOrderUiData> ?: emptyList()
                            _state.update { it.copy(pastOrders = orders, pastOrdersFetched = true) }
                            pastOrdersCompleted = true
                            checkCompletion()
                        }
                    },
                    onError = {
                        if (requestId == currentRequestId) {
                            pastOrdersCompleted = true
                            checkCompletion()
                        }
                    }
                )
            } catch (e: Exception) {
                if (requestId == currentRequestId) {
                    pastOrdersCompleted = true
                    checkCompletion()
                }
            }
        }
    }

    fun fetchFoodOrders(
        limit: Int,
        page: Int,
        callback: () -> Unit = {}
    ) {
        // Cancel any ongoing food orders request
        foodOrdersJob?.cancel()
        _state.update {
            it.copy(
                foodOrders = emptyList()
            )
        }

        // Set loading state
        _state.update { it.copy(isLoading = true) }

        // Increment request ID for this new request
        val requestId = ++currentRequestId
        currentTabStatus = OrderItemStatus.ALL.itemStatus

        foodOrdersJob = viewModelScope.launch {
            try {
                unZipViewStateFlow(
                    foodOrdersUseCase(
                        RequestData(
                            limit = limit,
                            page = page
                        )
                    ),
                    onSuccess = {
                        // Only process response if this is still the current request
                        if (requestId == currentRequestId && currentTabStatus == OrderItemStatus.ALL.itemStatus) {
                            val orders = it as List<FoodOrderUIData>
                            val currentProducts = state.value.foodOrders
                            val currentProductIds = currentProducts.map { it.id }
                            val uniqueFrequentProducts =
                                orders.filterNot { currentProductIds.contains(it.id) }
                            if (uniqueFrequentProducts.isNotEmpty()) {
                                _state.update { s ->
                                    s.copy(
                                        foodOrders = currentProducts + uniqueFrequentProducts,
                                        isLoading = false
                                    )
                                }
                            } else {
                                _state.update { s -> s.copy(isLoading = false) }
                            }
                            callback()
                        }
                    },
                    onError = {
                        if (requestId == currentRequestId) {
                            _state.update { it.copy(isLoading = false) }
                            callback()
                        }
                    }
                )
            } catch (e: Exception) {
                // Handle cancellation or other errors
                if (requestId == currentRequestId) {
                    _state.update { it.copy(isLoading = false) }
                    callback()
                }
            }
        }
    }

    fun getOrdersByTabStatus(tabStatus: String): List<FoodOrderUIData> {
        return when (tabStatus) {
            OrderItemStatus.ALL.itemStatus -> state.value.foodOrders
            OrderItemStatus.UPCOMING.itemStatus -> state.value.foodOrders.filter {
                it.orderStatus == OrderTrackingStatuses.PENDING.status ||
                        it.orderStatus == OrderTrackingStatuses.IN_PROGRESS.status ||
                        it.orderStatus == OrderTrackingStatuses.DELAYED.status ||
                        it.orderStatus == OrderTrackingStatuses.OUT_FOR_DELIVERY.status
            }

            OrderItemStatus.CANCELLED.itemStatus -> state.value.foodOrders.filter {
                it.orderStatus == OrderTrackingStatuses.CANCELLED.status
            }

            OrderItemStatus.DELIVERED.itemStatus -> state.value.foodOrders.filter {
                it.orderStatus == OrderTrackingStatuses.DELIVERED.status
            }

            else -> emptyList()
        }
    }


    fun startOrderReview(
        orderForReviewData: OrderReviewData,
        bottomSheetNavigator: BottomSheetNavigator,
    ) {
        orderForReviewData.apply {
            val isDelivered =
                (orderStatus == OrderStatus.DELIVERED.status || orderStatus == OrderStatus.PARTIAL_RETURNED.status)

            if (salesOrderRating.isNull() && isDelivered) {
                bottomSheetNavigator.show(
                    OrderReviewBottomSheetScreen(
                        salesOrderId = salesOrderId,
                        deliveryDate = deliveredAt,
                        orderTotal = orderTotal,
                        shipmentId = shipmentId
                    )
                )
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        // Cancel any ongoing requests when ViewModel is cleared
        currentOrdersJob?.cancel()
        pastOrdersJob?.cancel()
        foodOrdersJob?.cancel()
        debounceJob?.cancel()
    }

    fun updateOrderDetails(order: OrderUiData) {
        _orderUiData.value = order
    }

    fun updateSelectedOuterTab(tabIndex: Int) {
        _state.update { it.copy(selectedOuterTab = tabIndex) }
    }

    fun updateSelectedInnerTab(tabIndex: Int) {
        _state.update { it.copy(selectedInnerTabIndex = tabIndex) }
    }

    fun resetTabSelections() {
        _state.update { it.copy(selectedOuterTab = 0, selectedInnerTabIndex = 0) }
    }

} 