package com.localpk.app.features.home.presentation

import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.localpk.app.base.BaseViewModel
import com.localpk.app.base.ViewState
import com.localpk.app.features.address.presentation.AddressListViewModel
import com.localpk.app.features.banner.data.BannerUseCaseParams
import com.localpk.app.features.banner.presentation.BannerViewModel
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.category.data.CategoryType
import com.localpk.app.features.category.data.CategoryUiModel
import com.localpk.app.features.category.data.SuperParent
import com.localpk.app.features.category.presentation.CategoryViewModel
import com.localpk.app.features.cricket_live.viewModel.MatchDetailViewModel
import com.localpk.app.features.flash_deals.data.FlashDealStatus
import com.localpk.app.features.flash_deals.viewModel.FlashDealViewModel
import com.localpk.app.features.frequently_ordered.viewModel.FrequentlyOrderedViewModel
import com.localpk.app.features.home.data.CategoryScrollState
import com.localpk.app.features.home.data.HomeState
import com.localpk.app.features.order_review.data.OrderForReview
import com.localpk.app.features.order_review.data.OrderReviewData
import com.localpk.app.features.order_review.data.OrderReviewResponse
import com.localpk.app.features.order_review.data.ReviewStatus
import com.localpk.app.features.order_review.domain.CancelOrderReviewUseCase
import com.localpk.app.features.order_review.domain.GetOrderForReviewUseCase
import com.localpk.app.features.order_review.presentation.OrderReviewBottomSheetScreen
import com.localpk.app.features.orders.data.OrderStatus
import com.localpk.app.features.recommendation_engine.viewModel.RecommendationEngineViewModel
import com.localpk.app.features.recommendation_engine.viewModel.RecommendationEngineViewModel.Companion.RECOMMEND_FOR_YOU
import com.localpk.app.features.restaurant.data.CuisineUI
import com.localpk.app.features.restaurant.domain.RestaurantParameters
import com.localpk.app.features.restaurant.viewModels.GetRestaurantViewModel
import com.localpk.app.features.swimlane_engine.data.LandingPageType
import com.localpk.app.features.swimlane_engine.data.SwimlaneUI
import com.localpk.app.features.swimlane_engine.presentation.SwimlaneEngineViewModel
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.FIRST_PAGE
import com.localpk.app.utils.FoodCategoryId
import com.localpk.app.utils.PRODUCT_LIMIT_MIN
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.isNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.component.KoinComponent

