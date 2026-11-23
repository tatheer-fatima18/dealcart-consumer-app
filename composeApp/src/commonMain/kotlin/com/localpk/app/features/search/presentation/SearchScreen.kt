package com.localpk.app.features.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.search.components.SearchProduct
import com.localpk.app.features.search.components.SearchRestaurant
import com.localpk.app.features.search.components.TopNavbar
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class SearchScreen(private val args: Boolean) : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<SearchViewModel>(
            key = "SearchViewModel_$args"
        ) {
            parametersOf(args)
        }
        val sharedPreferencesManager: SharedPreferencesManager = koinInject()
        val navigator = LocalNavigator.currentOrThrow
        val eventLogger: EventLogger = koinInject()

        val searchState by viewModel.searchState.collectAsState()

        // Clear search input and results on first composition
        LaunchedEffect(Unit) {
            viewModel.onSearchTextChange(EMPTY_STRING)
            viewModel.clearProducts()
            viewModel.clearRestaurants()
        }
        StatusBarIconColor(backgroundColor = Color.White)
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White).statusBarsPadding()
                ) {
                    TopNavbar(
                        isLoading = searchState.isLoading,
                        productsLiveData = searchState.products,
                        searchText = searchState.searchText,
                        onBackClick = { navigator.pop() },
                        onSearchChanged = {
                            viewModel.onSearchTextChange(it)
                            when {
                                it.length > 2 -> {
                                    if (viewModel.argsData)
                                        viewModel.getRestaurants(it)
                                    else
                                        viewModel.searchProduct(it)
                                }

                                else -> {
                                    viewModel.clearProducts()
                                    viewModel.clearRestaurants()
                                }
                            }
                        },
                        onSearch = {
                            if (viewModel.argsData) {
                                viewModel.getRestaurants(it)
                                sharedPreferencesManager.setRestaurantPastSearch(it)
                                viewModel.notifyPastSearch()
                            } else {
                                viewModel.searchProduct(it)
                                sharedPreferencesManager.setProductPastSearch(it)
                                viewModel.notifyPastSearch()
                            }
                        }
                    )
                    if (searchState.isLoading) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )
                    }
                    if (viewModel.argsData) {
                        SearchRestaurant(
                            restaurantsLiveData = searchState.restaurants,
                            searchText = searchState.searchText,
                            searchLiveData = searchState.searchHistory,
                            isLoading = searchState.isLoading,
                            trendingSearchLiveData = searchState.trendingSearches,
                            onClickChange = { data ->
                                viewModel.onSearchTextChange(data)
                                viewModel.getRestaurants(data)
                            },
                            onClickRemove = { data ->
                                viewModel.removePastSearch(data)
                            },
                            onClickTextChange = { data ->
                                viewModel.onSearchTextChange(data)
                                viewModel.searchProduct(data)
                            },
                            onClearAll = { viewModel.onActionClearPastSearch() },
                            onRestaurantCardClick = {
                                eventLogger.clickRestaurant(
                                    restaurantName = it.name,
                                    source = "search"
                                )
                            }

                        )
                    } else {
                        SearchProduct(
                            isLoading = searchState.isLoading,
                            onActionViewAll = { /* Implement if needed */ },
                            onCardClick = { /* TODO: Implement navigation to product detail */ },
                            onAddClick = { product ->
                                if (product.hasVariants.not()) {
                                    eventLogger.addToCartMergn(
                                        product.id,
                                        product.name,
                                        1,
                                        product.price,
                                        "recommendation_search",
                                        type = "quick_buy"
                                    )
                                }

                            },
                            pastOrderAddClick = { product ->
                                if (product.hasVariants.not()) {
                                    eventLogger.addToCartMergn(
                                        product.id,
                                        product.name,
                                        1,
                                        product.price,
                                        "past_order",
                                        type = "quick_buy"
                                    )
                                }
                            },
                            onDelete = { /* Implement delete logic if needed */ },
                            onDecrement = { /* Implement decrement logic if needed */ },
                            onIncrement = { /* Implement increment logic if needed */ },
                            onClearAll = { viewModel.onActionClearPastSearch() },
                            onClickChange = { data ->
                                viewModel.onSearchTextChange(data)
                                viewModel.searchProduct(data)
                            },
                            onClickRemove = { data ->
                                viewModel.removePastSearch(data)
                            },
                            onClickTextChange = { data ->
                                viewModel.onSearchTextChange(data)
                                viewModel.searchProduct(data)
                            },
                            onSearchCardClick = {
                                // Implement navigation to product detail
                                sharedPreferencesManager.setProductPastSearch(it.name)
                                viewModel.notifyPastSearch()
                            },
                            onSearchAddClick = {
                                if (it.hasVariants.not()) {
                                    eventLogger.addToCartMergn(
                                        it.id,
                                        it.name,
                                        1,
                                        it.price,
                                        "search_result",
                                        type = "quick_buy"
                                    )
                                }
                            },
                            onSearchDelete = { /* Implement search delete logic if needed */ },
                            onSearchIncrement = { /* Implement search increment logic if needed */ },
                            onSearchDecrement = { /* Implement search decrement logic if needed */ },
                            productsLiveData = searchState.products,
                            searchText = searchState.searchText,
                            searchLiveData = searchState.searchHistory,
                            recommendationList = emptyList(), //  Inject and use recommendation list if needed
                            recommendationTitle = "", // Inject and use recommendation title if needed
                            trendingSearchLiveData = searchState.trendingSearches
                        )
                    }
                }

            }
        }
    }

} 