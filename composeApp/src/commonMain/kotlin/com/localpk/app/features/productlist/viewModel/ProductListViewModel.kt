package com.localpk.app.features.productlist.viewModel

import androidx.lifecycle.viewModelScope
import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.category.data.CategoryType
import com.localpk.app.features.category.data.CategoryUiModel
import com.localpk.app.features.productlist.data.ProductFragmentArgs
import com.localpk.app.features.productlist.data.ProductHeaderAdapterUiData
import com.localpk.app.features.productlist.domain.ProductListWithSubCategoryUseCase
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.default
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val productListWithSubCategoryUseCase: ProductListWithSubCategoryUseCase,
    private val eventLogger: EventLogger
) : BaseViewModel<Any>() {

    val argCategoryId = MutableStateFlow<Long?>(null)

    val _selectedCategoryIdToLoadProducts = MutableStateFlow<Long?>(null)
    val selectedCategoryIdToLoadProducts: StateFlow<Long?> = _selectedCategoryIdToLoadProducts

    private val _categoryLiveData = MutableStateFlow<List<CategoryUiModel>>(emptyList())
    val categoryLiveData: StateFlow<List<CategoryUiModel>> = _categoryLiveData


    private val _selectedChildCategoryLiveData = MutableStateFlow<CategoryUiModel?>(null)
    val selectedChildCategoryLiveData: StateFlow<CategoryUiModel?> = _selectedChildCategoryLiveData


    val actionScrollToProductCategory = MutableStateFlow<Int?>(null)

    var categoriesListResponse: List<CategoryUiModel>? = null

    private val _productList = MutableStateFlow<List<ProductHeaderAdapterUiData>>(emptyList())
    val productList: StateFlow<List<ProductHeaderAdapterUiData>> = _productList

    // Login state
    val isLoggedIn = MutableStateFlow(sharedPreferencesManager.isLoggedIn())

    private val _isLoadingData = MutableStateFlow(false)
    val isLoadingData: StateFlow<Boolean> = _isLoadingData

    // Job to track and cancel ongoing product fetch requests
    private var currentProductJob: Job? = null
    
    // Job to handle debouncing of rapid tab switches
    private var debounceJob: Job? = null
    
    // Request ID to track which response belongs to which request
    private var currentRequestId = 0
    
    // Store the current category ID to verify response belongs to correct category
    private var currentCategoryId: Long? = null
    
    // Cache to store products for each parent category
    data class CategoryCacheData(
        val products: List<ProductHeaderAdapterUiData>,
        val scrollPosition: Int = 0
    )

    private val categoryProductCache = mutableMapOf<Long, CategoryCacheData>()

     fun loadCategories(categories: List<CategoryUiModel>) {
         categoriesListResponse =
             categories.filter { it.categoryType != CategoryType.SUPER_PARENT.value }
         parseCategoryAndSubcategoryFromId(categories)
    }

    private fun parseCategoryAndSubcategoryFromId(categories: List<CategoryUiModel>) {

        var scrollPosition = -1

        val parentCategories =
            categories.filter { it.categoryType == CategoryType.PARENT.value && it.id.toInt() != 2414 }


        categories.let { dataList ->


            val foundCategory = dataList.find { it.id == _selectedCategoryIdToLoadProducts.value }

            foundCategory?.let {


                if (foundCategory.categoryType == CategoryType.PARENT.value) {

                    _selectedChildCategoryLiveData.value =
                        dataList.firstOrNull { it.parentId == foundCategory.id }


                    _selectedChildCategoryLiveData.value?.let {
                        _selectedCategoryIdToLoadProducts.value = it.id
                    }

                    parentCategories.forEachIndexed { index, categoryUiModel ->
                        if (categoryUiModel.id == argCategoryId.value) {
                            scrollPosition = index
                        }
                    }
                } else {
                    // The category is child
                    _selectedCategoryIdToLoadProducts.value = foundCategory.id

                    val parentCategory = dataList.find { it.id == foundCategory.parentId }
                    _selectedChildCategoryLiveData.value = foundCategory

                    parentCategories.forEachIndexed { index, categoryUiModel ->
                        if (categoryUiModel.id == parentCategory?.id) {
                            scrollPosition = index
                        }
                    }

                }
                _categoryLiveData.value = parentCategories

                viewModelScope.launch {
                    actionScrollToProductCategory.emit(scrollPosition)
                }

                eventLogger.viewCategory(
                    categoryId = foundCategory.id.toString(),
                    categoryName = foundCategory.name.default()
                )
            }
        }

    }

    fun fetchProducts(data: ProductFragmentArgs) {
        val categoryId = data.parentId
        
        // Check cache first
        if (categoryProductCache.containsKey(categoryId)) {
            val cachedData = categoryProductCache[categoryId]!!
            _productList.value = cachedData.products
            _isLoadingData.value = false
            return
        }
        
        // Cancel any ongoing debounce job
        debounceJob?.cancel()
        
        // Increment request ID for this new request
        val requestId = ++currentRequestId
        
        // Store the current category ID
        currentCategoryId = data.parentId
        
        // Create a unique request identifier
        val requestIdentifier = "$requestId-$categoryId"
        
        // Clear current product list immediately to prevent stale data
        _productList.value = emptyList()
        _isLoadingData.value = true
        
        // Debounce rapid tab switches with 300ms delay
        debounceJob = viewModelScope.launch {
            delay(300) // Wait 300ms before making the API call
            
            // Check if this is still the current request after delay
            if (requestId != currentRequestId || currentCategoryId != categoryId) {
                return@launch // This request is outdated, don't proceed
            }
            
            // Cancel any ongoing product fetch request
            currentProductJob?.cancel()
            
            isLoggedIn.value = sharedPreferencesManager.isLoggedIn()
            
            currentProductJob = viewModelScope.launch {
                try {
                    unZipViewStateFlow(
                        productListWithSubCategoryUseCase(data),
                        onSuccess = {
                            // Only process response if this is still the current request and category
                            // AND the current category ID matches the request's category ID
                            if (requestId == currentRequestId && currentCategoryId == categoryId) {
                                val response = it as List<ProductHeaderAdapterUiData>
                                _productList.value = response
                                categoryProductCache[categoryId] = CategoryCacheData(products = response)
                                _isLoadingData.value = false
                            }
                        }
                    )
                } catch (e: Exception) {
                    // Handle cancellation or other errors
                    if (requestId == currentRequestId && currentCategoryId == categoryId) {
                        _isLoadingData.value = false
                    }
                }
            }
        }
    }

    fun updateQuantitySelectedItem(position: Int, quantity: Int) {
        val currentList = _productList.value?.toMutableList() ?: return
        val itemToUpdate = currentList.getOrNull(position)
        itemToUpdate?.childData?.itemQuantity = quantity
        _productList.value = currentList
    }


    override fun onCleared() {
        super.onCleared()
        // Cancel any ongoing requests when ViewModel is cleared
        currentProductJob?.cancel()
        debounceJob?.cancel()
    }
    
    fun clearCache() {
        _productList.value = emptyList()
        categoryProductCache.clear()
    }

    fun updateSelectedCategoryIdToLoadProducts(selectedCategoryId: Long) {
        _selectedCategoryIdToLoadProducts.value = selectedCategoryId
    }

    fun updateScrollPositionForCurrentCategory(categoryId: Long, scrollPosition: Int) {
        categoryProductCache[categoryId]?.let { cachedData ->
            categoryProductCache[categoryId] = cachedData.copy(
                scrollPosition = scrollPosition
            )
        }
    }

    fun getCachedDataForCategory(categoryId: Long): CategoryCacheData? {
        val cachedData = categoryProductCache[categoryId]
        return cachedData
    }

}