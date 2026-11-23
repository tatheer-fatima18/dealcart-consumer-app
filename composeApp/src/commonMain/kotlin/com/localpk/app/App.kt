@file:OptIn(ExperimentalMaterialApi::class)

package com.localpk.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.localpk.app.base.Platform
import com.localpk.app.common_components.CustomAlertDialog
import com.localpk.app.common_components.CustomSnackbar
import com.localpk.app.common_components.FloatingCartContainer
import com.localpk.app.common_components.FloatingCartSection
import com.localpk.app.common_components.GlobalDialogManager
import com.localpk.app.common_components.GlobalToastManager
import com.localpk.app.common_components.GlobalProductDetailStateManager
import com.localpk.app.common_components.SwipeBackGestureArea
import com.localpk.app.features.cart.data.CartTotals
import com.localpk.app.features.cart.view.CartScreen
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.cart.viewModel.FoodCartViewModel
import com.localpk.app.features.checkout.view.CartCheckoutScreen
import com.localpk.app.features.checkout.view.SelectPaymentMethodScreen
import com.localpk.app.features.home.components.BottomNavigationBar
import com.localpk.app.features.home.presentation.HomeScreen
import com.localpk.app.features.login.presentation.OTPScreen
import com.localpk.app.features.login.presentation.PhoneInputScreen
import com.localpk.app.features.orders.presentation.FoodOrderDetailScreen
import com.localpk.app.features.orders.presentation.OrderDetailScreen
import com.localpk.app.features.orders.presentation.OrdersScreen
import com.localpk.app.features.personal.presentation.AccountDeletionScreen
import com.localpk.app.features.product_detail.presentation.ProductDetailScreen
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.productlist.data.toProductUiData
import com.localpk.app.features.productlist.data.toProductDetailScreen
import com.localpk.app.features.productlist.presentation.ProductListScreen
import com.localpk.app.features.restaurant.data.CategoryItemUIDataAndRestaurantDetails
import com.localpk.app.features.restaurant.data.Image
import com.localpk.app.features.restaurant.data.ItemChoiceGroup
import com.localpk.app.features.restaurant.data.ItemPrice
import com.localpk.app.features.restaurant.data.ItemVariation
import com.localpk.app.features.restaurant.presentation.FoodDetailScreen
import com.localpk.app.features.restaurant.presentation.OrderTrackingScreen
import com.localpk.app.features.restaurant.presentation.RestaurantDetailScreen
import com.localpk.app.features.rewards.presentation.RewardsScreen
import com.localpk.app.features.search.presentation.SearchScreen
import com.localpk.app.features.user_location.presentation.UserLocationScreen
import com.localpk.app.features.view_all_products.presentation.ViewAllProductsScreen
import com.localpk.app.features.wallet.view.PaymentBottomSheetScreen
import com.localpk.app.features.wallet.view.ResponseScreen
import com.localpk.app.features.wallet.view.TopUpScreen
import com.localpk.app.features.wallet.view.WalletHistoryScreen
import com.localpk.app.features.wallet.view.WalletScreen
import com.localpk.app.features.webview.WebViewScreen
import com.localpk.app.firebase.pushNotification.NotificationEvent
import com.localpk.app.firebase.pushNotification.NotificationObserver
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.AppTheme
import com.localpk.app.theming.Background
import com.localpk.app.utils.DeepLinkHandler
import com.localpk.app.utils.DeepLinkResult
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.webChatHtml
import com.localpk.app.base.domain.ExternalLinkHandler
import com.localpk.app.common_components.AlertDialogType
import com.localpk.app.common_components.SnackbarType
import com.localpk.app.features.restaurant.viewModels.OrderTrackingViewModel
import com.localpk.app.utils.APP_STORE_URL
import com.localpk.app.utils.PLAY_STORE_URL
import com.localpk.app.utils.VERSION_CODE_INT
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.force_upgrade_btn_label
import dealcart.composeapp.generated.resources.update_available_message
import dealcart.composeapp.generated.resources.update_available_title
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App(
    deepLink: String? = null,
    onDeepLinkHandled: (String) -> Unit = {}
) {
    AppTheme(false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            val sharedPreferencesManager: SharedPreferencesManager = koinInject()
            val externalLinkHandler: ExternalLinkHandler = koinInject()
            val warehouseId = sharedPreferencesManager.getSelectedWarehouseId()
            val latitude = sharedPreferencesManager.getLatitude()
            val longitude = sharedPreferencesManager.getLongitude()
            val hasLocation =
                warehouseId != 0L && latitude != "0" && latitude.isNotBlank() && longitude != "0" && longitude.isNotBlank()
            val showDialog by GlobalDialogManager.showDialog.collectAsState()
            val dialogTitle by GlobalDialogManager.dialogTitle.collectAsState()
            val dialogMessage by GlobalDialogManager.dialogMessage.collectAsState()
            val confirmButtonText by GlobalDialogManager.confirmButtonText.collectAsState()
            val cancelButtonText by GlobalDialogManager.cancelButtonText.collectAsState()
            val dialogType by GlobalDialogManager.dialogType.collectAsState()
            val isAddressDialog by GlobalDialogManager.isAddressDialog.collectAsState()
            val dialogIcon by GlobalDialogManager.dialogIcon.collectAsState()
            val showForceUpdateDialog = remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                RemoteConfigs.initialize()
            }

            if(RemoteConfigs.isForceUpdateEnabled()) {
                LaunchedEffect(RemoteConfigs.getForceUpgradeVersionValue()) {
                    val forceUpgradeVersion =
                        RemoteConfigs.getForceUpgradeVersionValue().toIntOrNull()
                            .getValueOrDefault()
                    if (forceUpgradeVersion > VERSION_CODE_INT) {
                        showForceUpdateDialog.value = true
                    }
                }
            }

            if (showForceUpdateDialog.value) {
                CustomAlertDialog(
                    shouldShowDialog = showForceUpdateDialog,
                    title = stringResource(Res.string.update_available_title),
                    message = stringResource(Res.string.update_available_message),
                    confirmButton = stringResource(Res.string.force_upgrade_btn_label),
                    cancelButton = cancelButtonText,
                    onConfirm = {
                        try {
                            val storeUrl = if (Platform.isIos()) {
                                APP_STORE_URL
                            } else {
                                PLAY_STORE_URL
                            }
                            externalLinkHandler.openExternalLink(storeUrl)
                        } catch (e: Exception) {
                            val errorMessage = if (Platform.isIos()) {
                                "Please update your app from the App Store manually"
                            } else {
                                "Please update your app from the Play Store manually"
                            }
                            GlobalToastManager.showToast(errorMessage, type = SnackbarType.ERROR)
                        }
                    },
                    isAddressDialog = false,
                    icon = null,
                    dialogType = AlertDialogType.WARNING,
                    showCancelButton = false,
                    isDismissible = false,
                )
            }

            if (showDialog) {
                CustomAlertDialog(
                    shouldShowDialog = remember { mutableStateOf(true) },
                    title = dialogTitle,
                    message = dialogMessage,
                    confirmButton = confirmButtonText,
                    cancelButton = cancelButtonText,
                    onConfirm = { GlobalDialogManager.executeConfirm() },
                    onCancel = { GlobalDialogManager.executeCancel() },
                    isAddressDialog = isAddressDialog,
                    icon = dialogIcon,
                    dialogType = dialogType,
                )
            }

            BottomSheetNavigator(
                sheetShape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                ),
                sheetBackgroundColor = Background,
                sheetGesturesEnabled = true
            ) {
                Navigator(if (hasLocation) HomeScreen() else UserLocationScreen()) { navigator ->
                    // Handle deep link navigation
                    LaunchedEffect(deepLink) {
                        deepLink?.let { link ->
                            // Add delay to ensure app is fully loaded and navigation is ready
                            delay(2000) // 2 second delay
                            when (val deepLinkResult = DeepLinkHandler.parseDeepLink(link)) {
                                is DeepLinkResult.SearchScreen -> {
                                    navigator.push(SearchScreen(false))
                                }

                                is DeepLinkResult.OrdersScreen -> {
                                    navigator.push(OrdersScreen(deepLinkResult.tab))
                                }

                                is DeepLinkResult.CartScreen -> {
                                    navigator.push(CartScreen())
                                }

                                is DeepLinkResult.RewardsScreen -> {
                                    navigator.push(RewardsScreen())
                                }

                                is DeepLinkResult.WalletScreen -> {
                                    navigator.push(WalletScreen())
                                }

                                is DeepLinkResult.ProductScreen -> {
                                    // Create a basic product object for navigation
                                    val product = ProductUiData(
                                        id = deepLinkResult.productId,
                                        name = "",
                                        description = "",
                                        imageUrl = "",
                                        price = 0.0,
                                        finalPrice = 0.0,
                                        discountedPrice = 0.0,
                                        inventoryQty = 0,
                                        productCategory = null,
                                        purchasePrice = 0.0,
                                        isFavourite = false,
                                        isBackInStockSubscribed = false,
                                        isSponsored = false,
                                        slot = 0,
                                        showMrp = false,
                                        purchaseLimit = 0,
                                        itemQuantity = 0
                                    )
                                    navigator.push(product.toProductDetailScreen())
                                }

                                is DeepLinkResult.CategoryScreen -> {
                                    navigator.push(ProductListScreen(deepLinkResult.categoryId))
                                }

                                is DeepLinkResult.ReferralGroupScreen -> {}
                                is DeepLinkResult.ReferralUserScreen -> {}
                                is DeepLinkResult.VerifyEmailScreen -> {}
                                is DeepLinkResult.DolatAppScreen -> {}
                                is DeepLinkResult.PromotionalScreen -> {}
                                is DeepLinkResult.DolatProductScreen -> {}
                                is DeepLinkResult.DolatGroupScreen -> {}
                                is DeepLinkResult.FlashDealScreen -> {}
                                // Add other screen types as needed
                                is DeepLinkResult.SpinTheWheelScreen -> {}
                                is DeepLinkResult.CricketScreen -> {}
                                is DeepLinkResult.TikTokMainScreen -> {}
                                is DeepLinkResult.HelpCenterScreen -> {}
                                is DeepLinkResult.AddAddressScreen -> {}
                                is DeepLinkResult.InAppStoryScreen -> {}
                                is DeepLinkResult.InAppReviewScreen -> {}
                                is DeepLinkResult.InAppCommentsScreen -> {}
                                is DeepLinkResult.ChatScreen -> {
                                    navigator.push(
                                        WebViewScreen(
                                            url = "https://dealcart.callcenterstudio.com",
                                            htmlContent = webChatHtml.trimIndent(),
                                            isWebChat = true
                                        )
                                    )
                                }

                                is DeepLinkResult.FoodScreen -> {
                                    navigator.push(
                                        HomeScreen(
                                            superParentCategoryId = RemoteConfigs.getFoodSuperParentId()
                                                .toLong()
                                        )
                                    )
                                }

                                is DeepLinkResult.SuperParentScreen -> {
                                    navigator.push(HomeScreen(superParentCategoryId = deepLinkResult.superParentCategoryId))
                                }

                                is DeepLinkResult.OpenInBrowser -> {
                                    externalLinkHandler.openExternalLink(deepLinkResult.url)
                                }

                                null -> {}
                                DeepLinkResult.HomeScreen -> navigator.push(HomeScreen())
                            }
                            // Notify that the deep link has been handled
                            onDeepLinkHandled(link)
                        }
                    }
                    MainScaffold(navigator)
                }
            }
        }
    }
}

