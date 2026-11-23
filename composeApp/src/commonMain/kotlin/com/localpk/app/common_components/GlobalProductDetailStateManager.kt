package com.localpk.app.common_components

import com.localpk.app.features.productlist.data.ProductDetailUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object GlobalProductDetailStateManager {
    private val _productDetailState = MutableStateFlow<ProductDetailUiData?>(null)
    val productDetailState: StateFlow<ProductDetailUiData?> = _productDetailState.asStateFlow()

    fun updateProductDetail(productDetailUiData: ProductDetailUiData?) {
        _productDetailState.value = productDetailUiData
    }

    fun clear() {
        _productDetailState.value = null
    }
}

