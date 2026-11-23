package com.localpk.app.features.home.data

import com.localpk.app.features.address.data.AddressUiData
import com.localpk.app.features.category.data.CategoryUiModel
import com.localpk.app.features.cricket_live.data.CricketSwimlaneUiData
import com.localpk.app.features.restaurant.data.CuisineUI
import com.localpk.app.features.restaurant.data.RestaurantOrderId
import com.localpk.app.features.restaurant.data.RestaurantUI
import com.localpk.app.features.swimlane_engine.data.SwimlaneUI
import com.localpk.app.features.banner.data.Banner
import com.localpk.app.features.cricket_live.data.PoweredByDetailUiData
import com.localpk.app.features.flash_deals.data.FlashDealUiData
import com.localpk.app.features.order_review.data.OrderReviewData
import com.localpk.app.features.productlist.data.ProductUiData
import kotlin.collections.mutableMapOf

data class HomeState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val isUserLoggedIn: Boolean = false,
    val address: String = "",
    val categories: List<CategoryUiModel> = emptyList(),
    val allCategories: List<CategoryUiModel> = emptyList(),
    val nonGroceryCategories: List<CategoryUiModel> = emptyList(),
    val superParentCategories: List<CategoryUiModel> = emptyList(),
    val selectedSuperParentCategory: CategoryUiModel? = null,
    val filteredCategories: List<CategoryUiModel> = emptyList(),
    val isShowFashionCategorySection: Boolean = false,
    val matches: List<CricketSwimlaneUiData> = emptyList(),
    val mainSponsor: PoweredByDetailUiData? = null,
    val restaurantOrders: List<RestaurantOrderId> = emptyList(),
    val cuisines: List<CuisineUI> = emptyList(),
    val selectedCuisines: List<CuisineUI> = emptyList(),
    val restaurants: List<RestaurantUI> = emptyList(),
    val swimlanes: List<SwimlaneUI> = emptyList(),
    val isOrderReviewApplicable: Boolean = false,
    val showAddressSelectionBottomSheet: Boolean = false,
    val isDataLoading: Boolean = false,
    val searchText: String = "",
    val inboxCount: Int = 0,
    val banners: List<Banner> = emptyList(),
    val reOrderSwimlane: List<ProductUiData> = emptyList(),
    val recommendedProducts: List<ProductUiData> = emptyList(),
    val flashDeals: List<FlashDealUiData> = emptyList(),
    val addresses: List<AddressUiData> = emptyList(),
    val isReviewStarted: Boolean = false,
    val orderReviewData: OrderReviewData? = null,
    val categorySwimlanes: Map<String, List<SwimlaneUI>> = emptyMap(),
    val categoryScrollStates: Map<String, CategoryScrollState> = emptyMap(),
    val shouldResetScrollState: Boolean = false,
    val isLoadingBanners: Boolean = false,
    val isLoadingCategories: Boolean = false,
    val isLoadingSuperParentCategories: Boolean = false,
    val isLoadingReOrderSwimlane: Boolean = false,
    val isLoadingSwimlanes: Boolean = false,
    val areAddressesFetched: Boolean = false
) {
    companion object {
        val InitState = HomeState()
    }
}


data class ScrollInfo(
    val index: Int,
    val offset: Int,
    val loadedCount: Int
)

data class CategoryScrollState(
    val scrollIndex: Int,
    val scrollOffset: Int,
    val loadedCount: Int
)