class HomeViewModel(
    private val categoryViewModel: CategoryViewModel,
    private val bannerViewModel: BannerViewModel,
    private val swimlaneEngineViewModel: SwimlaneEngineViewModel,
    private val getRestaurantViewModel: GetRestaurantViewModel,
    private val frequentlyOrderedViewModel: FrequentlyOrderedViewModel,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val recommendationEngineViewModel: RecommendationEngineViewModel,
    private val getOrderForReviewUseCase: GetOrderForReviewUseCase,
    val flashDealsViewModel: FlashDealViewModel,
    val cartViewModel: CartViewModel,
    private val addressListViewModel: AddressListViewModel,
    private val eventLogger: EventLogger,
    private val cancelOrderReviewUseCase: CancelOrderReviewUseCase,
    private val matchDetailViewModel: MatchDetailViewModel
) : BaseViewModel<HomeState>(), KoinComponent {

    private val _homeState = MutableStateFlow(HomeState.InitState)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    private val _addressesFetched = MutableStateFlow(false)
    val addressesFetched = _addressesFetched.asStateFlow()

    private val _ratingFetched = MutableStateFlow(false)
    val ratingFetched = _ratingFetched.asStateFlow()

    init {
        loadHomeData()
        updateLoginStatus()
        if (sharedPreferencesManager.isLoggedIn()) {
            eventLogger.logUserInfo(
                sharedPreferencesManager.getUserId().toString(),
                sharedPreferencesManager.getUserName(),
                sharedPreferencesManager.getPhoneNumber()
            )
        }
    }

    fun observeCategories() {
        // Set loading state when starting to load categories
        val currentState = _homeState.value
        _homeState.value = currentState.copy(
            isLoadingCategories = true,
            isLoadingSuperParentCategories = RemoteConfigs.isSuperParentCategoriesEnabled()
        )

        categoryViewModel.loadCategories {
            launch {
                categoryViewModel.categories.collect { categories ->

                    val parentCategories =
                        categories.filter { !it.isNonGrocery && it.categoryType == CategoryType.PARENT.value }
                    val allCategories =
                        categories.filter { it.categoryType == CategoryType.PARENT.value }
                    val nonGroceryCategories = categories.filter { it.isNonGrocery }
                    val updatedState = _homeState.value
                    _homeState.value = updatedState.copy(
                        categories = parentCategories,
                        filteredCategories = parentCategories.filter { it.id != FoodCategoryId },
                        allCategories = allCategories,
                        nonGroceryCategories = nonGroceryCategories,
                        isLoadingCategories = false
                    )

                }
            }
            launch {
                if (RemoteConfigs.isSuperParentCategoriesEnabled()) {
                    categoryViewModel.superParentCategories.collect { superParentCategories ->
                        val updatedState = _homeState.value
                        _homeState.value = updatedState.copy(
                            superParentCategories = superParentCategories,
                            isLoadingSuperParentCategories = false
                        )
                    }
                } else {
                    val updatedState = _homeState.value
                    _homeState.value = updatedState.copy(
                        superParentCategories = emptyList(),
                        isLoadingSuperParentCategories = false
                    )
                }
            }
        }

    }

    private fun loadHomeData() {
        observeCategories()
        fetchUserSettings()
        loadBanners(
            BannerUseCaseParams(
                belongsTo = "home",
                bannerCacheVersion = "1"
            )
        )
        if (RemoteConfigs.isCricketEnabled())
            getMainSponsor()

        observeSwimlanes()
        getRestaurantOrderIds()
    }

    private fun fetchUserSettings() {
        /*  unZipViewStateFlow(fetchUserSettingsUseCase(Unit), onSuccess = {
              val response = it as UserSettings
              _userSettings.value = response
          }, onError = {
              _userSettings.value =
                  UserSettings(
                      bannerVersion = EMPTY_STRING,
                      categoryVersion = EMPTY_STRING
                  )
          })*/
    }

    fun onActionSuperParent(
        id: Long,
        listenerCallback: (selectedSuperParentCategoryId: String) -> Unit = {}
    ) {
        val currentState = _homeState.value
        val filteredCategories = currentState.categories.filter { it.parentId == id }
        val selectedSuperParentCategory = currentState.superParentCategories.find { it.id == id }
        _homeState.value = currentState.copy(
            filteredCategories = filteredCategories.filter { it.id != FoodCategoryId },
            selectedSuperParentCategory = selectedSuperParentCategory
        )
        if (selectedSuperParentCategory.isNotNull()) {
            // Track category click event
            eventLogger.homeCategoryClick(
                name = selectedSuperParentCategory?.name ?: "Unknown Category",
                id = selectedSuperParentCategory?.id ?: 0L,
                sort = null
            )
            listenerCallback(selectedSuperParentCategory?.id.toString())
            observeSwimlanes()
        }
    }

    fun updateSelectedSuperParentCategory(data: CategoryUiModel?) {
        val currentState = _homeState.value
        _homeState.value = currentState.copy(selectedSuperParentCategory = data)
    }

    fun updateFilteredCategories(data: List<CategoryUiModel>) {
        val filteredData = data.filter { it.id != FoodCategoryId }
        val currentState = _homeState.value
        _homeState.value = currentState.copy(filteredCategories = filteredData)
    }

    fun toggleCuisineSelection(cuisine: CuisineUI) {
        val currentState = _homeState.value
        val currentSelections = currentState.selectedCuisines.toMutableList()

        if (currentSelections.any { it.id == cuisine.id }) {
            currentSelections.removeAll { it.id == cuisine.id }
        } else {
            currentSelections.add(cuisine)
        }

        _homeState.value = currentState.copy(selectedCuisines = currentSelections)
        eventLogger.viewCuisines(homeState.value.selectedCuisines.map { it.name })
        getRestaurants()
    }

    fun selectAddress() {
        val currentState = _homeState.value
        _homeState.value = currentState.copy(showAddressSelectionBottomSheet = true)
    }

    fun onAddressSelectionDismiss() {
        val currentState = _homeState.value
        _homeState.value = currentState.copy(showAddressSelectionBottomSheet = false)
    }


    private fun getMainSponsor() {
        matchDetailViewModel.getMainSponsor {
            launch {
                matchDetailViewModel.mainSponsor.collectLatest { mainSponsor ->
                    val updatedState = _homeState.value
                    _homeState.value = updatedState.copy(
                        mainSponsor = mainSponsor
                    )
                }
            }
        }
    }

    fun matchesSummary() {
        matchDetailViewModel.matchesSummary {
            launch {
                matchDetailViewModel.matches.collectLatest { matches ->
                    val updatedState = _homeState.value
                    _homeState.value = updatedState.copy(
                        matches = matches
                    )
                }
            }
        }
    }

    fun loadBanners(params: BannerUseCaseParams) {
        // Set loading state when starting to load banners
        val currentState = _homeState.value
        _homeState.value = currentState.copy(isLoadingBanners = true)

        bannerViewModel.getBanners(
            belongsTo = params.belongsTo,
            bannerCacheVersion = params.bannerCacheVersion
        ) {
            launch {
                bannerViewModel.banners.collect { banners ->
                    val updatedState = _homeState.value
                    _homeState.value = updatedState.copy(
                        banners = banners,
                        isLoadingBanners = false
                    )
                }
            }
        }

    }

    private fun observeSwimlanes() {
        launch {
            // Observe loading state changes
            swimlaneEngineViewModel.isSwimlaneLoading.collectLatest { loading ->
                val currentState = _homeState.value
                _homeState.value = currentState.copy(isLoadingSwimlanes = loading)
            }
        }

        launch {
            // Always observe the swimlane key based on selected super parent category
            homeState.collect { state ->
                val swimlaneKey =
                    state.selectedSuperParentCategory?.id?.toString() ?: LandingPageType.Main.type
                swimlaneEngineViewModel.swimlaneMap[swimlaneKey]?.collectLatest { swimlanes ->
                    updateSwimlanes(swimlanes, swimlaneEngineViewModel.isSwimlaneLoading.value)
                }
            }
        }
    }

    fun refreshSwimlanes() {
        // Method to refresh swimlanes when address changes
        // This ensures we get fresh data without duplicating observations
        clearSwimlanes()
        swimlaneEngineViewModel.clearAllSwimlaneData() // Clear ALL swimlane data

        // Clear all category swimlanes and scroll states
        val currentState = _homeState.value
        _homeState.value = currentState.copy(
            categorySwimlanes = emptyMap(),
            categoryScrollStates = emptyMap()
        )

        // Force fetch new swimlanes
        swimlaneEngineViewModel.getSwimlanes(LandingPageType.Main.type) {
            observeSwimlanes()
        }
    }

    fun resetScrollState() {
        // Reset scroll state when address changes
        // This will trigger the HomeScreen to reset loadedCount and isInitialLoad
        val currentState = _homeState.value
        _homeState.value = currentState.copy(
            shouldResetScrollState = true
        )
    }

    fun resetScrollStateFlag() {
        // Reset the scroll state flag after it has been processed
        val currentState = _homeState.value
        _homeState.value = currentState.copy(
            shouldResetScrollState = false
        )
    }

    fun updateSwimlanes(swimlanes: List<SwimlaneUI>, isLoading: Boolean) {
        val currentState = _homeState.value
        _homeState.value = currentState.copy(
            swimlanes = swimlanes,
            isLoadingSwimlanes = isLoading
        )
    }

    private fun clearSwimlanes() {
        val currentState = _homeState.value
        _homeState.value = currentState.copy(
            swimlanes = emptyList(),
            recommendedProducts = emptyList(),
            flashDeals = emptyList()
        )
    }

    fun saveCategoryScrollState(
        categoryId: String,
        scrollIndex: Int,
        scrollOffset: Int,
        loadedCount: Int
    ) {
        val newState = CategoryScrollState(scrollIndex, scrollOffset, loadedCount)
        val currentStates = _homeState.value.categoryScrollStates.toMutableMap()
        currentStates[categoryId] = newState
        _homeState.value = _homeState.value.copy(categoryScrollStates = currentStates)
    }

    fun getCategoryScrollState(categoryId: String): CategoryScrollState? {
        return _homeState.value.categoryScrollStates[categoryId]
    }

    fun saveCategorySwimlanes(categoryId: String, swimlanes: List<SwimlaneUI>) {
        val currentStates = _homeState.value.categorySwimlanes.toMutableMap()
        currentStates[categoryId] = swimlanes
        _homeState.value = _homeState.value.copy(categorySwimlanes = currentStates)
    }

    fun getCategorySwimlanes(categoryId: String): List<SwimlaneUI>? {
        return _homeState.value.categorySwimlanes[categoryId]
    }


    fun calculateColors(selectedSuperParentCategory: CategoryUiModel?): List<Color> {
        return when (selectedSuperParentCategory?.id) {
            SuperParent.GROCERY.id -> listOf(
                Color(0xFFE0E7FF),
                Color(0xFFD1DBFF),
                Color(0xFF6366F1),
                Color(0xFFD1DBFF)
            )

            SuperParent.FOOD.id -> listOf(
                Color(0xFFFFF0D3),
                Color(0xFFFFE5B4),
                Color(0xFFE9A804),
                Color(0xFFFFE5B4)
            )

            SuperParent.FASHION.id -> listOf(
                Color(0xFFD4D5FF),
                Color(0xFFC7C8FD),
                Color(0xFF7C7EDC),
                Color(0xFFC3C5FF)
            )

            SuperParent.ELECTRONICS.id -> listOf(
                Color(0xFFC6DAFF),
                Color(0xFFB1D9FF),
                Color(0xFF4691C0),
                Color(0xFFC2E9FF)
            )

            SuperParent.BEAUTYHUB.id -> listOf(
                Color(0xFFFFE8F8),
                Color(0xFFFAD4EE),
                Color(0xFFE25EB7),
                Color(0xFFFAD4EE)
            )

            SuperParent.LIFESTYLE.id -> listOf(
                Color(0xFFFFDBBB),
                Color(0xFFF4CFAF),
                Color(0xFFBC8759),
                Color(0xFFFFDBBB)
            )

            else -> listOf(
                Color(0xFFF0F1F5),
                Color(0xFFE5E6EA),
                Color(0xFF6366F1),
                Color(0xFFF0F1F5)
            )
        }
    }

    private fun getRestaurantOrderIds() {
        getRestaurantViewModel.getRestaurantOrderIds()
        launch {
            getRestaurantViewModel.restaurantOrders.collect { restaurantOrders ->
                val currentState = _homeState.value
                _homeState.value = currentState.copy(
                    restaurantOrders = restaurantOrders
                )
            }
        }
    }

    fun getRestaurants() {
        _homeState.value = _homeState.value.copy(isLoading = true)
        val args = RestaurantParameters(
            query = EMPTY_STRING,
            cuisines = homeState.value.selectedCuisines.map { it.name }
        )
        getRestaurantViewModel.getRestaurants(
            args
        ) {
            launch {
                getRestaurantViewModel.restaurants.collect { restaurants ->
                    val currentState = _homeState.value
                    _homeState.value = currentState.copy(
                        restaurants = restaurants
                    )
                }
            }
            launch {
                getRestaurantViewModel.cuisines.collect { cuisines ->
                    val currentState = _homeState.value
                    _homeState.value = currentState.copy(
                        cuisines = cuisines,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun updateLoginStatus() {
        val currentState = _homeState.value
        _homeState.value = currentState.copy(
            isUserLoggedIn = sharedPreferencesManager.isLoggedIn()
        )
    }

    fun getReOrderSwimlaneProducts() {
        // Set loading state when starting to load reorder swimlane
        val currentState = _homeState.value
        _homeState.value = currentState.copy(isLoadingReOrderSwimlane = true)

        frequentlyOrderedViewModel.getFrequentlyOrderedItems(
            limit = PRODUCT_LIMIT_MIN,
            page = FIRST_PAGE,
            cart = cartViewModel.cartState.value
        )
        launch {
            frequentlyOrderedViewModel.frequentlyOrderedList.collect { frequentlyOrderedList ->
                val updatedState = _homeState.value
                _homeState.value = updatedState.copy(
                    reOrderSwimlane = frequentlyOrderedList,
                    isLoadingReOrderSwimlane = false
                )
            }
        }
    }

    fun getRecommendedProducts(swimlaneType: String) {
        if (sharedPreferencesManager.isLoggedIn() && homeState.value.recommendedProducts.isEmpty()) {
            recommendationEngineViewModel.getRecommendationItems(
                RECOMMEND_FOR_YOU,
                swimlaneType,
                0,
                cartViewModel.cartState.value
                // cart = MainActivity.viewCart.value
            )
            launch {
                recommendationEngineViewModel.recommendationList.collect { recommendedProducts ->
                    val currentState = _homeState.value
                    _homeState.value = currentState.copy(
                        recommendedProducts = recommendedProducts
                    )
                }
            }
        }
    }

    fun getFlashDeals() {
        if (homeState.value.flashDeals.isEmpty()) {
            flashDealsViewModel.fetchFlashDeals(
                FlashDealStatus.ACTIVE.status,
                cart = cartViewModel.cartState.value
            )
            launch {
                flashDealsViewModel.flashDeals.collect {
                    val currentState = _homeState.value
                    _homeState.value = currentState.copy(
                        flashDeals = it
                    )
                }
            }
        }

    }

    fun getAddresses() {
        addressListViewModel.getOperationalCities()
        addressListViewModel.loadSavedAddresses()
        launch {
            addressListViewModel.addresses.collect { addresses ->
                val currentState = _homeState.value
                _homeState.value = currentState.copy(
                    addresses = addresses
                )
            }
        }
        launch {
            addressListViewModel.isAddressesLoaded.collect { areAddressesFetched ->
                val currentState = _homeState.value
                _homeState.value = currentState.copy(
                    areAddressesFetched = areAddressesFetched
                )
                _addressesFetched.value = areAddressesFetched

            }
        }
    }

    fun fetchOrderForReview(bottomSheetNavigator: BottomSheetNavigator) {
        launch {
            getOrderForReviewUseCase(Unit).collect { viewState ->
                when (viewState) {
                    is ViewState.Success -> {
                        val order: OrderReviewResponse = viewState.data
                        val orderForReview: OrderForReview? = order.body

                        _ratingFetched.value = true

                        orderForReview?.let { review ->

                            val currentState = _homeState.value

                            _homeState.value = currentState.copy(
                                orderReviewData = OrderReviewData(
                                    shipmentId = review.shipmentId,
                                    salesOrderId = review.salesOrderId,
                                    orderTotal = review.orderTotal,
                                    createdAt = review.createdAt,
                                    deliveredAt = review.deliveredAt,
                                    salesOrderRating = null,
                                    orderStatus = OrderStatus.DELIVERED.status
                                ),
                            )

                            if (review.ratingStatus == ReviewStatus.PENDING.statusValue ||
                                (review.ratingStatus == ReviewStatus.SENT_TO_CUSTOMER.statusValue && currentState.isReviewStarted)
                            ) {
                                bottomSheetNavigator.show(
                                    OrderReviewBottomSheetScreen(
                                        shipmentId = review.shipmentId,
                                        salesOrderId = review.salesOrderId,
                                        deliveryDate = review.deliveredAt,
                                        orderTotal = review.orderTotal,
                                    )
                                )
                                val updatedState = _homeState.value
                                _homeState.value = updatedState.copy(
                                    isReviewStarted = false,
                                    isOrderReviewApplicable = false
                                )
                            } else if (review.ratingStatus == ReviewStatus.SENT_TO_CUSTOMER.statusValue) {
                                val updatedState = _homeState.value
                                _homeState.value = updatedState.copy(
                                    isOrderReviewApplicable = true
                                )
                            }
                        }
                    }

                    is ViewState.Error -> {
                        // TODO: Show error toast
                        // toastManager.showError(viewState.viewError)
                    }

                    is ViewState.Loading -> {
                        // Handle loading state if needed
                    }

                    is ViewState.Empty -> {
                        // Handle empty state if needed
                    }
                }
            }
        }
    }


    fun startOrderReview(
        orderForReviewData: OrderReviewData,
        bottomSheetNavigator: BottomSheetNavigator,
    ) {
        orderForReviewData.apply {
            val isDelivered =
                (orderStatus == OrderStatus.DELIVERED.status || orderStatus == OrderStatus.PARTIAL_RETURNED.status)

            if (salesOrderRating.isNull() && isDelivered) {
                bottomSheetNavigator.show(
                    OrderReviewBottomSheetScreen(
                        salesOrderId = salesOrderId,
                        deliveryDate = deliveredAt,
                        orderTotal = orderTotal,
                        shipmentId = shipmentId
                    )
                )
            }
        }
    }

    fun onActionCancelReview() {
        launch {
            cancelOrderReviewUseCase(_homeState.value.orderReviewData?.shipmentId.getValueOrDefault()).collect { viewState ->
                when (viewState) {
                    is ViewState.Success -> {
                        val updatedState = _homeState.value
                        _homeState.value = updatedState.copy(
                            isOrderReviewApplicable = false,
                            orderReviewData = null
                        )
                    }

                    is ViewState.Error -> {
                        // toastManager.showError(viewState.viewError)
                    }

                    is ViewState.Loading -> {
                        // Handle loading state if needed
                    }

                    is ViewState.Empty -> {
                        // Handle empty state if needed
                    }
                }
            }
        }
    }

    fun resetData() {
        val currentState = _homeState.value
        _homeState.value = currentState.copy(
            swimlanes = emptyList(),
            recommendedProducts = emptyList(),
            flashDeals = emptyList(),
            categories = emptyList(),
            allCategories = emptyList(),
            superParentCategories = emptyList(),
            nonGroceryCategories = emptyList(),
            filteredCategories = emptyList(),
            selectedSuperParentCategory = null,
            cuisines = emptyList(),
            selectedCuisines = emptyList(),
            restaurants = emptyList(),
            banners = emptyList(),
            reOrderSwimlane = emptyList()
        )
    }

}