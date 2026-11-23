package com.localpk.app.features.cart.viewModel

import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.address.data.AddressUiData
import com.localpk.app.features.cart.data.CartCheckoutArgs
import com.localpk.app.features.cart.data.LocalCartItem
import com.localpk.app.features.promo.view.PromoListingBottomSheetScreen
import com.localpk.app.features.restaurant.data.FoodCartUIData
import com.localpk.app.features.restaurant.data.FoodPromoRequest
import com.localpk.app.features.restaurant.domain.ApplyFoodPromoUseCase
import com.localpk.app.features.restaurant.domain.GetFoodCartUseCase
import com.localpk.app.features.restaurant.domain.UpdateFoodItemQuantityUseCase
import com.localpk.app.features.restaurant.data.UpdateQuantityBody
import com.localpk.app.features.restaurant.data.CartQuantity
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StringsResourceManager
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.default
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FoodCartViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val getFoodCartUseCase: GetFoodCartUseCase,
    private val applyFoodPromoUseCase: ApplyFoodPromoUseCase,
    private val updateFoodItemQuantityUseCase: UpdateFoodItemQuantityUseCase,
    private val eventLogger: EventLogger
) : BaseViewModel<Any>() {

    private val _addresses = MutableStateFlow<List<AddressUiData>>(emptyList())
    val addresses: StateFlow<List<AddressUiData>> = _addresses

    private val _foodCartState = MutableStateFlow<FoodCartUIData?>(null)
    val foodCartState: StateFlow<FoodCartUIData?> = _foodCartState

    private val _isDiscountSwitchChecked = MutableStateFlow(false)
    val isDiscountSwitchChecked: StateFlow<Boolean> = _isDiscountSwitchChecked

    private val _dcCoinDiscount = MutableStateFlow(0.0)
    val dcCoinDiscount: StateFlow<Double> = _dcCoinDiscount

    private val _discountedCartTotal = MutableStateFlow(0.0)
    private val _overallCartAndCoinDiscount = MutableStateFlow(0.0)

    private val _formattedOverallCartAndCoinDiscount = MutableStateFlow(EMPTY_STRING)
    val formattedOverallCartAndCoinDiscount: StateFlow<String> =
        _formattedOverallCartAndCoinDiscount

    private val _formattedDiscountedCartTotal = MutableStateFlow(EMPTY_STRING)
    val formattedDiscountedCartTotal: StateFlow<String> = _formattedDiscountedCartTotal

    private val _formattedDcCoinDiscount = MutableStateFlow(EMPTY_STRING)
    val formattedDcCoinDiscount: StateFlow<String> = _formattedDcCoinDiscount

    val dcCoinWarningTrigger = MutableStateFlow(false)
    val isGetFoodCartFailed = MutableStateFlow(false)
    val dcCoinErrorMessage = MutableStateFlow(EMPTY_STRING)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    val useDCCoinsAmountLimit = 500

    val isCartLoading = MutableStateFlow(false)
    private val isLoader = MutableStateFlow(false)

    // DCCoin
    private val _dcCoin = MutableStateFlow(0)
    private val dcCoin: StateFlow<Int> = _dcCoin

    // Promo ID
    private val _promoId = MutableStateFlow(0L)
    val promoId: StateFlow<Long> = _promoId

    // Local food cart items state - single source of truth for food cart
    private val _localFoodCartItems = MutableStateFlow<Map<Long, LocalCartItem>>(emptyMap())
    val localFoodCartItems: StateFlow<Map<Long, LocalCartItem>> = _localFoodCartItems

    val _checkoutData = MutableStateFlow<CartCheckoutArgs?>(null)
    val checkoutData: StateFlow<CartCheckoutArgs?> = _checkoutData

    init {
        _isLoggedIn.value = sharedPreferencesManager.isLoggedIn()
    }

    fun getFoodCart(successCallback: () -> Unit = {},  updateCounterLoadingState: () -> Unit = {},) {
        unZipViewStateFlow(getFoodCartUseCase(Unit),
            onSuccess = {
                val foodCart = it as FoodCartUIData
                _foodCartState.value = foodCart
                initializeLocalFoodCartItems(foodCart)
                cartData(foodCart)
                isCartLoading.value = false
                isLoader.value = false
                successCallback()
                updateCounterLoadingState()
            },
            onError = {
                isCartLoading.value = false
                isLoader.value = false
                isGetFoodCartFailed.value = true
                updateCounterLoadingState()
            }
        )
    }

    private fun initializeLocalFoodCartItems(foodCartUiData: FoodCartUIData) {
        val localItems = mutableMapOf<Long, LocalCartItem>()
        foodCartUiData.items?.forEach { item ->
            localItems[item.itemId ?: return@forEach] = LocalCartItem(
                productId = item.itemId,
                cartProductId = item.cartItemId,
                quantity = item.quantity ?: 0,
                actualPrice = item.listPrice ?: 0.0,
                discountedPrice = item.discountPrice ?: 0.0,
                productName = item.name ?: "",
                productImage = item.image ?: "",
                isTotal = false,
                isProductToBeDeliveredByThirdParty = true
            )
        }
        _localFoodCartItems.value = localItems
    }

    private fun cartData(cart: FoodCartUIData) {
        if (cart.id.isNotZero()) {

            _overallCartAndCoinDiscount.value = cart.items?.sumOf { it.itemDiscount() } ?: 0.0

//            MainActivity.updateBadge(
//                foodCount = cart.items?.size.getValueOrDefault(),
//                isFood = true
//            )
            updateCartDiscount(isDiscountSwitchChecked.value)

            _discountedCartTotal.value =
                (cart.totalItemsAmount
                        + cart.additionalFees?.deliveryFee.getValueOrDefault()
                        + cart.additionalFees?.valueAddedTax.getValueOrDefault()
                        + cart.additionalFees?.platformFee.getValueOrDefault()
                        - cart.discounts?.promoDiscount.getValueOrDefault()
                    .getValueOrDefault()
                        - if (isDiscountSwitchChecked.value.default()) dcCoinDiscount.value.getValueOrDefault() else ZERO
                        )

            if (cart.totalItemsAmount < useDCCoinsAmountLimit && isDiscountSwitchChecked.value.default()) {
                dcCoinWarningTrigger.value = true
                _isDiscountSwitchChecked.value = false
                dcCoinErrorMessage.value =
                    "\"⚠\\uFE0F Rs. 500 se ziada walay orders pe coins istemaal karein\""
            } else {
                dcCoinWarningTrigger.value = false
            }
        } else {
//            MainActivity.updateBadge(groceryCount = ZERO_INT, isFood = false)
            _discountedCartTotal.value = 0.0
            _overallCartAndCoinDiscount.value = 0.0
        }

        _formattedOverallCartAndCoinDiscount.value =
            _overallCartAndCoinDiscount.value.getValueOrDefault().formatPKRCurrency()

        _formattedDiscountedCartTotal.value =
            _discountedCartTotal.value.getValueOrDefault().formatPKRCurrency()
    }

    fun updateCartDiscount(checked: Boolean) {

        _isDiscountSwitchChecked.value = checked

        foodCartState.value?.let { cart ->
            if (cart.totalItemsAmount.getValueOrDefault() < useDCCoinsAmountLimit) {
                _isDiscountSwitchChecked.value = false
                return
            }

            if (checked) {
                _dcCoinDiscount.value =
                    cart.availableCoins?.amountInCurrency.getValueOrDefault()
                _dcCoin.value = cart.availableCoins?.totalDCCoins.getValueOrDefault()
                if (cart.totalItemsAmount.getValueOrDefault() <= dcCoinDiscount.value.getValueOrDefault()) {
                    _dcCoinDiscount.value = cart.totalItemsAmount.getValueOrDefault().toDouble()
                }

                dcCoinDiscount.value.let {
                    _discountedCartTotal.value =
                        cart.totalItemsAmount.getValueOrDefault() - it + cart.additionalFees?.deliveryFee.getValueOrDefault() + cart.additionalFees?.valueAddedTax.getValueOrDefault() + cart.additionalFees?.platformFee.getValueOrDefault() -
                                cart.discounts?.promoDiscount.getValueOrDefault()
                }
                _overallCartAndCoinDiscount.value =
                    (cart.items?.sumOf { it.itemDiscount() } ?: 0.0) + dcCoinDiscount.value
            } else {
                _discountedCartTotal.value =
                    (foodCartState.value?.totalItemsAmount.getValueOrDefault()
                            + cart.additionalFees?.deliveryFee.getValueOrDefault()
                            + cart.additionalFees?.valueAddedTax.getValueOrDefault()
                            + cart.additionalFees?.platformFee.getValueOrDefault()
                            - cart.discounts?.promoDiscount.getValueOrDefault())

                _overallCartAndCoinDiscount.value =
                    cart.items?.sumOf { it.itemDiscount() } ?: 0.0

                _dcCoin.value = ZERO_INT
                _dcCoinDiscount.value = ZERO
            }
        }

        _formattedDcCoinDiscount.value =
            _dcCoinDiscount.value.getValueOrDefault().formatPKRCurrency()


        _formattedDiscountedCartTotal.value =
            _discountedCartTotal.value.getValueOrDefault().formatPKRCurrency()

        _formattedOverallCartAndCoinDiscount.value =
            _overallCartAndCoinDiscount.value.getValueOrDefault().formatPKRCurrency()
    }

    fun onActionShowDcCoinWarning() {
        if (foodCartState.value?.availableCoins?.totalDCCoins.getValueOrDefault() > ZERO_INT) {
            dcCoinWarningTrigger.value = true
            dcCoinErrorMessage.value =
                "\"⚠\\uFE0F Rs. 500 se ziada walay orders pe coins istemaal karein\""
        }

    }

    fun onCheckOutClick(successCallback: () -> Unit, loginCallBack: () -> Unit) {
        if (sharedPreferencesManager.isLoggedIn()) {
            onActionPlacerOrder {
                successCallback()
            }
        } else {
            loginCallBack()
        }
    }

    fun onActionPlacerOrder(successCallback: () -> Unit) {
        eventLogger.clickFoodCheckout()
        foodCartState.value?.let { cart ->
            val argData = CartCheckoutArgs(
                dcCoins = cart.discounts?.coinDiscount.getValueOrDefault(),
                isDcCoinEnabled = isDiscountSwitchChecked.value,
                overallCartAndCoinDiscount = _overallCartAndCoinDiscount.value.getValueOrDefault(),
                discountedCartTotal = _discountedCartTotal.value.getValueOrDefault().toInt(),
                formattedPromoDiscount = cart.formattedPromoCodeDiscount(),
                platformFee = cart.additionalFees?.platformFee.getValueOrDefault(),
                totalPromoDiscount = cart.discounts?.promoDiscount.getValueOrDefault(),
                dcCoinDiscount = dcCoinDiscount.value.getValueOrDefault(),
                foodCart = cart
            )
            _checkoutData.value = argData
            successCallback()
        }
    }

    fun navigateToMorePromos(
        bottomSheetNavigator: BottomSheetNavigator,
        loginCallBack: () -> Unit
    ) {
        if (sharedPreferencesManager.isLoggedIn()) {
            bottomSheetNavigator.show(
                PromoListingBottomSheetScreen(isFood = true)
            )
        } else {
            loginCallBack()
        }
    }

    fun handlePromoSelection(promoCode: String, promoId: Long, onHide: () -> Unit) {
        isLoader.value = true
        applyFoodPromo(promoId, isPromo = true)
        onHide()
    }

    fun applyFoodPromo(
        promoId: Long? = null,
        dcCoins: Int? = null,
        isPromo: Boolean? = false
    ) {
        unZipViewStateFlow(
            applyFoodPromoUseCase(
                FoodPromoRequest(
                    promoId = if (isPromo.default()) promoId.getValueOrDefault() else foodCartState.value?.promoDetail?.promoId.getValueOrDefault(),
                    coinsApplied = if (isPromo.default()) dcCoin.value.getValueOrDefault() else dcCoins.getValueOrDefault(),
                    totalAmount = foodCartState.value?.totalItemsAmount.getValueOrDefault()
                )
            ),
            onSuccess =
            {
                getFoodCart()
            },
            onError = {
                // showDcSuccess(it)
            }
        )
    }

    fun incrementFoodCartItemQuantity(
        cartItemId: Long, isCart: Boolean? = false,
        updateCounterLoadingState: () -> Unit = {},
        successCallback: () -> Unit = {}
    ) {
        val isLocalCart = isCart?.default()?.not() == true
        val currentItems = _localFoodCartItems.value.toMutableMap()
        val item = currentItems.values.find { it.cartProductId == cartItemId }

        if (item != null) {
            val newQuantity = item.quantity + 1
            unZipViewStateFlow(
                updateFoodItemQuantityUseCase(
                    UpdateQuantityBody(listOf(CartQuantity(cartItemId, newQuantity)))
                ),
                onSuccess = {
                    if (!isLocalCart)
                        successCallback()
                    if (isLocalCart) {
                        currentItems[item.productId] = item.copy(quantity = newQuantity)
                        _localFoodCartItems.value = currentItems
                    }
                    updateCounterLoadingState()
                },
                onError = {
                   /* if (isLocalCart) {
                        // Revert local change
                        currentItems[item.productId] = previousItem
                        _localFoodCartItems.value = currentItems
                    }*/
                    updateCounterLoadingState()
                }
            )
        }
    }

    fun decrementFoodCartItemQuantity(
        cartItemId: Long, isCart: Boolean? = false,
        updateCounterLoadingState: () -> Unit = {},
        successCallback: () -> Unit = {}
    ) {
        val isLocalCart = isCart?.default()?.not() == true
        var backupItem: LocalCartItem? = null
        val currentItems = _localFoodCartItems.value.toMutableMap()
        val item = currentItems.values.find { it.cartProductId == cartItemId }
        var newQuantity = 0

        if (isLocalCart && item != null) {
            backupItem = item.copy()
            newQuantity = item.quantity - 1
        } else if (item != null) {
            newQuantity = item.quantity - 1
        }
        unZipViewStateFlow(
            updateFoodItemQuantityUseCase(
                UpdateQuantityBody(listOf(CartQuantity(cartItemId, newQuantity)))
            ),
            onSuccess = {
                if (!isLocalCart)
                    successCallback()
                if (isLocalCart && item != null) {
                    if (newQuantity > 0) {
                        currentItems[item.productId] = item.copy(quantity = newQuantity)
                    } else {
                        currentItems.remove(item.productId)
                    }
                    _localFoodCartItems.value = currentItems
                }
                updateCounterLoadingState()
            },
            onError = {
                // Revert on failure
             /* if (isLocalCart && backupItem != null && item != null) {
                    currentItems[item.productId] = backupItem
                    _localFoodCartItems.value = currentItems
                }*/
                updateCounterLoadingState()
            }
        )

    }

    fun removeFoodCartItem(
        cartItemId: Long, isCart: Boolean? = false,
        updateCounterLoadingState: () -> Unit = {},
        successCallback: () -> Unit = {}
    ) {
        val isLocalCart = isCart?.default()?.not() == true
        var backupItem: LocalCartItem? = null
        val currentItems = _localFoodCartItems.value.toMutableMap()
        val item = currentItems.values.find { it.cartProductId == cartItemId }
        var newQuantity = 0

        if (isLocalCart && item != null) {
            backupItem = item.copy()
            newQuantity = 0
        } else if (item != null) {
            newQuantity = 0
        }
        unZipViewStateFlow(
            updateFoodItemQuantityUseCase(
                UpdateQuantityBody(listOf(CartQuantity(cartItemId, newQuantity)))
            ),
            onSuccess = {
                if (!isLocalCart)
                    successCallback()
                if (isLocalCart && item != null) {
                    if (newQuantity > 0) {
                        currentItems[item.productId] = item.copy(quantity = newQuantity)
                    } else {
                        currentItems.remove(item.productId)
                    }
                    _localFoodCartItems.value = currentItems
                }
                updateCounterLoadingState()
            },
            onError = {
                // Revert on failure
                /* if (isLocalCart && backupItem != null && item != null) {
                       currentItems[item.productId] = backupItem
                       _localFoodCartItems.value = currentItems
                   }*/
                updateCounterLoadingState()
            }
        )

    }

    fun clearFoodCartState() {
        _foodCartState.value = null
        _localFoodCartItems.value = emptyMap()
    }
}