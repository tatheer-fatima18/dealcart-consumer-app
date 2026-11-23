package com.localpk.app.features.search.presentation

import androidx.lifecycle.viewModelScope
import com.localpk.app.base.BaseViewModel
import com.localpk.app.base.ViewState
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.restaurant.data.RestaurantUI
import com.localpk.app.features.restaurant.domain.RestaurantParameters
import com.localpk.app.features.restaurant.domain.RestaurantUseCase
import com.localpk.app.features.search.domain.SearchUseCase
import com.localpk.app.features.search.domain.TrendingSearchUseCase
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.SharedPreferencesManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// State data class for SearchViewModel
data class SearchState(
    val products: List<ProductUiData> = emptyList(),
    val searchHistory: List<String> = emptyList(),
    val trendingSearches: List<String> = emptyList(),
    val searchText: String = EMPTY_STRING,
    val isLoading: Boolean = false,
    val restaurants: List<RestaurantUI> = emptyList()
)

class SearchViewModel(
    private val searchUseCase: SearchUseCase,
    private val trendingSearchUseCase: TrendingSearchUseCase,
    private val restaurantUseCase: RestaurantUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val eventLogger: EventLogger,
    internal val argsData: Boolean,
) : BaseViewModel<SearchState>() {

    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    // Job to track and cancel ongoing search requests
    private var searchProductJob: Job? = null
    private var searchRestaurantJob: Job? = null
    
    // Job to handle debouncing of rapid search requests
    private var debounceJob: Job? = null
    
    // Request ID to track which response belongs to which request
    private var currentRequestId = 0
    
    // Store the current search text to verify response belongs to correct search
    private var currentSearchText: String? = null

    init {
        loadTrendingSearches()
        notifyPastSearch()
    }

    fun notifyPastSearch() {
        val history = if (argsData) sharedPreferencesManager.getRestaurantPastSearch() else sharedPreferencesManager.getProductPastSearch()
        _searchState.update { it.copy(searchHistory = history) }
    }

    fun removePastSearch(text: String) {
        if (argsData)
            sharedPreferencesManager.removeRestaurantPastSearch(text)
        else
            sharedPreferencesManager.removeProductPastSearch(text)
        notifyPastSearch()
    }

    fun onSearchTextChange(text: String) {
        _searchState.update { it.copy(searchText = text) }
    }

    fun onActionClearPastSearch() {
        if (argsData) {
            sharedPreferencesManager.removeAllRestaurantPastSearch()
        } else {
            sharedPreferencesManager.removeAllProductPastSearch()
        }
        notifyPastSearch()
    }

    fun searchProduct(text: String) {
        // Cancel any ongoing debounce job
        debounceJob?.cancel()
        
        // Increment request ID for this new request
        val requestId = ++currentRequestId
        
        // Store the current search text
        currentSearchText = text
        
        // Debounce rapid search requests with 300ms delay
        debounceJob = viewModelScope.launch {
            delay(RemoteConfigs.getSearchDelayTime().toLong()) // Wait 300ms before making the API call
            
            // Check if this is still the current request after delay
            if (requestId != currentRequestId) {
                return@launch // This request is outdated, don't proceed
            }
            
            // Cancel any ongoing search product request
            searchProductJob?.cancel()
            
            _searchState.update { it.copy(isLoading = true) }
            
            searchProductJob = launch {
                try {
                    searchUseCase(text).collect { viewState ->
                        // Only process response if this is still the current request and search text
                        if (requestId == currentRequestId && currentSearchText == text) {
                            when (viewState) {
                                is ViewState.Success -> {
                                    eventLogger.search(text, viewState.data.isNotEmpty())
                                    _searchState.update {
                                        it.copy(
                                            products = viewState.data,
                                            isLoading = false
                                        )
                                    }
                                }

                                is ViewState.Loading -> {

                                    _searchState.update { it.copy(isLoading = true) }
                                }

                                is ViewState.Error -> {
                                    _searchState.update { it.copy(isLoading = false) }
                                }

                                else -> {}
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Handle cancellation or other errors
                    if (requestId == currentRequestId) {
                        _searchState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    private fun loadTrendingSearches() {
        launch {
            trendingSearchUseCase(Unit).collect { viewState ->
                when (viewState) {
                    is ViewState.Success -> {
                        val trending = viewState.data.body
                        _searchState.update { it.copy(trendingSearches = trending) }
                    }

                    else -> {}
                }
            }
        }
    }

    fun getRestaurants(text: String) {
        // Cancel any ongoing debounce job
        debounceJob?.cancel()
        
        // Increment request ID for this new request
        val requestId = ++currentRequestId
        
        // Store the current search text
        currentSearchText = text
        
        // Debounce rapid search requests with 300ms delay
        debounceJob = viewModelScope.launch {
            delay(500) // Wait 500ms before making the API call
            
            // Check if this is still the current request after delay
            if (requestId != currentRequestId) {
                return@launch // This request is outdated, don't proceed
            }
            
            // Cancel any ongoing search restaurant request
            searchRestaurantJob?.cancel()
            
            _searchState.update { it.copy(isLoading = true) }
            
            searchRestaurantJob = launch {
                try {
                    val args = RestaurantParameters(query = text)
                    unZipViewStateFlow(
                        viewStateFlow = restaurantUseCase(args).map { viewState ->
                            when (viewState) {
                                is ViewState.Success -> {
                                    // Only process response if this is still the current request and search text
                                    if (requestId == currentRequestId && currentSearchText == text) {
                                        val response = viewState.data
                                        ViewState.Success(_searchState.value.copy(
                                            restaurants = response.restaurants?.map { it.map() } ?: emptyList(),
                                            isLoading = false
                                        ))
                                    } else {
                                        ViewState.Loading
                                    }
                                }

                                is ViewState.Loading -> ViewState.Loading
                                is ViewState.Error -> {
                                    if (requestId == currentRequestId) {
                                        ViewState.Error(viewState.viewError)
                                    } else {
                                        ViewState.Loading
                                    }
                                }
                                else -> ViewState.Loading
                            }
                        },
                        onSuccess = { state ->
                            if (requestId == currentRequestId) {
                                _searchState.value = state
                            }
                        },
                        onError = {
                            if (requestId == currentRequestId) {
                                _searchState.update { it.copy(isLoading = false) }
                            }
                        }
                    )
                } catch (e: Exception) {
                    // Handle cancellation or other errors
                    if (requestId == currentRequestId) {
                        _searchState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }


    fun clearProducts() {
        _searchState.update { it.copy(products = emptyList()) }
    }

    fun clearRestaurants() {
        _searchState.update { it.copy(restaurants = emptyList()) }
    }

    override fun onCleared() {
        super.onCleared()
        // Cancel any ongoing requests when ViewModel is cleared
        searchProductJob?.cancel()
        searchRestaurantJob?.cancel()
        debounceJob?.cancel()
    }

} 