package com.localpk.app.features.orders.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.localpk.app.features.orders.data.CancelOrderReason
import com.localpk.app.features.orders.data.GlobalEvents
import com.localpk.app.features.orders.data.OrderStatus
import com.localpk.app.features.orders.data.OrderSupportArgs
import com.localpk.app.features.orders.presentation.CancellationSuccessBottomSheetScreen
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.DcOrangePrimary
import com.localpk.app.theming.HomeBlack
import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.LightGray
import com.localpk.app.theming.LocationBG
import com.localpk.app.theming.OrderOutBg
import com.localpk.app.theming.OrderStatusCancel
import com.localpk.app.theming.OrderStatusCancelText
import com.localpk.app.theming.OrderStatusDeliver
import com.localpk.app.theming.OrderStatusDeliverText
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextGray
import com.localpk.app.theming.TextSubduedHalf
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.bottom_sheet_bar
import dealcart.composeapp.generated.resources.delivery_truck
import dealcart.composeapp.generated.resources.promocode_dc_coins_invalid
import dealcart.composeapp.generated.resources.why_cancel_order
import dealcart.composeapp.generated.resources.write_any_comments
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CancellationBottomSheetContent(
    args: OrderSupportArgs,
    reasons: List<CancelOrderReason>,
    onSelectReason: (CancelOrderReason) -> Unit,
    onCommentChanged: (String) -> Unit,
    onCancelOrder: () -> Unit,
    comment: String,
    isCommentVisible: Boolean,
    isContinueEnabled: Boolean,
    isOrderCancelled: Boolean,
    isCancellationInProgress: Boolean,
    selectedReason: CancelOrderReason?
) {

    val bottomSheetNavigator = LocalBottomSheetNavigator.current

    val scrollState = rememberScrollState()

    val (bgColor, textColor) = when (args.orderStatus) {
        OrderStatus.DELIVERED.status -> OrderStatusDeliver to OrderStatusDeliverText
        OrderStatus.CONFIRMED.status -> LocationBG to ButtonSurfaceBrand
        OrderStatus.CANCELLED.status -> OrderStatusCancel to OrderStatusCancelText
        OrderStatus.PENDING.status -> OrderOutBg to DcOrangePrimary
        else -> Color.LightGray to Color.Black
    }

    LaunchedEffect(isOrderCancelled) {
        if (isOrderCancelled) {
            GlobalEvents.updateIsOrderCancelled(true)
            bottomSheetNavigator.replace(
                CancellationSuccessBottomSheetScreen()
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.bottom_sheet_bar),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = args.shippingAddress,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HomeBlack,
                        maxLines = 1,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.2.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(Res.drawable.delivery_truck),
                            contentDescription = "Delivery Truck",
                            modifier = Modifier.size(14.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${args.deliveryDate}, 2024",
                            fontSize = 12.sp,
                            color = TextGray,
                            fontFamily = PoppinsFontFamily(),
                            letterSpacing = 0.2.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .background(color = bgColor, shape = RoundedCornerShape(14.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = args.orderStatus,
                        fontSize = 11.sp,
                        color = textColor,
                        fontWeight = FontWeight.Medium,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.2.sp
                    )
                }
            }
        }

        Text(
            text = stringResource(Res.string.why_cancel_order),
            fontSize = 16.sp,
            fontFamily = PoppinsFontFamily(),
            color = ColorPrimaryText,
            letterSpacing = 0.2.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        reasons.forEach { reason ->
            OutlinedReasonItem(
                reason = reason,
                isSelected = selectedReason == reason,
                onSelected = {
                    onSelectReason(it)
                }
            )
        }

        if (isCommentVisible) {
            Spacer(modifier = Modifier.height(5.dp))
            BasicTextField(
                value = comment,
                onValueChange = { onCommentChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 16.dp)
                    .background(SurfaceBackground, shape = RoundedCornerShape(12.dp))
                    .border(BorderStroke(1.dp, Color.Transparent)),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (comment.isEmpty()) {
                            Text(
                                stringResource(Res.string.write_any_comments),
                                color = LightGray,
                                fontFamily = PoppinsFontFamily(),
                                fontSize = 10.sp,
                            )
                        }
                        innerTextField()
                    }
                },
                textStyle = TextStyle(
                    fontSize = 12.sp,
                    color = ColorPrimaryText,
                    fontFamily = PoppinsFontFamily()
                ),
                maxLines = 2,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(Res.string.promocode_dc_coins_invalid),
            fontSize = 11.sp,
            color = IconDanger,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontFamily = PoppinsFontFamily(),
            letterSpacing = 0.2.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            enabled = isContinueEnabled && !isCancellationInProgress,
            onClick = {
                onCancelOrder()
            },
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 16.dp)
                .height(44.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.elevation(32.dp),
            colors = buttonColors(
                backgroundColor = ColorPrimary,
                disabledBackgroundColor = SurfaceBackground
            )
        ) {
            Text(
                text = if (isCancellationInProgress) "CANCELLING..." else "CANCEL ORDER",
                fontSize = 13.sp,
                color = if (isContinueEnabled && !isCancellationInProgress) Color.White else TextSubduedHalf,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                bottomSheetNavigator.hide()
            },
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 16.dp)
                .height(44.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.elevation(32.dp),
            colors = buttonColors(
                backgroundColor = Color.White,
            ),
            border = BorderStroke(1.dp, ColorPrimary)
        ) {
            Text(
                text = "BACK",
                fontSize = 13.sp,
                color = ColorPrimary,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}