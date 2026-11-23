package com.localpk.app.features.orders.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.localpk.app.features.orders.components.CancellationBottomSheetContent
import com.localpk.app.features.orders.data.OrderSupportArgs
import com.localpk.app.features.orders.viewModel.CancelOrderBottomSheetViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class CancellationBottomSheetScreen(
    val orderId: Long,
    val orderStatus: String,
    val deliveryDate: String,
    private val orderCreatedAt: String,
    private val shippingAddress: String,
    private val isOrderCancellable: Boolean
) : Screen {

    @Composable
    override fun Content() {

        val args = OrderSupportArgs(
            orderId = orderId,
            orderStatus = orderStatus,
            orderCreatedAt = orderCreatedAt,
            shippingAddress = shippingAddress,
            isOrderCancellable = isOrderCancellable,
            deliveryDate = deliveryDate
        )

        val cancelOrderBottomSheetViewModel = koinViewModel<CancelOrderBottomSheetViewModel>(
            key = "CancelOrderBottomSheetViewModel_${args.orderId}"
        ) {
            parametersOf(args)
        }

        val reasons by cancelOrderBottomSheetViewModel.reasonsData.collectAsState()
        val comment by cancelOrderBottomSheetViewModel.cancelReasonComment.collectAsState()
        val isCommentVisible by cancelOrderBottomSheetViewModel.isCommentTextAreaVisible.collectAsState(
            false
        )
        val isContinueEnabled by cancelOrderBottomSheetViewModel.isContinueButtonEnabled.collectAsState()
        val isOrderCancelled by cancelOrderBottomSheetViewModel.isOrderCancelled.collectAsState()
        val isCancellationInProgress by cancelOrderBottomSheetViewModel.isCancellationInProgress.collectAsState()
        val selectedReason by cancelOrderBottomSheetViewModel.selectedReason.collectAsState()

        Surface(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
                .imePadding()
        ) {
            CancellationBottomSheetContent(
                args = args,
                reasons = reasons,
                comment = comment,
                isCommentVisible = isCommentVisible,
                isContinueEnabled = isContinueEnabled,
                isOrderCancelled = isOrderCancelled,
                isCancellationInProgress = isCancellationInProgress,
                onCancelOrder = { cancelOrderBottomSheetViewModel.cancelOrder() },
                onCommentChanged = {
                    cancelOrderBottomSheetViewModel.cancelReasonComment.value = it
                },
                onSelectReason = {
                    cancelOrderBottomSheetViewModel.updateSelectedReason(it)
                },
                selectedReason = selectedReason
            )
        }
    }
}