package com.localpk.app.features.restaurant.viewModels

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.restaurant.data.OrderTracking
import com.localpk.app.features.restaurant.data.RestaurantOrderConfiguration
import com.localpk.app.features.restaurant.data.RestaurantOrderId
import com.localpk.app.features.restaurant.domain.FoodOrderConfigurationDetailsUseCase
import com.localpk.app.features.restaurant.domain.FoodOrderStatusUseCase
import com.localpk.app.utils.getValueOrDefault
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderTrackingViewModel(
    private val foodOrderStatusUseCase: FoodOrderStatusUseCase,
    private val foodOrderConfigurationDetailsUseCase: FoodOrderConfigurationDetailsUseCase
) : BaseViewModel<Any>() {

    private val _trackingStatus = MutableStateFlow<OrderTracking?>(null)
    val trackingStatus: StateFlow<OrderTracking?> = _trackingStatus.asStateFlow()

    private val _restaurantConfigDetails = MutableStateFlow<RestaurantOrderConfiguration?>(null)
    val restaurantConfigDetails: StateFlow<RestaurantOrderConfiguration?> =
        _restaurantConfigDetails.asStateFlow()

    private val _isRefreshed = MutableStateFlow(false)
    val isRefreshed = _isRefreshed

    val _argsData = MutableStateFlow<RestaurantOrderId?>(null)
    val argsData: StateFlow<RestaurantOrderId?> = _argsData.asStateFlow()

    fun getTrackingStatus() {
        if (argsData.value == null)
            return
        unZipViewStateFlow(
            foodOrderStatusUseCase(argsData.value?.orderId.getValueOrDefault()),
            onSuccess = {
                val response = it as OrderTracking
                _trackingStatus.value = response
                _isRefreshed.value = false
            },
            onError = {
                _isRefreshed.value = false
            }
        )

    }

    fun getRestaurantConfigDetails() {
        if (argsData.value == null)
            return
        unZipViewStateFlow(
            foodOrderConfigurationDetailsUseCase(argsData.value?.restaurantId.getValueOrDefault()),
            onSuccess = {
                val response = it as RestaurantOrderConfiguration
                _restaurantConfigDetails.value = response
            },
            onError = {
                // showDcSuccess(it)
            }
        )
    }

    fun refreshData() {
        _isRefreshed.value = true
        getTrackingStatus()
    }

}