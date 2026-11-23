package com.localpk.app.features.restaurant.api

import com.localpk.app.base.data.BaseApiService
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
import com.localpk.app.utils.SharedPreferencesManager
import io.ktor.client.HttpClient

interface FoodApiService {
    suspend fun restaurantOrderInProgress(): List<RestaurantOrderId>
    suspend fun getRestaurants(
        latitude: Double,
        longitude: Double,
        query: String,
        cuisine: String
    ): GetRestaurantsResponse

    suspend fun foodOrderConfigurationDetails(id: Long): RestaurantOrderConfiguration
    suspend fun getTrackingStatus(orderId: Long): OrderTracking
    suspend fun getRestaurantById(restaurantId: Long): RestaurantDetail
    suspend fun addFoodItem(body: AddItemRequestBody) : AddFoodToCartResponse
    suspend fun getFoodCart(): FoodCart
    suspend fun updateFoodItemQuantity(body: UpdateQuantityBody)
    suspend fun placeFoodOrder(body: FoodCheckoutRequest): FoodCheckoutResponse
    suspend fun isFoodCart(): Boolean
    suspend fun getFoodOrders(
        page: Int,
        limit: Int
    ): FoodOrdersList

    suspend fun getFoodOrderDetail(
        orderId: Long
    ): FoodOrderDetailData

    suspend fun applyFoodPromo(body: FoodPromoRequest)
    suspend fun clearFoodCart()
}

class FoodApiServiceImpl(
    httpClient: HttpClient,
    baseUrl: String,
    sharedPreferencesManager: SharedPreferencesManager
) : BaseApiService(httpClient, baseUrl, sharedPreferencesManager), FoodApiService {

    override suspend fun restaurantOrderInProgress(): List<RestaurantOrderId> {
        return get("/orders/customer/in-progress")
    }

    override suspend fun getRestaurants(
        latitude: Double,
        longitude: Double,
        query: String,
        cuisine: String
    ): GetRestaurantsResponse {
        return get(
            endpoint = "/restaurants/search",
            parameters = mapOf(
                "latitude" to latitude,
                "longitude" to longitude,
                "cuisines" to cuisine,
                "query" to query
            )
        )
    }


    override suspend fun foodOrderConfigurationDetails(id: Long): RestaurantOrderConfiguration {
        return get("/restaurants/$id/info")
    }

    override suspend fun getTrackingStatus(orderId: Long): OrderTracking {
        return get("/orders/$orderId/tracking")
    }

    override suspend fun getRestaurantById(restaurantId: Long): RestaurantDetail {
        return get(endpoint = "/restaurants/$restaurantId")
    }

    override suspend fun addFoodItem(body: AddItemRequestBody): AddFoodToCartResponse {
        return post("/carts/add-item", requestBody = body)
    }

    override suspend fun getFoodCart(): FoodCart {
        return get(endpoint = "/carts/customer")
    }

    override suspend fun updateFoodItemQuantity(body: UpdateQuantityBody) {
        return patch("/carts/items", requestBody = body)
    }

    override suspend fun placeFoodOrder(body: FoodCheckoutRequest): FoodCheckoutResponse {
        return post("/orders", requestBody = body)
    }

    override suspend fun isFoodCart(): Boolean {
        return get("/carts/has-items")
    }

    override suspend fun getFoodOrders(page: Int, limit: Int): FoodOrdersList {
        return get(
            "/orders/customer", parameters = mapOf(
                "limit" to limit,
                "page" to page
            )
        )
    }

    override suspend fun getFoodOrderDetail(orderId: Long): FoodOrderDetailData {
        return get("/orders/$orderId/customer")
    }

    override suspend fun applyFoodPromo(body: FoodPromoRequest) {
        return post(endpoint = "/carts/apply-discount", requestBody = body)
    }

    override suspend fun clearFoodCart() {
        return delete(endpoint = "/carts")
    }
}