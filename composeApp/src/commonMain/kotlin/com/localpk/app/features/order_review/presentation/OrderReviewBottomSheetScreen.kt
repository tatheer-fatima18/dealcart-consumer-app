package com.localpk.app.features.order_review.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.order_review.data.OrderReviewBottomSheetUIData
import com.localpk.app.features.order_review.viewModel.OrderReviewViewModel
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.formatOrderDeliveryDate
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.agay_chalain
import dealcart.composeapp.generated.resources.how_was_your_experience_with_
import dealcart.composeapp.generated.resources.ic_bag_order_review
import dealcart.composeapp.generated.resources.ic_cross_review_sheet
import dealcart.composeapp.generated.resources.ic_one_last_step_calender
import dealcart.composeapp.generated.resources.ic_one_last_step_money
import dealcart.composeapp.generated.resources.ic_order_review_star_empty
import dealcart.composeapp.generated.resources.ic_order_review_star_filled
import dealcart.composeapp.generated.resources.ic_review_sad
import dealcart.composeapp.generated.resources.order_delivery
import dealcart.composeapp.generated.resources.total_bill
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

class OrderReviewBottomSheetScreen(
    private val salesOrderId: Long,
    private val deliveryDate: String,
    private val orderTotal: Long,
    private val shipmentId: Long? = null,
) : Screen {

    @Composable
    override fun Content() {
        val argsData = OrderReviewBottomSheetUIData(
            salesOrderId = salesOrderId,
            deliveryDate = deliveryDate,
            orderTotal = orderTotal,
            shipmentId = shipmentId
        )
        val viewModel = koinViewModel<OrderReviewViewModel>()
        val state by viewModel.state.collectAsState()

        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.updateArgsData(argsData)
        }

        Surface(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Close button
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { bottomSheetNavigator.hide() },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_cross_review_sheet),
                            tint = Color.Black,
                            contentDescription = "Close",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                }

                if (state.isRatingUnderFive) {
                    Image(
                        modifier = Modifier.size(55.dp),
                        painter = painterResource(Res.drawable.ic_review_sad),
                        contentDescription = null
                    )

                } else {
                    Image(
                        modifier = Modifier.size(55.dp),
                        painter = painterResource(Res.drawable.ic_bag_order_review),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))


                Text(
                    text = "Apka order #${argsData.salesOrderId}",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    letterSpacing = 0.02.em,
                    color = ColorPrimaryText,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(Res.string.how_was_your_experience_with_),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    letterSpacing = 0.02.em,
                    color = ColorPrimaryText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(5) { index ->
                        IconButton(
                            onClick = { viewModel.onRatingChanged((index + 1).toFloat()) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (state.rating > index) Res.drawable.ic_order_review_star_filled
                                    else Res.drawable.ic_order_review_star_empty
                                ),
                                contentDescription = "Star ${index + 1}",
                                tint = if (state.rating > index) ColorPrimary else SurfaceBackground,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                }


                if (state.rating > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.reviewText,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = ColorPrimaryText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.size(42.dp),
                                painter = painterResource(Res.drawable.ic_one_last_step_money),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = stringResource(Res.string.total_bill),
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    color = TextSubdued,
                                    lineHeight = 10.sp,
                                    letterSpacing = 0.02.em,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    modifier = Modifier.padding(bottom = 2.dp),
                                    text = argsData.orderTotal.toDouble().formatPKRCurrency(),
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF06B256),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.size(42.dp),
                                painter = painterResource(Res.drawable.ic_one_last_step_calender),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = stringResource(Res.string.order_delivery),
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    lineHeight = 10.sp,
                                    letterSpacing = 0.02.em,
                                    color = TextSubdued,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    modifier = Modifier.padding(bottom = 2.dp),
                                    text = argsData.deliveryDate.formatOrderDeliveryDate(),
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF06B256), // green_dark
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Continue button
                Button(
                    onClick = {
                        viewModel.onContinueClick(
                            navigateToOrderReviewCategoriesListingScreen = {
                                navigator.push(
                                    OrderReviewCategoriesListingScreen(
                                        id = salesOrderId,
                                        shipmentId = shipmentId.getValueOrDefault(),
                                        rating = viewModel.state.value.rating.toInt()
                                    )
                                )
                            },
                            navigateToOrderReviewSuccessBottomSheet = {
                                bottomSheetNavigator.replace(
                                    OrderReviewSuccessBottomSheet(
                                        orderId = salesOrderId,
                                        rating = viewModel.state.value.rating.toInt()
                                    )
                                )
                            }
                        )
                    },
                    enabled = state.rating > 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorPrimary,
                        disabledContainerColor = SurfaceBackground
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.agay_chalain),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = if (state.rating > 0) Color.White else TextSubduedHalf
                    )
                }
            }
        }
    }
} 