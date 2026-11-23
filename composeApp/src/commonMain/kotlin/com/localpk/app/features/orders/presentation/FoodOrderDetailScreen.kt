package com.localpk.app.features.orders.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.orders.components.LocationAndContactComponent
import com.localpk.app.features.orders.components.OrderDetailShimmerLoading
import com.localpk.app.features.orders.viewModel.FoodOrderDetailViewModel
import com.localpk.app.features.restaurant.components.FoodItemsCard
import com.localpk.app.features.restaurant.components.FoodOrderInfoComponent
import com.localpk.app.features.restaurant.components.FoodPaymentCard
import com.localpk.app.features.restaurant.components.FoodSummaryComponent
import com.localpk.app.features.restaurant.components.OrderDetailTopHeader
import com.localpk.app.features.restaurant.data.FoodOrderDetailUIData
import com.localpk.app.features.restaurant.presentation.OrderTrackingScreen
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.navigateToWebChat
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class FoodOrderDetailScreen(
    val id: Long,
    val status: String,
    val restaurantId: Long,
    val deliveryTime: String,
    val fromTrackingScreen: Boolean,
    private val fromCheckout: Boolean
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<FoodOrderDetailViewModel>(
            key = "FoodOrderDetailViewModel_$id"
        ) { parametersOf(id, status) }
        val state by viewModel.state.collectAsState()
        StatusBarIconColor(backgroundColor = Color.White)
        if (state.isLoading) {
            OrderDetailShimmerLoading()
        } else {
            Surface(color = Color.White) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp).statusBarsPadding()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OrderDetailTopHeader(
                        isInvoiceVisible = state.isInvoiceVisible,
                        onBackClick = {
                            if (fromCheckout) {
                                // Implement navigation logic for back from checkout
                                navigator.pop()
                            } else {
                                navigator.pop()
                            }
                        },
                        isCancellable = false,
                        onHelpClick = {
                            navigateToWebChat(navigator)
                        },
                        onPrintClick = { viewModel.onDownloadReceiptClicked() }
                    )
                    FoodOrderInfoComponent(
                        restaurantName = state.orderDetails?.restaurantName.default(),
                        restaurantLogo = state.orderDetails?.restaurantLogo.default(),
                        onTrackClick = {
                            navigator.push(
                                OrderTrackingScreen(
                                    orderId = id,
                                    restaurantId = restaurantId,
                                    isCheckout = false,
                                    fromDetails = true
                                )
                            )
                        },
                        orderDetails = state.orderDetails,
                        deliveryTime = deliveryTime
                    )
                    LocationAndContactComponent(
                        defaultPhoneNumber = state.userPhoneNumber.default(),
                        secondaryPhoneNumber = state.orderDetails?.secondaryPhoneNumber.orEmpty(),
                        latitude = state.orderDetails?.address?.latitude.getValueOrDefault(),
                        longitude = state.orderDetails?.address?.longitude.getValueOrDefault(),
                        shippingCity = state.orderDetails?.address?.city.default(),
                        shippingAddressText = state.orderDetails?.address?.addressLine2.default()
                    )
                    FoodOrderDetailCompose(
                        data = state.orderDetails
                    )
                }
            }
        }
    }
}

@Composable
fun FoodOrderDetailCompose(
    data: FoodOrderDetailUIData?,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        FoodItemsCard(
            data = data
        )
        if (data != null) {
            Spacer(modifier = Modifier.height(10.dp))
            FoodSummaryComponent(
                formattedOrderTotal = data.formattedOrderTotal(),
                platformFee = data.platformFee.getValueOrDefault(),
                deliveryFee = data.deliveryFee.getValueOrDefault(),
                promoCodeDiscount = data.promoCodeDiscount.getValueOrDefault(),
                dcCoinDiscount = data.dcCoinDiscount.getValueOrDefault(),
                formattedPromoCodeDiscount = data.formattedPromoCodeDiscount().default(),
                formattedGrandTotal = data.formattedGrandTotal().default(),
                formattedDcCoinDiscount = data.formattedDcCoinDiscount().default(),
                taxes = data.taxes,
                formattedTax = data.formattedTax().default(),
                modifier = Modifier.border(
                    width = 1.dp,
                    color = BorderPrimaryDefault,
                    shape = RoundedCornerShape(8.dp)
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        FoodPaymentCard(
            data?.paymentType.default(),
            data?.grandTotal().getValueOrDefault().toInt()
        )
    }
} 