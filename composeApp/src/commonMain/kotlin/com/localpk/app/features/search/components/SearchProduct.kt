package com.localpk.app.features.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.localpk.app.features.productlist.data.ProductUiData

@Composable
fun SearchProduct(
    searchLiveData: List<String>,
    trendingSearchLiveData: List<String>,
    recommendationList: List<ProductUiData>,
    onAddClick: (ProductUiData) -> Unit,
    pastOrderAddClick: (ProductUiData) -> Unit,
    onIncrement: (ProductUiData) -> Unit,
    onDecrement: (ProductUiData) -> Unit,
    onDelete: (ProductUiData) -> Unit,
    onCardClick: (ProductUiData) -> Unit,
    onClearAll: () -> Unit,
    onClickChange: (String) -> Unit,
    onClickRemove: (String) -> Unit,
    onClickTextChange: (String) -> Unit,
    onSearchAddClick: (ProductUiData) -> Unit,
    onSearchIncrement: (ProductUiData) -> Unit,
    onSearchDecrement: (ProductUiData) -> Unit,
    onSearchDelete: (ProductUiData) -> Unit,
    onSearchCardClick: (ProductUiData) -> Unit,
    onActionViewAll: () -> Unit,
    recommendationTitle: String,
    productsLiveData: List<ProductUiData>,
    searchText: String,
    isLoading: Boolean,
) {
    if (productsLiveData.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                if (productsLiveData.isEmpty() && searchText.isNotEmpty() && isLoading.not()) {
                    SearchProductNotFound()
                }
                if (productsLiveData.isEmpty() && searchLiveData.isNotEmpty() && searchText.isEmpty()) {
                    RecentSearch(
                        searchLiveData = searchLiveData,
                        onClearAll = { onClearAll() },
                        onClickChange = { data ->
                            onClickChange(data)
                        },
                        onClickRemove = { data ->
                            onClickRemove(data)
                        }
                    )
                }
            }
            if (productsLiveData.isEmpty()) {
                TrendingSearch(trendingSearchLiveData, onClick = {
                    onClickTextChange(it)
                })
            }
            if (recommendationList.isNotEmpty()) {
                PastOrders(
                    recommendationList = recommendationList,
                    onActionViewAll = { onActionViewAll() },
                    onCardClick = {
                        onCardClick(it)
                    },
                    onAddClick = {
                        pastOrderAddClick(it)
                    },
                    onDelete = {
                        onDelete(it)
                    },
                    onIncrement = {
                        onIncrement(it)
                    },
                    onDecrement = {
                        onDecrement(it)
                    },
                    recommendationTitle = recommendationTitle
                )
            }
        }
    } else {
        SearchProducts(
            productsLiveData = productsLiveData,
            onCardClick = {
                onSearchCardClick(it)
            },
            onAddClick = {
                onSearchAddClick(it)
            },
            onDelete = {
                onSearchDelete(it)
            },
            onIncrement = {
                onSearchIncrement(it)
            },
            onDecrement = {
                onSearchDecrement(it)
            }
        )
    }
} 