@Composable
fun MainScaffold(navigator: Navigator) {
    val currentScreen = navigator.lastItem
    val cartViewModel: CartViewModel = koinInject()
    val foodCartViewModel: FoodCartViewModel = koinInject()
    val orderTrackingViewModel: OrderTrackingViewModel = koinInject()

    fun openOrderTracking(orderId: String?, restaurantId: String?) {
        if (orderId != null) {
            try {
                val orderIdLong = orderId.toLongOrNull()
                if (orderIdLong != null) {
                    navigator.push(
                        OrderTrackingScreen(
                            orderIdLong,
                            restaurantId?.toLongOrNull(),
                            isCheckout = false,
                            fromDetails = false,
                            fromNotification = true
                        )
                    )
                }
            } catch (e: Exception) {
            }
        }
    }

    // Notification observer for handling notification taps - placed here to access navigator
    NotificationObserver(
        onOpenUrl = {
        },
        onNavigateToOrders = { tab ->
            navigator.push(OrdersScreen(tab))
        },
        onNavigateToOrderDetail = { id, status ->
            navigator.push(OrderDetailScreen(id = id, status = status))
        },
//        onNavigateToFlashDeals = { isExpired, isPending, isPN ->
//            // Navigate to flash deals screen with specific parameters
//            println("App.kt: Navigating to flash deals - expired: $isExpired, pending: $isPending, isPN: $isPN")
//        },
        onNavigateToProductDetails = { product ->
            navigator.push(
                product.toProductDetailScreen()
            )
        },
        onNavigateToWallet = {
            navigator.push(WalletScreen())
        },
        onNavigateToOrderTracking = { orderId, restaurantId ->
            openOrderTracking(orderId, restaurantId)
        },
        onNavigateToHome = {
            navigator.popUntil { false } // Clear all screens and go to home
            navigator.push(HomeScreen())
        },
        onRefreshCart = {
            cartViewModel.checkCartType()
        },
        onPaymentSuccess = {},
        onPaymentFailure = {},
        onNotificationReceived = { event ->
            when (event) {
                is NotificationEvent.RefreshCart -> {
                    cartViewModel.checkCartType()
                }

                is NotificationEvent.NavigateToOrderTracking -> {
                    orderTrackingViewModel.getTrackingStatus()
//                    if (currentScreen is OrderTrackingScreen) {
//                        println("debug App.kt: Currently on OrderTrackingScreen, refreshing data instead of navigating.")
//
//                    }
                }

                else -> {
                    // No-op for other passive events by default
                }
            }
        }
    )

    val hideBottomBar = listOf(
        currentScreen is UserLocationScreen,
        currentScreen is OTPScreen,
        currentScreen is PhoneInputScreen,
        currentScreen is WebViewScreen,
        currentScreen is AccountDeletionScreen,
        currentScreen is FoodDetailScreen,
        currentScreen is ProductDetailScreen
    ).contains(true)
    val showFloatingCart =
        listOf(
            currentScreen is HomeScreen,
            currentScreen is ProductListScreen,
            currentScreen is SearchScreen,
            currentScreen is ViewAllProductsScreen,
            currentScreen is FoodDetailScreen,
            currentScreen is ProductDetailScreen,
            currentScreen is RestaurantDetailScreen
        ).contains(true)


    val localCartItems by cartViewModel.localCartItems.collectAsState()
    val isCartFoodType by cartViewModel.isCartFoodType.collectAsState()
    val localFoodCartItems by foodCartViewModel.localFoodCartItems.collectAsState()
    val sharedPreferencesManager: SharedPreferencesManager = koinInject()
    val eventLogger: EventLogger = koinInject()

    // Global Toast Manager states
    val showToast by GlobalToastManager.showToast.collectAsState()
    val toastMessage by GlobalToastManager.toastMessage.collectAsState()
    val toastType by GlobalToastManager.toastType.collectAsState()
    val toastDuration by GlobalToastManager.toastDuration.collectAsState()

    // Compute cartTotals based on cart type
    val cartTotals = if (isCartFoodType) {
        val items = localFoodCartItems.values
        val cartTotal =
            items.sumOf { if (it.isTotal) it.discountedPrice else it.discountedPrice * it.quantity }
        val cartDiscount =
            items.sumOf { if (it.isTotal) it.actualPrice - it.discountedPrice else (it.actualPrice - it.discountedPrice) * it.quantity }
        val itemCount = items.sumOf { it.quantity }

        CartTotals(cartTotal, cartDiscount, itemCount, cartTotal)
    } else {
        val items = localCartItems.values
        val cartTotal =
            items.sumOf { if (it.isTotal) it.discountedPrice else it.discountedPrice * it.quantity }
        val cartDiscount =
            items.sumOf { if (it.isTotal) it.actualPrice - it.discountedPrice else (it.actualPrice - it.discountedPrice) * it.quantity }
        val itemCount = items.sumOf { it.quantity }
        val dcCityCartTotal = items
            .filter { !it.isProductToBeDeliveredByThirdParty }
            .sumOf { if (it.isTotal) it.discountedPrice else it.discountedPrice * it.quantity }
        CartTotals(cartTotal, cartDiscount, itemCount, dcCityCartTotal)
    }

    val cartMinOrderValue = if (isCartFoodType) {
        foodCartViewModel.foodCartState.value?.minimumOrderValue?.toFloat() ?: 0f
    } else {
        if (sharedPreferencesManager.isLoggedIn()) {
            cartViewModel.cartState.value?.dcCityCart?.minimumOrderValue
        } else {
            cartViewModel.cartState.value?.dcCityCart?.minimumOrderValue
        }
    }

    val badgeCount = if (isCartFoodType) {
        localFoodCartItems.size
    } else {
        localCartItems.size
    }

    val tabScreens = listOf(
        HomeScreen(),
        OrdersScreen(),
        WalletScreen(),
        RewardsScreen(),
        CartScreen()
    )

    // Function to determine selected tab based on current screen
    fun getSelectedTabIndex(currentScreen: Screen): Int {
        return when (currentScreen) {
            is HomeScreen -> 0
            is OrdersScreen -> 1
            is WalletScreen -> 2
            is RewardsScreen -> 3
            is CartScreen -> 4
            else -> {
                // For deep navigation, check if we're in a sub-screen of a main tab
                when {
                    currentScreen is OrderTrackingScreen -> 1 // Orders tab
                    currentScreen is OrderDetailScreen -> 1 // Orders tab
                    currentScreen is FoodOrderDetailScreen -> 1 // Orders tab
                    currentScreen is CartCheckoutScreen -> 4 // Cart tab
                    currentScreen is SelectPaymentMethodScreen -> 4 // Cart tab
                    currentScreen is PaymentBottomSheetScreen -> 2 // Wallet tab
                    currentScreen is TopUpScreen -> 2 // Wallet tab
                    currentScreen is WalletHistoryScreen -> 2 // Wallet tab
                    currentScreen is ResponseScreen -> 2 // Wallet tab
                    // Add more cases as needed for other sub-screens
                    else -> 0 // Default to home
                }
            }
        }
    }

    val selectedTab = getSelectedTabIndex(currentScreen)

    // Detect if currentScreen is ProductDetailScreen and extract product args
    val isProductDetailScreen = currentScreen is ProductDetailScreen
    val productDetailScreen =
        if (isProductDetailScreen) currentScreen as ProductDetailScreen else null
    val productDetailArgs = productDetailScreen?.let { screen ->
        ProductUiData(
            id = screen.id,
            name = screen.name,
            description = screen.description,
            imageUrl = screen.imageUrl,
            price = screen.price,
            finalPrice = screen.finalPrice,
            purchasePrice = screen.purchasePrice,
            discountedPrice = screen.discountedPrice,
            inventoryQty = screen.inventoryQty,
            isSponsored = screen.isSponsored,
            slot = screen.slot,
            isFromNotification = screen.isFromNotification,
            isFlashDeal = screen.isFlashDeal,
            flashDealId = screen.flashDealId,
            isDeepLink = screen.isDeepLink,
            itemQuantity = screen.itemQuantity,
            hasVariant = screen.hasVariant,
            showMrp = screen.showMrp,
            purchaseLimit = screen.purchaseLimit,
            storeName = screen.storeName,
            storeImage = screen.storeImage,
            isDeal = screen.isDeal,
            hasVariants = screen.hasVariants,
            productCategory = emptyList(),
            images = emptyList(),
            isFavourite = false,
            isBackInStockSubscribed = false,
        )
    }

    val productDetailState = GlobalProductDetailStateManager.productDetailState.collectAsState()
    val resolvedProductDetailArgs = if (isProductDetailScreen) {
        productDetailState.value?.toProductUiData() ?: productDetailArgs
    } else {
        null
    }

    // Detect if currentScreen is FoodDetailScreen and get its ViewModel
    val isFoodDetailScreen = currentScreen is FoodDetailScreen
    //  val foodDetailArgs = if (isFoodDetailScreen) (currentScreen as FoodDetailScreen).args else null
    // Only get the ViewModel if on FoodDetailScreen

    val json = Json { ignoreUnknownKeys = true }
    val foodDetailArgs = if (isFoodDetailScreen) {
        val foodDetailScreen = currentScreen as FoodDetailScreen
        CategoryItemUIDataAndRestaurantDetails(
            id = foodDetailScreen.id,
            name = foodDetailScreen.name,
            description = foodDetailScreen.description,
            image = foodDetailScreen.imageUrl?.let {
                Image(
                    id = 1,
                    full = it,
                    medium = it,
                    path = it,
                    thumbnail = it
                )
            },
            prices = foodDetailScreen.pricesJson?.let { json.decodeFromString<ItemPrice>(it) },
            variations = foodDetailScreen.variationsJson?.let {
                json.decodeFromString<List<ItemVariation>>(
                    it
                )
            },
            choiceGroups = foodDetailScreen.choiceGroupsJson?.let {
                json.decodeFromString<List<ItemChoiceGroup>>(
                    it
                )
            },
            itemQuantity = foodDetailScreen.itemQuantity,
            quantityListener = {},
            restaurantId = foodDetailScreen.restaurantId,
            restaurantName = foodDetailScreen.restaurantName,
            logoImage = null,
            cuisines = foodDetailScreen.cuisinesCsv?.split(",")?.filter { it.isNotEmpty() },
            deliveryFee = foodDetailScreen.deliveryFee,
            prepTime = foodDetailScreen.prepTime,
            minimumOrderValue = foodDetailScreen.minimumOrderValue
        )
    } else null


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.weight(1f)) {
            currentScreen.Content()

            // iOS swipe-to-go-back gesture
            if (Platform.isIos() && !shouldNotEnableSwipeBack(currentScreen)) {
                SwipeBackGestureArea(navigator)
            }
        }
        if (showFloatingCart && (cartTotals.cartTotal > 0 || isProductDetailScreen || isFoodDetailScreen)) {
            FloatingCartContainer(isNavigationPaddingRequired = isProductDetailScreen || isFoodDetailScreen) {
                FloatingCartSection(
                    cartTotal = cartTotals.cartTotal.toInt(),
                    cartMinOrderValue = cartMinOrderValue?.toFloat() ?: 999f,
                    cartDiscount = cartTotals.cartDiscount,
                    dcCityCartTotal = cartTotals.dcCityCartTotal.getValueOrDefault(),
                    isProductDetail = isProductDetailScreen,
                    isFoodDetail = isFoodDetailScreen,
                    itemCount = badgeCount,
                    onCartClick = {
                        if (isCartFoodType) {
                            eventLogger.viewFoodCart(source = "sticky bar")
                        } else
                            eventLogger.orderConfirmScreen(source = "sticky cart")
                        navigator.push(CartScreen())
                    },
                    foodDetailArgs = foodDetailArgs,
                    productDetailArgs = resolvedProductDetailArgs,
                    foodCartViewModel = foodCartViewModel,
                    showOnlyProductDetailCounter = (isProductDetailScreen || isFoodDetailScreen) && cartTotals.cartTotal == 0.0
                )
            }
        }

        if (!hideBottomBar) {
            BottomNavigationBar(
                selectedIndex = selectedTab,
                onTabSelected = { index ->
                    val targetScreen = tabScreens[index]
                    if (currentScreen::class != targetScreen::class) {
                        // Clear the navigation stack and push the target screen
                        navigator.popUntil { false } // Clear all screens
                        navigator.push(targetScreen)
                    }
                },
                cartItemCount = badgeCount
            )
        }
    }

    // Global Custom Snackbar
    CustomSnackbar(
        message = toastMessage,
        type = toastType,
        duration = toastDuration,
        showSnackbar = showToast,
        onSnackbarDismissed = { GlobalToastManager.executeToastDismiss() }
    )
}


private fun shouldNotEnableSwipeBack(currentScreen: Screen): Boolean {
    return when (currentScreen) {
        // Add screens that should NOT have swipe-back here
        // For example:
        // is HomeScreen -> true
        else -> false // Default: enable swipe-back for all other screens
    }
}


