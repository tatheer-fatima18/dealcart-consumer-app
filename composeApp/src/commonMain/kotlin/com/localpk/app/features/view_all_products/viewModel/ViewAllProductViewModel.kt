package com.localpk.app.features.view_all_products.viewModel

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.cart.data.CartUiData
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.view_all_products.data.ViewAllProductArgs
import com.localpk.app.features.view_all_products.domain.ProductRequestData
import com.localpk.app.features.view_all_products.domain.ViewAllProductUseCase
import com.localpk.app.utils.FIRST_PAGE
import com.localpk.app.utils.VIEW_ALL_PRODUCT_LIMIT_MIN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ViewAllProductViewModel(
    private val args: ViewAllProductArgs,
    private val viewAllProductUseCase: ViewAllProductUseCase,
) : BaseViewModel<List<ProductUiData>>() {

    private val _viewAllProductList = MutableStateFlow<List<ProductUiData>>(emptyList())
    val viewAllProductList: StateFlow<List<ProductUiData>> = _viewAllProductList

    var currentPage = FIRST_PAGE
    val pageSize = VIEW_ALL_PRODUCT_LIMIT_MIN

    fun getViewALlProductItems(
        cart: CartUiData?,
        limit: Int, page: Int
    ) {
        val data = ProductRequestData(
            categoryId = args.categoryId,
            swimlaneId = args.swimlaneId,
            limit = limit,
            page = page,
            isPromotionalCategory = args.isPromotional
        )

        unZipViewStateFlow(
            viewAllProductUseCase(data),
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

                val currentProducts = _viewAllProductList.value
                val currentProductIds = currentProducts.map { it.id }

                val uniqueFrequentProducts =
                    it.filterNot { currentProductIds.contains(it.id) }

                if (uniqueFrequentProducts.isNotEmpty()) {
                    _viewAllProductList.value = currentProducts + uniqueFrequentProducts
                }
            },
            onError = { error ->
                //  toastManager.showSuccess(error)
            }
        )
    }
}