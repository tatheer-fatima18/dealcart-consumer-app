package com.localpk.app.features.orders.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.home.presentation.HomeScreen
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.login.presentation.PhoneInputScreen
import com.localpk.app.features.orders.data.OrderStatus
import com.localpk.app.features.orders.data.OrderUiData
import com.localpk.app.features.restaurant.data.FoodOrderUIData
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.isNotNull
import dealcart.composeapp.generated.resources.Login
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.apnay_filhal
import dealcart.composeapp.generated.resources.items_dekhain
import dealcart.composeapp.generated.resources.no_orders
import dealcart.composeapp.generated.resources.sign_in_to_orders
import dealcart.composeapp.generated.resources.view_orders
import org.jetbrains.compose.resources.stringResource

@Composable
fun OrdersComponent(
    currentOrders: List<OrderUiData>? = null,
    isFood: Boolean = false,
    currentFoodOrders: List<FoodOrderUIData>? = null,
    listState: LazyListState = rememberLazyListState(),
    onLoadMore: (() -> Unit)? = null,
    onOrderClick: (OrderUiData) -> Unit = {},
    onFoodOrderClick: (FoodOrderUIData) -> Unit = {},
    onTrackClick: (FoodOrderUIData) -> Unit = {},
    onReviewClick: (OrderUiData) -> Unit = {},
    isLoggedIn: Boolean,
    isLoading: Boolean = false
) {
    val navigator = LocalNavigator.currentOrThrow

    val ordersAvailable =
        if (isFood) currentFoodOrders?.isNotEmpty() == true else currentOrders?.isNotEmpty() == true

    val hasTriggeredLoadMore = remember { mutableStateOf(false) }
    val previousTotalItemsCount = remember { mutableIntStateOf(0) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val currentTotalItemsCount = layoutInfo.totalItemsCount

                if (
                    lastVisibleItemIndex != null &&
                    currentTotalItemsCount > 0 &&
                    lastVisibleItemIndex >= currentTotalItemsCount - 5 &&
                    hasTriggeredLoadMore.value.not()
                ) {
                    onLoadMore?.invoke()
                    hasTriggeredLoadMore.value = true
                }

                if (currentTotalItemsCount > previousTotalItemsCount.intValue) {
                    hasTriggeredLoadMore.value = false
                    previousTotalItemsCount.intValue = currentTotalItemsCount
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        if (isLoading) {
            // Show shimmer loading
            OrderShimmerLoading()
        } else if (ordersAvailable) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(8.dp),
                state = listState
            ) {
                if (isFood) {
                    itemsIndexed(currentFoodOrders.orEmpty()) { _, order ->
                        FoodOrderItemLayout(
                            orderData = order,
                            orderStatus = EMPTY_STRING,
                            onOrderClick = { onFoodOrderClick(order) },
                            onTrackClick = { onTrackClick(order) }
                        )
                    }
                } else {
                    itemsIndexed(currentOrders.orEmpty()) { _, order ->
                        val isOrderDelivered =
                            order.orderStatus == OrderStatus.DELIVERED.status ||
                                    order.orderStatus == OrderStatus.PARTIAL_RETURNED.status
                        val isOrderRated = order.salesOrderRating.isNotNull() && isOrderDelivered
                        val ratingValue = order.salesOrderRating?.rating?.toFloat() ?: 0f

                        OrderItemLayout(
                            orderData = order,
                            orderStatus = EMPTY_STRING,
                            onOrderClick = { onOrderClick(order) },
                            isOrderDelivered = isOrderDelivered,
                            isOrderRated = isOrderRated,
                            onReviewClick = { onReviewClick(order) },
                            rating = ratingValue
                        )
                    }
                }
            }
        } else {

            EmptyOrderComponent(
                isButtonVisible = true,
                onClickViewAll = {
                    if (isLoggedIn) navigator.popUntil { screen ->
                        screen is HomeScreen
                    } else navigator.push(
                        PhoneInputScreen(
                            navSource = ScreenSource.ORDERS_LISTING.key
                        )
                    )
                },
                orderText = if (isLoggedIn) stringResource(Res.string.no_orders) else stringResource(
                    Res.string.view_orders
                ),
                orderTextDescription = if (isLoggedIn) stringResource(Res.string.apnay_filhal) else stringResource(
                    Res.string.sign_in_to_orders
                ),
                buttonText = if (isLoggedIn) stringResource(Res.string.items_dekhain) else stringResource(
                    Res.string.Login
                ),
            )
        }

        /*  if (refreshState.isRefreshing) {
              LaunchedEffect(key1 = true) {
                  onRefresh()
              }
          }

          LaunchedEffect(isRefreshing) {
              if (isRefreshing) {
                  refreshState.startRefresh()
              } else {
                  refreshState.endRefresh()
              }
          }

          PullToRefreshContainer(
              state = refreshState,
              modifier = Modifier.align(Alignment.TopCenter),
              containerColor = Color.White
          )*/
    }
} 