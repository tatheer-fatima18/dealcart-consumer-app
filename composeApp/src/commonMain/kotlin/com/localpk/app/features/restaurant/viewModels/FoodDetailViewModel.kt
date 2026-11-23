package com.localpk.app.features.restaurant.viewModels

import androidx.lifecycle.viewModelScope
import com.localpk.app.base.BaseViewModel
import com.localpk.app.common_components.GlobalFoodDetailStateManager
import com.localpk.app.features.restaurant.data.CategoryItemUIDataAndRestaurantDetails
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNullOrEmpty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class FoodDetailViewModel() : BaseViewModel<Any>() {

    private val _quantity = MutableStateFlow(0)
    val quantity: StateFlow<Int> = _quantity

    // Shared state for variation, choices, and instructions
    private val _selectedVariationId = MutableStateFlow(0L)
    val selectedVariationId: StateFlow<Long> = _selectedVariationId

    private val _argsData: MutableStateFlow<CategoryItemUIDataAndRestaurantDetails?> =
        MutableStateFlow(null)
    val argsData: StateFlow<CategoryItemUIDataAndRestaurantDetails?> = _argsData.asStateFlow()

    private val _selectedChoicesIds = MutableStateFlow<Map<Long, List<Long>>>(emptyMap())
    val selectedChoicesIds: StateFlow<Map<Long, List<Long>>> = _selectedChoicesIds

    private val _specialInstructions = MutableStateFlow("")
    val specialInstructions: StateFlow<String> = _specialInstructions

    private val _hasVariations: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val hasVariations: StateFlow<Boolean?> = _hasVariations.asStateFlow()

    private val _hasChoiceGroups: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val hasChoiceGroups: StateFlow<Boolean?> = _hasChoiceGroups.asStateFlow()


    // Reactive isAddToCartDisabled
    val isAddToCartDisabled: StateFlow<Boolean> = combine(
        selectedVariationId,
        selectedChoicesIds
    ) { selectedVariationId, selectedChoicesIds ->
        if (hasVariations.value?.not() == true && hasChoiceGroups.value?.not() == true) {
            false
        } else {
            val argsData = argsData.value
            val hasSelectedRequiredChoices =
                argsData?.choiceGroups?.filter { it.isRequired == true }?.all { group ->
                    val selected = selectedChoicesIds[group.id]
                    val min = group.minSelectable?.getValueOrDefault() ?: 1
                    !selected.isNullOrEmpty() && selected.size >= min
                } != false
            (hasVariations.value == true && selectedVariationId == 0L) || !hasSelectedRequiredChoices
        }
    }.stateIn(viewModelScope, Eagerly, false)

    fun setSelectedVariationId(id: Long) {
        _selectedVariationId.value = id
        GlobalFoodDetailStateManager.updateSelectedVariationId(id)
     //   GlobalFoodDetailStateManager.updateIsAddToCartDisabled(isAddToCartDisabled.value)
    }

    fun setSelectedChoicesIds(map: Map<Long, List<Long>>) {
        _selectedChoicesIds.value = map
        GlobalFoodDetailStateManager.updateSelectedChoicesIds(map)
    //    GlobalFoodDetailStateManager.updateIsAddToCartDisabled(isAddToCartDisabled.value)
    }

    fun setSpecialInstructions(text: String) {
        _specialInstructions.value = text
        GlobalFoodDetailStateManager.updateSpecialInstructions(text)
    }

    fun updateChoice(groupId: Long, choices: List<Long>) {
        _selectedChoicesIds.update { it.toMutableMap().apply { put(groupId, choices) } }
    }


    fun init(argsData: CategoryItemUIDataAndRestaurantDetails) {
        _argsData.value = argsData
        if (argsData.itemQuantity != ZERO_INT && argsData.hasVariationsOrChoiceGroups()
                .not()
        ) {
            _quantity.value = argsData.itemQuantity
        } else {
            _quantity.value = ZERO_INT
        }
        _hasVariations.value = argsData.variations.isNotNullOrEmpty()
        _hasChoiceGroups.value = argsData.choiceGroups.isNotNullOrEmpty()
    }
}