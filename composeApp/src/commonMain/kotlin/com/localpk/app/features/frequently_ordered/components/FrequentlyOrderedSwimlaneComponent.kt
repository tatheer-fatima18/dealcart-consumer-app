package com.localpk.app.features.frequently_ordered.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.localpk.app.common_components.SingleProductItem
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.swimlane_engine.data.SwimlaneType

@Composable
fun FrequentlyOrderedSwimlaneComponent(
    viewAllProductList: List<ProductUiData>,
    onCardClick: (ProductUiData) -> Unit,
    onAddClick: (ProductUiData) -> Unit,
    swimlaneType: SwimlaneType? = null,
    onLoadMore: () -> Unit = {}
) {
    val listState = rememberLazyGridState()
    var hasTriggeredLoadMore by remember { mutableStateOf(false) }
    var previousTotalItemsCount = 0


    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val currentTotalItemsCount = layoutInfo.totalItemsCount

                if (lastVisibleItemIndex != null && currentTotalItemsCount > 0) {
                    if (lastVisibleItemIndex >= currentTotalItemsCount - 5 && !hasTriggeredLoadMore) {
                        onLoadMore()
                        hasTriggeredLoadMore = true
                    }
                }

                if (currentTotalItemsCount > previousTotalItemsCount) {
                    hasTriggeredLoadMore = false
                    previousTotalItemsCount = currentTotalItemsCount
                }
            }
    }


    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(viewAllProductList.size) { item ->
            SingleProductItem(
                productList = true,
                data = viewAllProductList[item],
                cardClick = {
                    onCardClick(viewAllProductList[item])
                },
                onAddClick = {
                    onAddClick(viewAllProductList[item])
                },
                swimlaneType = swimlaneType
            )
        }
    }
}