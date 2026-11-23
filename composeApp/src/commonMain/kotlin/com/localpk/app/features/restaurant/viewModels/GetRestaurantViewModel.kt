package com.localpk.app.features.restaurant.viewModels

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.restaurant.data.CuisineUI
import com.localpk.app.features.restaurant.data.GetRestaurantsResponse
import com.localpk.app.features.restaurant.data.RestaurantOrderId
import com.localpk.app.features.restaurant.data.RestaurantUI
import com.localpk.app.features.restaurant.domain.RestaurantOrderIdUseCase
import com.localpk.app.features.restaurant.domain.RestaurantParameters
import com.localpk.app.features.restaurant.domain.RestaurantUseCase
import com.localpk.app.utils.orEmpty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GetRestaurantViewModel(
    private val restaurantOrderIdUseCase: RestaurantOrderIdUseCase,
    private val restaurantUseCase: RestaurantUseCase
) : BaseViewModel<Any>() {

    private val _cuisines = MutableStateFlow<List<CuisineUI>>(emptyList())
    val cuisines: StateFlow<List<CuisineUI>> = _cuisines.asStateFlow()

    private val _restaurantOrders = MutableStateFlow<List<RestaurantOrderId>>(emptyList())
    val restaurantOrders: StateFlow<List<RestaurantOrderId>> = _restaurantOrders.asStateFlow()

    private val _restaurants = MutableStateFlow<List<RestaurantUI>>(emptyList())
    val restaurants: StateFlow<List<RestaurantUI>> = _restaurants.asStateFlow()

    fun getRestaurants(args: RestaurantParameters, successCallback: () -> Unit = {}) {
        unZipViewStateFlow(
            restaurantUseCase(args),
            onSuccess = {
                val response = it as GetRestaurantsResponse
                _cuisines.value = response.cuisines?.map { cuisine -> cuisine.map() }.orEmpty()
                _restaurants.value =
                    response.restaurants?.map { restaurant -> restaurant.map() }.orEmpty()
                successCallback()
            },
            onError = {
//                showFavouritesSuccess(R.drawable.ic_dc_sad_orange, it)
            }
        )
    }

    fun getRestaurantOrderIds() {
        unZipViewStateFlow(
            restaurantOrderIdUseCase(Unit),
            onSuccess = {
                val restaurantOrders = it as List<RestaurantOrderId>
                _restaurantOrders.value = restaurantOrders
            },
            onError = {
               // showFavouritesSuccess(R.drawable.ic_dc_sad_orange, it)
            }
        )
    }


//    fun onActionOrder() {
//        navigate(
//            navigationProvider.getPushNavigator(
//                HomeFragmentDirections.actionNavigationHomeToOrderHostFragment(
//                    1
//                )
//            )
//        )
//    }

//    fun onActionOrderTracking() {
//        restaurantOrdersLiveData.value?.firstOrNull()?.let { order ->
//            navigate(
//                navigationProvider.getPushNavigator(
//                    HomeFragmentDirections.actionHomeToTracking(
//                        order
//                    )
//                )
//            )
//        }
//    }
}