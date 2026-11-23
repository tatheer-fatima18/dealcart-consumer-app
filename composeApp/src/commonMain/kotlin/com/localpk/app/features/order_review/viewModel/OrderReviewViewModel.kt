package com.localpk.app.features.order_review.viewModel

import com.localpk.app.base.BaseViewModel
import com.localpk.app.base.ViewState
import com.localpk.app.features.order_review.data.AddOrderReviewRequest
import com.localpk.app.features.order_review.data.OrderReviewActionContinueArgs
import com.localpk.app.features.order_review.data.OrderReviewBottomSheetUIData
import com.localpk.app.features.order_review.data.RatingLevel
import com.localpk.app.features.order_review.domain.AddOrderReviewUseCase
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO_FLOAT
import com.localpk.app.utils.getValueOrDefault
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrderReviewViewModel(
    private val addOrderReviewUseCase: AddOrderReviewUseCase,
) : BaseViewModel<Any>() {

    private val _state = MutableStateFlow(OrderReviewState())
    val state: StateFlow<OrderReviewState> = _state

    private val _argsData = MutableStateFlow<OrderReviewBottomSheetUIData?>(null)
    val argsData: StateFlow<OrderReviewBottomSheetUIData?> = _argsData.asStateFlow()


    private fun observeRating() {
        val currentRating = state.value.rating
        updateReviewText(currentRating)
    }

    fun onRatingChanged(newRating: Float) {
        _state.update { it.copy(rating = newRating) }
        updateReviewText(newRating)
    }

    private fun updateReviewText(rating: Float) {
        val reviewText = when (rating.toInt()) {
            5 -> {
                _state.update { it.copy(isRatingUnderFive = false) }
                RatingLevel.EXCELLENT.value
            }
            4 -> {
                _state.update { it.copy(isRatingUnderFive = true) }
                RatingLevel.GOOD.value
            }
            3 -> {
                _state.update { it.copy(isRatingUnderFive = true) }
                RatingLevel.NEUTRAL.value
            }
            2 -> {
                _state.update { it.copy(isRatingUnderFive = true) }
                RatingLevel.BAD.value
            }
            0 -> {
                _state.update { it.copy(isRatingUnderFive = false) }
                RatingLevel.WORST.value
            }
            else -> {
                _state.update { it.copy(isRatingUnderFive = true) }
                RatingLevel.WORST.value
            }
        }
        
        _state.update { it.copy(reviewText = reviewText) }
    }


    fun onContinueClick(
        navigateToOrderReviewCategoriesListingScreen: () -> Unit = {},
        navigateToOrderReviewSuccessBottomSheet: () -> Unit = {},
    ) {
        val currentRating = state.value.rating
        //  argsData.continueListener(currentRating.toInt())
        val argsData = argsData.value
        if (argsData == null) {
            return
        }
        val args = OrderReviewActionContinueArgs(
            salesOrderId = argsData.salesOrderId,
            shipmentId = argsData.shipmentId.getValueOrDefault(),
            rating = currentRating.toInt()
        )
        onActionContinue(
            args,
            navigateToOrderReviewCategoriesListingScreen,
            navigateToOrderReviewSuccessBottomSheet,
        )
    }

    fun onCloseClick() {
        //  argsData.closeListener?.invoke()
    }

    fun onActionContinue(
        args: OrderReviewActionContinueArgs,
        navigateToOrderReviewCategoriesListingScreen: () -> Unit = {},
        navigateToOrderReviewSuccessBottomSheet: () -> Unit = {},
    ) {
        if (args.rating == 5) {
            val orderReviewRequest = AddOrderReviewRequest(
                args.salesOrderId,
                args.shipmentId,
                args.rating.toLong(),
                EMPTY_STRING,
                emptyList()
            )
            onFiverStarReview(
                orderReviewRequest,
                navigateToOrderReviewSuccessBottomSheet,
            )

        } else {
            navigateToOrderReviewCategoriesListingScreen()
        }
    }


    private fun onFiverStarReview(
        orderReviewRequest: AddOrderReviewRequest,
        navigateToOrderReviewSuccessBottomSheet: () -> Unit = {},
    ) {
        launch {
            addOrderReviewUseCase(orderReviewRequest).collect { viewState ->
                when (viewState) {
                    is ViewState.Success -> {
                        navigateToOrderReviewSuccessBottomSheet()
                    }

                    is ViewState.Error -> {}
                    is ViewState.Loading -> {}
                    is ViewState.Empty -> {}
                }
            }
        }
    }

    fun updateArgsData(data: OrderReviewBottomSheetUIData) {
        _argsData.value = data
        _state.update {
            it.copy(
                orderData = data,
                rating = ZERO_FLOAT,
                reviewText = EMPTY_STRING,
                isRatingUnderFive = false
            )
        }

        // Set up rating observer
        observeRating()
    }
}

data class OrderReviewState(
    val orderData: OrderReviewBottomSheetUIData? = null,
    val rating: Float = ZERO_FLOAT,
    val reviewText: String = EMPTY_STRING,
    val isRatingUnderFive: Boolean = false
) 