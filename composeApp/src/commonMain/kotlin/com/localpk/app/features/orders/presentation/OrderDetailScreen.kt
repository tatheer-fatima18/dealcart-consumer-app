package com.localpk.app.features.orders.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.cart.data.CartOrderType
import com.localpk.app.features.flash_deals.data.FlashDealUiData
import com.localpk.app.features.order_review.data.OrderReviewData
import com.localpk.app.features.orders.components.DisclaimerCard
import com.localpk.app.features.orders.components.ItemsCard
import com.localpk.app.features.orders.components.LocationAndContactComponent
import com.localpk.app.features.orders.components.OrderDetailShimmerLoading
import com.localpk.app.features.orders.components.PaymentCard
import com.localpk.app.features.orders.components.RatingCard
import com.localpk.app.features.orders.components.SummaryComponent
import com.localpk.app.features.orders.components.TimelineCard
import com.localpk.app.features.orders.components.TopHeader
import com.localpk.app.features.orders.data.OrderDetailModel
import com.localpk.app.features.orders.data.OrderItemDetailUiData
import com.localpk.app.features.orders.data.OrderStatus
import com.localpk.app.features.cart.data.PaymentDetails
import com.localpk.app.features.orders.components.DeliveryCard
import com.localpk.app.features.orders.data.GlobalEvents
import com.localpk.app.features.orders.viewModel.OrderDetailViewModel
import com.localpk.app.features.orders.viewModel.OrderListingViewModel
import com.localpk.app.features.productlist.data.StoreType
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.theming.TextSuccess
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.capitalizeFirstLetter
import com.localpk.app.utils.default
import com.localpk.app.utils.formatCartDeliveryDate
import com.localpk.app.utils.formatDateWithOne
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.navigateToWebChat
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.dealcart
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder
import dealcart.composeapp.generated.resources.out_for_delivery
import dealcart.composeapp.generated.resources.partner
import dealcart.composeapp.generated.resources.your_order_redeliver
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class OrderDetailScreen(
    val id: Long,
    val status: String
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val orderListingViewModel = koinViewModel<OrderListingViewModel>()
        val args by orderListingViewModel.orderUiData.collectAsState()
        val eventLogger: EventLogger = koinInject()
        val orderDetailViewModel = koinViewModel<OrderDetailViewModel>(
            key = "OrderDetailViewModel_$id"
        ) {
            parametersOf(id, navigator, bottomSheetNavigator)
        }

        val state by orderDetailViewModel.state.collectAsState()

        LaunchedEffect(args) {
            if (args != null) {
                orderDetailViewModel._argsData.value = args
            }
        }

        LaunchedEffect(Unit) {
            GlobalEvents.isOrderCancelled.collect { cancelled ->
                if (cancelled) {
                    orderDetailViewModel.fetchOrderDetail(id)
                }
            }
        }
        StatusBarIconColor(backgroundColor = Color.White)

        if (state.isLoading) {
            OrderDetailShimmerLoading()
        } else {
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()).statusBarsPadding()
                        .padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TopHeader(
                        onBackClick = { navigator.pop() },
                        onHelpClick = {
                            eventLogger.WhatsappHelpButton()
                            navigateToWebChat(navigator) },
                        isCancellable = true,
                        onCancelClick = {
                            bottomSheetNavigator.show(
                                CancellationBottomSheetScreen(
                                    orderId = state.confirmedOrderDetails?.id.getValueOrDefault(),
                                    orderStatus = state.confirmedOrderDetails?.orderStatus.default(),
                                    deliveryDate = if (state.confirmedOrderDetails?.deliverBy == CartOrderType.POSTEX.key) "${state.confirmedOrderDetails?.deliveryStartRange.formatCartDeliveryDate()} - ${state.confirmedOrderDetails?.deliveryEndRange.formatCartDeliveryDate()}" else state.confirmedOrderDetails?.createdAt.formatDateWithOne(),
                                    shippingAddress = state.confirmedOrderDetails?.shippingAddressText.default(),
                                    isOrderCancellable = state.confirmedOrderDetails?.getIsOrderCancellable()
                                        .default(),
                                    orderCreatedAt = state.confirmedOrderDetails?.createdAt.default()
                                )
                            )
                        }
                    )

                    OrderInfoComponent(
                        orderDetails = state.confirmedOrderDetails,
                        totalItems = state.salesOrderItems.size,
                        isOrderDelivered = state.isOrderDelivered,
                        isOrderRated = state.isOrderRated,
                        onReviewClick = {
                            state.confirmedOrderDetails?.apply {
                                orderDetailViewModel.startOrderReview(
                                    OrderReviewData(
                                        shipmentId = salesOrderItems?.firstOrNull()?.shipmentItem?.shipment?.id.getValueOrDefault(),
                                        salesOrderId = id.getValueOrDefault(),
                                        orderTotal = orderTotal.toLong().getValueOrDefault(),
                                        createdAt = createdAt.default(),
                                        deliveredAt = deliveredAt.default(),
                                        salesOrderRating = salesOrderRating,
                                        orderStatus = orderStatus.default()
                                    ), bottomSheetNavigator
                                )
                            }
                        },
                        rating = state.rating,
                        cancellationComment = state.cancellationComment,
                        isReAttempt = state.isReAttempt,
                        deliveryDate = state.deliveryDate
                    )

                    LocationAndContactComponent(
                        defaultPhoneNumber = state.userPhoneNumber.toString(),
                        secondaryPhoneNumber = state.confirmedOrderDetails?.secondaryPhoneNumber
                            ?: EMPTY_STRING,
                        latitude = 24.8607,
                        longitude = 67.0011,
                        shippingCity = state.confirmedOrderDetails?.shippingCity ?: EMPTY_STRING,
                        shippingAddressText = state.confirmedOrderDetails?.shippingAddressText
                            ?: EMPTY_STRING
                    )

                    state.confirmedOrderDetails?.let { orderDetails ->
                        TimelineCard(
                            timeLineModel = orderDetails.timeLine(),
                            deliverBy = orderDetails.deliverBy,
                            orderStatus = orderDetails.orderStatus,
                            deliveryStartRange = orderDetails.deliveryStartRange,
                            deliveryDate = state.deliveryDate,
                            paymentStatus = orderDetails.paymentStatus,
                            deliveryTimeEnabled = orderDetails.isDeliveryTimeEnabled,
                            formattedDeliveryTime = orderDetails.formattedDeliveryTime()
                        )
                    }

                    args?.paymentDetails?.let {
                        OrderDetailCompose(
                            paymentDetails = it,
                            salesOrderItems = state.salesOrderItems,
                            flashDealsItems = state.flashDealsItems,
                            data = state.confirmedOrderDetails,
                            platformFeeName = state.platformFeeName,
                            packagingFeeName = state.packagingFeeName
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderInfoComponent(
    orderDetails: OrderDetailModel?,
    totalItems: Int,
    isOrderDelivered: Boolean,
    isOrderRated: Boolean,
    onReviewClick: () -> Unit,
    rating: Float?,
    cancellationComment: String,
    isReAttempt: Boolean,
    deliveryDate: String
) {
    val orderStatus = orderDetails?.orderStatus ?: EMPTY_STRING
    val deliverBy = orderDetails?.deliverBy ?: EMPTY_STRING

    val textColor = when (orderStatus) {
        OrderStatus.DELIVERED.status -> TextSuccess
        OrderStatus.CANCELLED.status -> IconDanger
        OrderStatus.RETURNED.status -> TextSubdued
        else -> ButtonSurfaceBrand
    }

    val deliveryText = if (deliverBy == CartOrderType.DEAL_CART.key) {
        Res.string.dealcart
    } else {
        Res.string.partner
    }

    val deliveryBgColor = if (deliverBy == CartOrderType.DEAL_CART.key) {
        ButtonSurfaceBrand
    } else {
        TextSubduedHalf
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .border(
                width = 1.dp,
                color = BorderPrimaryDefault,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = orderDetails?.id?.toString() ?: EMPTY_STRING,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubduedHalf,
                    )
                )

                Text(
                    text = when {
                        orderStatus == OrderStatus.DELIVERED.status -> OrderStatus.DELIVERED.status.capitalizeFirstLetter()
                        orderStatus == OrderStatus.CANCELLED.status -> OrderStatus.CANCELLED.status.capitalizeFirstLetter()
                        orderStatus == OrderStatus.RETURNED.status -> OrderStatus.RETURNED.status.capitalizeFirstLetter()
                        !orderDetails?.dispatchedAt.isNullOrEmpty() -> stringResource(Res.string.out_for_delivery)
                        !orderDetails?.packedAt.isNullOrEmpty() -> OrderStatus.PACKED.status.capitalizeFirstLetter()
                        !orderDetails?.confirmedAt.isNullOrEmpty() -> OrderStatus.CONFIRMED.status.capitalizeFirstLetter()
                        else -> orderDetails?.orderStatus?.capitalizeFirstLetter() ?: ""
                    },
                    style = TextStyle(
                        color = textColor,
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                    )
                )

                Spacer(modifier = Modifier.weight(.1f))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$totalItems item(s)",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = TextSubduedHalf,
                        )
                    )

                    VerticalDivider(
                        modifier = Modifier
                            .height(11.dp)
                            .align(Alignment.CenterVertically),
                        thickness = 1.dp,
                        color = BorderPrimaryDefault
                    )

                    Text(
                        text = orderDetails?.orderTotal?.formatPKRCurrency().toString(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            color = TextSubdued,
                        )
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = BorderPrimaryDefault
            )

            if (rating != null) {
                RatingCard(
                    isOrderDelivered = isOrderDelivered,
                    isOrderRated = isOrderRated,
                    onReviewClick = onReviewClick,
                    rating = rating
                )
            }

            if (orderStatus == OrderStatus.CANCELLED.status) {
                DisclaimerCard(cancellationComment)
            }

            if (isReAttempt) {
                DisclaimerCard(
                    stringResource(Res.string.your_order_redeliver, deliveryDate)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (orderDetails?.storeName != StoreType.DEAL_CART.storeName) {
                    Box(
                        modifier = Modifier
                            .width(38.dp)
                            .height(30.dp)
                            .border(
                                BorderStroke(
                                    width = 0.5.dp,
                                    color = BorderPrimaryDefault
                                ),
                                RoundedCornerShape(4.dp)
                            )
                            .clip(shape = RoundedCornerShape(4.dp))
                    ) {
                        loadImagePainterWithValidation(
                            imageUrl = if (orderDetails?.storeImage.isNullOrEmpty()
                                    .not()
                            ) orderDetails?.storeImage else EMPTY_STRING,
                            fallbackRes = Res.drawable.ic_dealcart_placeholder,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                    }

                    Text(
                        text = (orderDetails?.storeName ?: "").split("_").joinToString(" "),
                        modifier = Modifier.width(180.dp),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            color = TextSubdued
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (deliverBy.isNotEmpty()) {
                    Spacer(modifier = Modifier.weight(.1f))

                    Text(
                        modifier = Modifier
                            .background(
                                color = deliveryBgColor,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(8.dp),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Medium,
                                    fontStyle = FontStyle.Italic,
                                )
                            ) {
                                append("Fulfilled by")
                            }
                            append(" ")
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                )
                            ) {
                                append(stringResource(deliveryText))
                            }
                        },
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun OrderDetailCompose(
    salesOrderItems: List<OrderItemDetailUiData>,
    flashDealsItems: List<FlashDealUiData>?,
    data: OrderDetailModel?,
    platformFeeName: String,
    packagingFeeName: String,
    paymentDetails: PaymentDetails
) {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        ItemsCard(
            totalItemsCount = data?.pickedOrderItemsCount ?: 0,
            salesOrderItems = salesOrderItems,
            flashDealsItems = flashDealsItems
        )

        Spacer(modifier = Modifier.height(10.dp))

        PaymentCard(paymentDetails = paymentDetails)

        if (data?.deliverBy == CartOrderType.DEAL_CART.key) {
            Spacer(modifier = Modifier.height(10.dp))
            DeliveryCard(data.deliveryType, data.deliveryFee, data.deliveryDate)
        }

        if (data != null) {
            Spacer(modifier = Modifier.height(10.dp))
            SummaryComponent(
                data = data,
                platformFeeName = platformFeeName,
                packagingFeeName = packagingFeeName
            )
        }

    }
} 