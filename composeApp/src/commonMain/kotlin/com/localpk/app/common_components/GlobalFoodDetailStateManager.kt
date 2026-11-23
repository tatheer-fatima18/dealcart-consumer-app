package com.localpk.app.common_components

import com.localpk.app.features.restaurant.data.CategoryItemUIDataAndRestaurantDetails
import com.localpk.app.utils.getValueOrDefault
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object GlobalFoodDetailStateManager {

    // Initial values
    private const val INITIAL_QUANTITY = 0
    private const val INITIAL_SELECTED_VARIATION_ID = 0L
    private val INITIAL_SELECTED_CHOICES_IDS = emptyMap<Long, List<Long>>()
    private const val INITIAL_SPECIAL_INSTRUCTIONS = ""
    private const val INITIAL_IS_ADD_TO_CART_DISABLED = true
    private val INITIAL_ARGS_DATA: CategoryItemUIDataAndRestaurantDetails? = null

    // State flows
    private val _quantity = MutableStateFlow(INITIAL_QUANTITY)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    private val _selectedVariationId = MutableStateFlow(INITIAL_SELECTED_VARIATION_ID)
    val selectedVariationId: StateFlow<Long> = _selectedVariationId.asStateFlow()

    private val _selectedChoicesIds = MutableStateFlow(INITIAL_SELECTED_CHOICES_IDS)
    val selectedChoicesIds: StateFlow<Map<Long, List<Long>>> = _selectedChoicesIds.asStateFlow()

    private val _specialInstructions = MutableStateFlow(INITIAL_SPECIAL_INSTRUCTIONS)
    val specialInstructions: StateFlow<String> = _specialInstructions.asStateFlow()

    private val _isAddToCartDisabled = MutableStateFlow(INITIAL_IS_ADD_TO_CART_DISABLED)
    val isAddToCartDisabled: StateFlow<Boolean> = _isAddToCartDisabled.asStateFlow()

    private val _argsData = MutableStateFlow(INITIAL_ARGS_DATA)
    val argsData: StateFlow<CategoryItemUIDataAndRestaurantDetails?> = _argsData.asStateFlow()

    // Update methods
    fun updateQuantity(quantity: Int) {
        _quantity.value = quantity
    }

    fun updateSelectedVariationId(variationId: Long) {
        _selectedVariationId.value = variationId
        calculateAndSetIsAddToCartDisabled()
    }

    fun updateSelectedChoicesIds(choicesIds: Map<Long, List<Long>>) {
        _selectedChoicesIds.value = choicesIds
        calculateAndSetIsAddToCartDisabled()
    }

    fun updateSpecialInstructions(instructions: String) {
        _specialInstructions.value = instructions
    }

    fun updateIsAddToCartDisabled(isDisabled: Boolean) {
        _isAddToCartDisabled.value = isDisabled
    }

    // Calculate and set isAddToCartDisabled based on current state
    fun calculateAndSetIsAddToCartDisabled() {
        val argsData = _argsData.value
        val selectedVariationId = _selectedVariationId.value
        val selectedChoicesIds = _selectedChoicesIds.value
        
        val hasVariations = argsData?.variations?.isNotEmpty() == true
        val hasChoiceGroups = argsData?.choiceGroups?.isNotEmpty() == true
        
        val isDisabled = if (!hasVariations && !hasChoiceGroups) {
            false
        } else {
            val hasSelectedRequiredChoices = argsData?.choiceGroups?.filter { it.isRequired == true }?.all { group ->
                val selected = selectedChoicesIds[group.id]
                val min = group.minSelectable?.getValueOrDefault() ?: 1
                !selected.isNullOrEmpty() && selected.size >= min
            } != false
            
            (hasVariations && selectedVariationId == 0L) || !hasSelectedRequiredChoices
        }
        
        _isAddToCartDisabled.value = isDisabled
    }


    // Initialize with args data
    fun initializeWithArgs(argsData: CategoryItemUIDataAndRestaurantDetails) {
        _argsData.value = argsData
        _quantity.value = if (argsData.itemQuantity != 0 && !argsData.hasVariationsOrChoiceGroups()) {
            argsData.itemQuantity
        } else {
            INITIAL_QUANTITY
        }
        calculateAndSetIsAddToCartDisabled()
    }

    // Restore all values to initial state
    fun restoreInitialValues() {
        _quantity.value = INITIAL_QUANTITY
        _selectedVariationId.value = INITIAL_SELECTED_VARIATION_ID
        _selectedChoicesIds.value = INITIAL_SELECTED_CHOICES_IDS
        _specialInstructions.value = INITIAL_SPECIAL_INSTRUCTIONS
        _isAddToCartDisabled.value = INITIAL_IS_ADD_TO_CART_DISABLED
        _argsData.value = INITIAL_ARGS_DATA
    }

    // Clear all state (alias for restoreInitialValues)
    fun clearState() {
        restoreInitialValues()
    }
}
