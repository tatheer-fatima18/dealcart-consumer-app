package com.localpk.app.features.frequently_ordered.viewModel

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.cart.data.CartUiData
import com.localpk.app.features.frequently_ordered.domain.FrequentlyOrderedUseCase
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.restaurant.domain.RequestData
import com.localpk.app.utils.FIRST_PAGE
import com.localpk.app.utils.PRODUCT_LIMIT_MIN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FrequentlyOrderedViewModel(
    private val frequentlyOrderedUseCase: FrequentlyOrderedUseCase
) : BaseViewModel<List<ProductUiData>>() {

    private val _frequentlyOrderedList = MutableStateFlow<List<ProductUiData>>(emptyList())
    val frequentlyOrderedList: StateFlow<List<ProductUiData>> = _frequentlyOrderedList

    var currentPage = FIRST_PAGE
    val pageSize = PRODUCT_LIMIT_MIN

    fun getFrequentlyOrderedItems(
        cart: CartUiData?,
        limit: Int,
        page: Int
    ) {
        val data = RequestData(
            limit = limit,
            page = page
        )

        unZipViewStateFlow(
            frequentlyOrderedUseCase(data),
            onSuccess = { it ->

                cart?.dcCityCart?.cartDetails?.forEach { cartItem ->
                    it.find {
                        it.id == cartItem.productId
                    }?.apply {
                        itemQuantity = cartItem.quantity
                    }
                }

                cart?.multiCityCart?.cartDetails?.forEach { cartItem ->
                    it.find {
                        it.id == cartItem.productId
                    }?.apply {
                        itemQuantity = cartItem.quantity
                    }
                }

                cart?.sellerCarts?.forEach { carts ->
                    carts.cartDetails.forEach { cartItem ->
                        it.find {
                            it.id == cartItem.productId
                        }?.apply {
                            itemQuantity = cartItem.quantity
                        }
                    }
                }

                val currentProducts = _frequentlyOrderedList.value
                val currentProductIds = currentProducts.map { it.id }

                val uniqueFrequentProducts =
                    it.filterNot { currentProductIds.contains(it.id) }

                if (uniqueFrequentProducts.isNotEmpty()) {
                    _frequentlyOrderedList.value = currentProducts + uniqueFrequentProducts
                }
            },
            onError = { error ->
                // toastManager.showSuccess(error)
            }
        )
    }

    fun emptyFrequentlyOrderedList() {
        _frequentlyOrderedList.value = emptyList()
    }
}