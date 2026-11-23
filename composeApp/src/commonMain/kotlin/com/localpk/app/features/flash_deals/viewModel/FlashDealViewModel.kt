package com.localpk.app.features.flash_deals.viewModel

import androidx.lifecycle.viewModelScope
import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.cart.data.CartUiData
import com.localpk.app.features.dynamicLinksService.data.DynamicLinkRequestFlashDeal
import com.localpk.app.features.dynamicLinksService.data.DynamicLinkResponse
import com.localpk.app.features.dynamicLinksService.domain.DynamicLinkFlashDealUseCase
import com.localpk.app.features.flash_deals.data.FlashDealUiData
import com.localpk.app.features.flash_deals.domain.FlashDealDetailUseCase
import com.localpk.app.features.flash_deals.domain.FlashDealsUseCase
import com.localpk.app.features.productlist.data.ProductDetailUiData
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.isNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


class FlashDealViewModel(
    private val flashDealsUseCase: FlashDealsUseCase,
    private val flashDealDetailUseCase: FlashDealDetailUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val dynamicLinkFlashDealUseCase: DynamicLinkFlashDealUseCase
) : BaseViewModel<Any>() {

    private val _flashDeals = MutableStateFlow<List<FlashDealUiData>>(emptyList())
    val flashDeals: StateFlow<List<FlashDealUiData>> = _flashDeals.asStateFlow()

    private val _flashDealsDetail = MutableStateFlow<ProductDetailUiData?>(null)
    val flashDealsDetail: StateFlow<ProductDetailUiData?> = _flashDealsDetail.asStateFlow()

    // For one-time events (SingleLiveEvent equivalent)
    private val _actionError = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val actionError: SharedFlow<Unit> = _actionError

    private val _actionSuccess = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val actionSuccess: SharedFlow<Unit> = _actionSuccess

    // For boolean UI state
    private val _actionFlashDealRequestInProgress = MutableStateFlow(false)
    val actionFlashDealRequestInProgress: StateFlow<Boolean> = _actionFlashDealRequestInProgress

    private val _dynamicLink = MutableStateFlow(EMPTY_STRING)
    val dynamicLink: StateFlow<String> = _dynamicLink


    fun updateFlashDealDetails(flashDealDetail: ProductDetailUiData?) {
        _flashDealsDetail.value = flashDealDetail
    }

    fun fetchFlashDeals(
        status: String,
        cart: CartUiData?
    ) {
        if (flashDeals.value.isEmpty())
            unZipViewStateFlow(flashDealsUseCase(status), onSuccess = { it ->
                val response = it as List<FlashDealUiData>

                cart?.dcCityCart?.cartDetails?.forEach { cartItem ->
                    response.find {
                        it.productId == cartItem.productId
                    }?.apply {
                        itemQuantity = cartItem.quantity
                    }
                }

                cart?.multiCityCart?.cartDetails?.forEach { cartItem ->
                    response.find {
                        it.productId == cartItem.productId
                    }?.apply {
                        itemQuantity = cartItem.quantity
                    }
                }

                cart?.sellerCarts?.forEach { carts ->
                    carts.cartDetails.forEach { cartItem ->
                        response.find {
                            it.productId == cartItem.productId
                        }?.apply {
                            itemQuantity = cartItem.quantity
                        }
                    }
                }

                _flashDeals.value = response
                viewModelScope.launch {
                    _actionSuccess.emit(Unit)
                }
            }, onError = {})
    }

    fun fetchFlashDealsDetail(id: Long, onCallback: (ProductDetailUiData) -> Unit = {}) {
        if (flashDealsDetail.value.isNull())
            unZipViewStateFlow(
                flashDealDetailUseCase(id),
                onSuccess = {
                    val response = it as ProductDetailUiData
                    _flashDealsDetail.value = response
                    _actionFlashDealRequestInProgress.value = true
                    onCallback.invoke(response)
                },
                onError = {
                    viewModelScope.launch {
                        _actionError.emit(Unit)
                    }
                    _actionFlashDealRequestInProgress.value = false

                    //Optional fallback UI handler
//                if (it.message == "Maazrat, flash deals ab available nahi. App ko band karke dobara open karein!" && sharedPreferencesManager.getFlashDeal() == EMPTY_STRING) {
//                    showFavouritesSuccess(R.drawable.ic_dc_sad_orange, it)
//                }
                }
            )
    }

    fun fetchFlashShareLink(
        productId: Long,
        dealId: Long,
        productImage: String,
        userId: Long
    ) {
        val requestBody = DynamicLinkRequestFlashDeal(productId, dealId, productImage, userId)
        unZipViewStateFlow(
            dynamicLinkFlashDealUseCase(requestBody),
            onSuccess = {
                val response = it as DynamicLinkResponse
                _dynamicLink.value = response.body.shortLink
            },
            onError = {}
        )
    }

    fun clearDynamicLink() {
        _dynamicLink.value = EMPTY_STRING
    }
}