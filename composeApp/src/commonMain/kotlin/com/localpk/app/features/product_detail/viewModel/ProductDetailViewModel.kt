package com.localpk.app.features.product_detail.viewModel

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.product_detail.domain.ProductDetailUseCase
import com.localpk.app.features.productlist.data.ProductDetailUiData
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.productlist.data.Variant
import com.localpk.app.features.productlist.data.VariantDetail
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.isNotZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.localpk.app.common_components.GlobalProductDetailStateManager

class ProductDetailViewModel(
    private val args: ProductUiData,
    private val productDetailUseCase: ProductDetailUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel<ProductDetailUiData>() {

    private val _productDetailLiveData = MutableStateFlow<ProductDetailUiData?>(null)
    val productDetailLiveData: StateFlow<ProductDetailUiData?> =
        _productDetailLiveData.asStateFlow()

    private val _showDescription = MutableStateFlow(true)
    val showDescription: StateFlow<Boolean> = _showDescription.asStateFlow()

    private val _showShortDescription = MutableStateFlow(true)
    val showShortDescription: StateFlow<Boolean> = _showShortDescription.asStateFlow()

    val _actionButtonVisibility = MutableStateFlow(false)
    val actionButtonVisibility: StateFlow<Boolean> = _actionButtonVisibility.asStateFlow()

    private val _isProductRequestInProgress = MutableStateFlow(false)
    val isProductRequestInProgress: StateFlow<Boolean> = _isProductRequestInProgress.asStateFlow()

    private val _quantity = MutableStateFlow(0)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _selectedVariant = MutableStateFlow<Variant?>(null)
    val selectedVariant: StateFlow<Variant?> = _selectedVariant.asStateFlow()

    private val _selectedVariantDetails = MutableStateFlow<List<VariantDetail>>(emptyList())
    val selectedVariantDetails: StateFlow<List<VariantDetail>> =
        _selectedVariantDetails.asStateFlow()

    private val _isOutOfStock = MutableStateFlow(false)
    val isOutOfStock: StateFlow<Boolean> = _isOutOfStock.asStateFlow()

    private val _isDataLoading = MutableStateFlow(false)
    val isDataLoading: StateFlow<Boolean> = _isDataLoading.asStateFlow()


    init {
        _isProductRequestInProgress.value = false
        val initialDetail = args.toProductDetailUiData()
        _productDetailLiveData.value = initialDetail
        GlobalProductDetailStateManager.updateProductDetail(initialDetail)
        if (args.isFlashDeal.not()) {
            fetchProductDetails(args.id)
        }
        _isLoggedIn.value = sharedPreferencesManager.isLoggedIn()

//        if (argsData.isFromNotification) {
//            eventLogger.backInStockPn(argsData.id)
//        }

        if (args.itemQuantity != ZERO_INT) {
            _quantity.value = args.itemQuantity
        } else {
            _quantity.value = ZERO_INT
        }

    }

    fun updateProductDetail(productDetailUiData: ProductDetailUiData) {
        _productDetailLiveData.value = productDetailUiData
        GlobalProductDetailStateManager.updateProductDetail(productDetailUiData)
    }

    private fun fetchProductDetails(productId: Long) {
        _isDataLoading.value = true
        unZipViewStateFlow(
            viewStateFlow = productDetailUseCase(productId),
            onSuccess = {
                val data = it
                if (data.id.isNotZero() && data.message.isEmpty()) {
                    _productDetailLiveData.value = data
                    GlobalProductDetailStateManager.updateProductDetail(data)
                    _actionButtonVisibility.value = true
                    _isOutOfStock.value = data.inventoryQty <= ZERO_INT
                    _showShortDescription.value = data.showShortDescription()
                    //   eventLogger.productDetail(productDetailUiData = data)
                    _selectedVariantDetails.value =
                        data.selectedVariantDetails.map { selectedVariantDetail -> selectedVariantDetail.copy() }
                    _selectedVariant.value = data.selectedVariant
                    if (data.attributes.isNotNull()) {
                        val selectedFirstAttributeOption =
                            data.selectedVariantDetails.firstOrNull { variantDetail ->
                                data.attributes?.first()?.options?.any { it.id == variantDetail.optionId }
                                    .default()
                            }
                        if (selectedFirstAttributeOption != null) {
                            getAvailableOptions(
                                selectedFirstAttributeOption
                            )
                        }
                    }

                    _isProductRequestInProgress.value = true
                }
                _isDataLoading.value = false
//            else {
//                if (data.message == stringsResourceManager.getString(R.string.product_not_found)) {
//                    navigate(navigationProvider.getPopNavigator())
//                }
//            }
            }
        )
    }

    private fun getAvailableOptions(
        variantDetail: VariantDetail
    ) {
        productDetailLiveData.value?.let { productDetail ->
            val filterVariants = productDetail.variants?.filter { variant ->
                variant.details.any { it.optionId == variantDetail.optionId }
            }

            productDetail.attributes?.let { attributes ->
                if (attributes.isEmpty().not()) {
                    for (index in attributes.indices) {
                        val attribute = attributes[index]
                        for (option in attribute.options) {
                            if (attributes.size == 1) {
                                val variants = productDetail.variants?.filter { it.quantity <= 0 }
                                if (variants != null) {
                                    loop@ for (variant in variants) {
                                        option.isDisabled = variant.details.any {
                                            it.optionId == option.id && it.attributeId == attribute.id
                                        }
                                        if (variant.details.any { variantDetail ->
                                                variantDetail.optionId == option.id
                                            }) {
                                            break@loop
                                        }
                                    }
                                }
                            } else if (index > 0) {
                                val variants = filterVariants?.filter { it.quantity <= 0 }
                                if (variants != null) {
                                    loop@ for (variant in variants) {
                                        option.isDisabled = variant.details.any {
                                            it.optionId == option.id && it.attributeId == attribute.id
                                        }
                                        if (variant.details.any { variantDetail ->
                                                variantDetail.optionId == option.id
                                            }) {
                                            break@loop
                                        }
                                    }
                                }
                            }
                            option.isSelected =
                                selectedVariant.value?.details?.any { it.optionId == option.id } == true
                        }

                    }
                }
            }
        }
        _isOutOfStock.value = selectedVariant.value?.quantity.getValueOrDefault() <= ZERO_INT
    }

    fun getVariantProductId(variantDetails: List<VariantDetail>) {
        var productId: Long? = ZERO_LONG
        productDetailLiveData.value?.let {
            val item = it.variants?.firstOrNull { variant ->
                variant.details.containsAll(variantDetails)
            }
            productId = item?.productId
        }

        if (productId != ZERO_LONG) {
            fetchProductDetails(productId.getValueOrDefault())
        }
    }

    fun onActionAddQuantity() {
        if (args.isFlashDeal && quantity.value.getValueOrDefault() >= productDetailLiveData.value?.maxPurchasableQuantity.getValueOrDefault()) {
//            showFavouritesSuccess(
//                R.drawable.ic_dc_sad_orange,
//                stringsResourceManager.getString(R.string.deal_product_limit_message)
//            )
        } else {
            _quantity.value = (quantity.value.getValueOrDefault() + 1)
        }
    }

    fun onActionRemoveQuantity() {
        if (quantity.value.getValueOrDefault() > 1)
            _quantity.value = (quantity.value.getValueOrDefault() - 1)
        else {
            _quantity.value = ZERO_INT
        }
    }

    override fun onCleared() {
        super.onCleared()
        GlobalProductDetailStateManager.clear()
    }

}