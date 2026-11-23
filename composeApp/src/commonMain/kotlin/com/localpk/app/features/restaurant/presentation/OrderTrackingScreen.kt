package com.localpk.app.features.restaurant.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.home.presentation.HomeScreen
import com.localpk.app.features.orders.presentation.FoodOrderDetailScreen
import com.localpk.app.features.restaurant.components.TrackingScreen
import com.localpk.app.features.restaurant.data.OrderTrackingStatuses
import com.localpk.app.features.restaurant.data.RestaurantOrderId
import com.localpk.app.features.restaurant.viewModels.ORSDirectionViewModel
import com.localpk.app.features.restaurant.viewModels.OrderTrackingViewModel
import com.localpk.app.features.user_location.data.Geolocation
import com.localpk.app.features.wallet.view.WalletScreen
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotZero
import com.localpk.app.utils.navigateToWebChat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class OrderTrackingScreen(
    private val orderId: Long?,
    private val restaurantId: Long?,
    private val isCheckout: Boolean?,
    private val fromDetails: Boolean?,
    private val fromNotification: Boolean = false
) : Screen {

    companion object {
        private val _isFood = MutableStateFlow(false)
        val isFood: StateFlow<Boolean> = _isFood
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {

        val args = remember {
            RestaurantOrderId(orderId, restaurantId, isCheckout, fromDetails)
        }

        val orderTrackingViewModel: OrderTrackingViewModel = koinInject()

        val mapViewModel = koinViewModel<ORSDirectionViewModel>()

        val isRefreshing by orderTrackingViewModel.isRefreshed.collectAsState()
        val eventLogger: EventLogger = koinInject()
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = {
                orderTrackingViewModel.refreshData()
            }
        )

        LaunchedEffect(Unit) {
            orderTrackingViewModel._argsData.value = args
            orderTrackingViewModel.getTrackingStatus()
            orderTrackingViewModel.getRestaurantConfigDetails()
        }

        LaunchedEffect(fromNotification) {
            if (fromNotification) {
                orderTrackingViewModel.getTrackingStatus()
                orderTrackingViewModel.getRestaurantConfigDetails()
            }
        }

        val path by mapViewModel.routePath.collectAsState()
        val trackingStatus by orderTrackingViewModel.trackingStatus.collectAsState()
        val restaurantConfigDetails by orderTrackingViewModel.restaurantConfigDetails.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        val startLatLng = Geolocation(
            trackingStatus?.latitude.getValueOrDefault(),
            trackingStatus?.longitude.getValueOrDefault()
        )
        val endLatLng = Geolocation(
            restaurantConfigDetails?.latitude.getValueOrDefault(),
            restaurantConfigDetails?.longitude.getValueOrDefault()
        )

        LaunchedEffect(startLatLng, endLatLng, trackingStatus?.trackingStatus.default()) {
            if (startLatLng.latitude.isNotZero() && startLatLng.longitude.isNotZero() &&
                endLatLng.latitude.isNotZero() && endLatLng.longitude.isNotZero() &&
                (trackingStatus?.trackingStatus.default() == OrderTrackingStatuses.DELIVERED.status ||
                        trackingStatus?.trackingStatus.default() == OrderTrackingStatuses.OUT_FOR_DELIVERY.status)
            ) {
                mapViewModel.fetchRoute(
                    start = startLatLng,
                    end = endLatLng,
                    apiKey = RemoteConfigs.getDirectionsPublicKey()
                )
            }
        }
        StatusBarIconColor(backgroundColor = SurfaceBackground)
        Surface(color = SurfaceBackground) {
            Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
                TrackingScreen(
                    onBack = {
                        if (args.isCheckout == true)
                            navigator.push(HomeScreen())
                        //onActionHome(true)
                        else
                            navigator.pop()
                    },
                    orderStatus = trackingStatus?.trackingStatus.default(),
                    isPrepaid = trackingStatus?.isPrepaid.default(),
                    onHelp = {
                        eventLogger.WhatsappHelpButton()
                        navigateToWebChat(navigator)
                    },
                    onCall = {
                        openDialPad(restaurantConfigDetails?.phoneNumber.default())
                    },
                    onClickRestaurants = {
                        if (args.isCheckout == true)
                            navigator.push(HomeScreen())
                        else
                            navigator.pop()
                        // onActionHome(true)
                    },
                    onClickOrders = {
                        if (fromDetails.default())
                            navigator.pop()
                        else {
                            navigator.push(
                                FoodOrderDetailScreen(
                                    id = orderId.getValueOrDefault(),
                                    restaurantId = restaurantId.getValueOrDefault(),
                                    status = trackingStatus?.trackingStatus.default(),
                                    deliveryTime = trackingStatus?.deliveryTime.default(),
                                    fromTrackingScreen = true,
                                    fromCheckout = args.isCheckout.default()
                                )
                            )
                        }
                    },
                    onClickWallet = {
                        navigator.push(WalletScreen())
                    },
                    startLatLng = startLatLng,
                    endLatLng = endLatLng,
                    path = path,
                    deliveryTime = trackingStatus?.deliveryTime.default(),
                    logo = restaurantConfigDetails?.logo.default(),
                    isCheckOut = args.isCheckout.default()
                )

                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

expect fun openDialPad(phoneNumber: String)
