package com.localpk.app.features.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.localpk.app.features.restaurant.components.RestaurantCard
import com.localpk.app.features.restaurant.data.RestaurantDetailArgsData
import com.localpk.app.features.restaurant.data.RestaurantUI

@Composable
fun SearchRestaurants(
    restaurantsLiveData: List<RestaurantUI>,
    onRestaurantCardClick: (RestaurantDetailArgsData) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(restaurantsLiveData) { restaurant ->
            RestaurantCard(restaurant = restaurant) { onRestaurantCardClick(it) }
        }
    }
} 