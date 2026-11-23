package com.localpk.app.features.order_review.viewModel

import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.order_review.data.AddOrderReviewRequest
import com.localpk.app.features.order_review.data.CategoryUIData
import com.localpk.app.features.order_review.data.OrderReviewCategoryArgs
import com.localpk.app.features.order_review.data.OrderReviewCategoryReasonUIData
import com.localpk.app.features.order_review.data.OrderReviewCategoryUIData
import com.localpk.app.features.order_review.data.OrderReviewSuccessArgs
import com.localpk.app.features.order_review.data.RatingLevel
import com.localpk.app.features.order_review.data.RatingType
import com.localpk.app.features.order_review.domain.AddOrderReviewUseCase
import com.localpk.app.features.order_review.domain.GetOrderReviewCategoriesUseCase
import com.localpk.app.features.order_review.presentation.OrderReviewSuccessBottomSheet
import com.localpk.app.utils.BIT_DELAY
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO_FLOAT
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.getValueOrDefault
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrderReviewCategoriesListingViewModel(
    private val getOrderReviewCategoriesUseCase: GetOrderReviewCategoriesUseCase,
    private val addOrderReviewUseCase: AddOrderReviewUseCase
) : BaseViewModel<Any>() {

    private val _state = MutableStateFlow(OrderReviewCategoriesListingState())
    val state: StateFlow<OrderReviewCategoriesListingState> = _state

    private val _argsData = MutableStateFlow<OrderReviewCategoryArgs?>(null)
    val argsData: StateFlow<OrderReviewCategoryArgs?> = _argsData.asStateFlow()


    private fun fetchCategories(screenRatingType: String) {
        launch {
            unZipViewStateFlow(
                getOrderReviewCategoriesUseCase(screenRatingType),
                onSuccess = { items ->
                    val categories: List<CategoryUIData> = items as List<CategoryUIData>
                    _state.update {
                        it.copy(
                            categoriesLiveData = categories,
                            categoriesData = categories.map { category ->
                                OrderReviewCategoryUIData(
                                    id = category.id,
                                    name = category.name,
                                    image = category.image.full
                                )
                            }
                        )
                    }
                },
                onError = { error ->
                    // Handle error silently or show appropriate message
                }
            )
        }
    }

    private fun updateReviewText(rating: Float) {
        val reviewText = when (rating.toInt()) {
            5 -> RatingLevel.EXCELLENT.value
            4 -> RatingLevel.GOOD.value
            3 -> RatingLevel.NEUTRAL.value
            2 -> RatingLevel.BAD.value
            else -> RatingLevel.WORST.value
        }
        _state.update { it.copy(reviewText = reviewText) }
    }

    fun onActionClickCategory(id: Long) {
        val categories = state.value.categoriesLiveData
        val selectedCategory = categories?.find { it.id == id }

        selectedCategory?.let { category ->
            val reasons = category.reasons.map { reason ->
                OrderReviewCategoryReasonUIData(reason.id, reason.name)
            }.toMutableList().apply {
                add(
                    OrderReviewCategoryReasonUIData(
                        id = ZERO_LONG,
                        name = "Other"
                    )
                )
            }

            _state.update {
                it.copy(
                    selectedCategoryName = category.name,
                    reasonsData = reasons,
                    isCategorySelected = true
                )
            }
        }
    }

    fun onActionContinue(navigator: Navigator, bottomSheetNavigator: BottomSheetNavigator) {
        val selectedReasonIds = state.value.reasonsData
            ?.filter { it.isSelected && it.id != ZERO_LONG }
            ?.map { it.id }

        val orderReviewRequest = AddOrderReviewRequest(
            state.value.orderId.getValueOrDefault(),
            state.value.shipmentId.getValueOrDefault(),
            state.value.rating.toLong(),
            state.value.reasonComment,
            selectedReasonIds.orEmpty()
        )

        addOrderReview(orderReviewRequest, navigator, bottomSheetNavigator)
    }

    private fun addOrderReview(request: AddOrderReviewRequest, navigator: Navigator, bottomSheetNavigator: BottomSheetNavigator ) {
            unZipViewStateFlow(
                addOrderReviewUseCase(request),
                onSuccess = {
                    launch {
                        navigator.pop()
                        delay(BIT_DELAY)
                        //    argsData.successListener.invoke()
                        bottomSheetNavigator.show(OrderReviewSuccessBottomSheet(
                            orderId = argsData.value?.id.getValueOrDefault(),
                            rating = state.value.rating.toInt()
                        ))
                    }
                },
                onError = { error ->
                    // TODO: Show error toast
                    // toastManager.showError(error)
                }
            )
    }

    fun onActionOtherReasonClick() {
        val currentIsOtherReasonSelected = state.value.isOtherReasonSelected
        _state.update { it.copy(isOtherReasonSelected = !currentIsOtherReasonSelected) }
        onActionReasonCheckBoxClick()
    }

    fun onActionReasonCheckBoxClick() {
        val selectedReasonIds = state.value.reasonsData
            ?.filter { it.isSelected }
            ?.map { it.id }

        val isOtherReasonSelected = state.value.isOtherReasonSelected
        val reasonComment = state.value.reasonComment

        val isContinueButtonEnabled = if (isOtherReasonSelected) {
            reasonComment.isNotEmpty()
        } else {
            selectedReasonIds?.isNotEmpty() == true
        }

        _state.update { it.copy(isContinueButtonEnabled = isContinueButtonEnabled) }
    }

    fun onReasonSelected(reasonId: Long, isSelected: Boolean) {
        val updatedReasons = state.value.reasonsData?.map { reason ->
            if (reason.id == reasonId) {
                reason.copy(isSelected = isSelected)
            } else {
                reason
            }
        }

        _state.update { it.copy(reasonsData = updatedReasons) }
        onActionReasonCheckBoxClick()
    }

    fun onReasonCommentChanged(comment: String) {
        _state.update { it.copy(reasonComment = comment) }
        onActionReasonCheckBoxClick()
    }

    fun updateIsCategorySelected(isCategorySelected: Boolean) {
        _state.update { it.copy(isCategorySelected = isCategorySelected) }
    }

    fun updateArgsAndState(args: OrderReviewCategoryArgs) {
        _argsData.value = args
        _state.update {
            it.copy(
                rating = args.rating.toFloat(),
                orderId = args.id,
                shipmentId = args.shipmentId
            )
        }

        val screenRatingType = if (args.rating == 4) {
            RatingType.SCREEN_4_RATING.value
        } else {
            RatingType.SCREEN_1_3_RATING.value
        }

        fetchCategories(screenRatingType)
        updateReviewText(args.rating.toFloat())
    }

    private fun navigateToOrderReviewSuccessBottomSheet(orderReviewArgs: OrderReviewSuccessArgs) {
        // TODO: Implement navigation
    }
}

data class OrderReviewCategoriesListingState(
    val categoriesLiveData: List<CategoryUIData>? = null,
    val categoriesData: List<OrderReviewCategoryUIData> = emptyList(),
    val reasonsData: List<OrderReviewCategoryReasonUIData>? = null,
    val rating: Float = ZERO_FLOAT,
    val orderId: Long = ZERO_LONG,
    val shipmentId: Long = ZERO_LONG,
    val selectedCategoryName: String = EMPTY_STRING,
    val isCategorySelected: Boolean = false,
    val reasonComment: String = EMPTY_STRING,
    val reviewText: String = EMPTY_STRING,
    val isOtherReasonSelected: Boolean = false,
    val isContinueButtonEnabled: Boolean = false
) 