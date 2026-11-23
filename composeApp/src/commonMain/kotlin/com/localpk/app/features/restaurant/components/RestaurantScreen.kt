package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.localpk.app.features.product_detail.components.Header
import com.localpk.app.features.restaurant.data.CategoryItemUIData
import com.localpk.app.features.restaurant.data.RestaurantDetailArgsData
import com.localpk.app.features.restaurant.data.RestaurantDetailUI
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun RestaurantScreen(
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    restaurantMenuDetails: RestaurantDetailUI?,
    restaurantDetails: RestaurantDetailArgsData,
    restaurantLogoImage: String,
    onInfoClick: () -> Unit,
    onFoodAddClick: (CategoryItemUIData, String) -> Unit,
    onFoodCardClick: (CategoryItemUIData, String) -> Unit,
    onIncrementClick: (CategoryItemUIData) -> Unit,
    onDecrementClick: (CategoryItemUIData) -> Unit,
    onRestaurantCategoryClick: (String, String) -> Unit
) {
    if (restaurantMenuDetails != null) {

        val selectedTabIndex = remember { mutableIntStateOf(0) }
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        val validCategories = remember(restaurantMenuDetails.menu?.categories) {
            restaurantMenuDetails.menu?.categories.orEmpty()
                .filter { !it.items.isNullOrEmpty() }
        }

        val categoryToListIndex = remember(validCategories) {
            val map = mutableMapOf<Int, Int>()
            var currentIndex = 0
            validCategories.forEachIndexed { index, category ->
                map[index] = currentIndex
                currentIndex += 1 + category.items?.size.getValueOrDefault()
            }
            map
        }

        val listIndexToTabIndex = remember(categoryToListIndex) {
            categoryToListIndex.entries.associate { (tabIndex, listIndex) ->
                listIndex to tabIndex
            }
        }

        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemIndex }
                .distinctUntilChanged()
                .collect { visibleIndex ->
                    listIndexToTabIndex[visibleIndex]?.let { tabIndex ->
                        if (selectedTabIndex.intValue != tabIndex) {
                            selectedTabIndex.intValue = tabIndex
                        }
                    }
                }
        }

        Surface(
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White).statusBarsPadding()
            ) {
                Header(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 8.dp, end = 6.dp)
                        .padding(8.dp),
                    onBackClick = { onBackClick() },
                    onSearchClick = { onSearchClick() }
                )
                RestaurantInfo(
                    restaurantsDetails = restaurantDetails,
                    restaurantLogoImage =
                    restaurantLogoImage
                ) { onInfoClick() }
                if (validCategories.isEmpty().not()) {
                    TabRowSectionFood(
                        menu = validCategories,
                        selectedTabIndex = selectedTabIndex,
                        onClick = { index ->
                            selectedTabIndex.intValue = index
                            coroutineScope.launch {
                                categoryToListIndex[index]?.let { listIndex ->
                                    listState.animateScrollToItem(listIndex)
                                }
                            }
                            onRestaurantCategoryClick(
                                restaurantDetails.name,
                                validCategories[index].name.default()
                            )
                        })
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        validCategories.forEachIndexed { _, category ->
                            item {
                                SectionTitle(
                                    category.name.default(),
                                    category.description.default()
                                )
                            }
                            items(category.items.orEmptyArrayList(), key = { it.id }) { item ->
                                FoodItemCard(
                                    item = item,
                                    onAddClick = {
                                        onFoodAddClick(
                                            item,
                                            validCategories[selectedTabIndex.intValue].name.default()
                                        )
                                    },
                                    onCardClick = {
                                        onFoodCardClick(
                                            item,
                                            validCategories[selectedTabIndex.intValue].name.default()
                                        )
                                    },
                                    onDecrementClick = { onDecrementClick(item) },
                                    onIncrementClick = {
                                        onIncrementClick(item)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



