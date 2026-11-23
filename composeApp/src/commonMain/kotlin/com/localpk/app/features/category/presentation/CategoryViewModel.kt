package com.localpk.app.features.category.presentation

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.category.data.CategoryType
import com.localpk.app.features.category.data.CategoryUiModel
import com.localpk.app.features.category.domain.CategoryUseCase
import com.localpk.app.utils.isNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class CategoryViewModel(
    private val categoryUseCase: CategoryUseCase
) : BaseViewModel<List<CategoryUiModel>>() {

    private val _categories = MutableStateFlow<List<CategoryUiModel>>(emptyList())
    val categories: StateFlow<List<CategoryUiModel>> = _categories.asStateFlow()

    private val _superParentCategories = MutableStateFlow<List<CategoryUiModel>>(emptyList())
    val superParentCategories: StateFlow<List<CategoryUiModel>> = _superParentCategories.asStateFlow()
    init {
    //    loadCategories()
    }

     fun loadCategories(successCallback: () -> Unit = {}) {
        unZipViewStateFlow(
            viewStateFlow = categoryUseCase(Unit),
            onSuccess = { categories ->
                _categories.value = categories
                _superParentCategories.value =
                categories.filter { it.parentId.isNull() && it.categoryType == CategoryType.SUPER_PARENT.value }
                    successCallback()

            },
            onError = { viewError ->
                // Handle error if needed

            }
        )
    }
} 