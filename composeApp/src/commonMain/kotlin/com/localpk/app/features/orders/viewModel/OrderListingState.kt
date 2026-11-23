package com.localpk.app.features.orders.viewModel


import com.localpk.app.features.orders.data.OrderUiData
import com.localpk.app.features.orders.data.PastOrderUiData
import com.localpk.app.features.orders.data.StatusModel
import com.localpk.app.features.restaurant.data.FoodOrderUIData
import com.localpk.app.utils.ZERO_FLOAT


data class OrderListingState(
    val isUserLoggedIn: Boolean = false,
    val isRefreshed: Boolean = false,
    val pastOrders: List<PastOrderUiData> = emptyList(),
    val currentOrders: List<OrderUiData> = emptyList(),
    val foodOrders: List<FoodOrderUIData> = emptyList(),
    val isOrderRated: Boolean = false,
    val rating: Float = ZERO_FLOAT,
    val currentPage: Int = 1,
    val pageSize: Int = 30,
    val statusData: List<StatusModel> = emptyList(),
    val isLoading: Boolean = false,
    val selectedOuterTab: Int = 0,
    val selectedInnerTabIndex: Int = 0,
    val currentOrdersFetched: Boolean = false,
    val pastOrdersFetched: Boolean = false
) 