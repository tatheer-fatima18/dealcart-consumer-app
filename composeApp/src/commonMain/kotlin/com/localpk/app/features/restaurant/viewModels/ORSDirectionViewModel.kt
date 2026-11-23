package com.localpk.app.features.restaurant.viewModels

import androidx.lifecycle.viewModelScope
import com.localpk.app.api.RouteRepository
import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.address.data.AddAddressUiData
import com.localpk.app.features.address.data.AddressUiData
import com.localpk.app.features.address.data.AreaData
import com.localpk.app.features.address.data.UserAddress
import com.localpk.app.features.user_location.data.Geolocation
import com.localpk.app.utils.BIT_DELAY
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ORSDirectionViewModel(
    private val routeRepository: RouteRepository
) : BaseViewModel<Any>() {

    private val _routePath = MutableStateFlow<List<Geolocation>>(emptyList())
    val routePath: StateFlow<List<Geolocation>> = _routePath.asStateFlow()

    fun fetchRoute(start: Geolocation, end: Geolocation, apiKey: String) {
        viewModelScope.launch {
            val path = routeRepository.getRoutePath(start, end, apiKey)
            _routePath.value = path
        }
    }
}