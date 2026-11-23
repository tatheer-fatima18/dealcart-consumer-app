package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.localpk.app.features.restaurant.data.OrderTrackingStatuses
import com.localpk.app.features.user_location.data.Geolocation

@Composable
fun TrackingScreen(
    onBack: () -> Unit,
    orderStatus: String,
    isPrepaid: Boolean,
    onHelp: () -> Unit,
    onCall: () -> Unit,
    onClickOrders: () -> Unit,
    onClickRestaurants: () -> Unit,
    onClickWallet: () -> Unit,
    startLatLng: Geolocation,
    endLatLng: Geolocation,
    path: List<Geolocation>?,
    deliveryTime: String,
    logo: String,
    isCheckOut: Boolean
) {
    val pendingStatus = orderStatus == OrderTrackingStatuses.PENDING.status
    val confirmedStatus = orderStatus == OrderTrackingStatuses.IN_PROGRESS.status
    val cancelledStatus = orderStatus == OrderTrackingStatuses.CANCELLED.status
    val outForDelStatus = orderStatus == OrderTrackingStatuses.OUT_FOR_DELIVERY.status
    val deliveredStatus = orderStatus == OrderTrackingStatuses.DELIVERED.status
    val delayedStatus = orderStatus == OrderTrackingStatuses.DELAYED.status

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)).statusBarsPadding()
    ) {
        if (pendingStatus || confirmedStatus || cancelledStatus || delayedStatus) {
            ConfirmedTracking(
                onBack = onBack,
                onHelp = onHelp,
                isPrepaid = isPrepaid,
                isDelayed = delayedStatus,
                onClickWallet = onClickWallet,
                onClickRestaurants = onClickRestaurants,
                onCall = onCall,
                onClickOrders = onClickOrders,
                pendingStatus = pendingStatus,
                confirmedStatus = confirmedStatus,
                cancelledStatus = cancelledStatus,
                deliveryTime = deliveryTime,
                logo = logo,
                isCheckOut = isCheckOut
            )
        } else if (outForDelStatus || deliveredStatus) {
            DeliveredStatus(
                startLatLng = startLatLng,
                endLatLng = endLatLng,
                outForDelStatus = outForDelStatus,
                deliveredStatus = deliveredStatus,
                onCall = onCall,
                onHelp = onHelp,
                onBack = onBack,
                onClickOrders = onClickOrders,
                path = path,
                deliveryTime = deliveryTime,
                logo = logo,
                isCheckOut = isCheckOut
            )
        }
    }
}
