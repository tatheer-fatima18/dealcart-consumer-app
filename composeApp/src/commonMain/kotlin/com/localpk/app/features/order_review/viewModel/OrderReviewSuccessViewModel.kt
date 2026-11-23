package com.localpk.app.features.order_review.viewModel

import com.localpk.app.base.BaseViewModel
import com.localpk.app.base.NavigationEvent
import com.localpk.app.features.order_review.data.OrderReviewSuccessArgs
import com.localpk.app.utils.ZERO_FLOAT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderReviewSuccessViewModel(
) : BaseViewModel<OrderReviewSuccessState>() {

    private val _state = MutableStateFlow(OrderReviewSuccessState())
    val state: StateFlow<OrderReviewSuccessState> = _state.asStateFlow()

}

data class OrderReviewSuccessState(
    val rating: Float = ZERO_FLOAT,
)

