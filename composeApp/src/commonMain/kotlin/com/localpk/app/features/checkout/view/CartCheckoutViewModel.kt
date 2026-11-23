package com.localpk.app.features.checkout.view

import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.localpk.app.base.BaseViewModel
import com.localpk.app.common_components.GlobalToastManager
import com.localpk.app.common_components.SnackbarType
import com.localpk.app.features.address.data.AddressUiData
import com.localpk.app.features.address.data.toAddPhoneNumberBottomSheetScreen
import com.localpk.app.features.address.presentation.AddressListViewModel
import com.localpk.app.features.cart.data.CardDetails
import com.localpk.app.features.cart.data.CartCheckoutArgs
import com.localpk.app.features.cart.data.CartUiData
import com.localpk.app.features.cart.data.CartUiModel
import com.localpk.app.features.cart.data.CheckoutRequest
import com.localpk.app.features.cart.data.CheckoutResponse
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.features.cart.data.OneLastStepData
import com.localpk.app.features.cart.data.OrderPlacementPlatformType
import com.localpk.app.features.cart.data.PaymentDetails
import com.localpk.app.features.cart.data.PaymentMethod
import com.localpk.app.features.cart.data.PaymentMethodUiData
import com.localpk.app.features.cart.data.ProductOOSDetails
import com.localpk.app.features.cart.domain.CheckoutUseCase
import com.localpk.app.features.cart.view.OOSBottomSheetScreen
import com.localpk.app.features.restaurant.data.FoodCheckoutRequest
import com.localpk.app.features.restaurant.data.FoodCheckoutResponse
import com.localpk.app.features.restaurant.data.RestaurantOrderId
import com.localpk.app.features.restaurant.domain.PlaceFoodOrderUseCase
import com.localpk.app.features.wallet.data.WalletBalance
import com.localpk.app.features.wallet.data.WalletBalanceResponse
import com.localpk.app.features.wallet.domain.WalletBalanceUseCase
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.default
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getDeliveryDate
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.isNotZero
import com.localpk.app.utils.isNull
import com.localpk.app.utils.orEmpty
import com.localpk.app.utils.orEmptyArrayList
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartCheckoutViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val checkoutUseCase: CheckoutUseCase,
    private val walletBalanceUseCase: WalletBalanceUseCase,
    private val placeFoodOrderUseCase: PlaceFoodOrderUseCase,
    private val eventLogger: EventLogger,
    private val addressListViewModel: AddressListViewModel
) : BaseViewModel<Any>() {

    // List Data
    val _argsData = MutableStateFlow<CartCheckoutArgs?>(null)
    val argsData: StateFlow<CartCheckoutArgs?> = _argsData

    private val _addresses = MutableStateFlow<List<AddressUiData>>(emptyList())
    val addresses: StateFlow<List<AddressUiData>> = _addresses.asStateFlow()

    private val _addressesFetched = MutableStateFlow(false)
    val addressesFetched = _addressesFetched.asStateFlow()

    private val _selectedAddressUiData = MutableStateFlow<AddressUiData?>(null)
    val selectedAddressUiData: StateFlow<AddressUiData?> = _selectedAddressUiData.asStateFlow()

    private val _multiCityCartProduct = MutableStateFlow<List<CartUiModel>>(emptyList())
    val multiCityCartProduct: StateFlow<List<CartUiModel>> = _multiCityCartProduct.asStateFlow()

    private val _multiCityProductFlash = MutableStateFlow<List<CartUiModel>>(emptyList())
    val multiCityProductFlash: StateFlow<List<CartUiModel>> = _multiCityProductFlash.asStateFlow()

    private val _cartProductFlash = MutableStateFlow<List<CartUiModel>>(emptyList())
    val cartProductFlash: StateFlow<List<CartUiModel>> = _cartProductFlash.asStateFlow()

    private val _cartProductSingle = MutableStateFlow<List<CartUiModel>>(emptyList())
    val cartProductSingle: StateFlow<List<CartUiModel>> = _cartProductSingle.asStateFlow()

    val _oosProducts = MutableStateFlow<List<ProductOOSDetails>>(emptyList())
    val oosProducts: StateFlow<List<ProductOOSDetails>> = _oosProducts.asStateFlow()

    private val _walletAmount = MutableStateFlow<WalletBalance?>(null)
    val walletAmount: StateFlow<WalletBalance?> = _walletAmount.asStateFlow()

    val deliveryDate = MutableStateFlow(EMPTY_STRING)
    val phoneNumber = MutableStateFlow(EMPTY_STRING)
    val paymentMethod = MutableStateFlow(PaymentMethod.CASH_ON_DELIVERY.method)
    val isOrderRequestInProcess = MutableStateFlow(false)
    private val redirectToPaymentSelectScreen = MutableStateFlow(false)

    val paymentId = MutableStateFlow(PaymentMethod.CASH_ON_DELIVERY.id)

    val optionalFee = MutableStateFlow(ZERO_INT)
    val isSaveFeeChecked = MutableStateFlow(false)
    val saveCustomAmount = MutableStateFlow(ZERO_INT)

    val platformFeeName = MutableStateFlow(EMPTY_STRING)
    val packagingFeeName = MutableStateFlow(EMPTY_STRING)

    val isWalletEnabled = MutableStateFlow(false)
    private val walletDisabledByUser = MutableStateFlow(false)

    val discountedCartTotal = MutableStateFlow(ZERO_INT)
    val showAddressSelectionBottomSheet = MutableStateFlow(false)

    private var redirectTo: (redirectToPaymentLoadingScreen: Boolean, paymentId: Double, paymentType: String) -> Unit =
        { _, _, _ -> }

    private val _oneLastStepData = MutableStateFlow<OneLastStepData?>(null)
    val oneLastStepData: StateFlow<OneLastStepData?> = _oneLastStepData

    private val _paymentMethodUiData = MutableStateFlow<PaymentMethodUiData?>(null)
    val paymentMethodUiData: StateFlow<PaymentMethodUiData?> = _paymentMethodUiData

    private val _addPhoneNumberListener = MutableStateFlow<(() -> Unit)?>(null)
    val addPhoneNumberListener: StateFlow<(() -> Unit)?> = _addPhoneNumberListener.asStateFlow()

    fun updateAskToAddPhoneNumberBottomSheetListener(listener: (() -> Unit)? = null) {
        _addPhoneNumberListener.value = listener
    }

    init {
        isOrderRequestInProcess.value = false
        if (redirectToPaymentSelectScreen.value.default()) {
            redirectToPaymentSelectScreen.value = false
            setPaymentData(
                PaymentMethodUiData(
                    paymentType = paymentMethod.value.default(),
                    paymentId = paymentId.value.getValueOrDefault(),
                    listener = { paymentType, id ->
                        paymentMethod.value = paymentType
                        paymentId.value = id
                    },
                    isFood = argsData.value?.foodCart.isNotNull()
                )
            )
            push(
                SelectPaymentMethodScreen()
            )

        } else {
            deliveryDate.value = getDeliveryDate()
            phoneNumber.value = sharedPreferencesManager.getPhoneNumber()
            if (argsData.value?.cart.isNotNull())
                cartData(argsData.value?.cart)
            fetchWalletAmount()
            packagingFeeName.value = RemoteConfigs.getPackagingFeeName()
            platformFeeName.value = RemoteConfigs.getPlatformFeeName()

            if (
                RemoteConfigs.isOptionalFeeEnabled() &&
                argsData.value?.isDCCityOrder.default() && argsData.value?.isMultiCityOrder.default()
                    .not()
            ) {
                optionalFee.value = sharedPreferencesManager.getContributionValue()
                saveCustomAmount.value = sharedPreferencesManager.getCustomContributionValue()
                isSaveFeeChecked.value = sharedPreferencesManager.getSaveContributionValue()
            } else {
                optionalFee.value = 0
                saveCustomAmount.value = 0
                isSaveFeeChecked.value = false
            }
        }
    }

    private fun cartData(cart: CartUiData?) {
        if (cart?.id.isNotZero()) {
            val cartData =
                cart?.dcCityCart?.cartDetails
            val multiCityCartData =
                cart?.multiCityCart?.cartDetails

            val cartSingleProducts =
                cartData?.filter { !it.isFlashDeal }?.map { cartItem ->
                    cartItem.copy(isQuantityButtonHidden = true)
                }

            val cartFlashDealProducts = cartData?.filter { it.isFlashDeal }?.map { cartItem ->
                cartItem.copy(isQuantityButtonHidden = true)
            }

            val multiCityCartSingleProducts =
                multiCityCartData?.filter { !it.isFlashDeal }?.map { cartItem ->
                    cartItem.copy(isQuantityButtonHidden = true)
                }

            val nonGroceryFlashDealProducts =
                multiCityCartData?.filter { it.isFlashDeal }?.map { cartItem ->
                    cartItem.copy(isQuantityButtonHidden = true)
                }

            _cartProductSingle.value = cartSingleProducts.orEmpty()
            _cartProductFlash.value = cartFlashDealProducts.orEmpty()
            _multiCityProductFlash.value = nonGroceryFlashDealProducts.orEmpty()
            _multiCityCartProduct.value = multiCityCartSingleProducts.orEmpty()
        }
    }

    private fun placeFoodOrder(
        checkoutRequest: FoodCheckoutRequest,
        successCallback: (RestaurantOrderId) -> Unit
    ) {
        unZipViewStateFlow(placeFoodOrderUseCase(checkoutRequest), onSuccess = {
            val response = it as FoodCheckoutResponse
            isOrderRequestInProcess.value = false
            successCallback(
                RestaurantOrderId(
                    orderId = response.id.getValueOrDefault(),
                    restaurantId = response.restaurantId.getValueOrDefault(),
                    isCheckout = true
                )
            )
            eventLogger.foodConfirmOrder(
                foodItems = argsData.value?.foodCart?.items?.map { it.name.default() },
                id = response.id.getValueOrDefault(),
                totalItemsAmount = argsData.value?.foodCart?.totalItemsAmount.getValueOrDefault()
            )

        },
            onError = {
                isOrderRequestInProcess.value = false
            }
        )
    }


    fun navigateToAddressSelection() {
        if (sharedPreferencesManager.isLoggedIn()) {
            showAddressSelectionBottomSheet.value = true
        }
    }

    private fun navigateToAddressValidation(bottomSheetNavigator: BottomSheetNavigator) {
        bottomSheetNavigator.show(AddressValidationBottomSheet())
    }

    fun formattedTotalWalletBalance(): String {
        return walletAmount.value?.Balance.getValueOrDefault().formatPKRCurrency()
    }

    private fun navigateToAddSecondaryPhoneBottomSheet(bottomSheetNavigator: BottomSheetNavigator) {
        updateAskToAddPhoneNumberBottomSheetListener {
            onActionChangePhoneNumber(bottomSheetNavigator)
        }
        bottomSheetNavigator.show(AskToAddPhoneNumberBottomSheet())
    }

    private fun fetchWalletAmount() {
        if (argsData.value?.foodCart.isNull()) {
            unZipViewStateFlow(
                walletBalanceUseCase("dcpay"),
                onSuccess = {
                    val response = it as WalletBalanceResponse
                    _walletAmount.value = response.body
                    if (walletAmount.value?.Balance.getValueOrDefault() > 0 && !walletDisabledByUser.value)
                        isWalletEnabled.value = true
                }
            )
        } else {
            _walletAmount.value = WalletBalance(Balance = ZERO)
            isWalletEnabled.value = false
        }
    }

    fun updateWalletUsage(checked: Boolean) {
        if (checked.not()) {
            walletDisabledByUser.value = true
        }
        isWalletEnabled.value = checked && argsData.value?.foodCart.isNull()
    }

    private fun getWalletBalance(discountedCartTotal: Int): Int? {
        return if (isWalletEnabled.value && walletAmount.value?.Balance.getValueOrDefault() >= discountedCartTotal) {
            discountedCartTotal
        } else if (isWalletEnabled.value && walletAmount.value?.Balance.getValueOrDefault()
                .toInt() > 0 && walletAmount.value?.Balance.getValueOrDefault()
                .toInt() < discountedCartTotal
        ) {
            walletAmount.value?.Balance.getValueOrDefault().toInt()
        } else null
    }

    fun calculatePayment(method: String, discountedCartTotal: Int): Int? {
        if (paymentMethod.value.default() != method) {
            return null
        }

        if (!isWalletEnabled.value) {
            return discountedCartTotal
        }

        return when {
            walletAmount.value?.Balance.getValueOrDefault()
                .toInt() == 0 -> discountedCartTotal

            walletAmount.value?.Balance.getValueOrDefault() >= discountedCartTotal -> null
            walletAmount.value?.Balance.getValueOrDefault() > 0 -> discountedCartTotal - walletAmount.value?.Balance.getValueOrDefault()
                .toInt()

            else -> null
        }
    }

    fun onActionChangePhoneNumber(bottomSheetNavigator: BottomSheetNavigator) {
        selectedAddressUiData.value?.let { addressData ->
            bottomSheetNavigator.show(
                addressData.toAddPhoneNumberBottomSheetScreen()
            )
        }
    }

    fun navigateToOOsBottomSheet(
        bottomSheetNavigator: BottomSheetNavigator
    ) {
        bottomSheetNavigator.show(
            OOSBottomSheetScreen()
        )
    }

//    private fun logEventSavYour(orderId: Long) {
//        val data = argsData.value?.cart?.dcCityCart?.cartDetails
////        val cartItems = JSONArray()
////        data?.forEach { product ->
////            cartItems.put(
////                SavyourAffiliate.cartItems(
////                    product.id.toString(),
////                    product.productName,
////                    (product.discountedPrice / product.quantity).toString(),
////                    product.quantity.toString(),
////                    if (SavyourAffiliate.isNewUser.default) "new_user" else "old_user"
////                )
////            )
////        }
////
////        SavyourAffiliate.order(
////            orderId = orderId.toString(),
////            cartTotal = argsData.cart?.cartTotal.getValueOrDefault().toString(),
////            invoiceId = orderId.toString(),
////            grossAmount = (argsData.cart?.cartTotal.getValueOrDefault() + argsData.cart?.dcDeliveryFee.getValueOrDefault() + argsData.cart?.multiCityDeliveryFee.getValueOrDefault() - argsData.cart?.dcCityCart?.PromoDetails?.discountValue.getValueOrDefault()).toString(),
////            taxAmount = EMPTY_STRING,
////            groceryDeliveryAmount = argsData.cart?.dcDeliveryFee.toString(),
////            nonGroceryDeliveryAmount = argsData.cart?.multiCityDeliveryFee.toString(),
////            discountCode = if (argsData.cart?.dcCityCart?.PromoDetails?.promoCode?.isNotEmpty() == true) "CODE" else "NO_CODE",
////            discountAmount = if (argsData.cart?.dcCityCart?.PromoDetails?.promoCode?.isNotEmpty() == true)
////                argsData.cart?.dcCityCart?.PromoDetails?.discountValue.getValueOrDefault()
////                    .toString()
////            else EMPTY_STRING,
////            paymentOption = "cash",
////            creditCardBin = EMPTY_STRING,
////            creditCardCompany = EMPTY_STRING,
////            isNewUser = SavyourAffiliate.isNewUser,
////            cartItems = cartItems
////        )
//    }

    private fun setAddress(addressUiData: AddressUiData) {
        _selectedAddressUiData.value = addressUiData
        sharedPreferencesManager.setDefaultAddressId(addressUiData.id)
        sharedPreferencesManager.setDefaultAddress(addressUiData.address)
    }

    fun getAddresses(arg: Boolean = false) {
        if (arg) {
            addressListViewModel.loadSavedAddresses()
        }
        launch {
            addressListViewModel.isAddressesLoaded.collect { addressesFetched ->
                _addressesFetched.value = addressesFetched
            }
        }
        launch {
            addressListViewModel.addresses.collect { addresses ->
                val defaultAddressId = sharedPreferencesManager.getDefaultAddressId()

                val defaultAddress = addresses.find { it.id == defaultAddressId }
                _addresses.value = addresses

                if (defaultAddress != null) {
                    setAddress(defaultAddress)
                } else {
                    navigateToAddressSelection()
                }
            }
        }
    }

    fun onActionPlaceOrder(
        successGroceryCallback: () -> Unit,
        successFoodCallback: (RestaurantOrderId) -> Unit,
        discountedCartTotal: Int?,
        bottomSheetNavigator: BottomSheetNavigator,
        navigator: Navigator,
        sameDayDeliveryFee: Double?,
        deliveryType: DeliveryType,
        setAddressValidationArgs: () -> Unit
    ) {
        when {
            selectedAddressUiData.value?.isSecondaryContactNeeded == true -> navigateToAddSecondaryPhoneBottomSheet(
                bottomSheetNavigator
            )

            selectedAddressUiData.value?.isOrderReturned == true -> {
                setAddressValidationArgs()
                navigateToAddressValidation(
                    bottomSheetNavigator
                )
            }

            else -> {
                val total =
                    discountedCartTotal.getValueOrDefault() + optionalFee.value.getValueOrDefault() + sameDayDeliveryFee.getValueOrDefault()
                        .toInt()
                if (selectedAddressUiData.value.isNull()) {
                    GlobalToastManager.showToast("Address select kijiye", SnackbarType.ERROR)
                    return
                }

                isOrderRequestInProcess.value = true
                selectedAddressUiData.value?.let { addressUiData ->
                    if (argsData.value?.foodCart.isNotNull()) {
                        val request = FoodCheckoutRequest(
                            addressId = addressUiData.id,
                            latitude = addressUiData.lat,
                            longitude = addressUiData.lng,
                            secondaryPhoneNumber = addressUiData.secondaryPhoneNumber,
                            city = addressUiData.city,
                            addressLine1 = EMPTY_STRING,
                            addressLine2 = addressUiData.address.ifEmpty { addressUiData.addressDescription },
                            state = addressUiData.city,
                            zip = addressUiData.city,
                            customerId = sharedPreferencesManager.getUserId(),
                            phoneNumber = sharedPreferencesManager.getPhoneNumber(),
                            customerName = sharedPreferencesManager.getUserName(),
                            platformType = OrderPlacementPlatformType.IOS.key,
                        )
                        placeFoodOrder(request) {
                            successFoodCallback(it)
                        }
                    } else {
                        val request = CheckoutRequest(
                            customer = sharedPreferencesManager.getUserId(),
                            address = addressUiData.id,
                            latitude = addressUiData.lat,
                            longitude = addressUiData.lng,
                            isCoinEnabled = argsData.value?.isDcCoinEnabled.default(),
                            dcCoinDiscount = argsData.value?.dcCoins.getValueOrDefault(),
                            paymentType = paymentMethod.value.default(),
                            paymentCnic = EMPTY_STRING,
                            paymentPhoneNumber = EMPTY_STRING,
                            optionalContributionFee = optionalFee.value.takeIf { it != 0 },
                            city = sharedPreferencesManager.getSelectedCity(),
                            packagingFee = argsData.value?.packagingFee.getValueOrDefault(),
                            platformFee = argsData.value?.platformFee.getValueOrDefault(),
                            platformType = OrderPlacementPlatformType.IOS.key,
                            paymentDetails = PaymentDetails(
                                wallet = getWalletBalance(total)?.toDouble(),
                                cod = calculatePayment(
                                    PaymentMethod.CASH_ON_DELIVERY.method,
                                    total
                                )?.toDouble(),
                                jazzcash = calculatePayment(
                                    PaymentMethod.JAZZ_CASH.method,
                                    total
                                )?.toDouble(),
                                card = calculatePayment(
                                    PaymentMethod.CARD.method,
                                    total
                                )?.toDouble(),
                                easypaisa = calculatePayment(
                                    PaymentMethod.EASYPAISA.method,
                                    total
                                )?.toDouble()
                            ),
                            deliveryType = deliveryType.key
                        )
                        placeOrder(request, total, navigator) {
                            successGroceryCallback()
                        }
                    }
                } ?: {
                    isOrderRequestInProcess.value = false
                    GlobalToastManager.showToast("Address select kijiye", SnackbarType.ERROR)
                }
            }
        }
    }

    private fun placeOrder(
        checkoutRequest: CheckoutRequest,
        discountedCartTotal: Int,
        navigator: Navigator,
        successCallback: () -> Unit
    ) {
        unZipViewStateFlow(checkoutUseCase(checkoutRequest), onSuccess = {
            val response = it as CheckoutResponse
            when (response.success) {
                true -> {
                    val oneLastStepData = OneLastStepData(
                        argsData.value?.overallCartAndCoinDiscount,
                        argsData.value?.dcCityFlashDeals!!
                    )
                    _oneLastStepData.value = oneLastStepData
                    val isSavyourOrder = false

                    eventLogger.confirmOrder(
                        cart = argsData.value?.cart,
                        orderId = response.body.id.getValueOrDefault(),
                        isSavyourOrder,
                        if (argsData.value?.packagingFee != 0) argsData.value?.packagingFee else null,
                        if (argsData.value?.platformFee != 0) argsData.value?.platformFee else null,
                        argsData.value?.parentCategories
                    )

                    if (isSaveFeeChecked.value.default()) {
                        sharedPreferencesManager.setSaveContributionValue(isSaveFeeChecked.value.default())
                        sharedPreferencesManager.setCustomContributionValue(saveCustomAmount.value.getValueOrDefault())
                        sharedPreferencesManager.setContributionValue(optionalFee.value.getValueOrDefault())

                    } else {
                        sharedPreferencesManager.setSaveContributionValue(false)
                        sharedPreferencesManager.setCustomContributionValue(ZERO_INT)
                        sharedPreferencesManager.setContributionValue(ZERO_INT)
                    }

//                    logEventSavyour(response.body.id.getValueOrDefault())
                    eventLogger.purchaseSuccess(
                        argsData.value?.cart,
                        discountedCartTotal
                    )
                    if (!response.body.isOrderAlreadyPlaced.default()) {
                        eventLogger.firstPurchaseSuccess(
                            argsData.value?.cart,
                            discountedCartTotal
                        )
                    }

                    when (paymentMethod.value) {
                        PaymentMethod.JAZZ_CASH.method -> {
                            val jazzCashAmount = calculatePayment(
                                PaymentMethod.JAZZ_CASH.method,
                                discountedCartTotal.getValueOrDefault()
                            )
                            if (jazzCashAmount.isNotNull() && jazzCashAmount.isNotZero()) {
                                val cardPayment = response.body.cardPaymentDetails
                                val cardDetails = CardDetails(
                                    cardPayment?.encryptionKey,
                                    cardPayment?.clientSecret
                                )
                            }
                        }

                        PaymentMethod.CARD.method, PaymentMethod.EASYPAISA.method -> {
                            val cardAmount = calculatePayment(
                                paymentMethod.value.default(),
                                discountedCartTotal.getValueOrDefault()
                            )
                            if (cardAmount.isNotNull() && cardAmount.isNotZero()) {
                                val cardPayment = response.body.cardPaymentDetails
                                navigator.push(
                                    CheckoutPaymentBottomSheetScreen(
                                        encryptionKey = cardPayment?.encryptionKey,
                                        clientSecret = cardPayment?.clientSecret,
                                        paymentType = paymentMethod.value.default()
                                    )
                                )
                                setRedirectCallback { redirectToPaymentLoadingScreen, id, type ->
                                    paymentId.value = id
                                    paymentMethod.value = type
                                    if (redirectToPaymentLoadingScreen) {
                                        successCallback()
                                        isOrderRequestInProcess.value = false
                                    } else {
                                        isOrderRequestInProcess.value = false
                                        redirectToPaymentSelectScreen.value = false
                                    }
                                }
                            }
                        }

                        else -> {
                            isOrderRequestInProcess.value = false
                            successCallback()
                        }
                    }
                }

                else -> {
                    isOrderRequestInProcess.value = false
                    _oosProducts.value = response.body.oosItems.orEmptyArrayList()
                }
            }
        },
            onError = {
                GlobalToastManager.showToast(
                    message = it.message,
                    type = SnackbarType.ERROR
                )
                isOrderRequestInProcess.value = false
                Firebase.crashlytics.log(it.message)
            }
        )
    }

    private fun setRedirectCallback(
        callback: (redirectToPaymentLoadingScreen: Boolean, paymentId: Double, paymentType: String) -> Unit
    ) {
        redirectTo = callback
    }

    fun triggerRedirect(
        redirectToPaymentLoadingScreen: Boolean,
        paymentId: Double,
        paymentType: String
    ) {
        redirectTo.invoke(redirectToPaymentLoadingScreen, paymentId, paymentType)
    }

    fun emptyOOSProducts() {
        _oosProducts.value = emptyList()
    }

    fun setPaymentData(
        paymentMethodUiData: PaymentMethodUiData
    ) {
        _paymentMethodUiData.value = paymentMethodUiData
    }


}