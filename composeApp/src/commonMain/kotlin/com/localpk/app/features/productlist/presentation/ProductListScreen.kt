package com.localpk.app.features.productlist.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.banner.presentation.BannerViewModel
import com.localpk.app.features.category.presentation.CategoryViewModel
import com.localpk.app.features.product_detail.components.Header
import com.localpk.app.features.productlist.components.ProductListingScreen
import com.localpk.app.features.productlist.data.ProductFragmentArgs
import com.localpk.app.features.productlist.viewModel.ProductListViewModel
import com.localpk.app.features.search.presentation.SearchScreen
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.TabsUnselectedColor
import com.localpk.app.theming.transparent
import com.localpk.app.utils.BUNDLE
import com.localpk.app.utils.CATEGORY
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.isNull
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class ProductListScreen(
    private val catId: Long,
    private val navigateFromParent: Boolean = true,
) : Screen {

    @Composable
    override fun Content() {

        val bannerViewModel = koinViewModel<BannerViewModel>()
        val categoryViewModel = koinViewModel<CategoryViewModel>()
        val productListViewModel = koinViewModel<ProductListViewModel>()

        if (productListViewModel.argCategoryId.value.isNull()) {
            productListViewModel.argCategoryId.value = catId
        }

        val parentCategories by productListViewModel.categoryLiveData.collectAsState()
        val selectedChildCategory by productListViewModel.selectedChildCategoryLiveData.collectAsState()
        val categoryBannersLiveData by bannerViewModel.categoryBanners.collectAsState()
        val eventLogger: EventLogger = koinInject()

        val categories = parentCategories
        //     if (categories.isEmpty()) return

        LaunchedEffect(Unit) {
            productListViewModel.selectedCategoryIdToLoadProducts.value?.let { } ?: run {
                productListViewModel.updateSelectedCategoryIdToLoadProducts(productListViewModel.argCategoryId.value.getValueOrDefault())
            }

            if (productListViewModel.categoryLiveData.value.isEmpty()) {
                if (categoryViewModel.categories.value.isEmpty()) {
                    categoryViewModel.loadCategories {
                        productListViewModel.loadCategories(categoryViewModel.categories.value)
                    }
                } else {
                    productListViewModel.loadCategories(categoryViewModel.categories.value)
                }
            }
        }


        var selectedTabIndex by remember { mutableStateOf<Int?>(null) }
        val safeTabIndex = selectedTabIndex?.coerceIn(categories.indices) ?: 0

        if (safeTabIndex !in categories.indices) return

        var selectedCategory by remember { mutableStateOf(categories.firstOrNull { it.id == productListViewModel.argCategoryId.value }) }

        // Reset tab index when category changes and handle tab selection reactively
        LaunchedEffect(productListViewModel.argCategoryId, categories) {
            if (categories.isNotEmpty()) {
                val correctIndex =
                    categories.indexOfFirst { it.id == productListViewModel.argCategoryId.value }
                if (correctIndex >= 0) {
                    selectedTabIndex = correctIndex
                }
            }
        }

        val subCategories = productListViewModel.categoriesListResponse
            ?.filter { it.parentId == selectedCategory?.id }
            ?: emptyList()

        val args = ProductFragmentArgs(
            parentId = selectedCategory?.id.getValueOrDefault(),
            parentName = selectedCategory?.name.default(),
            subCategoryId = selectedChildCategory?.id.getValueOrDefault(),
            subCategories = subCategories,
            categoryBanner = null,
            navigateFromParent = true,
            subCategoryName = selectedChildCategory?.name ?: EMPTY_STRING
        )

        LaunchedEffect(args.parentId, selectedTabIndex) {
            // Only make API call if a category is actually selected (not null)
            if (selectedTabIndex != null) {
                selectedCategory = categories[selectedTabIndex.getValueOrDefault()]
                productListViewModel.fetchProducts(args)
                bannerViewModel.getBanners(
                    EMPTY_STRING,
                    CATEGORY,
                    args.parentId
                )
            }
        }

        val finalCategoryBanners = categoryBannersLiveData.filter {
            // Filter out BUNDLE if RemoteConfigs disabled (placeholder condition here)
            it.redirectType != BUNDLE
        }

        val updatedArgs = args.copy(categoryBanner = finalCategoryBanners)


        val dataItems by productListViewModel.productList.collectAsState()
        val isLoading by productListViewModel.isLoadingData.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        // Clear cache when navigating away from this screen
        DisposableEffect(Unit) {
            onDispose {
                //      productListViewModel.clearCache()
            }
        }

        StatusBarIconColor(backgroundColor = Color.White)
        Column(
            modifier = Modifier
                .fillMaxSize().statusBarsPadding()
                .background(Color.White)
        ) {
            Header(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                onBackClick = { navigator.pop() },
                onSearchClick = {
                    navigator.push(SearchScreen(false))
                },
            )

            ScrollableTabRow(
                selectedTabIndex = safeTabIndex,
                edgePadding = 0.dp,
                indicator = {},
                divider = {}
            ) {
                categories.forEachIndexed { index, category ->
                    Tab(
                        selected = index == safeTabIndex,
                        onClick = {
                            selectedTabIndex = index
                            eventLogger.productCategoryClick(category.name)
                        },
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 8.dp)
                            .padding(start = 2.dp)
                            .height(40.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (index == safeTabIndex) ColorPrimary else Color.White
                            )
                            .border(
                                width = 1.dp,
                                color = if (index == safeTabIndex) transparent else TabsUnselectedColor,
                                shape = RoundedCornerShape(50)
                            ),
                        text = {
                            Text(
                                text = category.name,
                                color = if (index == safeTabIndex) Color.White else ColorPrimary
                            )
                        }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                ProductListingScreen(
                    productArgs = updatedArgs,
                    dataItems = dataItems,
                    onProductClick = { product, subCategory ->
                        eventLogger.productClicked(
                            product,
                            updatedArgs.parentName,
                            subCategory,
                            "product_list"
                        )
                        productListViewModel.argCategoryId.value = updatedArgs.parentId
                    },
                    onAddClick = { product, _ ->
                        if (product.hasVariants.not()) {
                            eventLogger.addToCartMergn(
                                product.id,
                                product.name,
                                1,
                                product.price,
                                "product_list",
                                type = "quick_buy"
                            )
                        }

                    },
                    onBannerClick = {},
                    onBannerToProductClick = {},
                    onBannerToBottomSheetClick = {},
                    onBannerToVideoClick = {},
                    subCategoryClick = { _, _ -> },
                    isLoading = isLoading,
                    onScrollPositionChange = { scrollPosition ->
                        // Save scroll position for current category as user scrolls
                        if (selectedCategory?.id.isNotNull()) {
                            productListViewModel.updateScrollPositionForCurrentCategory(
                                selectedCategory?.id.getValueOrDefault(),
                                scrollPosition
                            )
                        }

                    },
                    productListViewModel = productListViewModel
                )
            }
        }
    }

}