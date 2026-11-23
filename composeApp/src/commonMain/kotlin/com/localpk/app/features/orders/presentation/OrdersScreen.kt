package com.localpk.app.features.orders.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.order_review.data.OrderReviewData
import com.localpk.app.features.orders.data.GlobalEvents
import com.localpk.app.features.orders.components.OrdersComponent
import com.localpk.app.features.orders.components.TopHeader
import com.localpk.app.features.orders.data.OrderItemStatus
import com.localpk.app.features.orders.data.OrderStatus
import com.localpk.app.features.orders.data.OrderUiData
import com.localpk.app.features.orders.data.PastOrderUiData
import com.localpk.app.features.orders.data.StatusModel
import com.localpk.app.features.orders.viewModel.OrderListingState
import com.localpk.app.features.orders.viewModel.OrderListingViewModel
import com.localpk.app.features.restaurant.presentation.OrderTrackingScreen
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.default
import com.localpk.app.utils.navigateToWebChat
import org.koin.compose.koinInject

class OrdersScreen(private val innerTabIndex: Int = ZERO_INT) : Screen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val orderListingViewModel: OrderListingViewModel = koinInject()
        val state by orderListingViewModel.state.collectAsState()
        val outerTabs = listOf("Grocery & More", "Food")
        val sharedPreferencesManager: SharedPreferencesManager = koinInject()
        val tabs = state.statusData
        val eventLogger: EventLogger = koinInject()

        // Use ViewModel state for tab selection to persist across navigation
        val selectedOuterTab = state.selectedOuterTab
        val selectedInnerTabIndex = state.selectedInnerTabIndex

        // Initialize tab selection if coming from navigation with specific tab
        LaunchedEffect(innerTabIndex) {
            if (innerTabIndex != ZERO_INT && innerTabIndex != selectedInnerTabIndex) {
                orderListingViewModel.updateSelectedInnerTab(innerTabIndex)
            }
        }

        LaunchedEffect(Unit) {
            GlobalEvents.isOrderCancelled.collect { cancelled ->
                if (cancelled) {
                    refreshOrders(
                        orderListingViewModel,
                        selectedOuterTab,
                        selectedInnerTabIndex,
                        tabs
                    )
                    GlobalEvents.updateIsOrderCancelled(false)
                }
            }
        }

        // Pull to refresh state
        val isRefreshing by orderListingViewModel.state.collectAsState()
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshing.isLoading,
            onRefresh = {
                // Switch to All tab (index 0) when pull to refresh happens on Grocery & More tab
                val tabIndexToRefresh =
                    if (selectedOuterTab == 0 && tabs.isNotEmpty()) { // Grocery & More
                        val allTabIndex = 0.coerceIn(0, (tabs.size - 1).coerceAtLeast(0))
                        orderListingViewModel.updateSelectedInnerTab(allTabIndex)
                        allTabIndex
                    } else {
                        // For Food tab or if tabs is empty, refresh current tab
                        selectedInnerTabIndex.coerceIn(0, (tabs.size - 1).coerceAtLeast(0))
                    }
                // Refresh based on current outer tab
                refreshOrders(orderListingViewModel, selectedOuterTab, tabIndexToRefresh, tabs)
            }
        )
        StatusBarIconColor(backgroundColor = Color.White)
        Surface(color = Color.White) {
            Box(modifier = Modifier.pullRefresh(pullRefreshState)) {

                PullRefreshIndicator(
                    refreshing = isRefreshing.isLoading,
                    state = pullRefreshState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(10f)
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp).statusBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TopHeader(
                        onBackClick = {
                            orderListingViewModel.resetTabSelections()
                            navigator.pop()
                        },
                        onHelpClick = {
                            eventLogger.WhatsappHelpButton()
                            navigateToWebChat(navigator)
                        }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        outerTabs.forEachIndexed { index, tab ->
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { orderListingViewModel.updateSelectedOuterTab(index) },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = tab,
                                    color = if (selectedOuterTab == index) TextSubdued else TextSubduedHalf,
                                    fontWeight = if (selectedOuterTab == index) SemiBold else FontWeight.Medium,
                                    fontSize = 14.sp,
                                    fontFamily = PoppinsFontFamily()
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Spacer(
                                    modifier = Modifier
                                        .height(2.dp)
                                        .fillMaxWidth()
                                        .background(if (selectedOuterTab == index) ButtonSurfaceBrand else BorderPrimaryDefault)
                                )
                            }
                        }
                    }
                    if (selectedOuterTab == 0) {
                        GroceryOrderTabs(
                            tabs = tabs,
                            state = state,
                            orderListingViewModel = orderListingViewModel,
                            onTabSelected = { index ->
                                orderListingViewModel.updateSelectedInnerTab(
                                    index
                                )
                            },
                            isLoggedIn = sharedPreferencesManager.isLoggedIn()
                        )
                    } else {
                        FoodOrderTabs(
                            tabs = tabs.filter {
                                it.status == OrderItemStatus.ALL.itemStatus ||
                                        it.status == OrderItemStatus.UPCOMING.itemStatus ||
                                        it.status == OrderItemStatus.DELIVERED.itemStatus ||
                                        it.status == OrderItemStatus.CANCELLED.itemStatus
                            },
                            state = state,
                            orderListingViewModel = orderListingViewModel,
                            isLoggedIn = sharedPreferencesManager.isLoggedIn(),
                            onTabSelected = { index ->
                                orderListingViewModel.updateSelectedInnerTab(
                                    index
                                )
                            }
                        )
                    }

                }
            }
        }


    }


    @Composable
    fun GroceryOrderTabs(
        tabs: List<StatusModel>,
        state: OrderListingState,
        orderListingViewModel: OrderListingViewModel,
        onTabSelected: (Int) -> Unit = {},
        isLoggedIn: Boolean
    ) {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        var isRefreshing by remember { mutableStateOf(false) }
        val pagerState = rememberPagerState(pageCount = { tabs.size })
        // Clamp selectedTabIndex to valid range to prevent IndexOutOfBoundsException
        var selectedTabIndex =
            state.selectedInnerTabIndex.coerceIn(0, (tabs.size - 1).coerceAtLeast(0))

        LaunchedEffect(selectedTabIndex) {
            // Ensure selectedTabIndex is still valid before accessing
            val safeIndex = selectedTabIndex.coerceIn(0, (tabs.size - 1).coerceAtLeast(0))
            orderListingViewModel.updateIsRefreshed(true)
            if (tabs.isNotEmpty()) {
                pagerState.animateScrollToPage(safeIndex)
                onTabSelected(safeIndex) // Notify parent of tab selection
                if (orderListingViewModel.state.value.isUserLoggedIn) {
                    tabs.getOrNull(safeIndex)?.let { tab ->
                        when (tab.status) {
                            OrderItemStatus.UPCOMING.itemStatus -> {
                                // Only fetch if current orders haven't been fetched yet
                                if (!state.currentOrdersFetched) {
                                    orderListingViewModel.fetchCurrentOrders {
                                        orderListingViewModel.updateIsRefreshed(true)
                                        isRefreshing = false
                                    }
                                } else {
                                    // Use existing current orders data (even if empty, they've been fetched)
                                    orderListingViewModel.updateIsRefreshed(true)
                                    isRefreshing = false
                                }
                            }

                            OrderItemStatus.ALL.itemStatus -> {
                                // Only fetch if orders haven't been fetched yet
                                if (!state.currentOrdersFetched || !state.pastOrdersFetched) {
                                    orderListingViewModel.fetchAllOrders(tab.title) {
                                        orderListingViewModel.updateIsRefreshed(true)
                                        isRefreshing = false
                                    }
                                } else {
                                    // Use existing data (even if empty, they've been fetched)
                                    orderListingViewModel.updateIsRefreshed(true)
                                    isRefreshing = false
                                }
                            }

                            else -> {
                                // Only fetch if past orders haven't been fetched yet
                                if (!state.pastOrdersFetched) {
                                    orderListingViewModel.fetchPastOrders(tab.title) {
                                        orderListingViewModel.updateIsRefreshed(true)
                                        isRefreshing = false
                                    }
                                } else {
                                    // Use existing past orders data (even if empty, they've been fetched)
                                    orderListingViewModel.updateIsRefreshed(true)
                                    isRefreshing = false
                                }
                            }
                        }
                    }
                }
            }
        }
        LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
            if (!pagerState.isScrollInProgress) {
                selectedTabIndex = pagerState.currentPage
            }
        }
        Column {
            LazyRow(
                modifier = Modifier
                    .zIndex(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(Color.White),
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(tabs) { index, currentTab ->
                    val isSelected = selectedTabIndex == index
                    // Disable all tabs when loading (except the currently selected one to allow staying on it)
                    val isDisabled = state.isLoading && !isSelected
                    Text(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(
                                color = if (isSelected) ButtonSurfaceBrand else Color.Transparent,
                            )
                            .clickable(enabled = !isDisabled) {
                                if (!isDisabled) {
                                    orderListingViewModel.updateSelectedInnerTab(index)
                                }
                            }
                            .padding(8.dp),
                        text = currentTab.title,
                        color = if (isSelected) Color.White else if (isDisabled) TextSubduedHalf else TextSubdued,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) SemiBold else Normal,
                        fontFamily = PoppinsFontFamily()
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                key = { page -> tabs[page].id }
            ) { index ->
                val orderUiData = remember(
                    tabs[index].status,
                    state.currentOrders,
                    state.pastOrders,
                    state.isLoading
                ) {
                    mutableListOf<OrderUiData>().apply {
                        when (tabs[index].status) {
                            OrderItemStatus.UPCOMING.itemStatus -> addAll(state.currentOrders)
                            OrderItemStatus.ALL.itemStatus -> {
                                addAll(state.currentOrders)
                                addAll(state.pastOrders.map { it.toOrderUiData() })
                            }

                            else -> {
                                // Filter past orders based on tab status
                                val filteredPastOrders = state.pastOrders.filter { pastOrder ->
                                    when (tabs[index].status) {
                                        OrderItemStatus.DELIVERED.itemStatus ->
                                            pastOrder.orderStatus == OrderStatus.DELIVERED.status

                                        OrderItemStatus.CANCELLED.itemStatus ->
                                            pastOrder.orderStatus == OrderStatus.CANCELLED.status

                                        OrderItemStatus.RETURNED.itemStatus ->
                                            pastOrder.orderStatus == OrderStatus.RETURNED.status

                                        else -> true
                                    }
                                }
                                addAll(filteredPastOrders.map { it.toOrderUiData() })
                            }
                        }
                    }
                }
                OrdersComponent(
                    currentOrders = orderUiData,
                    isFood = false,
                    onReviewClick = { order ->
                        orderListingViewModel.startOrderReview(
                            OrderReviewData(
                                shipmentId = order.shipmentId,
                                salesOrderId = order.id,
                                orderTotal = order.orderTotal.toLong(),
                                createdAt = order.createdAt.default(),
                                deliveredAt = order.deliveryDate,
                                salesOrderRating = order.salesOrderRating,
                                orderStatus = order.orderStatus
                            ), bottomSheetNavigator
                        )

                    },
                    isLoading = state.isLoading,
                    isLoggedIn = isLoggedIn,
                    onOrderClick = { order ->
                        orderListingViewModel.updateOrderDetails(order)
                        navigator.push(OrderDetailScreen(id = order.id, status = order.orderStatus))
                    }
                )
            }
        }
    }

    @Composable
    fun FoodOrderTabs(
        tabs: List<StatusModel>,
        state: OrderListingState,
        orderListingViewModel: OrderListingViewModel,
        isLoggedIn: Boolean,
        onTabSelected: (Int) -> Unit = {}
    ) {
        val navigator = LocalNavigator.currentOrThrow
        val pagerState = rememberPagerState(pageCount = { tabs.size })
        // Clamp selectedTabIndex to valid range to prevent IndexOutOfBoundsException
        var selectedTabIndex =
            state.selectedInnerTabIndex.coerceIn(0, (tabs.size - 1).coerceAtLeast(0))
        var isRefreshing by remember { mutableStateOf(false) }
        LaunchedEffect(selectedTabIndex) {
            // Ensure selectedTabIndex is still valid before scrolling
            val safeIndex = selectedTabIndex.coerceIn(0, (tabs.size - 1).coerceAtLeast(0))
            if (tabs.isNotEmpty()) {
                pagerState.animateScrollToPage(safeIndex)
                if (orderListingViewModel.state.value.isUserLoggedIn) {
                    isRefreshing = true
                    tabs.getOrNull(safeIndex)?.let { tab ->
                        when (tab.status) {
                            OrderItemStatus.ALL.itemStatus -> {
                                // Only fetch if food orders are not already available
                                if (state.foodOrders.isEmpty()) {
                                    orderListingViewModel.fetchFoodOrders(
                                        limit = orderListingViewModel.pageSize,
                                        page = orderListingViewModel.currentPage
                                    ) {
                                        orderListingViewModel.updateIsRefreshed(true)
                                        isRefreshing = false
                                    }
                                } else {
                                    // Use existing food orders data
                                    orderListingViewModel.updateIsRefreshed(true)
                                    isRefreshing = false
                                }
                            }

                            else -> {
                                orderListingViewModel.updateIsRefreshed(true)
                                isRefreshing = false
                            }
                        }
                    }
                }
            }
        }
        LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
            if (!pagerState.isScrollInProgress) {
                selectedTabIndex = pagerState.currentPage
            }
        }
        Column {
            LazyRow(
                modifier = Modifier
                    .zIndex(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(Color.White),
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(tabs) { index, currentTab ->
                    val isSelected = selectedTabIndex == index
                    Text(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(
                                color = if (isSelected) ButtonSurfaceBrand else Color.Transparent,
                            )
                            .clickable { orderListingViewModel.updateSelectedInnerTab(index) }
                            .padding(8.dp),
                        text = currentTab.title,
                        color = if (isSelected) Color.White else TextSubdued,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) SemiBold else Normal,
                        fontFamily = PoppinsFontFamily()
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                key = { page -> tabs[page].id }
            ) { index ->
                val orderUiData = remember(tabs[index].status, state.foodOrders, state.isLoading) {
                    orderListingViewModel.getOrdersByTabStatus(tabs[index].status)
                }
                OrdersComponent(
                    isFood = true,
                    currentFoodOrders = orderUiData,
                    isLoading = state.isLoading,
                    isLoggedIn = isLoggedIn,
                    onFoodOrderClick = { order ->
                        navigator.push(
                            FoodOrderDetailScreen(
                                id = order.id,
                                status = order.orderStatus,
                                deliveryTime = order.deliveryTime,
                                restaurantId = order.restaurantId,
                                fromCheckout = order.fromCheckout,
                                fromTrackingScreen = order.fromTrackingScreen
                            )
                        )
                    },
                    onTrackClick = { order ->
                        navigator.push(
                            OrderTrackingScreen(
                                orderId = order.id,
                                restaurantId = order.restaurantId,
                                isCheckout = false,
                                fromDetails = false
                            )
                        )
                    }
                )
            }
        }
    }


}

fun refreshOrders(
    orderListingViewModel: OrderListingViewModel,
    selectedOuterTab: Int,
    selectedInnerTabIndex: Int,
    tabs: List<StatusModel>
) {
    // Clamp index to valid range to prevent IndexOutOfBoundsException
    val safeIndex = selectedInnerTabIndex.coerceIn(0, (tabs.size - 1).coerceAtLeast(0))
    // Get the currently selected inner tab based on index
    val selectedInnerTab = tabs.getOrNull(safeIndex)

    selectedInnerTab?.let { tab ->
        when (selectedOuterTab) {
            0 -> { // Grocery & More
                when (tab.status) {
                    OrderItemStatus.UPCOMING.itemStatus -> {
                        orderListingViewModel.fetchCurrentOrders()
                    }

                    OrderItemStatus.ALL.itemStatus -> {
                        orderListingViewModel.fetchAllOrders(tab.title)
                    }

                    else -> {
                        orderListingViewModel.fetchPastOrders(tab.title)
                    }
                }
            }

            1 -> { // Food
                when (tab.status) {
                    OrderItemStatus.ALL.itemStatus -> {
                        orderListingViewModel.fetchFoodOrders(
                            limit = orderListingViewModel.pageSize,
                            page = orderListingViewModel.currentPage
                        )
                    }

                    else -> {
                        // For other food tabs, no specific API call needed as they filter existing data
                    }
                }
            }
        }
    }
}

private fun PastOrderUiData.toOrderUiData() = OrderUiData(
    id = orderId,
    createdAt = orderDate,
    deliveryDate = EMPTY_STRING,
    orderStatus = orderStatus,
    orderTotal = orderTotal,
    totalItems = orderItemsCount,
    packedAt = EMPTY_STRING,
    confirmedAt = EMPTY_STRING,
    cancelledAt = if (orderStatus == OrderStatus.CANCELLED.status) statusTimeStampDate else EMPTY_STRING,
    dispatchedAt = EMPTY_STRING,
    deliveredAt = if (orderStatus == OrderStatus.DELIVERED.status) statusTimeStampDate else EMPTY_STRING,
    totalAmount = 0.0,
    originalOrderItemsCount = originalOrderItemsCount,
    paymentType = paymentType,
    orderPlacedTime = orderTime,
    deliveryStartRange = EMPTY_STRING,
    deliveryEndRange = EMPTY_STRING,
    shippingAddressText = shippingAddressText,
    deliveryStartTime = EMPTY_STRING,
    deliverBy = deliverBy,
    deliveredTime = if (orderStatus == OrderStatus.DELIVERED.status) statusTimeStamp else EMPTY_STRING,
    cancelledTime = if (orderStatus == OrderStatus.CANCELLED.status) statusTimeStamp else EMPTY_STRING,
    orderDeliveryStatus = EMPTY_STRING,
    deliveryEndTime = EMPTY_STRING,
    returnedAt = if (orderStatus == OrderStatus.RETURNED.status) statusTimeStampDate else EMPTY_STRING,
    returnedTime = if (orderStatus == OrderStatus.RETURNED.status) statusTimeStamp else EMPTY_STRING,
    formattedOrderTotal = "", // Add formatting if needed
    products = products,
    paymentDetails = paymentDetails,
    salesOrderRating = salesOrderRating,
    shipmentId = shipmentId,
    deliveryType = deliveryType
)
