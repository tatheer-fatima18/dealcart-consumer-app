package com.localpk.app.features.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.localpk.app.features.restaurant.data.RestaurantDetailArgsData
import com.localpk.app.features.restaurant.data.RestaurantUI

@Composable
fun SearchRestaurant(
    restaurantsLiveData: List<RestaurantUI>,
    searchText: String,
    searchLiveData: List<String>,
    isLoading: Boolean,
    trendingSearchLiveData: List<String>,
    onClearAll: () -> Unit,
    onClickChange: (String) -> Unit,
    onClickRemove: (String) -> Unit,
    onClickTextChange: (String) -> Unit,
    onRestaurantCardClick: (RestaurantDetailArgsData) -> Unit
) {
    if (restaurantsLiveData.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                if (restaurantsLiveData.isEmpty() && searchText.isNotEmpty() && isLoading.not()) {
                    SearchProductNotFound()
                }
                if (restaurantsLiveData.isEmpty() && searchLiveData.isNotEmpty() && searchText.isEmpty()) {
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
            if (restaurantsLiveData.isEmpty()) {
                TrendingSearch(trendingSearchLiveData, onClick = {
                    onClickTextChange(it)
                })
            }
        }
    } else {
        SearchRestaurants(
            restaurantsLiveData = restaurantsLiveData,
            onRestaurantCardClick = { onRestaurantCardClick(it) })
    }
} 