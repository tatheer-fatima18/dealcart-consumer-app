package com.localpk.app.features.cart.viewModel

import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.localpk.app.base.BaseViewModel
import com.localpk.app.base.ViewState
import com.localpk.app.common_components.GlobalDialogManager
import com.localpk.app.common_components.GlobalToastManager
import com.localpk.app.common_components.SnackbarType
import com.localpk.app.features.address.data.AddressUiData
import com.localpk.app.features.bottomsheet.data.GeneralBottomSheetUiData
import com.localpk.app.features.bottomsheet.view.GeneralBottomSheetScreen
import com.localpk.app.features.cart.data.AddToCartRequest
import com.localpk.app.features.cart.data.CartBundle
import com.localpk.app.features.cart.data.CartBundleResponse
import com.localpk.app.features.cart.data.CartCheckoutArgs
import com.localpk.app.features.cart.data.CartRequest
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.features.cart.data.CartUiData
import com.localpk.app.features.cart.data.CartUiModel
import com.localpk.app.features.cart.data.LocalCartItem
import com.localpk.app.features.cart.data.OOSBottomSheetArgs
import com.localpk.app.features.cart.data.ProductInfo
import com.localpk.app.features.cart.data.ProductOOSDetails
import com.localpk.app.features.cart.data.PromoCodeRequest
import com.localpk.app.features.cart.data.PromoNavigationArgs
import com.localpk.app.features.cart.data.cartUiData
import com.localpk.app.features.cart.domain.AddToCartUseCase
import com.localpk.app.features.cart.domain.ApplyPromoCodeUseCase
import com.localpk.app.features.cart.domain.BundleSwitchUseCase
import com.localpk.app.features.cart.domain.ClearCartUseCase
import com.localpk.app.features.cart.domain.DecreaseBundleQuantityUseCase
import com.localpk.app.features.cart.domain.DecreaseItemQuantityUseCase
import com.localpk.app.features.cart.domain.FetchCartUseCase
import com.localpk.app.features.cart.domain.IncreaseBundleQuantityUseCase
import com.localpk.app.features.cart.domain.IncreaseItemQuantityUseCase
import com.localpk.app.features.cart.domain.IsFoodCartUseCase
import com.localpk.app.features.cart.domain.RemoveCartUseCase
import com.localpk.app.features.cart.domain.ValidateBundleUseCase
import com.localpk.app.features.cart.view.CartMovBottomSheetScreen
import com.localpk.app.features.orders.data.GlobalEvents
import com.localpk.app.features.product_detail.data.CartProductType
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.promo.view.PromoListingBottomSheetScreen
import com.localpk.app.features.restaurant.data.AddItemRequestBody
import com.localpk.app.features.restaurant.domain.AddFoodItemToCartUseCase
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.network.GeneralStringResponse
import com.localpk.app.utils.BIT_DELAY
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.default
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getDeliveryDate
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNullOrEmpty
import com.localpk.app.utils.isNotZero
import com.localpk.app.utils.isNull
import com.localpk.app.utils.orEmpty
import com.localpk.app.utils.orEmptyArrayList
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.disclaimer
import dealcart.composeapp.generated.resources.message_keep_nic_with_you
import dealcart.composeapp.generated.resources.message_terms_condition_acceptance
import dealcart.composeapp.generated.resources.no
import dealcart.composeapp.generated.resources.title_keep_nic_with_you
import dealcart.composeapp.generated.resources.yes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class CartViewModel(
    private val increaseItemQuantityUseCase: IncreaseItemQuantityUseCase,
    private val decreaseItemQuantityUseCase: DecreaseItemQuantityUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val fetchCartUseCase: FetchCartUseCase,
    private val removeCartUseCase: RemoveCartUseCase,
    private val increaseBundleQuantityUseCase: IncreaseBundleQuantityUseCase,
    private val bundleSwitchUseCase: BundleSwitchUseCase,
    private val decreaseBundleQuantityUseCase: DecreaseBundleQuantityUseCase,
    private val validateBundleUseCase: ValidateBundleUseCase,
    private val promoCodeUseCase: ApplyPromoCodeUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val isFoodCartUseCase: IsFoodCartUseCase,
    private val foodCartViewModel: FoodCartViewModel,
    private val clearCartUseCase: ClearCartUseCase,
    private val addFoodItemToCartUseCase: AddFoodItemToCartUseCase,
    private val eventLogger: EventLogger
) : BaseViewModel<Any>() {

    // Local cart items state - single source of truth
    private val _localCartItems = MutableStateFlow<Map<Long, LocalCartItem>>(emptyMap())
    val localCartItems: StateFlow<Map<Long, LocalCartItem>> = _localCartItems.asStateFlow()

    val _isCartLoading = MutableStateFlow(false)
    val isCartLoading: StateFlow<Boolean> = _isCartLoading.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(sharedPreferencesManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isUpdatingQuantity = MutableStateFlow(false)
    val isUpdatingQuantity: StateFlow<Boolean> = _isUpdatingQuantity.asStateFlow()

    // Cart type state
    private val _isCartFoodType = MutableStateFlow(false)
    val isCartFoodType: StateFlow<Boolean> = _isCartFoodType

    private val _addresses = MutableStateFlow<List<AddressUiData>>(emptyList())
    val addresses: StateFlow<List<AddressUiData>> = _addresses

    private val _cartState = MutableStateFlow<CartUiData?>(null)
    val cartState: StateFlow<CartUiData?> = _cartState

    val _checkoutData = MutableStateFlow<CartCheckoutArgs?>(null)
    val checkoutData: StateFlow<CartCheckoutArgs?> = _checkoutData

    private val _isDiscountSwitchChecked = MutableStateFlow(false)
    val isDiscountSwitchChecked: StateFlow<Boolean> = _isDiscountSwitchChecked

    private val _dcCoinDiscount = MutableStateFlow(0.0)
    val dcCoinDiscount: StateFlow<Double> = _dcCoinDiscount

    private val _parentCategories = MutableStateFlow(EMPTY_STRING)
    val parentCategories: StateFlow<String> = _parentCategories

    private val _discountedCartTotal = MutableStateFlow(0.0)
    private val _overallCartAndCoinDiscount = MutableStateFlow(0.0)

    private val _formattedOverallCartAndCoinDiscount = MutableStateFlow(EMPTY_STRING)
    val formattedOverallCartAndCoinDiscount: StateFlow<String> =
        _formattedOverallCartAndCoinDiscount

    private val _formattedDiscountedCartTotal = MutableStateFlow(EMPTY_STRING)
    val formattedDiscountedCartTotal: StateFlow<String> = _formattedDiscountedCartTotal

    private val _formattedDcCoinDiscount = MutableStateFlow(EMPTY_STRING)
    val formattedDcCoinDiscount: StateFlow<String> = _formattedDcCoinDiscount

    // Bundle logic
    val bundleQuantity = MutableStateFlow(1)
    val showBundleDialog = MutableStateFlow(false)

    private val _bundleDetails = MutableStateFlow<CartBundle?>(null)
    val bundleDetails: StateFlow<CartBundle?> = _bundleDetails

    val dcCoinWarningTrigger = MutableStateFlow(false)
    val dcCoinErrorMessage = MutableStateFlow(EMPTY_STRING)
    val deliveryDate = MutableStateFlow(EMPTY_STRING)

    private val _selectedDeliveryType = MutableStateFlow(DeliveryType.STANDARD)
    val selectedDeliveryType: StateFlow<DeliveryType> = _selectedDeliveryType


    val isLoader = MutableStateFlow(false)
    val isGetCartDetailsFailed = MutableStateFlow(false)

    // Fee info
    val packagingFee = MutableStateFlow(ZERO_INT)
    val platformFee = MutableStateFlow(ZERO_INT)

    val useDCCoinsAmountLimit = 500
    private val detailed = true

    // Callback for when screen becomes active (e.g., after login)
    private val _onScreenResume = MutableStateFlow<(() -> Unit)?>(null)
    val onScreenResume: StateFlow<(() -> Unit)?> = _onScreenResume.asStateFlow()

    val _promoNavigationArgs = MutableStateFlow<PromoNavigationArgs?>(null)
    val promoNavigationArgs: StateFlow<PromoNavigationArgs?> = _promoNavigationArgs

    private var pendingAddToCartItem: ProductUiData? = null
    private var pendingAddFoodRequest: AddItemRequestBody? = null
    private var pendingAddType: PendingAddType? = null

    enum class PendingAddType { REGULAR, FOOD }

    val updateOOsState = MutableStateFlow(false)

    private val _argsOOSBottomSheet = MutableStateFlow<OOSBottomSheetArgs?>(null)
    val argsOOSBottomSheet: StateFlow<OOSBottomSheetArgs?> = _argsOOSBottomSheet

    private val _addressValidationBottomSheetListener = MutableStateFlow<(() -> Unit)?>(null)
    val addressValidationBottomSheetListener: StateFlow<(() -> Unit)?> = _addressValidationBottomSheetListener.asStateFlow()


    // Set callback to be executed when screen becomes active
    fun setOnScreenResumeCallback(callback: () -> Unit) {
        _onScreenResume.value = callback
    }

    // Execute the callback and clear it
    fun executeOnScreenResumeCallback() {
        _onScreenResume.value?.invoke()
        _onScreenResume.value = null
    }

    fun executeCheckoutAfterCartLoad(
        bottomSheetNavigator: BottomSheetNavigator,
        navigateToCheckout: () -> Unit = {},
        cartMovConfirmListener: () -> Unit = {}
    ) {
        viewModelScope.launch {
            while (_isCartLoading.value) {
                delay(100)
            }
            executeOnScreenResumeCallback()
            onCheckOutClick(bottomSheetNavigator, navigateToCheckout, cartMovConfirmListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        pendingAddToCartItem = null
        pendingAddFoodRequest = null
        pendingAddType = null
    }

    init {
        _isLoggedIn.value = sharedPreferencesManager.isLoggedIn()
        deliveryDate.value = getDeliveryDate()
        initializeDeliveryType()
        checkCartType()
        //eventLogger.orderConfirmScreen()
    }

    private fun initializeDeliveryType() {
        _selectedDeliveryType.value = DeliveryType.STANDARD
    }

    fun updateDeliveryType(deliveryType: DeliveryType) {
        val isExpressRequested = deliveryType == DeliveryType.EXPRESS
        val isSameDayEligible = cartState.value?.isSameDeliveryEligible == true
        val coercedType =
            if (isExpressRequested && isSameDayEligible.not()) DeliveryType.STANDARD else deliveryType
        _selectedDeliveryType.value = coercedType
        // Recalculate formatted total with new delivery type
        cartState.value?.let { cart ->
            _formattedDiscountedCartTotal.value = (_discountedCartTotal.value.getValueOrDefault()
                .toInt() + packagingFee.value.getValueOrDefault() + platformFee.value.getValueOrDefault() + if (coercedType == DeliveryType.EXPRESS) cart.sameDayDeliveryFee.getValueOrDefault()
                .toInt() else 0).toDouble().formatPKRCurrency()
        }
    }

    fun checkCartType(successListenerCallback: () -> Unit = {}) {
        _isCartLoading.value = true
        if (sharedPreferencesManager.isLoggedIn()) {
            viewModelScope.launch {
                isFoodCartUseCase(Unit).collect { state ->
                    when (state) {
                        is ViewState.Success -> {
                            _isCartFoodType.value = state.data
                            if (state.data) {
                                foodCartViewModel.getFoodCart {
                                    _isCartLoading.value = false
                                    _isUpdatingQuantity.value = false
                                }
                            } else {
                                fetchUserCart(
                                    successCallback = successListenerCallback
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }
        } else {
            fetchUserCart()
        }
    }


    private fun initializeLocalCartItems(cartUiData: CartUiData) {
        val localItems = mutableMapOf<Long, LocalCartItem>()

        // Extract items from dcCityCart
        cartUiData.dcCityCart?.cartDetails?.forEach { item ->
            localItems[item.productId] = LocalCartItem(
                productId = item.productId,
                cartProductId = item.id, // No cartProductId from API fetch
                quantity = item.quantity,
                actualPrice = item.actualPrice,
                discountedPrice = item.discountedPrice,
                productName = item.productName,
                productImage = item.productImage,
                isTotal = true,// <-- from API, prices are totals
                isProductToBeDeliveredByThirdParty = false
            )
        }

        // Extract items from multiCityCart
        cartUiData.multiCityCart?.cartDetails?.forEach { item ->
            localItems[item.productId] = LocalCartItem(
                productId = item.productId,
                cartProductId = item.id,
                quantity = item.quantity,
                actualPrice = item.actualPrice,
                discountedPrice = item.discountedPrice,
                productName = item.productName,
                productImage = item.productImage,
                isTotal = true,
                isProductToBeDeliveredByThirdParty = true
            )
        }

        // Extract items from sellerCarts
        cartUiData.sellerCarts?.forEach { sellerCart ->
            sellerCart.cartDetails.forEach { item ->
                localItems[item.productId] = LocalCartItem(
                    productId = item.productId,
                    cartProductId = item.id,
                    quantity = item.quantity,
                    actualPrice = item.actualPrice,
                    discountedPrice = item.discountedPrice,
                    productName = item.productName,
                    productImage = item.productImage,
                    isTotal = true,
                    isProductToBeDeliveredByThirdParty = true
                )
            }
        }

        cartUiData.dcCityCart?.flashDeals?.forEach { item ->
            if (item.flashDealId != null) {
                localItems[item.productId] = LocalCartItem(
                    productId = item.flashDealId,
                    cartProductId = item.id, // No cartProductId from API fetch
                    quantity = item.quantity,
                    actualPrice = item.actualPrice,
                    discountedPrice = item.discountedPrice,
                    productName = item.productName,
                    productImage = item.productImage,
                    isTotal = true, // <-- from API, prices are totals
                    isProductToBeDeliveredByThirdParty = true
                )
            }
        }
        _localCartItems.value = localItems
    }


    fun addToCart(
        productData: ProductUiData,
        isCart: Boolean? = false,
        quantity: Int? = 1,
        cartProductType: CartProductType = CartProductType.INDIVIDUAL,
        updateCounterLoadingState: () -> Unit = {},
        successCallback: () -> Unit = {}
    ) {
        val isCartEmpty = _localCartItems.value.isEmpty()
        val isFoodCartNotEmpty = foodCartViewModel.localFoodCartItems.value.isNotEmpty()
        if (isCartEmpty && isFoodCartNotEmpty) {
            pendingAddToCartItem = productData
            pendingAddType = PendingAddType.REGULAR
            // Show dialog using GlobalDialogManager
            GlobalDialogManager.showDialog(
                title = "Start New Order",
                message = "You have added items from a restaurant. Clear your cart to start fresh?",
                confirmButton = "Yes",
                cancelButton = "No",
                onConfirm = { onClearFoodCartConfirmed(updateCounterLoadingState = updateCounterLoadingState) },
                onCancel = { onClearFoodCartDialogDismissed() }
            )
        } else {
            actuallyAddToCart(
                productData,
                isCart,
                quantity,
                successCallback,
                cartProductType,
                updateCounterLoadingState
            )
        }
    }

    private fun actuallyAddToCart(
        productData: ProductUiData,
        isCart: Boolean? = false,
        quantity: Int? = 1,
        successCallback: () -> Unit = {},
        cartProductType: CartProductType = CartProductType.INDIVIDUAL,
        updateCounterLoadingState: () -> Unit = {}
    ) {
        val isLocalCart = isCart?.default()?.not() == true
        val productId = productData.id

        _isCartFoodType.value = false

        _isUpdatingQuantity.value = true


        val addToCartRequest = AddToCartRequest(
            product = productId,
            deal = if (cartProductType == CartProductType.DEAL) productData.flashDealId else null,
            quantity = quantity.getValueOrDefault()
        )

        viewModelScope.launch {
            addToCartUseCase(Pair(addToCartRequest, false)).collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        if (isLocalCart) {
                            val currentItems = _localCartItems.value.toMutableMap()
                            val cartProductId = state.data.productId
                            val isProductToBeDeliveredByThirdParty =
                                state.data.isProductToBeDeliveredByThirdParty

                            val newItem = LocalCartItem(
                                productId = if (productData.isFlashDeal) productData.flashDealId else productId,
                                cartProductId = cartProductId,
                                quantity = quantity.getValueOrDefault(),
                                actualPrice = productData.price,
                                discountedPrice = productData.discountedPrice,
                                productName = productData.name,
                                productImage = productData.imageUrl.toString(),
                                isTotal = false, // <-- locally, prices are per-unit
                                isProductToBeDeliveredByThirdParty = isProductToBeDeliveredByThirdParty
                            )
                            currentItems[productId] = newItem
                            _localCartItems.value = currentItems

                        } else
                            successCallback()
                        _isUpdatingQuantity.value = false
                        updateCounterLoadingState()
                    }

                    is ViewState.Error -> {
                        if (state.viewError.message.contains("khreednay ki limit"))
                            GlobalToastManager.showToast(
                                message = "Oops! You’ve reached the maximum quantity allowed for this product",
                                type = SnackbarType.ERROR
                            )
                        else if (state.viewError.message.contains("Out of stock"))
                            GlobalToastManager.showToast(
                                message = "Oops! This item is now out of stock.",
                                type = SnackbarType.ERROR
                            )
                        else
                            GlobalToastManager.showToast(
                                message = "Oops! Something went wrong. Please try again in a moment.",
                                type = SnackbarType.ERROR
                            )
                        if (isLocalCart) {
                            // Revert optimistic update on failure
                            val revertedItems = _localCartItems.value.toMutableMap()
                            revertedItems.remove(productId)
                            _localCartItems.value = emptyMap() // Force state change
                            _localCartItems.value = revertedItems
                        }
                        _isUpdatingQuantity.value = false
                        updateCounterLoadingState()
                    }

                    is ViewState.Loading -> {}
                    ViewState.Empty -> {}
                }
            }
        }
    }

    fun incrementItemQuantity(
        productId: Long,
        cartProductId: Long,
        isCart: Boolean? = false,
        successCallback: () -> Unit = {},
        updateCounterLoadingState: () -> Unit = {},
        failureCallback: () -> Unit = {}
    ) {
        val isLocalCart = isCart?.default()?.not() == true
        var backupItem: LocalCartItem? = null

        _isUpdatingQuantity.value = true
        viewModelScope.launch {
            unZipViewStateFlow(increaseItemQuantityUseCase(cartProductId), onSuccess = {
                if (isLocalCart.not())
                    successCallback()
                if (isLocalCart) {
                    val currentItems = _localCartItems.value.toMutableMap()
                    val currentItem = currentItems[productId]
                    if (currentItem != null) {
                        backupItem = currentItem.copy()
                        // Optimistic update before API call
                        val optimisticItem =
                            currentItem.copy(quantity = currentItem.quantity + 1, isTotal = false)
                        currentItems[productId] = optimisticItem
                        _localCartItems.value = currentItems
                    }
                }
                updateCounterLoadingState()

            }, onError = {
                /*if (isLocalCart && backupItem != null) {
                     val revertedItems = _localCartItems.value.toMutableMap()
                     revertedItems[productId] = backupItem
                     _localCartItems.value = revertedItems
                 }*/
                if (it.message.contains("khreednay ki limit"))
                    GlobalToastManager.showToast(
                        message = "Oops! You’ve reached the maximum quantity allowed for this product",
                        type = SnackbarType.ERROR
                    )
                else if (it.message.contains("Out of stock"))
                    GlobalToastManager.showToast(
                        message = "Oops! This item is now out of stock.",
                        type = SnackbarType.ERROR
                    )
                else
                    GlobalToastManager.showToast(
                        message = "Oops! Something went wrong. Please try again in a moment.",
                        type = SnackbarType.ERROR
                    )
                // Revert optimistic update on failure
                _isUpdatingQuantity.value = false
                updateCounterLoadingState()
                failureCallback()
            })


        }
    }


    fun decreaseItemQuantity(
        productId: Long, cartProductId: Long,
        isCart: Boolean? = false,
        successCallback: () -> Unit = {},
        updateCounterLoadingState: () -> Unit = {},
        failureCallback: () -> Unit = {}
    ) {

        val isLocalCart = isCart?.default()?.not() == true
        var backupItem: LocalCartItem? = null


        _isUpdatingQuantity.value = true
        viewModelScope.launch {
            decreaseItemQuantityUseCase(cartProductId).collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        if (isLocalCart.not())
                            successCallback()
                        if (isCart?.not() == true) {
                            _isUpdatingQuantity.value = false
                        }
                        if (isLocalCart) {
                            val currentItems = _localCartItems.value.toMutableMap()
                            val currentItem = currentItems[productId]
                            if (currentItem != null && cartProductId != 0L) {
                                // Optimistic update before API call
                                backupItem = currentItem.copy()
                                if (currentItem.quantity > 1) {
                                    currentItems[productId] =
                                        currentItem.copy(
                                            quantity = currentItem.quantity - 1,
                                            isTotal = false
                                        )
                                } else {
                                    currentItems.remove(productId)
                                }
                                _localCartItems.value = currentItems
                            }
                        }
                        updateCounterLoadingState()
                    }

                    is ViewState.Error -> {
                        GlobalToastManager.showToast(
                            message = "Oops! Something went wrong. Please try again in a moment.",
                            type = SnackbarType.ERROR
                        )
                        /* if (isLocalCart && backupItem != null) {
                             val revertedItems = _localCartItems.value.toMutableMap()
                             revertedItems[productId] = backupItem
                             _localCartItems.value = revertedItems
                         }*/
                        _isUpdatingQuantity.value = false
                        updateCounterLoadingState()
                        failureCallback()
                    }

                    is ViewState.Loading -> {}
                    ViewState.Empty -> {}
                }
            }
        }
    }


    fun removeCartItem(
        isCart: Boolean? = false,
        productData: ProductInfo,
        updateCounterLoadingState: () -> Unit = {},
        successCallback: () -> Unit = {},
        failureCallback: () -> Unit = {}
    ) {
        val isLocalCart = isCart?.default()?.not() == true
        var backupItem: LocalCartItem? = null

        eventLogger.deleteItemFromCart(productData)
        _isUpdatingQuantity.value = true
        viewModelScope.launch {
            removeCartUseCase(productData.cartProductId).collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        if (isLocalCart.not()) {
                            successCallback()
                        } else {
                            val currentItems = _localCartItems.value.toMutableMap()
                            val previousItem = currentItems[productData.productId]
                            if (previousItem != null && productData.cartProductId != 0L) {
                                backupItem = previousItem.copy()
                                currentItems.remove(productData.productId)
                                _localCartItems.value = currentItems
                            }
                        }
                        updateCounterLoadingState()
                    }

                    is ViewState.Error -> {
                        GlobalToastManager.showToast(
                            message = "Oops! Something went wrong. Please try again in a moment.",
                            type = SnackbarType.ERROR
                        )
                        /*if (isLocalCart && backupItem != null) {
                              val revertedItems = _localCartItems.value.toMutableMap()
                              revertedItems[productId] = backupItem
                              _localCartItems.value = revertedItems
                          }*/
                        _isUpdatingQuantity.value = false
                        updateCounterLoadingState()
                        failureCallback()
                    }

                    is ViewState.Loading -> {}
                    ViewState.Empty -> {}
                }
            }
        }
    }

    fun fetchUserCart(
        isCartScreen: Boolean = false,
        fetchCartListener: () -> Unit = {},
        updateCounterLoadingState: () -> Unit = {},
        successCallback: () -> Unit = {},
    ) {
        isLoader.value = true
        val request = CartRequest(
            detailed = detailed
        )
        unZipViewStateFlow(
            fetchCartUseCase(
                request
            ),
            onSuccess = {
                val cart = it as CartUiData
                _cartState.value = cart
                _parentCategories.value = cart.dcCityCart?.cartDetails?.map { it.parentCategories }
                    ?.joinToString(separator = ",") + cart.multiCityCart?.cartDetails?.map { it.parentCategories }
                    ?.joinToString(separator = ",")
                cartData(cart)
                initializeLocalCartItems(cart) // <-- restore local items from fetched cart
                bundleQuantity.value =
                    cart.dcCityCart?.bundle?.quantity.getValueOrDefault()
                if (
                    cart.dcCityCart?.cartTotal.getValueOrDefault() > cart.dcCityCart?.getMinOrderVal()
                        .getValueOrDefault()
                ) {
                    fetchCartListener()
                }
                _isCartLoading.value = false
                isLoader.value = false
                packagingFee.value =
                    if (cart.dcCityCart?.isEnabled == false) ZERO_INT else RemoteConfigs.getPackagingFeeAmount()

                platformFee.value =
                    if (cart.dcCityCart?.isEnabled == false) ZERO_INT else RemoteConfigs.getPlatformFeeAmount()
                _isUpdatingQuantity.value = false
                updateCounterLoadingState()
                successCallback()
            }, onError = {
                _isCartLoading.value = false
                isLoader.value = false
                _isUpdatingQuantity.value = false
                updateCounterLoadingState()
                if (isCartScreen) {
                    isGetCartDetailsFailed.value = true
                }

            }
        )
    }

    private fun areAllShowMrpFalse(): Boolean {
        val dcCityCartItems =
            cartState.value?.dcCityCart?.cartDetails.orEmptyArrayList()
        val multiCityCartItems =
            cartState.value?.multiCityCart?.cartDetails.orEmptyArrayList()
        val sellerCartItems =
            cartState.value?.sellerCarts?.flatMap { it.cartDetails }.orEmptyArrayList()
        return dcCityCartItems.filter { it.showMrp != null }
            .all { it.showMrp == true || it.actualPrice <= it.discountedPrice } && multiCityCartItems.filter { it.showMrp != null }
            .all { it.showMrp == true || it.actualPrice <= it.discountedPrice } && sellerCartItems.filter { it.showMrp != null }
            .all { it.showMrp == true || it.actualPrice <= it.discountedPrice }
    }

    private fun cartData(cart: CartUiData) {
        if (cart.id.isNotZero()) {

            _overallCartAndCoinDiscount.value = cart.discountTotal

            updateCartDiscount(isDiscountSwitchChecked.value)

            var totalPromoDiscount = ZERO
            totalPromoDiscount += if (cart.dcCityCart?.isEnabled == true) cart.dcCityCart.PromoDetails?.discountValue?.toDouble()
                .getValueOrDefault() else ZERO
            totalPromoDiscount += if (cart.multiCityCart?.isEnabled == true) cart.multiCityCart.PromoDetails?.discountValue?.toDouble()
                .getValueOrDefault() else ZERO
            totalPromoDiscount += cart.sellerCarts?.sumOf {
                if (it.isEnabled) it.PromoDetails?.discountValue?.toDouble()
                    .getValueOrDefault() else ZERO
            }.getValueOrDefault()

            _discountedCartTotal.value =
                (cart.cartTotal
                        + cart.dcDeliveryFee
                        + cart.multiCityDeliveryFee
                        - totalPromoDiscount
                    .getValueOrDefault()
                        - if (isDiscountSwitchChecked.value.default()) dcCoinDiscount.value.getValueOrDefault() else ZERO
                        )

            val groceryIds =
                cart.dcCityCart?.cartDetails?.map { it.id }?.joinToString(separator = ",")

            val nonGroceryIds =
                cart.multiCityCart?.cartDetails?.map { it.id }?.joinToString(separator = ",")

            val dcCategories =
                cart.dcCityCart?.cartDetails?.map { it.parentCategories }
                    ?.joinToString(separator = ",")

            val nonDcCategories =
                cart.multiCityCart?.cartDetails?.map { it.parentCategories }
                    ?.joinToString(separator = ",")

            val parentCategories = (dcCategories + nonDcCategories)

//            eventLogger.viewCart(
//                sharedPreferencesManager.getUserId(),
//                sharedPreferencesManager.getSelectedCity(),
//                groceryIds + nonGroceryIds,
//                cart.cartTotal,
//                cart.discountTotal,
//                packagingFee.value.getValueOrDefault(),
//                platformFee.value.getValueOrDefault(),
//                parentCategories
//            )

            if (cart.cartTotal < useDCCoinsAmountLimit && isDiscountSwitchChecked.value.default()) {
                dcCoinWarningTrigger.value = true
                _isDiscountSwitchChecked.value = false
                dcCoinErrorMessage.value =
                    "⚠\uFE0F Rs. 500 se ziada walay orders pe coins istemaal karein"
            } else if (cart.dcCityCart?.hasRestrictedPromotionalDiscountProducts == true && isDiscountSwitchChecked.value) {
                dcCoinWarningTrigger.value = true
                _isDiscountSwitchChecked.value = false
                dcCoinErrorMessage.value =
                    "Cart mai Nicotine\\/Tambaku items honay ki wajah se, DC coins istemaal nahi kiye ja sakte"
            } else {
                dcCoinWarningTrigger.value = false
            }
        } else {
            _cartState.value = cartUiData()
            // MainActivity.updateBadge(groceryCount = ZERO_INT, isFood = false)
            _discountedCartTotal.value = 0.0
            _overallCartAndCoinDiscount.value = 0.0
        }

        packagingFee.value =
            if (cart.dcCityCart?.isEnabled == false) ZERO_INT else RemoteConfigs.getPackagingFeeAmount()
        platformFee.value =
            if (cart.dcCityCart?.isEnabled == false) ZERO_INT else RemoteConfigs.getPlatformFeeAmount()

        if (!cart.isSameDeliveryEligible && selectedDeliveryType.value == DeliveryType.EXPRESS) {
            _selectedDeliveryType.value = DeliveryType.STANDARD
        }

        _formattedOverallCartAndCoinDiscount.value =
            _overallCartAndCoinDiscount.value.getValueOrDefault().formatPKRCurrency()
        // Coerce delivery type if Express is selected but cart isn't eligible for same-day
        if (selectedDeliveryType.value == DeliveryType.EXPRESS && cart.isSameDeliveryEligible.not()) {
            _selectedDeliveryType.value = DeliveryType.STANDARD
        }
        _formattedDiscountedCartTotal.value = (_discountedCartTotal.value.getValueOrDefault()
            .toInt() + packagingFee.value.getValueOrDefault() + platformFee.value.getValueOrDefault() + if (selectedDeliveryType.value == DeliveryType.EXPRESS) cart.sameDayDeliveryFee.getValueOrDefault()
            .toInt() else 0).toDouble().formatPKRCurrency()
    }

    fun updateCartDiscount(checked: Boolean) {
        _isDiscountSwitchChecked.value = checked

        cartState.value?.let { cart ->
            if (cart.cartTotal.getValueOrDefault() < useDCCoinsAmountLimit) {
                _isDiscountSwitchChecked.value = false
                return
            }

            if (checked) {
                _dcCoinDiscount.value = cart.amountInCurrency
                if (cart.cartTotal.getValueOrDefault() <= dcCoinDiscount.value.getValueOrDefault()) {
                    _dcCoinDiscount.value = cart.cartTotal
                }

                dcCoinDiscount.value.let {
                    _discountedCartTotal.value =
                        cart.cartTotal.getValueOrDefault() - it + cart.dcDeliveryFee.getValueOrDefault() + cart.multiCityDeliveryFee.getValueOrDefault() -
                                cart.dcCityCart?.PromoDetails?.discountValue.getValueOrDefault()
                    -cart.multiCityCart?.PromoDetails?.discountValue.getValueOrDefault()
                }
                _overallCartAndCoinDiscount.value =
                    (cart.discountTotal) + dcCoinDiscount.value
            } else {
                var totalPromoDiscount = ZERO
                totalPromoDiscount += if (cart.dcCityCart?.isEnabled == true) cart.dcCityCart.PromoDetails?.discountValue?.toDouble()
                    .getValueOrDefault() else ZERO
                totalPromoDiscount += if (cart.multiCityCart?.isEnabled == true) cart.multiCityCart.PromoDetails?.discountValue?.toDouble()
                    .getValueOrDefault() else ZERO
                totalPromoDiscount += cart.sellerCarts?.sumOf {
                    if (it.isEnabled) it.PromoDetails?.discountValue?.toDouble()
                        .getValueOrDefault() else ZERO
                }.getValueOrDefault()
                _discountedCartTotal.value = (cartState.value?.cartTotal.getValueOrDefault()
                        + cart.dcDeliveryFee.getValueOrDefault()
                        + cart.multiCityDeliveryFee.getValueOrDefault()
                        - totalPromoDiscount)

                _overallCartAndCoinDiscount.value = cart.discountTotal
            }
        }
        packagingFee.value =
            if (cartState.value?.dcCityCart?.isEnabled == false) ZERO_INT else RemoteConfigs.getPackagingFeeAmount()
        platformFee.value =
            if (cartState.value?.dcCityCart?.isEnabled == false) ZERO_INT else RemoteConfigs.getPlatformFeeAmount()

        _formattedDcCoinDiscount.value =
            _dcCoinDiscount.value.getValueOrDefault().formatPKRCurrency()

        _formattedDiscountedCartTotal.value = (_discountedCartTotal.value.getValueOrDefault()
            .toInt() + packagingFee.value.getValueOrDefault() + platformFee.value.getValueOrDefault() + if (selectedDeliveryType.value == DeliveryType.EXPRESS) cartState.value?.sameDayDeliveryFee.getValueOrDefault()
            .toInt() else 0).toDouble()
            .formatPKRCurrency()

        _formattedOverallCartAndCoinDiscount.value =
            _overallCartAndCoinDiscount.value.getValueOrDefault().formatPKRCurrency()

    }

    fun onActionPromoCode(
        isEnabled: Boolean,
        promoCode: String?,
        itemIds: List<Long>,
        successCallback: () -> Unit = {},
        onError: () -> Unit = {}
    ) {
        val request = PromoCodeRequest(
            promoCode = promoCode,
            itemIds = itemIds,
            isEnabled = isEnabled
        )
        _isUpdatingQuantity.value = true
        unZipViewStateFlow(
            promoCodeUseCase(
                request
            ),
            onSuccess = {
                fetchUserCart {
                    successCallback()
                }
            },
            onError = {
                _isUpdatingQuantity.value = false
                isLoader.value = false
                GlobalToastManager.showToast(it.message, SnackbarType.ERROR)
                onError()

            }
        )
    }

    private fun onActionPlacerOrder(
        bottomSheetNavigator: BottomSheetNavigator,
        navigateToCheckout: () -> Unit
    ) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        cartState.value?.let { cart ->
            val dcCityFlashDeals = cart.dcCityCart?.flashDeals.orEmpty()
                .filter { it.isFlashDeal && it.totalJoinedPersons + 1 < it.totalPersonsRequired }
            val dcCityExpiredDeals =
                dcCityFlashDeals.filter { it.expiryEndTime - currentTime < 0 }
            val multiCityFlashDeals = cart.multiCityCart?.flashDeals.orEmpty()
                .filter { it.isFlashDeal && it.totalJoinedPersons + 1 < it.totalPersonsRequired }
            val multiCityExpiredDeals =
                multiCityFlashDeals.filter { it.expiryEndTime - currentTime < 0 }
            if (dcCityExpiredDeals.isNotEmpty() || multiCityExpiredDeals.isNotEmpty()) {
                //toastManager.showError("Flash deal expire ho gaey hay.")
                return
            }

            val argData = CartCheckoutArgs(
                dcCoins = cart.totalDCCoins,
                isDcCoinEnabled = isDiscountSwitchChecked.value,
                dcCityFlashDeals = dcCityFlashDeals,
                multiCityFlashDeals = multiCityFlashDeals,
                groups = emptyList(),
                cart = cart,
                overallCartAndCoinDiscount = _overallCartAndCoinDiscount.value.getValueOrDefault(),
                discountedCartTotal = _discountedCartTotal.value.getValueOrDefault().toInt() +
                        packagingFee.value.getValueOrDefault() +
                        platformFee.value.getValueOrDefault(),
                showMrp = areAllShowMrpFalse().default().not(),
                isDCCityOrder = cart.dcCityCart?.cartDetails?.size.getValueOrDefault() > 0 && cart.dcCityCart?.isEnabled == true,
                isMultiCityOrder = cart.multiCityCart?.cartDetails?.size.getValueOrDefault() > 0 && cart.multiCityCart?.isEnabled == true,
                formattedPromoDiscount = cart.formattedTotalPromoDiscount(
                    cart.dcCityCart?.isEnabled == true,
                    cart.multiCityCart?.isEnabled == true
                ),
                packagingFee = packagingFee.value,
                platformFee = platformFee.value,
                parentCategories = parentCategories.value.default(),
                totalPromoDiscount = cart.totalPromoDiscount(
                    cart.dcCityCart?.isEnabled == true,
                    cart.multiCityCart?.isEnabled == true
                ),
                dcCoinDiscount = dcCoinDiscount.value.getValueOrDefault()
            )

            _checkoutData.value = argData

            if (cart.dcCityCart?.hasNicotineTobaccoItems == true) {
                val data = GeneralBottomSheetUiData(
                    title = Res.string.disclaimer,
                    description = Res.string.message_terms_condition_acceptance,
                    secondaryTitle = Res.string.title_keep_nic_with_you,
                    secondaryDescription = Res.string.message_keep_nic_with_you,
                    positiveButton = Res.string.yes,
                    negativeButton = Res.string.no
                ).apply {
                    confirmListener = {
                        launch {
                            delay(BIT_DELAY)
                            if (cart.dcCityCart.isBundle()) {
                                validateBundle(argData, navigateToCheckout)
                            } else {
                                navigateToCheckout()
                            }
                        }
                    }
                    onDismiss = {
                        bottomSheetNavigator.hide()
                    }
                }
                GlobalEvents.updateGeneralBottomSheetScreen(data)
                bottomSheetNavigator.show(
                    GeneralBottomSheetScreen()
                )
            } else {
                if (cart.dcCityCart?.isBundle() == true) {
                    validateBundle(argData, navigateToCheckout)
                } else {
                    navigateToCheckout()

                }
            }
        }
    }


    fun navigateToMorePromos(
        bottomSheetNavigator: BottomSheetNavigator
    ) {
        if (sharedPreferencesManager.isLoggedIn()) {
            bottomSheetNavigator.show(
                PromoListingBottomSheetScreen(isFood = false)
            )
        }
    }

    fun handlePromoSelection(promoCode: String, promoId: Long, onHide: () -> Unit) {
        isLoader.value = true
        onActionPromoCode(
            isEnabled = promoNavigationArgs.value?.isBucketEnabled.default(),
            promoCode = promoCode,
            itemIds = promoNavigationArgs.value?.itemIds.orEmptyArrayList(),
            successCallback = {
                _promoNavigationArgs.value = null
            },
            onError = {
                _promoNavigationArgs.value = null
            }
        )
        onHide()
    }


    fun handleMOVCancel(
        bottomSheetNavigator: BottomSheetNavigator,
        navigateToCheckout: () -> Unit
    ) {
        launch {
            delay(BIT_DELAY)
            onActionPromoCode(
                isEnabled = false,
                promoCode = cartState.value?.dcCityCart?.PromoDetails?.promoCode.default(),
                itemIds = cartState.value?.dcCityCart?.cartDetails?.map { it.id } ?: emptyList(),
                successCallback = {
                    onActionPlacerOrder(bottomSheetNavigator) {
                        navigateToCheckout()
                    }
                }
            )
        }
    }


    fun onActionShowDcCoinWarning() {
        if (cartState.value?.totalDCCoins.getValueOrDefault() > ZERO_INT) {
            dcCoinWarningTrigger.value = true
            if (cartState.value?.dcCityCart?.hasRestrictedPromotionalDiscountProducts == true || cartState.value?.multiCityCart?.hasRestrictedPromotionalDiscountProducts == true) {
                dcCoinErrorMessage.value =
                    "Cart mai Nicotine\\/Tambaku items honay ki wajah se, DC coins istemaal nahi kiye ja sakte"
            } else {
                dcCoinErrorMessage.value =
                    "\"⚠\\uFE0F Rs. 500 se ziada walay orders pe coins istemaal karein\""
            }
        }

    }

    fun increaseBundleQuantity() {
        unZipViewStateFlow(
            increaseBundleQuantityUseCase(Unit),
            onSuccess = {
                val data = it as CartBundleResponse
                if (data.body?.productOOSDetails.isNull()) {
                    fetchUserCart()
                } else {
                    _bundleDetails.value = data.body
                    showBundleDialog.value = true
                }
            }
        )
    }

    fun bundleSwitch() {
        unZipViewStateFlow(
            bundleSwitchUseCase(Unit),
            onSuccess = {
                val resp = it as GeneralStringResponse
                if (resp.success.not()) {
//                    showFavouritesSuccess(
//                        R.drawable.ic_dc_sad_orange,
//                        resp.body?.message.default()
//                    )
                } else {
                    fetchUserCart()
                }
            }
        )
    }

    fun decreaseBundleQuantity() {
        unZipViewStateFlow(
            decreaseBundleQuantityUseCase(
                bundleQuantity.value.getValueOrDefault() - 1
            ),
            onSuccess = {
                val data = it as GeneralStringResponse
                if (data.success) {
                    fetchUserCart()
                }
            }
        )
    }

    private fun validateBundle(argsData: CartCheckoutArgs, navigateToCheckout: () -> Unit) {
        unZipViewStateFlow(
            validateBundleUseCase(Unit),
            onSuccess = {
                val data = it as GeneralStringResponse
                if (data.success)
                    navigateToCheckout()
                else {
                    //  showSuccess(stringsResourceManager.getString(R.string.bundle_atleast_3))
                }
            }
        )
    }

    fun onCheckOutClick(
        bottomSheetNavigator: BottomSheetNavigator,
        navigateToCheckout: () -> Unit = {},
        cartMovConfirmListener: () -> Unit = {}
    ) {
        eventLogger.beginCheckout(cartState.value?.cartTotal.getValueOrDefault())
        if (sharedPreferencesManager.isLoggedIn()) {
            val dcCartDetails: List<CartUiModel> =
                cartState.value?.dcCityCart?.cartDetails.orEmptyArrayList() + cartState.value?.dcCityCart?.flashDeals.orEmptyArrayList()
            if (dcCartDetails.isNotEmpty() && cartState.value?.dcCityCart?.cartTotal.getValueOrDefault() < cartState.value?.dcCityCart
                    ?.getMinOrderVal()
                    .getValueOrDefault()
                    .getValueOrDefault() && cartState.value?.dcCityCart?.isEnabled == true
            ) {
                bottomSheetNavigator.show(
                    CartMovBottomSheetScreen(
                        feeNeeded = cartState.value?.dcCityCart?.amountNeededForFreeDelivery.getValueOrDefault(),
                        nonGroceryCheck = cartState.value?.multiCityCart?.cartDetails?.isNotEmpty() == true || cartState.value?.sellerCarts?.any { it.cartDetails.isNotEmpty() } == true,
                        placeOrderCheck = true
                    )
                )
            } else {
                onActionPlacerOrder(bottomSheetNavigator) {
                    navigateToCheckout()
                }
            }
        }
    }

    fun formattedDcCoinDiscount(cart: CartUiData): String =
        cart.amountInCurrency.formatPKRCurrency()

    fun addToCartFoodItem(request: AddItemRequestBody, updateCounterLoadingState: () -> Unit = {}) {
        val isCartNotEmpty = _localCartItems.value.isNotEmpty()
        val isFoodCartEmpty = foodCartViewModel.localFoodCartItems.value.isEmpty()
        if (isCartNotEmpty && isFoodCartEmpty) {
            pendingAddFoodRequest = request
            pendingAddType = PendingAddType.FOOD
            // Show dialog using GlobalDialogManager
            GlobalDialogManager.showDialog(
                title = "Start New Order",
                message = "You have grocery items. Clear your cart to start fresh?",
                confirmButton = "Yes",
                cancelButton = "No",
                onConfirm = { onClearFoodCartConfirmed(updateCounterLoadingState = updateCounterLoadingState) },
                onCancel = { onClearFoodCartDialogDismissed() }
            )
        } else {
            actuallyAddToCartFoodItem(request, updateCounterLoadingState)
        }
    }

    private fun actuallyAddToCartFoodItem(
        request: AddItemRequestBody,
        updateCounterLoadingState: () -> Unit = {},
    ) {
        _isCartFoodType.value = true
        unZipViewStateFlow(
            addFoodItemToCartUseCase(request),
            onSuccess = { response ->
                foodCartViewModel.getFoodCart(updateCounterLoadingState = updateCounterLoadingState)
            },
            onError = { error ->
                if (error.message.contains("clear your cart")) {
                    pendingAddFoodRequest = request
                    pendingAddType = PendingAddType.FOOD
                    updateCounterLoadingState()
                    GlobalDialogManager.showDialog(
                        title = "Start New Order",
                        message = "You have added items from another restaurant. Clear your cart to start fresh?",
                        confirmButton = "Yes",
                        cancelButton = "No",
                        onConfirm = { onClearFoodCartConfirmed(updateCounterLoadingState = updateCounterLoadingState) },
                        onCancel = { onClearFoodCartDialogDismissed() }
                    )
                }
            }
        )
    }

    fun onClearFoodCartDialogDismissed() {
        GlobalDialogManager.hideDialog()
        pendingAddToCartItem = null
        pendingAddFoodRequest = null
        pendingAddType = null
    }

    fun onClearFoodCartConfirmed(updateCounterLoadingState: () -> Unit = {}) {
        when (pendingAddType) {
            PendingAddType.REGULAR -> clearCart(
                isFoodCart = true,
                updateCounterLoadingState = updateCounterLoadingState
            )

            PendingAddType.FOOD -> {
                // Clear food cart, then add the pending food item
                clearCart(isFoodCart = true, updateCounterLoadingState = updateCounterLoadingState)

            }

            else -> clearCart(
                isFoodCart = true,
                updateCounterLoadingState = updateCounterLoadingState
            )
        }
    }

    // Update clearCart to handle both types of pending adds
    fun clearCart(
        isFoodCart: Boolean = false,
        updateCounterLoadingState: () -> Unit = {},
        successCallback: () -> Unit = {}
    ) {
        unZipViewStateFlow(
            clearCartUseCase(isFoodCart),
            onSuccess = {
                successCallback()
                _localCartItems.value = emptyMap()
                _cartState.value = null
                foodCartViewModel.clearFoodCartState()
                // --- FIX: Update isCartFoodType to match the new cart type ---
                if (isFoodCart) {
                    _isCartFoodType.value = false // Switching to regular cart
                } else {
                    _isCartFoodType.value = true // Switching to food cart
                }
                // -----------------------------------------------------------
                if (pendingAddType == PendingAddType.REGULAR) {
                    pendingAddToCartItem?.let {
                        actuallyAddToCart(it, updateCounterLoadingState = updateCounterLoadingState)
                        pendingAddToCartItem = null
                    }
                } else if (pendingAddType == PendingAddType.FOOD) {
                    pendingAddFoodRequest?.let { req ->
                        actuallyAddToCartFoodItem(
                            req,
                            updateCounterLoadingState = updateCounterLoadingState
                        )
                        _isCartFoodType.value = true // Ensure UI uses food cart for totals
                        pendingAddFoodRequest = null
                    }
                }
                pendingAddType = null
                GlobalDialogManager.hideDialog()
            },
            onError = {
                GlobalDialogManager.hideDialog()
                pendingAddToCartItem = null
                pendingAddFoodRequest = null
                pendingAddType = null
            }
        )
    }


    fun clearLocalCartItems() {
        _localCartItems.value = emptyMap()
        _cartState.value = null
    }


    fun removeGroceryItems(
        isCart: Boolean? = false,
        successCallback: () -> Unit = {}
    ) {
        val cartDetails = cartState.value?.dcCityCart?.cartDetails
        if (cartDetails.isNullOrEmpty()) {
            successCallback()
        } else {

            // Track completion of all API calls
            var completedCount = 0
            val totalItems = cartDetails.size

            cartDetails.forEach { cartItem ->
                removeCartItem(
                    isCart = isCart,
                    productData = ProductInfo(
                        productId = cartItem.productId,
                        cartProductId = cartItem.id.getValueOrDefault(),
                        quantity = localCartItems.value[cartItem.productId]?.quantity
                            .getValueOrDefault(),
                        price = cartItem.discountedPrice,
                        productName = cartItem.productName
                    ),
                    successCallback = {
                        completedCount++
                        // Only call success callback when all items have been removed
                        if (completedCount == totalItems) {
                            successCallback()
                        }
                    }
                )
            }
        }
    }

    fun removeGroceryItemsUsingLocalCart(
        isCart: Boolean? = false,
        successCallback: () -> Unit = {}
    ) {
        val cartDetails = localCartItems.value
        if (cartDetails.isEmpty()) {
            successCallback()
        } else {

            // Track completion of all API calls
            var completedCount = 0
            val totalItems = cartDetails.size

            cartDetails.forEach { cartItem ->
                removeCartItem(
                    isCart = isCart,
                    productData = ProductInfo(
                        productId = cartItem.value.productId,
                        cartProductId = cartItem.value.cartProductId.getValueOrDefault(),
                        quantity = cartItem.value.quantity.getValueOrDefault(),
                        price = cartItem.value.discountedPrice,
                        productName = cartItem.value.productName.default()
                    ),
                    successCallback = {
                        completedCount++
                        // Only call success callback when all items have been removed
                        if (completedCount == totalItems) {
                            successCallback()
                        }
                    }
                )
            }
        }
    }

    fun setIsCartLoading(isCartLoading: Boolean) {
        _isCartLoading.value = isCartLoading
    }

    fun setIsUpdatingQuantity(isUpdating: Boolean) {
        _isUpdatingQuantity.value = isUpdating
    }

    fun removeOosItems(
        OOSItems: List<ProductOOSDetails>,
        emptyOOSItemsFromCheckout: () -> Unit = {}
    ) {
        var totalCount = 0
        var completedCount = 0
        val totalRemovableItems = OOSItems.size
        val addToCartItems = mutableListOf<ProductOOSDetails>()

        OOSItems.forEach {
            if (it.availableQuantity.getValueOrDefault() <= 0) {
                totalCount++
            } else {
                addToCartItems.add(it)
                totalCount += 2
            }
        }
        OOSItems.forEach { item ->
            val productId = item.product?.id.getValueOrDefault()
            val cartItemId = localCartItems.value[productId]?.cartProductId.getValueOrDefault()

            if (cartItemId.isNotZero()) {
                removeCartItem(
                    isCart = true,
                    productData = ProductInfo(
                        productId = productId,
                        cartProductId = cartItemId,
                        quantity = localCartItems.value[productId]?.quantity.getValueOrDefault(),
                        price = 0.0,
                        productName = item.product?.name.default()
                    ),
                    successCallback = {
                        completedCount++
                        if (completedCount == totalRemovableItems && addToCartItems.isNotNullOrEmpty()) {
                            addOOSItems(
                                OOSItems = addToCartItems,
                                successCallback = {
                                    completedCount++
                                    if (completedCount == totalCount) {
                                        setIsUpdatingQuantity(true)
                                        fetchUserCart {
                                            emptyOOSItemsFromCheckout()
                                        }
                                    }
                                }
                            )
                        }
                        if (completedCount == totalCount) {
                            setIsUpdatingQuantity(true)
                            fetchUserCart {
                                emptyOOSItemsFromCheckout()
                            }
                        }
                    }
                )
            }
        }
    }

    private fun addOOSItems(
        OOSItems: List<ProductOOSDetails>,
        successCallback: () -> Unit = {}
    ) {
        OOSItems.forEach { item ->
            val productId = item.product?.id.getValueOrDefault()

            val dcCityCart =
                checkoutData.value?.cart?.dcCityCart?.cartDetails?.firstOrNull { it.productId == productId }
            val multiCityCart =
                checkoutData.value?.cart?.multiCityCart?.cartDetails?.firstOrNull { it.productId == productId }

            val cart = dcCityCart ?: multiCityCart

            addToCart(
                isCart = true,
                productData = ProductUiData(
                    id = item.product?.id.getValueOrDefault(),
                    name = item.product?.name.default(),
                    images = item.product?.images.orEmpty(),
                    imageUrl = item.product?.images?.firstOrNull()?.medium,
                    description = EMPTY_STRING,
                    discountedPrice = cart?.discountedPrice.getValueOrDefault(),
                    isDeal = false,
                    itemQuantity = item.availableQuantity.getValueOrDefault(),
                    finalPrice = cart?.actualPrice.getValueOrDefault(),
                    isBackInStockSubscribed = false,
                    inventoryQty = ZERO_INT,
                    isFavourite = false,
                    isSponsored = false,
                    showMrp = item.product?.isMRPRestricted.default(),
                    slot = ZERO_INT,
                    productCategory = emptyList(),
                    purchasePrice = ZERO,
                    price = cart?.actualPrice.getValueOrDefault(),
                    purchaseLimit = ZERO_INT
                ),
                quantity = item.availableQuantity.getValueOrDefault(),
                successCallback = {
                    successCallback()
                }
            )
        }
    }

    fun updateArgsOOSBottomSheet(args: OOSBottomSheetArgs?) {
        _argsOOSBottomSheet.value = args
    }

    fun updateAddressValidationBottomSheetArgs(listener: (() -> Unit)? = {}) {
        _addressValidationBottomSheetListener.value = listener
    }

}
