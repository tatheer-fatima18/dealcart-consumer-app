package com.localpk.app.features.checkout.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.address.data.AddressUiData
import com.localpk.app.features.address.data.toMapScreen
import com.localpk.app.features.address.presentation.MapScreen
import com.localpk.app.features.cart.components.AddressComponent
import com.localpk.app.features.cart.components.CartCheckoutSegregation
import com.localpk.app.features.cart.components.CartEstimatedDelivery
import com.localpk.app.features.cart.components.CartTopNav
import com.localpk.app.features.cart.components.PlaceOrderComponent
import com.localpk.app.features.cart.data.CartCheckoutArgs
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.features.cart.data.OOSBottomSheetArgs
import com.localpk.app.features.cart.data.PaymentMethodUiData
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.cart.viewModel.FoodCartViewModel
import com.localpk.app.features.checkout.components.CartCheckoutSummaryComponent
import com.localpk.app.features.checkout.components.DealCartSupportComponent
import com.localpk.app.features.checkout.components.DeliveryComponent
import com.localpk.app.features.checkout.components.DeliverySheetContent
import com.localpk.app.features.checkout.components.PaymentComponent
import com.localpk.app.features.checkout.components.SupportBottomSheetContent
import com.localpk.app.features.home.components.AddressBottomSheet
import com.localpk.app.features.login.components.TermsBottomSheet
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.one_last_step.view.OneLastStepScreen
import com.localpk.app.features.orders.data.GlobalEvents
import com.localpk.app.features.restaurant.presentation.OrderTrackingScreen
import com.localpk.app.features.user_location.presentation.UserLocationViewModel
import com.localpk.app.features.wallet.data.WalletBalance
import com.localpk.app.features.wallet.view.TopUpScreen
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.utils.BIT_DELAY
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.isNotNullOrEmpty
import com.localpk.app.utils.orEmpty
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.bachat_bazaar
import dealcart.composeapp.generated.resources.checkout
import dealcart.composeapp.generated.resources.dealcart_grocery
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class CartCheckoutScreen(
    private val initialDeliveryType: String? = DeliveryType.STANDARD.key
) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<CartCheckoutViewModel>()
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val sharedPreferencesManager: SharedPreferencesManager = koinInject()

        LaunchedEffect(Unit) {
            viewModel.emptyOOSProducts()
        }

        val foodCartViewModel = koinViewModel<FoodCartViewModel>()
        val cartViewModel = koinViewModel<CartViewModel>()
        val userLocationViewModel = koinViewModel<UserLocationViewModel>()
        val args =
            if (cartViewModel.isCartFoodType.value) foodCartViewModel.checkoutData.value else cartViewModel.checkoutData.value

        var isButtonSelected = false

        val selectedAddressUiData by viewModel.selectedAddressUiData.collectAsState()
        val oosProductsLiveData by viewModel.oosProducts.collectAsState()
        val isSaveFeeChecked by viewModel.isSaveFeeChecked.collectAsState(
            initial = false
        )
        val saveCustomAmount by viewModel.saveCustomAmount.collectAsState()
        val packagingFeeName by viewModel.packagingFeeName.collectAsState()
        val platformFeeName by viewModel.platformFeeName.collectAsState()
        val optionalFee by viewModel.optionalFee.collectAsState()
        val isWalletEnabled by viewModel.isWalletEnabled.collectAsState()
        val walletAmount by viewModel.walletAmount.collectAsState()
        val paymentMethod by viewModel.paymentMethod.collectAsState()
        val showAddressSelectionBottomSheet by viewModel.showAddressSelectionBottomSheet.collectAsState()
        val addresses by viewModel.addresses.collectAsState()
        val addressesFetched by viewModel.addressesFetched.collectAsState()
        val isOrderInProgress by viewModel.isOrderRequestInProcess.collectAsState()
        val hasHandledOosProducts = remember { mutableStateOf(false) }
        val localCartItems by cartViewModel.localCartItems.collectAsState()
        val localFoodCartItems by foodCartViewModel.localFoodCartItems.collectAsState()
        val openSupportSheet = remember { mutableStateOf(false) }
        val openDeliveryFeeSheet = remember { mutableStateOf(false) }
        val deliveryType = remember {
            mutableStateOf(
                initialDeliveryType?.let { typeString ->
                    DeliveryType.entries.find { it.key == typeString }
                } ?: DeliveryType.STANDARD
            )
        }

        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            viewModel._argsData.value = args
            viewModel.getAddresses()
            viewModel.isOrderRequestInProcess.value = false
        }

        LaunchedEffect(oosProductsLiveData) {
            val oosItems = oosProductsLiveData
            if (oosItems.isNotNullOrEmpty() && !hasHandledOosProducts.value) {
                hasHandledOosProducts.value = true
                cartViewModel.setIsUpdatingQuantity(true)
                cartViewModel.fetchUserCart()
                val argsValue = OOSBottomSheetArgs(
                    oosItems = oosItems
                ).apply {
                    onDismiss = {
                        bottomSheetNavigator.hide()
                        coroutineScope.launch {
                            delay(BIT_DELAY)
                        }
                        navigator.pop()
                        cartViewModel.updateArgsOOSBottomSheet(null)
                        GlobalEvents.updateIsCartFetchNeeded(false)
                        cartViewModel.updateOOsState.value = true

                    }

                }
                cartViewModel.updateArgsOOSBottomSheet(argsValue)
                viewModel.navigateToOOsBottomSheet(bottomSheetNavigator)
            }
        }

        StatusBarIconColor(backgroundColor = SurfaceBackground)
        Surface(
            color = SurfaceBackground
        )
        {
            CartCheckoutScreen(
                args = args,
                selectedAddressUiData = selectedAddressUiData,
                isSaveFeeChecked = isSaveFeeChecked,
                saveCustomAmount = saveCustomAmount,
                packagingFeeName = packagingFeeName,
                platformFeeName = platformFeeName,
                optionalFee = optionalFee,
                walletAmount = walletAmount,
                isWalletEnabled = isWalletEnabled,
                paymentMethod = paymentMethod,
                isOrderInProgress = isOrderInProgress,
                jazzPhoneNumber = sharedPreferencesManager.getJazzPhoneNumber(),
                paymentId = viewModel.paymentId.value.getValueOrDefault(),
                phoneNumber = sharedPreferencesManager.getPhoneNumber(),
                contributionValue = sharedPreferencesManager.getContributionValue(),
                onEditAddressClick = { viewModel.navigateToAddressSelection() },
                onEditPhoneNumberClick = {
                    //   eventLogger.clickEdit()
                    viewModel.onActionChangePhoneNumber(bottomSheetNavigator)
                },
                onAddPhoneNumberClick = {
                    viewModel.onActionChangePhoneNumber(bottomSheetNavigator)
                },
                updateWalletUsage = { viewModel.updateWalletUsage(it) },
                onClick = {
                    navigator.push(TopUpScreen())
                    //eventLogger.initiateTopUp(TopUpSource.CHECKOUT.source)
                },
                onContributionSelected = {
                    viewModel.optionalFee.value = it ?: ZERO_INT
                },
                onSaveCustomAmountChanged = {
                    viewModel.saveCustomAmount.value = it
                },
                onSaveFeeCheckedChanged = {
                    viewModel.isSaveFeeChecked.value = it
                },
                onButtonSelectedChanged = { isButtonSelected = it },
                onLearnMoreClick = { openSupportSheet.value = true },
                onActionPlaceOrder = {
                    viewModel.onActionPlaceOrder(
                        successGroceryCallback = {
                            cartViewModel.clearLocalCartItems()
                            cartViewModel.updateDeliveryType(DeliveryType.STANDARD)
                            navigator.push(OneLastStepScreen())
                        },
                        successFoodCallback = {
                            foodCartViewModel.clearFoodCartState()
                            navigator.pop()
                            navigator.push(
                                OrderTrackingScreen(
                                    orderId = it.orderId,
                                    restaurantId = it.restaurantId,
                                    isCheckout = it.isCheckout,
                                    fromDetails = it.fromDetails
                                )
                            )

                        },
                        discountedCartTotal = args?.discountedCartTotal,
                        bottomSheetNavigator = bottomSheetNavigator,
                        navigator = navigator,
                        sameDayDeliveryFee = if (deliveryType.value == DeliveryType.EXPRESS) args?.cart?.sameDayDeliveryFee else 0.0,
                        deliveryType = deliveryType.value,
                        setAddressValidationArgs = {
                            cartViewModel.updateAddressValidationBottomSheetArgs {
                                navigator.push(MapScreen(navigationFrom = ScreenSource.CART.key))
                            }
                        }
                    )
                },
                formattedTotalWalletBalance = viewModel.formattedTotalWalletBalance(),
                calculatePayment = {
                    viewModel.calculatePayment(
                        it,
                        args?.discountedCartTotal.getValueOrDefault()
                    )
                },
                onDeliveryFeeClick = { openDeliveryFeeSheet.value = true },
                deliveryType = deliveryType,
                onDeliveryTypeSelection = {
                    cartViewModel.updateDeliveryType(it)
                },
                onClickToSelectPayment = {
                    viewModel.setPaymentData(
                        PaymentMethodUiData(
                            paymentType = paymentMethod.default(),
                            paymentId = viewModel.paymentId.value.getValueOrDefault(),
                            listener = { paymentType, id ->
                                viewModel.paymentMethod.value = paymentType
                                viewModel.paymentId.value = id
                            },
                            isFood = args?.foodCart.isNotNull()
                        )
                    )
                }
            )

            if (showAddressSelectionBottomSheet) {
                AddressBottomSheet(
                    onDismiss = {
                        viewModel.showAddressSelectionBottomSheet.value =
                            false
                    }, addresses = addresses,
                    areAddressLoaded = addressesFetched,
                    onClearFoodCart = {
                        cartViewModel.clearCart(isFoodCart = true) {
                            navigator.push(MapScreen(navigationFrom = ScreenSource.CART.key))
                            viewModel.showAddressSelectionBottomSheet.value =
                                false
                        }
                    },
                    onConfirm = { model ->
                        userLocationViewModel.getUserLocationWarehouse(
                            model.lat,
                            model.lng,
                            model.city,
                            addressDialogSuccessCallback = {
                                sharedPreferencesManager.setDefaultAddressId(model.id)
                                sharedPreferencesManager.setDefaultAddress(model.address)
                                viewModel.showAddressSelectionBottomSheet.value = false
                                navigator.pop()

                            }
                        )
                    },
                    defaultAddressId = sharedPreferencesManager.getDefaultAddressId(),
                    onAddressChange = { model ->
                        cartViewModel.clearCart(isFoodCart = true) {
                            userLocationViewModel.getUserLocationWarehouse(
                                model.lat,
                                model.lng,
                                model.city,
                                successCallback = {
                                    sharedPreferencesManager.setDefaultAddressId(model.id)
                                    sharedPreferencesManager.setDefaultAddress(model.address)
                                    viewModel.showAddressSelectionBottomSheet.value = false
                                    navigator.pop()

                                }
                            )
                        }

                    },
                    onEdit = { address ->
                        navigator.push(
                            address.copy(navigationFrom = ScreenSource.CART.key).toMapScreen()
                        )
                    },
                    cart = localCartItems.size,
                    foodCart = localFoodCartItems.size,
                    newAddressClick = {
                        navigator.push(MapScreen(navigationFrom = ScreenSource.CART.key))
                    }

                )
            }

            AnimatedVisibility(
                visible = openSupportSheet.value,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )

            ) {
                TermsBottomSheet(
                    onClose = { openSupportSheet.value = false },
                    content = { SupportBottomSheetContent() })
            }

            AnimatedVisibility(
                visible = openDeliveryFeeSheet.value,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )

            ) {
                TermsBottomSheet(
                    onClose = { openDeliveryFeeSheet.value = false },
                    content = {
                        DeliverySheetContent(
                            formattedDeliveryDate = args?.cart?.dcCityCart?.formattedDeliveryDate()
                                .default(),
                            deliveryType = deliveryType.value,
                            formattedSameDayDeliveryFee = args?.cart?.formattedSameDayDeliveryFee()
                                .default()
                        ) { selectedDeliveryType ->
                            deliveryType.value = selectedDeliveryType
                            cartViewModel.updateDeliveryType(selectedDeliveryType)
                            openDeliveryFeeSheet.value = false
                        }
                    },
                    heightModifier = Modifier.wrapContentHeight()
                )
            }
        }
    }

    @Composable
    fun CartCheckoutScreen(
        args: CartCheckoutArgs?,
        selectedAddressUiData: AddressUiData?,
        isSaveFeeChecked: Boolean,
        saveCustomAmount: Int?,
        packagingFeeName: String?,
        platformFeeName: String?,
        optionalFee: Int?,
        walletAmount: WalletBalance?,
        isWalletEnabled: Boolean?,
        paymentMethod: String?,
        isOrderInProgress: Boolean?,
        jazzPhoneNumber: String,
        paymentId: Double,
        phoneNumber: String,
        contributionValue: Int,
        onEditAddressClick: () -> Unit,
        onEditPhoneNumberClick: () -> Unit,
        onAddPhoneNumberClick: () -> Unit,
        updateWalletUsage: (checked: Boolean) -> Unit,
        onClick: () -> Unit,
        onContributionSelected: (Int?) -> Unit,
        onSaveCustomAmountChanged: (Int) -> Unit,
        onSaveFeeCheckedChanged: (Boolean) -> Unit,
        onButtonSelectedChanged: (Boolean) -> Unit,
        onLearnMoreClick: () -> Unit,
        onActionPlaceOrder: () -> Unit,
        formattedTotalWalletBalance: String,
        calculatePayment: (String) -> Int?,
        onDeliveryFeeClick: () -> Unit,
        deliveryType: MutableState<DeliveryType>,
        onDeliveryTypeSelection: (DeliveryType) -> Unit,
        onClickToSelectPayment: () -> Unit,
    ) {
        val navigator = LocalNavigator.currentOrThrow
        Column(
            modifier = Modifier
                .fillMaxSize().statusBarsPadding(),
        ) {
            CartTopNav(
                onBackClick = {
                    GlobalEvents.updateIsCartFetchNeeded(false)
                    navigator.pop()
                },
                screen = stringResource(Res.string.checkout)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (args?.foodCart.isNotNull())
                    CartEstimatedDelivery(args?.foodCart?.estimatedDeliveryTime.default())
                else
                    Spacer(modifier = Modifier.height(2.dp))
                AddressComponent(
                    selectedAddressUiData = selectedAddressUiData,
                    defaultPhoneNumber = phoneNumber,
                    secondaryPhoneNumber = "0${selectedAddressUiData?.secondaryPhoneNumber.orEmpty()}",
                    onEditAddressClick = {
                        onEditAddressClick()
                    },
                    onEditPhoneNumberClick = {
                        onEditPhoneNumberClick()
                    },
                    onAddPhoneNumberClick = {
                        onAddPhoneNumberClick()
                    }
                )
                args?.cart?.dcCityCart?.let { dcCityCart ->
                    if (dcCityCart.isEnabled)
                        CartCheckoutSegregation(
                            storeName = stringResource(Res.string.dealcart_grocery),
                            isPostexDelivery = dcCityCart.isPostexDelivery(),
                            formattedDeliveryDate = dcCityCart.formattedDeliveryDate(),
                            cartDetails = dcCityCart.cartDetails + dcCityCart.flashDeals,
                            onClickDeliveryFee = {
                                if (dcCityCart.isPostexDelivery().not()) {
                                    onDeliveryFeeClick()
                                }
                            },
                            deliveryType = deliveryType.value,
                            isDeliverySelectionAvailable = (args.isDCCityOrder.default() && args.isMultiCityOrder.default()
                                .not() && args.cart.sellerCarts?.any { it.isEnabled } == false).not() && dcCityCart.isPostexDelivery()
                                .not() && args.cart.isSameDeliveryEligible && RemoteConfigs.isSameDayDeliveryEnabled()
                        )
                }
                args?.cart?.multiCityCart?.let { multiCityCart ->
                    if (multiCityCart.isEnabled)
                        CartCheckoutSegregation(
                            storeName = stringResource(Res.string.bachat_bazaar),
                            isPostexDelivery = multiCityCart.isPostexDelivery(),
                            formattedDeliveryDate = multiCityCart.formattedDeliveryDate(),
                            cartDetails = multiCityCart.cartDetails + multiCityCart.flashDeals
                        )
                }
                args?.cart?.sellerCarts?.let { sellerCart ->
                    sellerCart.forEach {
                        if (it.isEnabled)
                            CartCheckoutSegregation(
                                storeName = it.sellerName.split("_").joinToString(" "),
                                isPostexDelivery = it.isPostexDelivery(),
                                formattedDeliveryDate = it.formattedDeliveryDate(),
                                cartDetails = it.cartDetails + it.flashDeals
                            )
                    }
                }

                if ((args?.isDCCityOrder.default()
                            && args?.isMultiCityOrder.default().not()
                            && args?.cart?.sellerCarts?.any { it.isEnabled } == false
                            ) && args.cart.isSameDeliveryEligible.default() && RemoteConfigs.isSameDayDeliveryEnabled()
                ) {
                    DeliveryComponent(
                        formattedDeliveryDate = args.cart.dcCityCart?.formattedDeliveryDate()
                            .default(),
                        deliveryType = deliveryType.value,
                        formattedSameDayDeliveryFee = args.cart.formattedSameDayDeliveryFee()
                            .default()
                    )
                    { selectedDeliveryType ->
                        deliveryType.value = selectedDeliveryType
                        onDeliveryTypeSelection(selectedDeliveryType)
                    }
                }
                PaymentComponent(
                    discountedCartTotal = args?.discountedCartTotal.getValueOrDefault(),
                    isWalletEnabled = isWalletEnabled,
                    walletAmount = walletAmount,
                    foodCart = args?.foodCart,
                    updateWalletUsage = { updateWalletUsage(it) },
                    onClick = {
                        onClick()
                    },
                    paymentMethod = paymentMethod,
                    jazzPhoneNumber = jazzPhoneNumber,
                    paymentId = paymentId,
                    onClickToSelectPayment = {
                        onClickToSelectPayment()
                        navigator.push(
                            SelectPaymentMethodScreen()
                        )
                    }
                )
                if (
                    RemoteConfigs.isOptionalFeeEnabled() &&
                    args?.isDCCityOrder.default()
                    && args?.isMultiCityOrder.default().not()
                    && args?.cart?.sellerCarts?.any { it.isEnabled } == false
                ) {
                    DealCartSupportComponent(
                        isSaveFeeChecked = isSaveFeeChecked,
                        saveCustomAmount = saveCustomAmount,
                        contributionValue = contributionValue,
                        onContributionSelected = {
                            onContributionSelected(it)
                        },
                        onSaveCustomAmountChanged = {
                            onSaveCustomAmountChanged(it)
                        },
                        onSaveFeeCheckedChanged = {
                            onSaveFeeCheckedChanged(it)
                        },
                        onButtonSelectedChanged = { onButtonSelectedChanged(it) },
                        onLearnMoreClick = { onLearnMoreClick() }
                    )
                }
                CartCheckoutSummaryComponent(
                    cart = args?.cart,
                    packagingFeeName = packagingFeeName,
                    platformFeeName = platformFeeName,
                    optionalFee = optionalFee,
                    isWalletEnabled = isWalletEnabled,
                    walletAmount = walletAmount,
                    discountedCartTotal = args?.discountedCartTotal.getValueOrDefault(),
                    paymentMethod = paymentMethod,
                    platformFee = args?.platformFee.getValueOrDefault(),
                    packagingFee = args?.packagingFee.getValueOrDefault(),
                    dcDeliveryFee = args?.cart?.dcDeliveryFee.getValueOrDefault(),
                    multiCityDeliveryFee = args?.cart?.multiCityDeliveryFee.getValueOrDefault(),
                    deliveryFee = args?.foodCart?.additionalFees?.deliveryFee.getValueOrDefault(),
                    foodCart = args?.foodCart,
                    totalBill = args?.discountedCartTotal.getValueOrDefault() + optionalFee.getValueOrDefault() + if (deliveryType.value == DeliveryType.EXPRESS) args?.cart?.sameDayDeliveryFee.getValueOrDefault()
                        .toInt() else 0,
                    isSellerPromoApplied = args?.cart?.sellerCarts?.any { it.PromoDetails?.isValidated == true } == true,
                    isMultiCityPromoApplied = args?.cart?.multiCityCart?.PromoDetails?.isValidated == true,
                    isDCCityPromoApplied = args?.cart?.dcCityCart?.PromoDetails?.isValidated == true,
                    totalPromoDiscount = args?.totalPromoDiscount.getValueOrDefault(),
                    dcCoinDiscount = args?.dcCoinDiscount.getValueOrDefault(),
                    dcCoinEnabled = args?.isDcCoinEnabled == true,
                    formattedDcCoinDiscount = args?.formattedDcCoinDiscount().default(),
                    formattedTotalWalletBalance = formattedTotalWalletBalance,
                    calculatePayment = { calculatePayment(it) },
                    paymentType = paymentMethod.default(),
                    selectedDeliveryType = deliveryType.value
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
            PlaceOrderComponent(
                onCheckOutClick = {
                    onActionPlaceOrder()
                },
                overAllDiscount = args?.formattedOverallCartAndCoinDiscount().default(),
                discountedCartTotal = args?.discountedCartTotal.getValueOrDefault() + optionalFee.getValueOrDefault() + if (deliveryType.value == DeliveryType.EXPRESS) args?.cart?.sameDayDeliveryFee.getValueOrDefault()
                    .toInt() else 0,
                isOrderInProgress = isOrderInProgress
            )
        }
    }
}