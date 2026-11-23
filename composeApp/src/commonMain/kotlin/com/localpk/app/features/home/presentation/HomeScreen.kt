package com.localpk.app.features.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ExternalLinkHandler
import com.localpk.app.features.address.data.toMapScreen
import com.localpk.app.features.address.presentation.MapScreen
import com.localpk.app.features.banner.data.Banner
import com.localpk.app.features.bottomsheet.data.GeneralBottomSheetUiData
import com.localpk.app.features.bottomsheet.view.GeneralBottomSheetScreen
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.cart.viewModel.FoodCartViewModel
import com.localpk.app.features.category.data.CategoryUiModel
import com.localpk.app.features.category.data.SuperParent
import com.localpk.app.features.cricket_live.presentation.MatchDetailScreen
import com.localpk.app.features.frequently_ordered.presentation.FrequentlyOrderedScreen
import com.localpk.app.features.home.components.AddressBottomSheet
import com.localpk.app.features.home.components.BannerShimmer
import com.localpk.app.features.home.components.BannerSlider
import com.localpk.app.features.home.components.CategoriesComponent
import com.localpk.app.features.home.components.CategoriesShimmer
import com.localpk.app.features.home.components.CricketSection
import com.localpk.app.features.home.components.ReOrderShimmer
import com.localpk.app.features.home.components.SuperParentCategoriesShimmer
import com.localpk.app.features.home.components.RestaurantSection
import com.localpk.app.features.home.components.ReviewSection
import com.localpk.app.features.home.components.SuperParentCategoriesComposeView
import com.localpk.app.features.home.components.TopSection
import com.localpk.app.features.home.components.swimlanes.ReOrderComposeView
import com.localpk.app.features.home.components.swimlanes.RenderSwimlane
import com.localpk.app.features.home.components.swimlanes.SwimlaneSkeleton
import com.localpk.app.features.login.presentation.PhoneInputScreen
import com.localpk.app.features.order_review.data.OrderReviewData
import com.localpk.app.features.orders.data.GlobalEvents
import com.localpk.app.features.orders.data.OrderStatus
import com.localpk.app.features.personal.presentation.PersonalViewModel
import com.localpk.app.features.product_detail.presentation.ProductDetailScreen
import com.localpk.app.features.productlist.presentation.ProductListScreen
import com.localpk.app.features.productlist.viewModel.ProductListViewModel
import com.localpk.app.features.profile.presentation.ProfileScreen
import com.localpk.app.features.restaurant.components.FoodLandingPage
import com.localpk.app.features.restaurant.components.FoodLandingPageSkeleton
import com.localpk.app.features.restaurant.components.NoRestaurantsScreen
import com.localpk.app.features.restaurant.presentation.OrderTrackingScreen
import com.localpk.app.features.restaurant.presentation.RestaurantDetailScreen
import com.localpk.app.features.search.presentation.SearchScreen
import com.localpk.app.features.swimlane_engine.data.LandingPageType
import com.localpk.app.features.swimlane_engine.data.SwimlaneType
import com.localpk.app.features.swimlane_engine.presentation.SwimlaneEngineViewModel
import com.localpk.app.features.user_location.presentation.UserLocationScreen
import com.localpk.app.features.user_location.presentation.UserLocationViewModel
import com.localpk.app.firebase.pushNotification.PushNotificationServiceImpl
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.CATEGORY
import com.localpk.app.utils.CRICKET
import com.localpk.app.utils.DEAL
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EXTERNAL
import com.localpk.app.utils.PRODUCT
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.navigateToWebChat
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.StatusBarIconColor
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.disclaimer
import dealcart.composeapp.generated.resources.message_keep_nic_with_you
import dealcart.composeapp.generated.resources.message_terms_condition_acceptance
import dealcart.composeapp.generated.resources.no
import dealcart.composeapp.generated.resources.title_keep_nic_with_you
import dealcart.composeapp.generated.resources.yes
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class HomeScreen(
    private val superParentCategoryId: Long? = null
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val viewModel = koinViewModel<HomeViewModel>()
        val swimlaneEngineViewModel: SwimlaneEngineViewModel = koinInject()
        val userLocationViewModel = koinViewModel<UserLocationViewModel>()
        val productListViewModel = koinViewModel<ProductListViewModel>()
        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()
        val cartViewModel = koinViewModel<CartViewModel>()
        val foodCartViewModel = koinViewModel<FoodCartViewModel>()
        val externalLinkHandler: ExternalLinkHandler = koinInject()
        val eventLogger: EventLogger = koinInject()
        val homeState by viewModel.homeState.collectAsState()
        val viewState by viewModel.viewState.collectAsState()
        val localCartItems by cartViewModel.localCartItems.collectAsState()
        val localFoodCartItems by foodCartViewModel.localFoodCartItems.collectAsState()


        val personalViewModel = koinViewModel<PersonalViewModel>()


        LaunchedEffect(Unit) {
            if (!viewModel.addressesFetched.value && sharedPreferencesManager.isLoggedIn())
                viewModel.getAddresses()
            if (sharedPreferencesManager.isLoggedIn() && !viewModel.ratingFetched.value)
                viewModel.fetchOrderForReview(bottomSheetNavigator)
            if (sharedPreferencesManager.getUserName()
                    .isEmpty() && sharedPreferencesManager.isLoggedIn()
            )
                personalViewModel.getPersonalDetails()

            eventLogger.homePage(
                sharedPreferencesManager.isLoggedIn(),
                sharedPreferencesManager.getUserId().toInt(),
                sharedPreferencesManager.getUserName(),
                sharedPreferencesManager.getPhoneNumber()
            )
            if (homeState.selectedSuperParentCategory?.id == null && sharedPreferencesManager.isLoggedIn() && homeState.reOrderSwimlane.isEmpty())
                viewModel.getReOrderSwimlaneProducts()
        }


        LaunchedEffect(superParentCategoryId, homeState.superParentCategories) {
            superParentCategoryId?.let { categoryId ->
                // Only proceed if categories are loaded and not empty
                if (homeState.superParentCategories.isNotEmpty()) {
                    // Find the super parent category by ID and set it as selected
                    val superParentCategory =
                        homeState.superParentCategories.find { it.id == categoryId }
                    if (superParentCategory != null) {
                        viewModel.updateSelectedSuperParentCategory(superParentCategory)
                        if (categoryId == SuperParent.FOOD.id) {
                            viewModel.getRestaurants()
                        }
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            delay(2000)

            val token = PushNotificationServiceImpl().getToken()
            try {
                if (token != null) {
                    sharedPreferencesManager.setFCMToken(token)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        // Multiplatform color calculation for super parent category
        val (topColor, tileBg, categoryNameColour, categoryColour) = remember(homeState.selectedSuperParentCategory) {
            viewModel.calculateColors(homeState.selectedSuperParentCategory)
        }
        StatusBarIconColor(backgroundColor = topColor)

        val listState = remember { LazyListState() }
        val loadedCount = remember { mutableStateOf(2) }
        val isLoadingNext = remember { mutableStateOf(false) }
        val isInitialLoad = remember { mutableStateOf(true) }
        val requestedSwimlaneTypes = remember { mutableSetOf<String>() }
        val isRecommendedProductsLoading = remember { mutableStateOf(false) }
        val isFlashDealsRequested = remember { mutableStateOf(false) }
        val isFlashDealsLoading = remember { mutableStateOf(false) }

        // Helper function to safely call getRecommendedProducts
        fun callGetRecommendedProducts(swimlaneType: String) {
            if (!isRecommendedProductsLoading.value && !requestedSwimlaneTypes.contains(swimlaneType) && sharedPreferencesManager.isLoggedIn()) {
                requestedSwimlaneTypes.add(swimlaneType)
                isRecommendedProductsLoading.value = true
                viewModel.getRecommendedProducts(swimlaneType)
            }
        }

        // Helper function to safely call getFlashDeals
        fun callGetFlashDeals() {
            if (!isFlashDealsLoading.value && !isFlashDealsRequested.value) {
                isFlashDealsRequested.value = true
                isFlashDealsLoading.value = true
                viewModel.getFlashDeals()
            }
        }

        // Reset loading state when recommended products are updated
        LaunchedEffect(homeState.recommendedProducts) {
            isRecommendedProductsLoading.value = false
        }

        // Reset loading state when flash deals are updated
        LaunchedEffect(homeState.flashDeals) {
            isFlashDealsLoading.value = false
        }

        // Handle scroll state reset when address changes
        LaunchedEffect(homeState.shouldResetScrollState) {
            if (homeState.shouldResetScrollState) {
                // Reset scroll state and loaded count
                loadedCount.value = 2
                isInitialLoad.value = true
                isLoadingNext.value = false

                // Reset the flag
                viewModel.resetScrollStateFlag()
            }
        }


        // LaunchedEffect to handle swimlane fetching and observing when super parent category changes
        LaunchedEffect(homeState.selectedSuperParentCategory?.id) {
            val categoryId = homeState.selectedSuperParentCategory?.id?.toString() ?: "ALL_TAB"
            val swimlaneKey = if (homeState.selectedSuperParentCategory?.id == null)
                LandingPageType.Main.type
            else
                categoryId

            // Check if we have saved swimlanes for this category
            val savedSwimlanes = viewModel.getCategorySwimlanes(categoryId)
            val savedScrollState = viewModel.getCategoryScrollState(categoryId)

            if (savedSwimlanes != null) {
                // Use saved swimlanes
                viewModel.updateSwimlanes(savedSwimlanes, homeState.isLoadingSwimlanes)

                if (savedScrollState != null) {
                    // Restore scroll state
                    loadedCount.value = savedScrollState.loadedCount
                    isInitialLoad.value = false

                    // Restore scroll position after UI updates
                    try {
                        listState.scrollToItem(
                            index = savedScrollState.scrollIndex,
                            scrollOffset = savedScrollState.scrollOffset
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    loadedCount.value = 2
                    isInitialLoad.value = true
                }
            } else {
                // No saved swimlanes - fetch new ones
                loadedCount.value = 2
                isInitialLoad.value = true


                // Fetch appropriate swimlanes based on selected category
                if (homeState.selectedSuperParentCategory?.id == null) {
                    swimlaneEngineViewModel.getSwimlanes(landingPage = LandingPageType.Main.type)
                } else if (homeState.selectedSuperParentCategory?.id != SuperParent.FOOD.id) {
                    swimlaneEngineViewModel.getSwimlanes(
                        landingPage = LandingPageType.CATEGORY.type,
                        superParentCategoryId = homeState.selectedSuperParentCategory?.id.toString()
                    )
                }

                // Observe swimlane updates and save them
                swimlaneEngineViewModel.swimlaneMap[swimlaneKey]?.collect { swimlanes ->
                    viewModel.updateSwimlanes(swimlanes, swimlaneEngineViewModel.isSwimlaneLoading.value)
                    // Save swimlanes for this category only if we have data
                    if (swimlanes.isNotEmpty()) {
                        viewModel.saveCategorySwimlanes(categoryId, swimlanes)
                    }
                }

                // Reset loading state when switching categories
                isLoadingNext.value = false
            }
        }


        val scrollOffset by remember {
            derivedStateOf {
                listState.firstVisibleItemScrollOffset
            }
        }
        val targetHeight = if (scrollOffset > 10) 32.dp else 82.dp
        val animatedHeight by animateDpAsState(
            targetValue = targetHeight,
            label = EMPTY_STRING
        )

        // Remember the visible items info to avoid recomposition
        val visibleItemsInfo = remember(listState) {
            derivedStateOf {
                listState.layoutInfo.visibleItemsInfo
            }
        }

        // Remember the last visible index to avoid recomposition
        val lastVisibleIndex = remember(visibleItemsInfo.value) {
            derivedStateOf {
                visibleItemsInfo.value.lastOrNull()?.index
            }
        }

        val headerCount = remember(
            homeState.banners.isNotEmpty(),
            homeState.selectedSuperParentCategory?.id,
            homeState.reOrderSwimlane.isNotEmpty()
        ) {
            var count = 1
            if (homeState.banners.isNotEmpty()) count++ // banners
            if (homeState.selectedSuperParentCategory?.id == null && homeState.reOrderSwimlane.isNotEmpty()) count++ // reorder swimlane
            count
        }


        // Auto-save scroll state when user scrolls
        LaunchedEffect(listState, loadedCount.value) {
            snapshotFlow {
                Triple(
                    listState.firstVisibleItemIndex,
                    listState.firstVisibleItemScrollOffset,
                    loadedCount.value
                )
            }.collect { (index, offset, count) ->
                val categoryId = homeState.selectedSuperParentCategory?.id?.toString() ?: "ALL_TAB"
                viewModel.saveCategoryScrollState(categoryId, index, offset, count)
            }
        }

        LaunchedEffect(loadedCount.value, homeState.swimlanes.size) {
            snapshotFlow { lastVisibleIndex.value }
                .collect { lastIndex ->
                    if (lastIndex == null) {
                        return@collect
                    }

                    // Mark as not initial load when user scrolls
                    if (isInitialLoad.value && lastIndex > 0) {
                        isInitialLoad.value = false
                    }


                    // Check if we need to load the next swimlane
                    if (!isLoadingNext.value &&
                        !isInitialLoad.value && // Don't load on initial load
                        lastIndex >= headerCount + loadedCount.value - 1 && // Trigger when user reaches the last loaded item
                        loadedCount.value < homeState.swimlanes.size
                    ) {
                        isLoadingNext.value = true
                        try {
                            val nextSwimlane = homeState.swimlanes[loadedCount.value]

                            // Make API call for the next swimlane based on its type
                            when (nextSwimlane.getSwimlaneType()) {
                                SwimlaneType.PROMOTIONAL, SwimlaneType.MAX_ABS_DISCOUNT,
                                SwimlaneType.MIN_AMOUNT_OFF, SwimlaneType.MIN_PERCENTAGE_OFF,
                                SwimlaneType.UNDER_AMOUNT, SwimlaneType.BEST_SELLING, SwimlaneType.NEWLY_ADDED -> {
                                    // These need additional data fetching
                                    swimlaneEngineViewModel.fetchSwimlaneDataIfNeeded(nextSwimlane.id)

                                    // Wait for data to be available
                                    val success = withTimeoutOrNull(5000) {
                                        waitForSwimlaneData(
                                            swimlaneId = nextSwimlane.id,
                                            viewModel = swimlaneEngineViewModel
                                        )
                                    } != null

                                    if (success) {
                                        loadedCount.value += 1
                                    } else {
                                        // Even if data fetch fails, increment to avoid getting stuck
                                        loadedCount.value += 1
                                    }
                                }

                                SwimlaneType.CART, SwimlaneType.HOME -> {
                                    callGetRecommendedProducts(nextSwimlane.getSwimlaneType()?.type.default())
                                    loadedCount.value += 1
                                }

//                                SwimlaneType.GROUP_DEALS -> {
//                                    callGetFlashDeals()
//                                    loadedCount.value += 1
//                                }

                                else -> {
                                    loadedCount.value += 1
                                }
                            }

                        } catch (e: Exception) {
                            // In case of any error, increment loadedCount to avoid getting stuck
                            loadedCount.value += 1
                        } finally {
                            isLoadingNext.value = false
                        }
                    }
                }
        }

        val selectedSuperParentCategory = homeState.selectedSuperParentCategory
        val isFoodCategory by remember(selectedSuperParentCategory) {
            derivedStateOf {
                selectedSuperParentCategory?.id == SuperParent.FOOD.id ||
                        selectedSuperParentCategory?.name == SuperParent.FOOD.properName
            }
        }

        var searchText by remember { mutableStateOf(EMPTY_STRING) }

        if (RemoteConfigs.isEnabledSearchKeys()) {
            var searchPage = 0
            val searchProductsKeys = RemoteConfigs.searchProductNamesList()

            LaunchedEffect(searchProductsKeys) {
                while (true) {
                    if (searchPage < searchProductsKeys.size) {
                        searchText = "Search ${searchProductsKeys[searchPage].productName}"
                        searchPage++
                    } else {
                        searchPage = 0
                    }
                    delay(2000)
                }
            }
        }

        val cricketPagerState =
            rememberPagerState(
                pageCount = { maxOf(1, homeState.matches.size.getValueOrDefault()) },
                initialPageOffsetFraction = 0f
            )

        // Multiplatform-compatible periodic polling for cricket matches
        // LaunchedEffect automatically cancels when composable leaves composition
        LaunchedEffect(
            homeState.selectedSuperParentCategory?.id,
            RemoteConfigs.isCricketEnabled()
        ) {
            // Only run if no super parent category is selected and cricket is enabled
            if (homeState.selectedSuperParentCategory?.id == null && RemoteConfigs.isCricketEnabled()) {
                try {
                    while (true) {
                        ensureActive() // Check if coroutine is still active
                        if (cricketPagerState.currentPage == 0) {
                            viewModel.matchesSummary()
                            delay(10000)
                        } else {
                            delay(5000)
                        }
                    }
                } catch (e: kotlinx.coroutines.CancellationException) {
                    // Coroutine was cancelled, which is expected when screen leaves composition
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Section with Address, Search, Profile
                TopSection(
                    address =
                    if (sharedPreferencesManager.getDefaultAddress()
                            .isNotEmpty() && sharedPreferencesManager.isLoggedIn()
                    )
                        sharedPreferencesManager.getDefaultAddress()
                    else if (sharedPreferencesManager.getSelectedCity().isNotEmpty())
                        sharedPreferencesManager.getSelectedCity()
                    else
                        "Select Address",
                    onProfileAction = {
                        navigator.push(ProfileScreen())
                    },
                    onAddressClick = {
                        if (sharedPreferencesManager.isLoggedIn()) {
                            viewModel.selectAddress()
                        } else {
                            navigator.push(UserLocationScreen())
                        }
                    },
                    onWhatsAppClick = {
                        eventLogger.WhatsappHelpButton()
                        navigateToWebChat(navigator)
                    },
                    isLoading = homeState.isDataLoading,
                    inboxCount = homeState.inboxCount,
                    searchText = searchText,
                    isInboxEnabled = RemoteConfigs.isInboxEnabled(), // Assuming this is enabled
                    onSearchClick = {
                        eventLogger.searchBarClick()
                        eventLogger.homeCategoryClick(
                            name = if (isFoodCategory) "Food Search" else "Grocery Search",
                            id = 0L,
                            sort = null
                        )
                        navigator.push(SearchScreen(isFoodCategory))
                    },
                    topColor = topColor,
                    restaurantOrders = homeState.restaurantOrders,
                    onOrderAction = { },
                    onOrderTrackingAction = {
                        homeState.restaurantOrders.firstOrNull()?.let { order ->
                            navigator.push(
                                OrderTrackingScreen(
                                    orderId = order.orderId,
                                    restaurantId = order.restaurantId,
                                    isCheckout = order.isCheckout,
                                    fromDetails = order.fromDetails
                                )
                            )
                        }
                    },
                    isFood = isFoodCategory
                )

                // Super Parent Categories (if enabled)
                if (RemoteConfigs.isSuperParentCategoriesEnabled()) {
                    if (homeState.isLoadingSuperParentCategories) {
                        // Show shimmer when loading super parent categories
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(topColor)
                        ) {
                            SuperParentCategoriesShimmer(
                                tileBg = tileBg,
                                animatedHeight = animatedHeight,
                                topColor = topColor
                            )
                        }
                    } else if (homeState.superParentCategories.isNotEmpty()) {
                        SuperParentCategoriesComposeView(
                            selectedSuperParentCategory = homeState.selectedSuperParentCategory,
                            superParentCategories = homeState.superParentCategories,
                            onSuperParentClick = { category ->
                                if (category.id == homeState.selectedSuperParentCategory?.id) {
                                    // Clicking on already selected category - go back to "All"
                                    viewModel.updateSelectedSuperParentCategory(null)
                                    viewModel.updateFilteredCategories(homeState.allCategories)
                                    eventLogger.clickSelectedSuperParentCategory(category.name)
                                } else {
                                    if (category.id != SuperParent.FOOD.id || category.name != SuperParent.FOOD.properName) {
                                        eventLogger.clickUnSelectedSuperParentCategory(
                                            category.name,
                                            category.id.toString()
                                        )
                                        viewModel.onActionSuperParent(category.id) {
                                            // Swimlane fetching will be handled by LaunchedEffect
                                        }
                                    } else {
                                        eventLogger.clickAllSuperParent(isFood = true)
                                        viewModel.getRestaurants()
                                    }
                                    viewModel.updateSelectedSuperParentCategory(category)
                                }
                            },
                            onAllClick = {
                                eventLogger.clickAllSuperParent(isFood = false)
                                viewModel.updateSelectedSuperParentCategory(null)
                                viewModel.updateFilteredCategories(homeState.allCategories)
                            },
                            topColor = topColor,
                            tileBg = tileBg,
                            animatedHeight = animatedHeight,
                            categoryColour = categoryNameColour
                        )
                    }
                }


                // Main Content
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    state = listState
                ) {
                    // Loading state
                    if (viewState is ViewState.Loading) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    if (isFoodCategory) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            when {
                                homeState.isLoading -> {
                                    FoodLandingPageSkeleton()
                                }

                                homeState.restaurants.isNotEmpty() -> {
                                    FoodLandingPage(
                                        cuisines = homeState.cuisines,
                                        restaurants = homeState.restaurants,
                                        selectedCuisines = homeState.selectedCuisines,
                                        onCuisineClick = {
                                            viewModel.toggleCuisineSelection(it)
                                        },
                                        onRestaurantCardClick = { data ->
                                            eventLogger.clickRestaurant(
                                                restaurantName = data.name,
                                                source = if (homeState.selectedCuisines.isEmpty()) "super parent" else "cuisine"
                                            )
                                            navigator.push(
                                                RestaurantDetailScreen(
                                                    id = data.id,
                                                    name = data.name,
                                                    logoImage = data.logoImage,
                                                    cuisines = data.cuisines?.joinToString(","),
                                                    deliveryFee = data.deliveryFee,
                                                    prepTime = data.prepTime,
                                                    minimumOrderValue = data.minimumOrderValue
                                                )
                                            )
//                                            if (sharedPreferencesManager.getDefaultAddressId()
//                                                    .toInt()
//                                                    .isNotZero()
//                                            )
//                                                viewModel.onActionRestaurant(data)
//                                            else
//                                                viewModel.selectAddress()

                                        },
                                        loggedIn = sharedPreferencesManager.isLoggedIn(),
                                        onConfirm = {
                                            navigator.push(PhoneInputScreen())
                                        }
                                    )
                                }

                                else -> {
                                    NoRestaurantsScreen {
                                        viewModel.selectAddress()
                                    }
                                }
                            }
                        }
                    } else {

                        item {
                            if (homeState.isLoadingBanners) {
                                BannerShimmer()
                            } else if (homeState.banners.isNotEmpty()) {
                                BannersSection(
                                    banners = homeState.banners,
                                    onBannerClick = { banner ->
                                        eventLogger.bannerClick(
                                            type = "home",
                                            id = banner.id,
                                            redirectType = banner.redirectType.default(),
                                            redirectURL = banner.redirectURL,
                                            matchName = EMPTY_STRING
                                        )
                                        when (banner.redirectType) {
                                            CATEGORY -> {
                                                productListViewModel.argCategoryId.value = null
                                                navigator.push(
                                                    ProductListScreen(
                                                        catId = banner.redirectTypeId.getValueOrDefault(),
                                                    )
                                                )
                                            }

                                            PRODUCT, DEAL -> {
                                                navigator.push(
                                                    ProductDetailScreen(
                                                        id = banner.redirectTypeId.getValueOrDefault(),
                                                        name = EMPTY_STRING,
                                                        description = EMPTY_STRING,
                                                        imageUrl = EMPTY_STRING,
                                                        price = ZERO,
                                                        finalPrice = ZERO,
                                                        discountedPrice = ZERO,
                                                        inventoryQty = 0,
                                                        purchasePrice = ZERO,
                                                        isSponsored = false,
                                                        slot = 0,
                                                        showMrp = false,
                                                        purchaseLimit = 0,
                                                        itemQuantity = 0
                                                    )
                                                )
                                            }

                                            EXTERNAL -> {
                                                if (banner.redirectURL.isNotEmpty()) {
                                                    externalLinkHandler.openExternalLink(banner.redirectURL)
                                                }
                                            }

                                            CRICKET -> {}
                                        }
                                    }
                                )
                            }
                        }

                        // Categories Section - Show shimmer when loading
                        item {
                            if (homeState.isLoadingCategories) {
                                CategoriesShimmer()
                            } else {
                                CategoriesComposeView(
                                    categories = homeState.filteredCategories,
                                    categoryColour = categoryColour,
                                    onCategoryClick = { cat ->
                                        if (cat.isNonGrocery) {
                                            eventLogger.clickNonGroceryCategoryTile(
                                                sharedPreferencesManager.getUserId(),
                                                categoryId = cat.id.toString(),
                                                categoryName = cat.name,
                                                destinationURL = EMPTY_STRING
                                            )

                                            if (cat.isNicotine) {
                                                val data = GeneralBottomSheetUiData(
                                                    title = Res.string.disclaimer,
                                                    description = Res.string.message_terms_condition_acceptance,
                                                    secondaryTitle = Res.string.title_keep_nic_with_you,
                                                    secondaryDescription = Res.string.message_keep_nic_with_you,
                                                    positiveButton = Res.string.yes,
                                                    negativeButton = Res.string.no
                                                ).apply {
                                                    confirmListener = {
                                                        productListViewModel.argCategoryId.value =
                                                            null
                                                        navigator.push(
                                                            ProductListScreen(
                                                                catId = cat.id,
                                                                navigateFromParent = false
                                                            )
                                                        )
                                                    }
                                                    onDismiss = { bottomSheetNavigator.hide() }
                                                }
                                                GlobalEvents.updateGeneralBottomSheetScreen(data)
                                                bottomSheetNavigator.show(
                                                    GeneralBottomSheetScreen()
                                                )
                                            } else {
                                                productListViewModel.argCategoryId.value = null
                                                navigator.push(
                                                    ProductListScreen(
                                                        catId = cat.id,
                                                        navigateFromParent = false
                                                    )
                                                )
                                            }
                                        } else {
                                            eventLogger.homeCategoryClick(
                                                cat.name,
                                                cat.id,
                                                cat.sort
                                            )
                                            if (cat.isNicotine) {
                                                val data = GeneralBottomSheetUiData(
                                                    title = Res.string.disclaimer,
                                                    description = Res.string.message_terms_condition_acceptance,
                                                    secondaryTitle = Res.string.title_keep_nic_with_you,
                                                    secondaryDescription = Res.string.message_keep_nic_with_you,
                                                    positiveButton = Res.string.yes,
                                                    negativeButton = Res.string.no
                                                ).apply {
                                                    confirmListener = {
                                                        productListViewModel.argCategoryId.value =
                                                            null
                                                        navigator.push(
                                                            ProductListScreen(
                                                                catId = cat.id,
                                                                navigateFromParent = true
                                                            )
                                                        )
                                                    }
                                                    onDismiss = { bottomSheetNavigator.hide() }
                                                }
                                                GlobalEvents.updateGeneralBottomSheetScreen(data)
                                                bottomSheetNavigator.show(
                                                    GeneralBottomSheetScreen()
                                                )
                                            } else {
                                                productListViewModel.argCategoryId.value = null
                                                navigator.push(ProductListScreen(catId = cat.id))
                                            }
                                        }
                                    }
                                )
                            }
                        }

                        if (homeState.selectedSuperParentCategory?.id == null && homeState.matches.isNotEmpty() && RemoteConfigs.isCricketEnabled()) {
                            item {
                                CricketSection(
                                    matches = homeState.matches,
                                    mainSponsor = homeState.mainSponsor,
                                    cricketPagerState = cricketPagerState,
                                    onMatchClick = { match ->
                                        navigator.push(
                                            MatchDetailScreen(
                                                id = match.id,
                                                currentBattingTeam = match.currentBattingTeam,
                                                teamA = match.teamA,
                                                flagTeamA = match.flagTeamA,
                                                scoreTeamA = match.scoreTeamA,
                                                wicketTeamA = match.wicketTeamA,
                                                overTeamA = match.overTeamA,
                                                currentRunRateTeamA = match.currentRunRateTeamA,
                                                isResultTeamA = match.isResultTeamA,
                                                teamB = match.teamB,
                                                flagTeamB = match.flagTeamB,
                                                scoreTeamB = match.scoreTeamB,
                                                wicketTeamB = match.wicketTeamB,
                                                overTeamB = match.overTeamB,
                                                currentRunRateTeamB = match.currentRunRateTeamB,
                                                requiredRunRateTeamB = match.requiredRunRateTeamB,
                                                score = match.score,
                                                wicket = match.wicket,
                                                currentRunRate = match.currentRunRate,
                                                requiredRunRate = match.requiredRunRate,
                                                isResultTeamB = match.isResultTeamB,
                                                status = match.status,
                                                over = match.over,
                                                message = match.message,
                                                isLive = match.isLive,
                                                isTeamAPlaying = match.isTeamAPlaying,
                                                isTeamBPlaying = match.isTeamBPlaying,
                                                startAtTime = match.startAtTime,
                                                startAtDate = match.startAtDate,
                                                isToday = match.isToday,
                                                isResult = match.isResult,
                                                wasMatchStarted = match.wasMatchStarted,
                                                isDLApplied = match.isDLApplied,
                                                reducedOversTeamA = match.reducedOversTeamA,
                                                isReducedOversTeamA = match.isReducedOversTeamA,
                                                reducedOversTeamB = match.reducedOversTeamB,
                                                isReducedOversTeamB = match.isReducedOversTeamB,
                                                reducedTargetTeamB = match.reducedTargetTeamB
                                            )
                                        )
                                    }
                                )
                            }
                        }

                        if (homeState.selectedSuperParentCategory?.id == null && sharedPreferencesManager.isLoggedIn())
                            item {
                                if (homeState.isLoadingReOrderSwimlane) {
                                    ReOrderShimmer()
                                } else if (homeState.reOrderSwimlane.isNotEmpty()) {
                                    ReOrderComposeView(
                                        frequentItems = homeState.reOrderSwimlane,
                                        viewAllClick = {
                                            navigator.push(
                                                FrequentlyOrderedScreen()
                                            )
                                        },
                                        onProductClick = {
                                            eventLogger.productClicked(
                                                data = it,
                                                productCategory = EMPTY_STRING,
                                                subCategoryName = EMPTY_STRING,
                                                source = "Reorder Swimlane"
                                            )
                                        },
                                        onAddClick = {
                                            eventLogger.addToCartMergn(
                                                it.id,
                                                it.name,
                                                1,
                                                it.price,
                                                "home_reorder_swimlane",
                                                type = "quick_buy"
                                            )
                                        },
                                    )
                                }
                            }


                        // Show shimmer for first 2 swimlanes when loading
                        if (homeState.isLoadingSwimlanes && homeState.swimlanes.isEmpty()) {
                            repeat(2) {
                                item {
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        SwimlaneSkeleton()
                                    }
                                }
                            }
                        } else {
                            items(
                                items = homeState.swimlanes.take(loadedCount.value),
                                key = { "swimlane-${it.id}" }
                            ) { swimlane ->
                                Surface(
                                    color = Color.White
                                ) {
                                    val swimlaneData =
                                        if (homeState.swimlanes.indexOf(swimlane) < 2)
                                            swimlane
                                        else
                                            swimlaneEngineViewModel.swimlaneDataMap[swimlane.id]?.collectAsState()?.value
                                    if (swimlaneData?.getSwimlaneType() == SwimlaneType.CART || swimlaneData?.getSwimlaneType() == SwimlaneType.HOME) {
                                        callGetRecommendedProducts(swimlaneData.getSwimlaneType()?.type.default())
                                    }
//                                else if (swimlaneData?.getSwimlaneType() == SwimlaneType.GROUP_DEALS) {
//                                    callGetFlashDeals()
//                                }
                                    RenderSwimlane(
                                        swimlaneData,
                                        homeState.recommendedProducts,
                                        homeState.flashDeals,
                                        eventLogger
                                    )
                                }
                            }
                        }

                        if (isLoadingNext.value) {
                            item {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    SwimlaneSkeleton()
                                }
                            }
                        }
                    }

                    /*    if (homeState.swimlanes.isNotEmpty()) {
                            items(homeState.swimlanes) { swimlane ->
                                RenderSwimlane(swimlane)
                            }
                        }*/

                    // Success state
                    if (viewState is ViewState.Success) {


                        // Restaurant Section (if Food super parent is selected)
                        if (homeState.selectedSuperParentCategory?.id == SuperParent.FOOD.id) {
                            item {
                                RestaurantSection(
                                    restaurants = homeState.restaurants,
                                    cuisines = homeState.cuisines,
                                    selectedCuisines = homeState.selectedCuisines,
                                    onCuisineClick = { cuisine ->
                                        viewModel.toggleCuisineSelection(cuisine)
                                    },
                                    onRestaurantClick = {
                                        // Navigate to restaurant
                                    }
                                )
                            }
                        }


                    }

                    // Error state
                    if (viewState is ViewState.Error) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Something went wrong",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                }
                            }
                        }
                    }
                }
            }

            // Address Selection Bottom Sheet

            AnimatedVisibility(
                visible = homeState.showAddressSelectionBottomSheet,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                AddressBottomSheet(
                    onDismiss = {
                        viewModel.onAddressSelectionDismiss()
                    },
                    addresses = homeState.addresses,
                    areAddressLoaded = homeState.areAddressesFetched,
                    onClearFoodCart = {
                        cartViewModel.clearCart(isFoodCart = true)
                        {
                            navigator.push(MapScreen())
                            viewModel.onAddressSelectionDismiss()
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
                                when {
                                    sharedPreferencesManager.getSelectedWarehouseId() != 1L -> {
                                        cartViewModel.removeGroceryItemsUsingLocalCart()
                                    }
                                }
                                viewModel.onAddressSelectionDismiss()
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
                                    viewModel.onAddressSelectionDismiss()
                                    foodCartViewModel.clearFoodCartState()
                                }
                            )
                        }
                    },
                    onEdit = { address ->
//                        eventLogger.logMoEngageGenericEvent(
//                            MO_HOME_EDIT_ADDRESS
//                        )
                        navigator.push(address.toMapScreen())
                    },
                    cart = localCartItems.size,
                    foodCart = localFoodCartItems.size,
                    newAddressClick = {
                        navigator.push(MapScreen())
                    }
                )
            }


            val startOrderReview = {
                homeState.orderReviewData?.apply {
                    viewModel.startOrderReview(
                        OrderReviewData(
                            shipmentId = shipmentId.getValueOrDefault(),
                            salesOrderId = salesOrderId.getValueOrDefault(),
                            orderTotal = orderTotal,
                            createdAt = createdAt,
                            deliveredAt = deliveredAt,
                            salesOrderRating = salesOrderRating,
                            orderStatus = OrderStatus.DELIVERED.status
                        ), bottomSheetNavigator
                    )
                }
            }

            ReviewSection(
                orderIdForReview = homeState.orderReviewData?.salesOrderId,
                startReview = {
                    startOrderReview()
                },
                onActionCancelReview = { viewModel.onActionCancelReview() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .clickable {
                        startOrderReview()
                    }
            )


        }

    }
}

@Composable
fun BannersSection(
    banners: List<Banner>,
    onBannerClick: (Banner) -> Unit
) {
    BannerSlider(
        banners = banners,
        onBannerClick = onBannerClick
    )
}

@Composable
fun CategoriesComposeView(
    categories: List<CategoryUiModel>,
    categoryColour: Color,
    onCategoryClick: (CategoryUiModel) -> Unit
) {
    if (categories.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .padding(start = 2.dp)
        ) {
            // Give the categories section a fixed height to avoid infinite constraints
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight() // Adjust as needed for your design (180-220dp typical)
            ) {
                CategoriesComponent(
                    parentCategories = categories,
                    onCategoryClick = onCategoryClick,
                    categoryColour = categoryColour,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

// Multiplatform suspend function to wait for swimlane data to be non-null
suspend fun waitForSwimlaneData(
    swimlaneId: Int,
    viewModel: SwimlaneEngineViewModel
) {
    val flow = viewModel.swimlaneDataMap[swimlaneId] ?: return
    // No flow, nothing to wait for
    flow.first { it != null }
}


