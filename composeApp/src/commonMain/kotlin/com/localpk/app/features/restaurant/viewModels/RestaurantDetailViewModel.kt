package com.localpk.app.features.restaurant.viewModels

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.restaurant.data.RestaurantDetailArgsData
import com.localpk.app.features.restaurant.data.RestaurantDetailUI
import com.localpk.app.features.restaurant.domain.RestaurantDetailUseCase
import com.localpk.app.utils.getValueOrDefault
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RestaurantDetailViewModel(
    private val restaurantDetailUseCase: RestaurantDetailUseCase
) : BaseViewModel<RestaurantDetailUI>() {

    private val _restaurantsDetails: MutableStateFlow<RestaurantDetailUI?> = MutableStateFlow(null)
    val restaurantsDetails: StateFlow<RestaurantDetailUI?> = _restaurantsDetails.asStateFlow()

    private val _argsData: MutableStateFlow<RestaurantDetailArgsData?> = MutableStateFlow(null)
    val argsData: StateFlow<RestaurantDetailArgsData?> = _argsData.asStateFlow()


    private fun getRestaurantDetails() {
        if(argsData.value == null) return
        unZipViewStateFlow(
            restaurantDetailUseCase(argsData.value?.id.getValueOrDefault()),
            onSuccess = { response ->
                _restaurantsDetails.value = response
            },
            onError = {
                // showDcSuccess(it)
            }
        )
    }

    fun setArgsData(args: RestaurantDetailArgsData) {
        _argsData.value = args
        getRestaurantDetails()
    }
}