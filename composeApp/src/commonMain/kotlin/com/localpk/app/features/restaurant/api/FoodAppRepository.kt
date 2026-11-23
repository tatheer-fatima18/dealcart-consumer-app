package com.localpk.app.features.restaurant.api

import com.localpk.app.features.restaurant.data.AddFoodToCartResponse
import com.localpk.app.features.restaurant.data.AddItemRequestBody
import com.localpk.app.features.restaurant.data.FoodCart
import com.localpk.app.features.restaurant.data.FoodCheckoutRequest
import com.localpk.app.features.restaurant.data.FoodCheckoutResponse
import com.localpk.app.features.restaurant.data.FoodOrderDetailData
import com.localpk.app.features.restaurant.data.FoodOrdersList
import com.localpk.app.features.restaurant.data.FoodPromoRequest
import com.localpk.app.features.restaurant.data.GetRestaurantsResponse
import com.localpk.app.features.restaurant.data.OrderTracking
import com.localpk.app.features.restaurant.data.RestaurantDetail
import com.localpk.app.features.restaurant.data.RestaurantOrderConfiguration
import com.localpk.app.features.restaurant.data.RestaurantOrderId
import com.localpk.app.features.restaurant.data.UpdateQuantityBody
import com.localpk.app.network.ResponseResult
import com.localpk.app.network.callApi
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager

interface FoodAppRepository {

    suspend fun restaurantOrderInProgress(): ResponseResult<List<RestaurantOrderId>>
    suspend fun getRestaurants(
        query: String,
        cuisines: List<String>?
    ): ResponseResult<GetRestaurantsResponse>

    suspend fun foodOrderConfigurationDetails(id: Long): ResponseResult<RestaurantOrderConfiguration>
    suspend fun getTrackingStatus(orderId: Long): ResponseResult<OrderTracking>
    suspend fun getRestaurantById(restaurantId: Long): ResponseResult<RestaurantDetail>
    suspend fun addFoodItem(body: AddItemRequestBody): ResponseResult<AddFoodToCartResponse>
    suspend fun getFoodCart(): ResponseResult<FoodCart>
    suspend fun updateFoodItemQuantity(body: UpdateQuantityBody): ResponseResult<Unit>
    suspend fun placeFoodOrder(body: FoodCheckoutRequest): ResponseResult<FoodCheckoutResponse>
    suspend fun clearCart(): ResponseResult<Unit>
    suspend fun isFoodCart(): ResponseResult<Boolean>
    suspend fun getFoodOrders(
        page: Int,
        limit: Int
    ): ResponseResult<FoodOrdersList>

    suspend fun getFoodOrderDetail(
        orderId: Long
    ): ResponseResult<FoodOrderDetailData>

    suspend fun applyFoodPromo(body: FoodPromoRequest): ResponseResult<Unit>
}

class FoodAppRepositoryImpl(
    private val service: FoodApiService,
    private val sharedPreferencesManager: SharedPreferencesManager
) : FoodAppRepository {
    override suspend fun updateFoodItemQuantity(body: UpdateQuantityBody): ResponseResult<Unit> {
        return callApi {
            service.updateFoodItemQuantity(body = body)
        }
    }

    override suspend fun getFoodOrderDetail(orderId: Long): ResponseResult<FoodOrderDetailData> {
        return callApi {
            service.getFoodOrderDetail(orderId = orderId)
        }
    }

    override suspend fun getFoodCart(): ResponseResult<FoodCart> {
        return callApi {
            service.getFoodCart()
        }
    }

    override suspend fun addFoodItem(body: AddItemRequestBody): ResponseResult<AddFoodToCartResponse> {
        return callApi {
            service.addFoodItem(
                body = body
            )
        }
    }

    override suspend fun foodOrderConfigurationDetails(id: Long): ResponseResult<RestaurantOrderConfiguration> {
        return callApi {
            service.foodOrderConfigurationDetails(id = id)
        }
    }

    override suspend fun getRestaurantById(restaurantId: Long): ResponseResult<RestaurantDetail> {
        return callApi {
            service.getRestaurantById(restaurantId = restaurantId)
        }
    }

    override suspend fun getTrackingStatus(orderId: Long): ResponseResult<OrderTracking> {
        return callApi {
            service.getTrackingStatus(
                orderId = orderId
            )
        }
    }

    override suspend fun applyFoodPromo(body: FoodPromoRequest): ResponseResult<Unit> {
        return callApi {
            service.applyFoodPromo(
                body = body
            )
        }
    }

    override suspend fun placeFoodOrder(body: FoodCheckoutRequest): ResponseResult<FoodCheckoutResponse> {
        return callApi {
            service.placeFoodOrder(
                body = body
            )
        }
    }

    override suspend fun restaurantOrderInProgress(): ResponseResult<List<RestaurantOrderId>> {
        return callApi {
            service.restaurantOrderInProgress()
        }
    }

    override suspend fun isFoodCart(): ResponseResult<Boolean> {
        return callApi {
            service.isFoodCart()
        }
    }

    override suspend fun clearCart(): ResponseResult<Unit> {
        return callApi {
            service.clearFoodCart()
        }
    }

    override suspend fun getFoodOrders(page: Int, limit: Int): ResponseResult<FoodOrdersList> {
        return callApi {
            service.getFoodOrders(
                page = page,
                limit = limit
            )
        }
    }

    override suspend fun getRestaurants(
        query: String,
        cuisines: List<String>?,
    ): ResponseResult<GetRestaurantsResponse> {
        val cuisineParam = if (cuisines.isNullOrEmpty()) {
            EMPTY_STRING
        } else {
            cuisines.joinToString(",")
        }
        return callApi {
            service.getRestaurants(
                latitude = sharedPreferencesManager.getLatitude().toDouble(),
                longitude = sharedPreferencesManager.getLongitude().toDouble(),
                cuisine = cuisineParam,
                query = query
            )
        }
    }

}