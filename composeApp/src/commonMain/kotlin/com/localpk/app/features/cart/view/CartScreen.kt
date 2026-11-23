package com.localpk.app.features.cart.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.common_components.GlobalToastManager
import com.localpk.app.common_components.SnackbarType
import com.localpk.app.features.cart.components.CartCheckOutView
import com.localpk.app.features.cart.components.CartEstimatedDelivery
import com.localpk.app.features.cart.components.CartLoadingComponent
import com.localpk.app.features.cart.components.CartPromoCodeView
import com.localpk.app.features.cart.components.CartSummaryComponent
import com.localpk.app.features.cart.components.CartTopNav
import com.localpk.app.features.cart.components.CartView
import com.localpk.app.features.cart.components.DCCoinsView
import com.localpk.app.features.cart.components.EmptyCartView
import com.localpk.app.features.cart.components.FoodCartProduct
import com.localpk.app.features.cart.components.RecommendedEngineSwimlane
import com.localpk.app.features.cart.components.SwipeToDeleteContainer
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.features.cart.data.PromoNavigationArgs
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.cart.viewModel.FoodCartViewModel
import com.localpk.app.features.checkout.components.DeliverySheetContent
import com.localpk.app.features.checkout.view.CartCheckoutScreen
import com.localpk.app.features.checkout.view.CartCheckoutViewModel
import com.localpk.app.features.home.presentation.HomeScreen
import com.localpk.app.features.login.components.TermsBottomSheet
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.login.presentation.PhoneInputScreen
import com.localpk.app.features.orders.data.GlobalEvents
import com.localpk.app.features.recommendation_engine.viewModel.RecommendationEngineViewModel
import com.localpk.app.features.recommendation_engine.viewModel.RecommendationEngineViewModel.Companion.RECOMMEND_BEST_SELLER
import com.localpk.app.features.restaurant.components.FoodSummaryComponent
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNullOrEmpty
import com.localpk.app.utils.orEmptyArrayList
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.bachat_bazaar
import dealcart.composeapp.generated.resources.cart
import dealcart.composeapp.generated.resources.dealcart_grocery
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class CartScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = koinViewModel<CartViewModel>()
        val navigate = LocalNavigator.currentOrThrow
        val eventLogger: EventLogger = koinInject()

        // Observe and execute callback when screen becomes active
        val onScreenResume by viewModel.onScreenResume.collectAsState()
        LaunchedEffect(onScreenResume) {
            onScreenResume?.let { callback ->
                callback()
            }
        }
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val isCartFoodType by viewModel.isCartFoodType.collectAsState()


        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()

        val isLoggedIn = sharedPreferencesManager.isLoggedIn()


        if (isCartFoodType && isLoggedIn) {
            val foodCartViewModel = koinViewModel<FoodCartViewModel>()

            val isCartLoading by viewModel.isCartLoading.collectAsState()
            val isUpdatingQuantity by viewModel.isUpdatingQuantity.collectAsState()
            val foodCartState by foodCartViewModel.foodCartState.collectAsState()
            val formattedOverallCartAndCoinDiscount by foodCartViewModel.formattedOverallCartAndCoinDiscount.collectAsState()
            val formattedDiscountedCartTotal by foodCartViewModel.formattedDiscountedCartTotal.collectAsState()
            val isDiscountSwitchChecked by foodCartViewModel.isDiscountSwitchChecked.collectAsState()
            val dcCoinErrorMessage by foodCartViewModel.dcCoinErrorMessage.collectAsState()
            val dcCoinWarningTrigger by foodCartViewModel.dcCoinWarningTrigger.collectAsState()

            LaunchedEffect(foodCartViewModel.isGetFoodCartFailed) {
                if (foodCartViewModel.isGetFoodCartFailed.value) {
                    GlobalToastManager.showToast(
                        message = "Unable to fetch cart details",
                        type = SnackbarType.ERROR
                    )
                    foodCartViewModel.isGetFoodCartFailed.value = false
                }

            }

            LaunchedEffect(Unit) {
                viewModel._isCartLoading.value = true
                foodCartViewModel.getFoodCart {
                    viewModel.setIsCartLoading(false)
                    viewModel.setIsUpdatingQuantity(false)
                }
            }

            Surface(color = SurfaceBackground) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    if (isUpdatingQuantity) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = ColorPrimary
                        )
                    }
                    CartTopNav(
                        onBackClick = { navigate.pop() },
                        screen = stringResource(Res.string.cart)
                    )
                    if (isCartLoading) {
                        CartLoadingComponent()
                    } else {
                        if (foodCartState?.id.getValueOrDefault() == ZERO_LONG ||
                            foodCartState?.items.isNullOrEmpty()
                        ) {
                            EmptyCartView {
                                navigate.push(HomeScreen())
                            }
                        } else {
                            Column {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .verticalScroll(rememberScrollState()),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CartEstimatedDelivery(foodCartState?.estimatedDeliveryTime.default())
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp)
                                            .background(
                                                color = Color.White,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        foodCartState?.items?.forEach { singleProduct ->
                                            SwipeToDeleteContainer(
                                                item = singleProduct,
                                                onDelete = {
                                                    foodCartViewModel.removeFoodCartItem(
                                                        singleProduct.cartItemId.getValueOrDefault(),
                                                        isCart = true
                                                    ) {
                                                        foodCartViewModel.getFoodCart()
                                                        viewModel.setIsUpdatingQuantity(false)
                                                    }
                                                }
                                            ) {
                                                FoodCartProduct(
                                                    singleProduct
                                                )
                                            }
                                        }
                                        CartPromoCodeView(
                                            promoDetails = foodCartState?.promoDetail?.map(),
                                            isPromoCodeValidated = foodCartState?.promoDetail?.map()?.isValidated.default(),
                                            onAddPromoCode = {
                                                foodCartViewModel.navigateToMorePromos(
                                                    bottomSheetNavigator,
                                                    loginCallBack = {
                                                        navigate.push(
                                                            PhoneInputScreen(
                                                                navSource = ScreenSource.CART.key
                                                            )
                                                        )

                                                    }
                                                )
                                            },
                                            onRemovePromoCode = {
                                                //foodCartViewModel.promoId_.value = null
                                                foodCartViewModel.applyFoodPromo(
                                                    promoId = null,
                                                    isPromo = true,
                                                )
                                            }
                                        )
                                    }
                                    if (isLoggedIn) {
                                        DCCoinsView(
                                            amountInCoins = foodCartState?.formattedTotalDCCoins()
                                                .default(),
                                            amountInRupees = foodCartState?.formattedAmountInCurrency()
                                                .default(),
                                            isDCCoinEnabled = foodCartState?.availableCoins?.isDCCoinEnabled == true,
                                            isDiscountSwitchChecked = isDiscountSwitchChecked,
                                            onDCCoinsSwitchClicked = {
                                                if (foodCartState?.totalItemsAmount.getValueOrDefault() <= foodCartViewModel.useDCCoinsAmountLimit)
                                                    foodCartViewModel.onActionShowDcCoinWarning()
                                                else {
                                                    foodCartViewModel.updateCartDiscount(
                                                        it
                                                    )
                                                    if (it)
                                                        foodCartViewModel.applyFoodPromo(
                                                            dcCoins = foodCartState?.availableCoins?.totalDCCoins,
                                                        )
                                                    else
                                                        foodCartViewModel.applyFoodPromo(
                                                            dcCoins = null
                                                        )
                                                }
                                            },
                                            dcCoinErrorMessage = dcCoinErrorMessage.default(),
                                            dcCoinWarningTrigger = dcCoinWarningTrigger,
                                            useDCCoinsAmountLimit = foodCartViewModel.useDCCoinsAmountLimit,
                                            cartTotal = foodCartState?.totalItemsAmount.getValueOrDefault()
                                                .toDouble(),
                                            hasRestrictedPromotionalDiscountDC = false,
                                            hasRestrictedPromotionalDiscountMultiCity = false
                                        )
                                    }
                                    foodCartState?.let {
                                        FoodSummaryComponent(
                                            formattedOrderTotal = it.formattedOrderTotal(),
                                            platformFee = it.additionalFees?.platformFee.getValueOrDefault(),
                                            deliveryFee = it.additionalFees?.deliveryFee.getValueOrDefault(),
                                            promoCodeDiscount = it.discounts?.promoDiscount.getValueOrDefault()
                                                .toLong(),
                                            dcCoinDiscount = it.discounts?.coinDiscount.getValueOrDefault()
                                                .toLong(),
                                            formattedPromoCodeDiscount = it.formattedPromoCodeDiscount()
                                                .default(),
                                            formattedGrandTotal = formattedDiscountedCartTotal.default(),
                                            formattedDcCoinDiscount = it.formattedAmountInCurrency(),
                                            taxes = it.additionalFees?.valueAddedTax.getValueOrDefault(),
                                            formattedTax = it.formattedTax().default(),
                                            modifier = Modifier.padding(horizontal = 8.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(0.dp))
                                }

                                CartCheckOutView(
                                    isLoggedIn = true,
                                    onCheckOutClick = {
                                        foodCartViewModel.onCheckOutClick(
                                            successCallback = {
                                                navigate.push(
                                                    CartCheckoutScreen()
                                                )
                                            },
                                            loginCallBack = {
                                                navigate.push(
                                                    PhoneInputScreen(
                                                        navSource = ScreenSource.CART.key
                                                    )
                                                )
                                            }
                                        )
                                    },
                                    overAllDiscount = formattedOverallCartAndCoinDiscount.default(),
                                    discountedCartTotal = formattedDiscountedCartTotal.default(),
                                    isAnyBucketChecked = foodCartState?.totalItemsAmount.getValueOrDefault() >= foodCartState?.minimumOrderValue.getValueOrDefault(),
                                    totalAmount = foodCartState?.totalItemsAmount.getValueOrDefault(),
                                    mov = foodCartState?.minimumOrderValue.getValueOrDefault(),
                                    isFood = true,
                                    itemCount = foodCartState?.items?.size.getValueOrDefault()
                                )
                            }
                        }
                    }

                }

            }

        } else {
            val recommendationEngineViewModel = koinViewModel<RecommendationEngineViewModel>()

            val cartCheckoutViewModel = koinViewModel<CartCheckoutViewModel>()
            val isCartLoading by viewModel.isCartLoading.collectAsState()
            val isUpdatingQuantity by viewModel.isUpdatingQuantity.collectAsState()
            val cartState by viewModel.cartState.collectAsState()
            val formattedOverallCartAndCoinDiscount by viewModel.formattedOverallCartAndCoinDiscount.collectAsState()
            val formattedDiscountedCartTotal by viewModel.formattedDiscountedCartTotal.collectAsState()
            val isDiscountSwitchChecked by viewModel.isDiscountSwitchChecked.collectAsState()
            val dcCoinErrorMessage by viewModel.dcCoinErrorMessage.collectAsState()
            val dcCoinWarningTrigger by viewModel.dcCoinWarningTrigger.collectAsState()
            val recommendationList by recommendationEngineViewModel.recommendationList.collectAsState()
            val showBundleDialog by viewModel.showBundleDialog.collectAsState()
            val bundleQuantity by viewModel.bundleQuantity.collectAsState()
            val packagingFeeName = RemoteConfigs.getPackagingFeeName()
            val platformFeeName = RemoteConfigs.getPlatformFeeName()
            val localCartItems by viewModel.localCartItems.collectAsState()
            val openDeliveryFeeSheet = remember { mutableStateOf(false) }
            val selectedDeliveryType by viewModel.selectedDeliveryType.collectAsState()
            val isSameDeliveryEligible = cartState?.isSameDeliveryEligible.default()
            val isGetCartDetailsFailed by viewModel.isGetCartDetailsFailed.collectAsState()
            val oosProductsLiveData by cartCheckoutViewModel.oosProducts.collectAsState()

            LaunchedEffect(isGetCartDetailsFailed) {
                if (isGetCartDetailsFailed) {
                    GlobalToastManager.showToast(
                        message = "Unable to fetch cart details",
                        type = SnackbarType.ERROR
                    )
                    viewModel.isGetCartDetailsFailed.value = false
                }

            }


            LaunchedEffect(Unit) {
                when {
                    sharedPreferencesManager.getSelectedWarehouseId() != 1L -> {
                        viewModel._isCartLoading.value = true
                        viewModel.fetchUserCart(
                            isCartScreen = true
                        ) {
                            viewModel.removeGroceryItems(
                                isCart = true,
                                successCallback = {
                                    viewModel.fetchUserCart(isCartScreen = true)
                                }
                            )
                        }
                    }

                    else -> {
                        if (GlobalEvents.isCartFetchNeeded.value) {
                            viewModel._isCartLoading.value = true
                            viewModel.fetchUserCart(isCartScreen = true)
                        }
                        GlobalEvents.updateIsCartFetchNeeded(true)
                    }
                }

                if (localCartItems.isNotNullOrEmpty())
                    recommendationEngineViewModel.getRecommendationItems(
                        RECOMMEND_BEST_SELLER,
                        ScreenSource.CART.key,
                        0,
                        cartState
                    )
            }

            LaunchedEffect(viewModel.isLoader.value, viewModel.updateOOsState.value) {
                if (!viewModel.isLoader.value && viewModel.updateOOsState.value) {
                    viewModel.updateOOsState.value = false
                    viewModel.removeOosItems(
                        OOSItems = oosProductsLiveData,
                        emptyOOSItemsFromCheckout = {
                            cartCheckoutViewModel.emptyOOSProducts()
                        }
                    )
                }
            }
            StatusBarIconColor(backgroundColor = SurfaceBackground)
            Surface(color = SurfaceBackground) {

                Column(
                    modifier = Modifier
                        .fillMaxSize().statusBarsPadding()
                ) {
                    // Loading bar above CartTopNav
                    if (isUpdatingQuantity) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = ColorPrimary
                        )
                    }
                    CartTopNav(
                        onBackClick = { navigate.pop() },
                        screen = stringResource(Res.string.cart)
                    )
                    if (isCartLoading) {
                        CartLoadingComponent()
                    } else {
                        if (
                            cartState?.id.getValueOrDefault() == ZERO_INT ||
                            (
                                    (cartState?.dcCityCart?.cartDetails.isNullOrEmpty())
                                            && (cartState?.sellerCarts?.isNullOrEmpty() == true)
                                            && (cartState?.multiCityCart?.cartDetails.isNullOrEmpty())
                                            && (cartState?.dcCityCart?.flashDeals.isNullOrEmpty())
                                    )
                        ) {
                            EmptyCartView { navigate.push(HomeScreen()) }
                        } else {
                            Column {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .verticalScroll(rememberScrollState()),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Spacer(modifier = Modifier.height(0.1.dp))
                                    if (cartState?.dcCityCart?.cartDetails?.isNotEmpty() == true || cartState?.dcCityCart?.flashDeals?.isNotEmpty() == true) {
                                        val dcCartDetails =
                                            cartState?.dcCityCart?.cartDetails.orEmptyArrayList() + cartState?.dcCityCart?.flashDeals.orEmptyArrayList()
                                        val itemIds = dcCartDetails.map { it.id }

                                        cartState?.dcCityCart?.isEnabled =
                                            if (isLoggedIn) cartState?.dcCityCart?.isEnabled == true else true
                                        CartView(
                                            singleProductItems = cartState?.dcCityCart?.cartDetails.orEmptyArrayList(),
                                            flashDealProductItems = cartState?.dcCityCart?.flashDeals.orEmptyArrayList(),
                                            promoDetails = cartState?.dcCityCart?.PromoDetails,
                                            isPromoCodeValidated = cartState?.dcCityCart?.PromoDetails?.isValidated == true,
                                            toggleBucketCheck = {
                                                viewModel.onActionPromoCode(
                                                    it,
                                                    cartState?.dcCityCart?.PromoDetails?.promoCode,
                                                    itemIds
                                                )
                                            },
                                            isPostexDelivery = cartState?.dcCityCart?.isPostexDelivery() == true,
                                            isBucketChecked = cartState?.dcCityCart?.isEnabled == true,
                                            storeName = stringResource(Res.string.dealcart_grocery),
                                            formattedDeliveryDate = cartState?.dcCityCart?.formattedDeliveryDate()
                                                .default(),
                                            formattedDeliveryFee = cartState?.dcCityCart?.formattedDeliveryFee()
                                                .default(),
                                            amountNeededForFreeDelivery = cartState?.dcCityCart?.amountNeededForFreeDelivery.getValueOrDefault(),
                                            getMinOrderVal = cartState?.dcCityCart?.getMinOrderVal()
                                                .getValueOrDefault(),
                                            cartTotal = cartState?.dcCityCart?.cartTotal.getValueOrDefault(),
                                            isBundle = cartState?.dcCityCart?.isBundle() == true,
                                            onRemovePromoCode = {
                                                viewModel.onActionPromoCode(
                                                    cartState?.dcCityCart?.isEnabled == true,
                                                    null,
                                                    itemIds
                                                )
                                            },
                                            onAddPromoCode = {
                                                viewModel._promoNavigationArgs.value =
                                                    PromoNavigationArgs(
                                                        cartState?.dcCityCart?.isEnabled == true,
                                                        itemIds,
                                                    )
                                                if (sharedPreferencesManager.isLoggedIn()) {
                                                    viewModel.navigateToMorePromos(
                                                        bottomSheetNavigator
                                                    )

                                                } else {
                                                    navigate.push(PhoneInputScreen(navSource = ScreenSource.PROMO_CART.key))
                                                }

                                            },
                                            showBundleDialog = showBundleDialog,
                                            bundleQuantity = bundleQuantity,
                                            dcCartBundle = cartState?.dcCityCart?.bundle,
                                            onClickDeliveryFee = {
                                                if (cartState?.dcCityCart?.isPostexDelivery() == false && isSameDeliveryEligible) {
                                                    openDeliveryFeeSheet.value = true
                                                }
                                            },
                                            deliveryType = selectedDeliveryType,
                                            isSameDeliveryEligible = isSameDeliveryEligible && RemoteConfigs.isSameDayDeliveryEnabled()
                                        )
                                    }
                                    if (cartState?.multiCityCart?.cartDetails?.isNotEmpty() == true || cartState?.multiCityCart?.flashDeals?.isNotEmpty() == true) {
                                        val multiCartDetails =
                                            cartState?.multiCityCart?.cartDetails.orEmptyArrayList() + cartState?.multiCityCart?.flashDeals.orEmptyArrayList()
                                        val itemIds = multiCartDetails.map { it.id }
                                        cartState?.multiCityCart?.isEnabled =
                                            if (isLoggedIn) cartState?.multiCityCart?.isEnabled == true else true
                                        CartView(
                                            singleProductItems = cartState?.multiCityCart?.cartDetails.orEmptyArrayList(),
                                            flashDealProductItems = cartState?.multiCityCart?.flashDeals.orEmptyArrayList(),
                                            promoDetails = cartState?.multiCityCart?.PromoDetails,
                                            isPromoCodeValidated = cartState?.multiCityCart?.PromoDetails?.isValidated == true,
                                            toggleBucketCheck = {
                                                viewModel.onActionPromoCode(
                                                    it,
                                                    cartState?.multiCityCart?.PromoDetails?.promoCode,
                                                    itemIds
                                                )
                                            },
                                            isPostexDelivery = cartState?.multiCityCart?.isPostexDelivery() == true,
                                            isBucketChecked = cartState?.multiCityCart?.isEnabled == true,
                                            storeName = stringResource(Res.string.bachat_bazaar),
                                            formattedDeliveryDate = cartState?.multiCityCart?.formattedDeliveryDate()
                                                .default(),
                                            formattedDeliveryFee = cartState?.multiCityCart?.formattedDeliveryFee()
                                                .default(),
                                            amountNeededForFreeDelivery = cartState?.multiCityCart?.amountNeededForFreeDelivery.getValueOrDefault(),
                                            getMinOrderVal = cartState?.multiCityCart?.getMinOrderVal()
                                                .getValueOrDefault(),
                                            cartTotal = cartState?.multiCityCart?.cartTotal.getValueOrDefault(),
                                            isBundle = cartState?.multiCityCart?.isBundle() == true,
                                            onRemovePromoCode = {
                                                viewModel.onActionPromoCode(
                                                    cartState?.multiCityCart?.isEnabled == true,
                                                    null,
                                                    itemIds
                                                )
                                            },
                                            onAddPromoCode = {
                                                viewModel._promoNavigationArgs.value =
                                                    PromoNavigationArgs(
                                                        cartState?.multiCityCart?.isEnabled == true,
                                                        itemIds,
                                                    )
                                                if (sharedPreferencesManager.isLoggedIn()) {
                                                    viewModel.navigateToMorePromos(
                                                        bottomSheetNavigator
                                                    )
                                                } else {
                                                    navigate.push(PhoneInputScreen(navSource = ScreenSource.PROMO_CART.key))
                                                }
                                            },
                                            onClickDeliveryFee = {
                                                if (cartState?.multiCityCart?.isPostexDelivery() == false && isSameDeliveryEligible) {
                                                    openDeliveryFeeSheet.value = true
                                                }
                                            },
                                            deliveryType = DeliveryType.STANDARD,
                                            isSameDeliveryEligible = isSameDeliveryEligible && RemoteConfigs.isSameDayDeliveryEnabled(),
                                        )
                                    }
                                    cartState?.sellerCarts?.forEach { segregationCart ->
                                        val sellerCartDetails =
                                            segregationCart.cartDetails + segregationCart.flashDeals
                                        val itemIds = sellerCartDetails.map { it.id }
                                        segregationCart.isEnabled =
                                            if (isLoggedIn) segregationCart.isEnabled else true
                                        CartView(
                                            singleProductItems = segregationCart.cartDetails,
                                            flashDealProductItems = segregationCart.flashDeals,
                                            promoDetails = segregationCart.PromoDetails,
                                            isPromoCodeValidated = segregationCart.PromoDetails?.isValidated == true,
                                            toggleBucketCheck = {
                                                viewModel.onActionPromoCode(
                                                    it,
                                                    segregationCart.PromoDetails?.promoCode,
                                                    itemIds
                                                )
                                            },
                                            isPostexDelivery = segregationCart.isPostexDelivery(),
                                            isBucketChecked = segregationCart.isEnabled,
                                            storeName = segregationCart.sellerName.split(
                                                "_"
                                            )
                                                .joinToString(" "),
                                            formattedDeliveryDate = segregationCart.formattedDeliveryDate(),
                                            formattedDeliveryFee = segregationCart.formattedDeliveryFee()
                                                .default(),
                                            amountNeededForFreeDelivery = segregationCart.amountNeededForFreeDelivery.getValueOrDefault(),
                                            getMinOrderVal = segregationCart.getMinOrderVal()
                                                .getValueOrDefault(),
                                            cartTotal = segregationCart.cartTotal.getValueOrDefault(),
                                            isBundle = segregationCart.isBundle(),
                                            onRemovePromoCode = {
                                                viewModel.onActionPromoCode(
                                                    segregationCart.isEnabled,
                                                    null,
                                                    itemIds
                                                )
                                            },
                                            onAddPromoCode = {
                                                viewModel._promoNavigationArgs.value =
                                                    PromoNavigationArgs(
                                                        segregationCart.isEnabled,
                                                        itemIds,
                                                    )
                                                if (sharedPreferencesManager.isLoggedIn()) {
                                                    viewModel.navigateToMorePromos(
                                                        bottomSheetNavigator
                                                    )
                                                } else {
                                                    navigate.push(PhoneInputScreen(navSource = ScreenSource.PROMO_CART.key))
                                                }
                                            },
                                            onClickDeliveryFee = {
                                                if (segregationCart.isPostexDelivery()
                                                        .not() && isSameDeliveryEligible
                                                ) {
                                                    openDeliveryFeeSheet.value = true
                                                }
                                            },
                                            deliveryType = DeliveryType.STANDARD,
                                            isSameDeliveryEligible = isSameDeliveryEligible && RemoteConfigs.isSameDayDeliveryEnabled()
                                        )
                                    }
                                    if (isLoggedIn) {
                                        DCCoinsView(
                                            amountInCoins = cartState?.formattedTotalDCCoins.default(),
                                            amountInRupees = cartState?.formattedAmountInCurrency.default(),
                                            isDCCoinEnabled = cartState?.isDCCoinEnabled == true,
                                            isDiscountSwitchChecked = isDiscountSwitchChecked,
                                            onDCCoinsSwitchClicked = {
                                                if (cartState?.cartTotal.getValueOrDefault() <= viewModel.useDCCoinsAmountLimit || cartState?.dcCityCart?.hasRestrictedPromotionalDiscountProducts == true || cartState?.multiCityCart?.hasRestrictedPromotionalDiscountProducts == true)
                                                    viewModel.onActionShowDcCoinWarning()
                                                else viewModel.updateCartDiscount(it)
                                            },
                                            dcCoinErrorMessage = dcCoinErrorMessage.default(),
                                            dcCoinWarningTrigger = dcCoinWarningTrigger,
                                            useDCCoinsAmountLimit = viewModel.useDCCoinsAmountLimit,
                                            cartTotal = cartState?.cartTotal.getValueOrDefault(),
                                            hasRestrictedPromotionalDiscountDC = cartState?.dcCityCart?.hasRestrictedPromotionalDiscountProducts == true,
                                            hasRestrictedPromotionalDiscountMultiCity = cartState?.multiCityCart?.hasRestrictedPromotionalDiscountProducts == true
                                        )
                                    }
                                    if (recommendationList.isNotEmpty()) {
                                        RecommendedEngineSwimlane(
                                            recommendationList,
                                            cardClick = {
//                                            eventLogger.onClickProductCartScreen(it.name)
//                                            eventLogger.onClickRecommendationProductCartScreen()
                                            },
                                            onAddClick = {
                                                if (it.hasVariants.not()) {
                                                    eventLogger.addToCartMergn(
                                                        it.id,
                                                        it.name,
                                                        1,
                                                        it.price,
                                                        "recommendation_cart",
                                                        type = "quick_buy"
                                                    )
                                                }
//                                            eventLogger.onClickPlusCartScreen(it.name)
//                                            eventLogger.clickQuickBuyRECart(
//                                                it.name,
//                                                1,
//                                                it.id
//                                            )
//                                            eventLogger.addToCartCartPage(
//                                                sharedPreferencesManager.getUserId(),
//                                                sharedPreferencesManager.getSelectedCity(),
//                                                it.id
//                                            )
                                            },
                                        )
                                    }

                                    cartState?.let {
                                        CartSummaryComponent(
                                            cart = it,
                                            packagingFeeName = packagingFeeName,
                                            platformFeeName = platformFeeName,
                                            discountedCartTotal = formattedDiscountedCartTotal.default(),
                                            platformFee = viewModel.platformFee.value.getValueOrDefault(),
                                            packagingFee = viewModel.packagingFee.value.getValueOrDefault(),
                                            dcCoinDiscount = viewModel.dcCoinDiscount.value.getValueOrDefault(),
                                            isDiscountSwitchChecked = viewModel.isDiscountSwitchChecked.value,
                                            formattedDcCoinDiscount = viewModel.formattedDcCoinDiscount(
                                                it
                                            ),
                                            selectedDeliveryType = selectedDeliveryType
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(0.dp))
                                }
                                CartCheckOutView(
                                    isLoggedIn = isLoggedIn,
                                    onCheckOutClick = {
                                        if (sharedPreferencesManager.isLoggedIn()) {
                                            viewModel.onCheckOutClick(
                                                bottomSheetNavigator,
                                                navigateToCheckout = {
                                                    navigate.push(
                                                        CartCheckoutScreen(selectedDeliveryType.key)
                                                    )
                                                },
                                                cartMovConfirmListener = { navigate.push(HomeScreen()) }
                                            )
                                        } else {
                                            navigate.push(PhoneInputScreen(navSource = ScreenSource.CART.key))
                                        }

                                    },
                                    overAllDiscount = formattedOverallCartAndCoinDiscount.default(),
                                    discountedCartTotal = formattedDiscountedCartTotal.default(),
                                    isAnyBucketChecked = run {
                                        val dcEnabled = cartState?.dcCityCart?.isEnabled == true
                                        val multiEnabled =
                                            cartState?.multiCityCart?.isEnabled == true
                                        val sellerAnyEnabled =
                                            cartState?.sellerCarts?.any { it.isEnabled } == true
                                        if (dcEnabled && !multiEnabled && !sellerAnyEnabled) {
                                            cartState?.dcCityCart?.amountNeededForFreeDelivery.getValueOrDefault() <= 0
                                        } else {
                                            dcEnabled || multiEnabled || sellerAnyEnabled
                                        }
                                    },
                                    itemCount = localCartItems.size
                                )
                            }
                        }
                    }

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
                                formattedDeliveryDate = cartState?.dcCityCart?.formattedDeliveryDate()
                                    .default(),
                                deliveryType = selectedDeliveryType,
                                formattedSameDayDeliveryFee = cartState?.formattedSameDayDeliveryFee()
                                    .default()
                            ) { selectedDeliveryType ->
                                viewModel.updateDeliveryType(selectedDeliveryType)
                                openDeliveryFeeSheet.value = false

                            }
                        },
                        heightModifier = Modifier.wrapContentHeight()
                    )
                }

            }
        }
    }
}
