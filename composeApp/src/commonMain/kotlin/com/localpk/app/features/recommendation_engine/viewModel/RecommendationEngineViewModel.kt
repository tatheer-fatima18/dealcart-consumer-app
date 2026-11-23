package com.localpk.app.features.recommendation_engine.viewModel

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.cart.data.CartUiData
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.productlist.data.SwimLaneType
import com.localpk.app.features.recommendation_engine.data.RecommendationEngineProductsRequest
import com.localpk.app.features.recommendation_engine.data.RecommendationEngineRequest
import com.localpk.app.features.recommendation_engine.data.SearchPastOrderResponse
import com.localpk.app.features.recommendation_engine.domain.RecommendationEngineProductsUseCase
import com.localpk.app.features.recommendation_engine.domain.RecommendationEngineSearchUseCase
import com.localpk.app.features.recommendation_engine.domain.RecommendationEngineUseCase
import com.localpk.app.utils.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecommendationEngineViewModel(
    private val recommendationEngineUseCase: RecommendationEngineUseCase,
    private val recommendationEngineSearchUseCase: RecommendationEngineSearchUseCase,
    private val recommendationEngineProductsUseCase: RecommendationEngineProductsUseCase
) : BaseViewModel<Any>() {

    private val _recommendationList = MutableStateFlow<List<ProductUiData>>(emptyList())
    val recommendationList: StateFlow<List<ProductUiData>> = _recommendationList.asStateFlow()

    private val _recommendedTitle = MutableStateFlow(EMPTY_STRING)
    val recommendedTitle: StateFlow<String> = _recommendedTitle.asStateFlow()

    private val _onRecommendedActionSuccess = MutableStateFlow(false)
    val onRecommendedActionSuccess: StateFlow<Boolean> = _onRecommendedActionSuccess.asStateFlow()

    init {
        _recommendationList.value = arrayListOf()
    }

    fun getRecommendationItems(
        apiText: String,
        type: String,
        productId: Number,
        cart: CartUiData?
    ) {
        unZipViewStateFlow(
            viewStateFlow = recommendationEngineUseCase(
                RecommendationEngineRequest(
                    swimlane = apiText,
                    screenType = type
                )
            ),
            onSuccess = {
                val recommendedItems = it as List<String>
                val productIds = recommendedItems.joinToString(separator = ",")
                if (productIds.isNotEmpty())
                    getRecommendedProducts(
                        productIds,
                        type,
                        productId,
                        cart
                    )
            }
        )
    }

    private fun getRecommendedProducts(
        query: String,
        type: String,
        productId: Number,
        cart: CartUiData?
    ) {
        unZipViewStateFlow(
            recommendationEngineProductsUseCase(
                RecommendationEngineProductsRequest(
                    query,
                    type,
                    productId
                )
            ),
            onSuccess = { detail ->
                val products = detail as List<ProductUiData>

                cart?.dcCityCart?.cartDetails?.forEach { cartItem ->
                    products.find { it.id == cartItem.productId }?.itemQuantity =
                        cartItem.quantity
                }
                cart?.multiCityCart?.cartDetails?.forEach { cartItem ->
                    products.find { it.id == cartItem.productId }?.itemQuantity =
                        cartItem.quantity
                }
                cart?.sellerCarts?.forEach { carts ->
                    carts.cartDetails.forEach { cartItem ->
                        products.find {
                            it.id == cartItem.productId
                        }?.itemQuantity =
                            cartItem.quantity
                    }
                }

                if (type != SwimLaneType.NEWLY_ADDED.status) {
                    _recommendationList.value = products
                }
                _onRecommendedActionSuccess.value = true
            },
            onError =
            {
                if (type != SwimLaneType.NEWLY_ADDED.status) {
                    _recommendationList.value = emptyList()
                }
            }
        )
    }

    fun pastOrderItems(cart: CartUiData?) {
        unZipViewStateFlow(recommendationEngineSearchUseCase(Unit), onSuccess = { response ->
            val data = response as SearchPastOrderResponse
            data.body.pastPurchaseItems?.let {
                if (it.isNotEmpty()) {
                    getRecommendedProducts(
                        it.joinToString(separator = ","),
                        EMPTY_STRING,
                        0,
                        cart
                    )
                    _recommendedTitle.value = "Past Orders"
                }
            }
            data.body.items?.let {
                if (it.isNotEmpty()) {
                    getRecommendedProducts(
                        it.joinToString(separator = ","),
                        EMPTY_STRING,
                        0,
                        cart
                    )
                    _recommendedTitle.value = "Tajweez Karda Items"
                }
            }
            data.body.recommendedItems?.let {
                if (it.isNotEmpty()) {
                    getRecommendedProducts(
                        it.joinToString(separator = ","),
                        EMPTY_STRING,
                        0,
                        cart
                    )
                    _recommendedTitle.value = "Tajweez Karda Items"
                }
            }
        })
    }

    companion object {
        const val RECOMMEND_FOR_YOU = "for-you"
        const val RECOMMEND_BEST_SELLER = "best-seller"
    }


}