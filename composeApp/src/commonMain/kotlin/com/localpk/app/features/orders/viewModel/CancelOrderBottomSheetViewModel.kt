package com.localpk.app.features.orders.viewModel

import androidx.lifecycle.viewModelScope
import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.orders.data.CancelOrderReason
import com.localpk.app.features.orders.data.CancelOrderReasonRequest
import com.localpk.app.features.orders.data.CancelOrderReasonRequestBody
import com.localpk.app.features.orders.data.OrderSupportArgs
import com.localpk.app.features.orders.domain.CancelOrderRequestUseCase
import com.localpk.app.features.orders.domain.GetCancelOrderReasonsUseCase
import com.localpk.app.utils.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.default
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CancelOrderBottomSheetViewModel(
    val argsData: OrderSupportArgs,
    private val getCancelOrderReasonsUseCase: GetCancelOrderReasonsUseCase,
    private val cancelOrderRequestUseCase: CancelOrderRequestUseCase,
//    private val eventLogger: EventLogger,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel<Any>() {
    
    // Reason list
    private val _reasonsData = MutableStateFlow<List<CancelOrderReason>>(emptyList())
    val reasonsData: StateFlow<List<CancelOrderReason>> = _reasonsData.asStateFlow()

    // Selected reason
    private val _selectedReason = MutableStateFlow<CancelOrderReason?>(null)
    val selectedReason: StateFlow<CancelOrderReason?> = _selectedReason

    // Other fields
    val isOtherReasonSelected = MutableStateFlow(false)
    val isCommentTextAreaVisible = MutableStateFlow(false)
    val isContinueButtonEnabled = MutableStateFlow(false)
    val isOrderCancelled = MutableStateFlow(false)
    val isCancellationInProgress = MutableStateFlow(false)
    val cancelReasonComment = MutableStateFlow(EMPTY_STRING)


    init {
        _reasonsData.value = emptyList()
        fetchCancelOrderReasons()
        viewModelScope.launch {
            combine(
                isOtherReasonSelected,
                cancelReasonComment
            ) { isOtherSelected, comment ->
                if (!isOtherSelected) {
                    comment.isNotEmpty()
                } else {
                    true
                }
            }.collect { isEnabled ->
                isContinueButtonEnabled.value = isEnabled
            }
        }
    }

    private fun fetchCancelOrderReasons() {
        unZipViewStateFlow(
            getCancelOrderReasonsUseCase(Unit),
            onSuccess = {
                val orderCancelReasons: List<CancelOrderReason> = it as List<CancelOrderReason>
                _reasonsData.value = orderCancelReasons
            }
        )
    }

    fun cancelOrder() {
        // Prevent multiple submissions
        if (isCancellationInProgress.value) {
            return
        }
        
        isCancellationInProgress.value = true
        
//        eventLogger.submitCancellation(
//            sharedPreferencesManager.getUserId(),
//            argsData.orderId,
//            _selectedReason.value?.reason.default(),
//            cancelReasonComment.value
//        )
        val cancelOrderReasonRequest = CancelOrderReasonRequest(
            orderId = argsData.orderId,
            cancelOrderReasonRequestBody = CancelOrderReasonRequestBody(
                commentType = _selectedReason.value?.reason.default(),
                commentDescription = cancelReasonComment.value
            )
        )

        unZipViewStateFlow(
            cancelOrderRequestUseCase(cancelOrderReasonRequest),
            onSuccess = {
                isOrderCancelled.value = true
            },
            onError = {
                // Reset loading state on error so user can try again
                isCancellationInProgress.value = false
            }
        )
    }

    fun updateSelectedReason(selectedReason: CancelOrderReason) {

        _selectedReason.value = selectedReason
        isOtherReasonSelected.value = selectedReason.reason != "Other"

        val isOtherSelected = isOtherReasonSelected.value
        val reasonComment = cancelReasonComment.value

        isCommentTextAreaVisible.value = !isOtherSelected
        isContinueButtonEnabled.value = if (!isOtherSelected) {
            reasonComment.isNotEmpty()
        } else {
            true
        }
    }

}