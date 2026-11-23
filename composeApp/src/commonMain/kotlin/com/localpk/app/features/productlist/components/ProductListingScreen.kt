package com.localpk.app.features.productlist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.localpk.app.features.productlist.data.HeaderRecyclerAdapterConstants
import com.localpk.app.features.productlist.data.ProductFragmentArgs
import com.localpk.app.features.productlist.data.ProductHeaderAdapterUiData
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.productlist.viewModel.ProductListViewModel
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.compose.koinInject

data class ScrollStateInfo(
    val firstVisibleIndex: Int,
    val categoryIndexKey: Int
)

@Composable
fun ProductListingScreen(
    productArgs: ProductFragmentArgs,
    dataItems: List<ProductHeaderAdapterUiData>,
    onProductClick: (ProductUiData, String) -> Unit,
    onAddClick: (ProductUiData, String) -> Unit,
    onBannerClick: (Long) -> Unit,
    onBannerToProductClick: (Long) -> Unit,
    onBannerToBottomSheetClick: () -> Unit,
    onBannerToVideoClick: (String) -> Unit,
    subCategoryClick: (String, Int) -> Unit,
    isLoading: Boolean,
    onScrollPositionChange: ((Int) -> Unit)? = null,
    productListViewModel: ProductListViewModel
) {
    val listState = rememberLazyGridState()
    val eventLogger: EventLogger = koinInject()
    // 1. State for subcategory tab index
    val selectedSubCategoryTabIndex = rememberSaveable { mutableIntStateOf(0) }
    // Flag to prevent scroll tracking during initial restoration
    var isRestoringScrollPosition by remember { mutableStateOf(false) }
    // Precompute headers for current data set
    val headerList = remember(dataItems) { dataItems.filter { it.type == HeaderRecyclerAdapterConstants.HEADER } }

    // Reset subcategory tab index when main category changes
    LaunchedEffect(productArgs.parentId) {
        selectedSubCategoryTabIndex.intValue = 0
        isRestoringScrollPosition = true
    }

    LaunchedEffect(listState, dataItems) {
        snapshotFlow {
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            val firstVisibleItem = visibleItems.firstOrNull()?.index ?: 0
            val lastVisibleItem = visibleItems.lastOrNull()?.index ?: 0

            // Determine which index to use for header logic
            val lastHeaderPosition =
                headerList.getOrNull(headerList.size.getValueOrDefault() - 1)?.parentPosition

            val categoryIndexKey =
                if (lastVisibleItem == lastHeaderPosition) lastVisibleItem else firstVisibleItem

            ScrollStateInfo(
                firstVisibleIndex = firstVisibleItem,
                categoryIndexKey = categoryIndexKey
            )
        }
            .distinctUntilChanged()
            .collect { state ->
                // ✅ 1. Notify parent ViewModel about scroll position (for caching)
                if (!isRestoringScrollPosition) {
                    onScrollPositionChange?.invoke(state.firstVisibleIndex)
                }

                // ✅ 2. Update category tab selection
                val targetParentId = dataItems.getOrNull(state.categoryIndexKey)?.parentData?.id
                val categoryIndex = productArgs.subCategories.indexOfFirst { it.id == targetParentId }
                if (categoryIndex in productArgs.subCategories.indices) {
                    selectedSubCategoryTabIndex.intValue = categoryIndex
                }
            }
    }

    // 🔹 Restore scroll position when data is loaded
    LaunchedEffect(dataItems) {
        if (dataItems.isNotEmpty()) {
            val cachedData = productListViewModel.getCachedDataForCategory(productArgs.parentId)
            cachedData?.let { data ->
                listState.scrollToItem(data.scrollPosition.coerceAtLeast(0))
            }
        }
        isRestoringScrollPosition = false
    }

    Column {
        SubCategoryTabs(
            listState = listState,
            selectedTabIndex = selectedSubCategoryTabIndex,
            categories = productArgs.subCategories,
            dataItems = dataItems.orEmptyArrayList(),
            onClick = { name, id ->
                eventLogger.productSubCategoryClick(name, id)
                val idx = productArgs.subCategories.indexOfFirst { it.id == id.toLong() }
                if (idx >= 0) {
                    selectedSubCategoryTabIndex.intValue = idx
                }
                subCategoryClick(name, id)
            }
        )

        ProductListing(
            state = listState,
            dataItems = dataItems.orEmptyArrayList(),
            onProductClick = { product, subCategory ->
                onProductClick(product, subCategory)
            },
            onAddClick = { product, subCategory ->
                onAddClick(product, subCategory)
            },
            onBannerClick = {
                onBannerClick(it)
            },
            onBannerToProductClick = {
                onBannerToProductClick(it)
            },
            onBannerToBottomSheetClick = {
                onBannerToBottomSheetClick()
            },
            onBannerToVideoClick = {
                onBannerToVideoClick(it)
            },
            isLoading = isLoading
        )
    }
}