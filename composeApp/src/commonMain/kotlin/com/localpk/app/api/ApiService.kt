package com.localpk.app.api

import com.localpk.app.base.data.BaseApiService
import com.localpk.app.features.address.data.AddAddressRequest
import com.localpk.app.features.address.data.CustomerAddressesResponse
import com.localpk.app.features.address.data.MapPolygonResponse
import com.localpk.app.features.banner.data.CarousalBanner
import com.localpk.app.features.cart.data.AddToCartRequest
import com.localpk.app.features.cart.data.AddToCartRawResponse
import com.localpk.app.features.cart.data.BundleQuantity
import com.localpk.app.features.cart.data.CardPaymentDetails
import com.localpk.app.features.cart.data.CartBundleResponse
import com.localpk.app.features.cart.data.CartResponse
import com.localpk.app.features.cart.data.CheckoutRequest
import com.localpk.app.features.cart.data.CheckoutResponse
import com.localpk.app.features.cart.data.OneLastStepAdResponse
import com.localpk.app.features.cart.data.OrderStatusResponse
import com.localpk.app.features.cart.data.PromoCodeRequest
import com.localpk.app.features.category.data.CategoryResponse
import com.localpk.app.features.cricket_live.data.PoweredByResponse
import com.localpk.app.features.flash_deals.data.FlashDealDetailResponse
import com.localpk.app.features.flash_deals.data.FlashDealsResponse
import com.localpk.app.features.frequently_ordered.data.FrequentlyOrderedResponse
import com.localpk.app.features.login.data.CheckIfCustomerExistsResponse
import com.localpk.app.features.order_review.data.AddOrderReviewRequest
import com.localpk.app.features.order_review.data.OrderReviewCategoriesResponse
import com.localpk.app.features.order_review.data.OrderReviewResponse
import com.localpk.app.features.orders.data.CancelOrderReasonRequestBody
import com.localpk.app.features.orders.data.CancelOrderReasonsResponse
import com.localpk.app.features.orders.data.CurrentOrdersResponse
import com.localpk.app.features.orders.data.DeliveredReceiptResponse
import com.localpk.app.features.orders.data.OrderDetailResponse
import com.localpk.app.features.orders.data.PastOrderResponse
import com.localpk.app.features.personal.data.ProfileDetails
import com.localpk.app.features.personal.data.ProfileDetailsResponse
import com.localpk.app.features.product_detail.data.ProductDetailResponse
import com.localpk.app.features.productlist.data.ProductResponse
import com.localpk.app.features.profile.data.CustomerSavingDetailResponse
import com.localpk.app.features.recommendation_engine.data.RecommendationEngineListProducts
import com.localpk.app.features.recommendation_engine.data.RecommendationEngineResponse
import com.localpk.app.features.recommendation_engine.data.SearchPastOrderResponse
import com.localpk.app.features.search.data.TrendingSearchResponse
import com.localpk.app.features.swimlane_engine.data.SwimlaneDataResponse
import com.localpk.app.features.swimlane_engine.data.SwimlaneResponse
import com.localpk.app.features.user_location.data.OperationalCities
import com.localpk.app.features.user_location.data.UserLocationResponse
import com.localpk.app.features.view_all_products.data.ViewAllProductResponse
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.features.wallet.data.CardTopUpRequest
import com.localpk.app.network.GeneralAddressResponse
import com.localpk.app.network.GeneralBodyResponse
import com.localpk.app.network.GeneralResponse
import com.localpk.app.network.GeneralResponseBodyLong
import com.localpk.app.network.GeneralStringResponse
import com.localpk.app.utils.FIRST_PAGE
import com.localpk.app.utils.PAGE_LIMIT
import com.localpk.app.utils.PRODUCT_PAGE_LIMIT
import com.localpk.app.utils.SharedPreferencesManager
import io.ktor.client.HttpClient


interface ApiService {
    suspend fun getCategories(): CategoryResponse
    suspend fun checkIfCustomerExists(phoneNumber: String): CheckIfCustomerExistsResponse
    suspend fun getBanners(params: String): CarousalBanner
    suspend fun getSwimlaneById(
        id: Int
    ): SwimlaneDataResponse

    suspend fun getSwimlaneByIdAndUserId(
        id: Int,
        userId: Int
    ): SwimlaneDataResponse

    suspend fun getSwimlaneEngine(
        landingPage: String,
        userType: String,
        superParentCategoryId: String
    ): SwimlaneResponse

    suspend fun getSwimlaneEngineByUserId(
        userId: Int,
        landingPage: String,
        userType: String,
        superParentCategoryId: String
    ): SwimlaneResponse

    suspend fun getProductDetail(id: Long): ProductDetailResponse
    suspend fun getProductDetailProtected(id: Long): ProductDetailResponse
    suspend fun getRecommendationProductsIds(
        queryText: String,
        screenType: String
    ): RecommendationEngineResponse

    suspend fun getRecommendationProductsIdsProtected(
        queryText: String,
        screenType: String
    ): RecommendationEngineResponse

    suspend fun getRecommendationProducts(
        queryText: String,
        screenType: String,
        productId: Number
    ): RecommendationEngineListProducts

    suspend fun getRecommendationProductsProtected(
        queryText: String,
        screenType: String,
        productId: Number
    ): RecommendationEngineListProducts

    suspend fun searchPastOrderItems(): SearchPastOrderResponse
    suspend fun searchRecommendedItems(): SearchPastOrderResponse

    suspend fun getProducts(
        warehouseId: Long,
        categoryId: Long,
        page: Int = FIRST_PAGE,
        limit: Int = PRODUCT_PAGE_LIMIT,
    ): ProductResponse

    suspend fun getProductsProtected(
        warehouseId: Long,
        categoryId: Long,
        page: Int = FIRST_PAGE,
        limit: Int = PRODUCT_PAGE_LIMIT,
    ): ProductResponse

    suspend fun getWarehouse(
        latitude: Double,
        longitude: Double,
        city: String
    ): UserLocationResponse

    suspend fun getOperationalCities(): OperationalCities
    suspend fun clearNonFoodCart()
    suspend fun getFrequentlyOrderedItems(
        limit: Int,
        page: Int
    ): FrequentlyOrderedResponse

    suspend fun getAllSwimlaneItems(
        categoryId: Long,
        limit: Int,
        page: Int
    ): ViewAllProductResponse

    suspend fun viewAllSwimlaneItems(
        id: Int? = null,
        limit: Int,
        page: Int
    ): ViewAllProductResponse

    suspend fun viewAllSwimlaneItemsById(
        id: Int? = null,
        limit: Int,
        page: Int,
        customerId: Long
    ): ViewAllProductResponse

    suspend fun getFlashDeals(
        status: String,
    ): FlashDealsResponse

    suspend fun getFlashDealDetail(
        id: Long,
    ): FlashDealDetailResponse

    suspend fun getFlashDealsProtected(
        status: String,
    ): FlashDealsResponse

    suspend fun getFlashDealDetailProtected(
        id: Long,
    ): FlashDealDetailResponse

    suspend fun getFlashDealProduct(
        id: Long
    ): FlashDealsResponse

    suspend fun searchProducts(
        name: String,
        warehouseId: Long,
        page: Int = FIRST_PAGE,
        limit: Int = RemoteConfigs.getSearchPageLimit(),
    ): ProductResponse

    suspend fun searchProductsProtected(
        name: String,
        warehouseId: Long,
        page: Int = FIRST_PAGE,
        limit: Int = RemoteConfigs.getSearchPageLimit(),
    ): ProductResponse

    suspend fun trendingSearch(): TrendingSearchResponse


    suspend fun increaseBundleQuantityProtected(): CartBundleResponse

    suspend fun increaseBundleQuantity(): CartBundleResponse

    suspend fun decreaseBundleQuantityProtected(
        body: BundleQuantity,
    ): GeneralStringResponse

    suspend fun decreaseBundleQuantity(
        body: BundleQuantity,
    ): GeneralStringResponse

    suspend fun bundleSwitchProtected(): GeneralStringResponse

    suspend fun bundleSwitch(): GeneralStringResponse

    suspend fun validateBundleProtected(): GeneralStringResponse

    suspend fun validateBundle(): GeneralStringResponse

    suspend fun addBundleToCartProtected(
        body: AddToCartRequest,
    ): AddToCartRawResponse

    suspend fun addBundleToCart(
        body: AddToCartRequest,
    ): AddToCartRawResponse

    suspend fun applyPromoCodeProtected(
        body: PromoCodeRequest
    ): GeneralResponse

    suspend fun applyPromoCode(
        body: PromoCodeRequest
    ): GeneralResponse

    suspend fun decreaseItemQuantity(
        itemId: Long,
    ): GeneralResponseBodyLong

    suspend fun decreaseItemQuantityProtected(
        itemId: Long,
        customerId: Long
    ): GeneralResponseBodyLong

    suspend fun increaseItemQuantity(
        itemId: Long
    ): GeneralResponseBodyLong

    suspend fun increaseItemQuantityProtected(
        itemId: Long
    ): GeneralResponseBodyLong

    suspend fun removeCartItemProtected(
        customerId: Long,
        cartId: Long,
    ): GeneralResponseBodyLong

    suspend fun removeCartItem(
        cartId: Long,
    ): GeneralResponseBodyLong

    suspend fun getCart(
        latitude: Double,
        longitude: Double,
        city: String
    ): CartResponse

    suspend fun getCartProtected(
        addressId: Long,
        latitude: Double,
        longitude: Double,
        city: String,
        detailed: Boolean
    ): CartResponse

    suspend fun addToCart(
        body: AddToCartRequest
    ): AddToCartRawResponse

    suspend fun addToCartProtected(
        body: AddToCartRequest,
        customerId: Long,
    ): AddToCartRawResponse

    suspend fun placeOrder(
        body: CheckoutRequest,
    ): CheckoutResponse

    suspend fun getOneLastStepAd(): List<OneLastStepAdResponse>

    suspend fun getOrderStatus(
        orderId: Long,
    ): OrderStatusResponse

    suspend fun getCurrentOrders(): CurrentOrdersResponse
    suspend fun cancelOrder(
        cancelOrderReasonRequestBody: CancelOrderReasonRequestBody,
        orderId: Long
    ): GeneralResponse

    suspend fun getInvoice(orderId: Long): DeliveredReceiptResponse
    suspend fun getCancelOrderReasons(): CancelOrderReasonsResponse
    suspend fun getOrderDetail(
        orderId: Long,
        latitude: Double,
        longitude: Double,
        city: String
    ): OrderDetailResponse

    suspend fun getPastOrders(orderStatus: String, month: Int): PastOrderResponse
    suspend fun addOrderReview(body: AddOrderReviewRequest): GeneralResponse

    suspend fun getProfileDetails(): ProfileDetailsResponse
    suspend fun updateProfileDetails(body: ProfileDetails): GeneralBodyResponse
    suspend fun verifyEmail(): GeneralStringResponse
    suspend fun sendEmail(): GeneralStringResponse
    suspend fun fetchCustomerAddress(customerId: Long): CustomerAddressesResponse
    suspend fun removeAddress(
        customerId: Long,
        addressId: Long
    ): GeneralResponse

    suspend fun updateAddress(
        addressId: Long,
        body: AddAddressRequest
    ): GeneralAddressResponse

    suspend fun addAddress(
        customerId: Long,
        body: AddAddressRequest,
    ): GeneralAddressResponse

    suspend fun getCustomerDetails(): CustomerSavingDetailResponse
    suspend fun getLatestOrderForReview(): OrderReviewResponse
    suspend fun getOrderReviewCategories(screenRating: String): OrderReviewCategoriesResponse
    suspend fun cancelReview(shipmentId: Long): GeneralResponse

    suspend fun cardTopUpRequest(
        body: CardTopUpRequest
    ): CardPaymentDetails

    suspend fun getAddressMapPolygon(): MapPolygonResponse

    suspend fun deleteCustomer(
        customerId: Long
    )

    suspend fun getPoweredByBanners(sponsorType: String): PoweredByResponse
}

class ApiServiceImpl(
    httpClient: HttpClient,
    baseUrl: String,
    sharedPreferencesManager: SharedPreferencesManager
) : BaseApiService(httpClient, baseUrl, sharedPreferencesManager), ApiService {

    override suspend fun getCategories(): CategoryResponse {
        return get(
            endpoint = "/api/consumer/category",
            parameters = mapOf(
                "limit" to 1000,
                "page" to 1,
                "status" to true
            )
        )
    }

    override suspend fun getBanners(params: String): CarousalBanner {
        return get(
            endpoint = "/api/consumer/banner",
            parameters = mapOf(
                "belongsTo" to params
            )
        )
    }

    override suspend fun getSwimlaneById(id: Int): SwimlaneDataResponse {
        return get("/api/consumer/swimlane/$id")
    }

    override suspend fun getSwimlaneByIdAndUserId(id: Int, userId: Int): SwimlaneDataResponse {
        return get("/api/consumer/swimlane/$id/$userId")
    }

    override suspend fun getSwimlaneEngine(
        landingPage: String,
        userType: String,
        superParentCategoryId: String
    ): SwimlaneResponse {
        return get(
            endpoint = "/api/consumer/swimlane",
            parameters = mapOf(
                "landingPageType" to landingPage,
                "userType" to userType,
                "superParentCategoryId" to superParentCategoryId
            )
        )
    }

    override suspend fun getSwimlaneEngineByUserId(
        userId: Int,
        landingPage: String,
        userType: String,
        superParentCategoryId: String
    ): SwimlaneResponse {
        return get(
            endpoint = "/api/consumer/swimlane/all/$userId",
            parameters = mapOf(
                "landingPageType" to landingPage,
                "userType" to userType,
                "superParentCategoryId" to superParentCategoryId
            )
        )
    }

    override suspend fun checkIfCustomerExists(phoneNumber: String): CheckIfCustomerExistsResponse {
        return get(
            endpoint = "/api/dolat-api/customer/{phoneNumber}/exists",
            parameters = mapOf("phoneNumber" to phoneNumber)
        )
    }

    override suspend fun getProductDetail(id: Long): ProductDetailResponse {
        return get("/api/consumer/products/details/$id")
    }

    override suspend fun getProductDetailProtected(id: Long): ProductDetailResponse {
        return get("/api/consumer/products/$id/details-with-customer")
    }

    override suspend fun getRecommendationProductsIds(
        queryText: String,
        screenType: String
    ): RecommendationEngineResponse {
        return get(
            endpoint = "/api/consumer/recommendation/$queryText/public",
            parameters = mapOf("swimlane" to screenType)
        )
    }

    override suspend fun getRecommendationProductsIdsProtected(
        queryText: String,
        screenType: String
    ): RecommendationEngineResponse {
        return get(
            endpoint = "/api/consumer/recommendation/$queryText",
            parameters = mapOf("swimlane" to screenType)
        )
    }

    override suspend fun getRecommendationProducts(
        queryText: String,
        screenType: String,
        productId: Number
    ): RecommendationEngineListProducts {
        return get(
            endpoint = "/api/consumer/products",
            parameters = mapOf(
                "productIds" to queryText,
                "swimlane" to screenType,
                "productId" to productId
            )
        )
    }

    override suspend fun getRecommendationProductsProtected(
        queryText: String,
        screenType: String,
        productId: Number
    ): RecommendationEngineListProducts {
        return get(
            endpoint = "/api/consumer/products/protected",
            parameters = mapOf(
                "productIds" to queryText,
                "swimlane" to screenType,
                "productId" to productId
            )
        )
    }

    override suspend fun searchPastOrderItems(): SearchPastOrderResponse {
        return get(
            endpoint = "/api/consumer/customer/past-order-recommendation-items"
        )
    }

    override suspend fun searchRecommendedItems(): SearchPastOrderResponse {
        return get(
            endpoint = "/api/consumer/customer/recommendations"
        )
    }

    override suspend fun getProducts(
        warehouseId: Long,
        categoryId: Long,
        page: Int,
        limit: Int
    ): ProductResponse {
        return get(
            endpoint = "/api/consumer/products/pricing",
            parameters = mapOf(
                "warehouse_id" to warehouseId,
                "category_id" to categoryId,
                "page" to page,
                "limit" to limit
            )
        )
    }

    override suspend fun getProductsProtected(
        warehouseId: Long,
        categoryId: Long,
        page: Int,
        limit: Int
    ): ProductResponse {
        return get(
            endpoint = "/api/consumer/products/pricing/protected",
            parameters = mapOf(
                "warehouse_id" to warehouseId,
                "category_id" to categoryId,
                "page" to page,
                "limit" to limit
            )
        )
    }

    override suspend fun getWarehouse(
        latitude: Double,
        longitude: Double,
        city: String
    ): UserLocationResponse {
        return get(
            endpoint = "/api/consumer/warehouse/coordinates",
            parameters = mapOf("latitude" to latitude, "longitude" to longitude, "city" to city)
        )
    }

    override suspend fun getOperationalCities(): OperationalCities {
        return get("/api/consumer/postex/operational-cities")
    }

    override suspend fun clearNonFoodCart() {
        return delete(endpoint = "/api/consumer/cart")
    }

    override suspend fun getFrequentlyOrderedItems(
        limit: Int,
        page: Int
    ): FrequentlyOrderedResponse {
        return get(
            endpoint = "/api/consumer/sales-order-item/frequently-ordered-items",
            parameters = mapOf(
                "limit" to limit,
                "page" to page
            )
        )
    }

    override suspend fun getAllSwimlaneItems(
        categoryId: Long,
        limit: Int,
        page: Int
    ): ViewAllProductResponse {
        return get(
            endpoint = "/api/consumer/products/$categoryId/list",
            parameters = mapOf(
                "limit" to limit,
                "page" to page
            )
        )
    }

    override suspend fun viewAllSwimlaneItems(
        id: Int?,
        limit: Int,
        page: Int
    ): ViewAllProductResponse {
        return get(
            endpoint = "/api/consumer/swimlane/$id/products", parameters = mapOf(
                "limit" to limit,
                "page" to page
            )
        )
    }

    override suspend fun viewAllSwimlaneItemsById(
        id: Int?,
        limit: Int,
        page: Int,
        customerId: Long
    ): ViewAllProductResponse {
        return get(
            endpoint = "/api/consumer/swimlane/$id/$customerId/products", parameters = mapOf(
                "limit" to limit,
                "page" to page
            )
        )
    }

    override suspend fun getFlashDeals(status: String): FlashDealsResponse {
        return get(
            "/api/consumer/deal/flash-deal", parameters = mapOf(
                "status" to status
            )
        )
    }

    override suspend fun getFlashDealDetail(id: Long): FlashDealDetailResponse {
        return get("/api/consumer/deal/flash-deal/$id")
    }

    override suspend fun getFlashDealDetailProtected(id: Long): FlashDealDetailResponse {
        return get(
            "/api/consumer/deal/flash-deal/$id/protected"
        )
    }

    override suspend fun getFlashDealsProtected(status: String): FlashDealsResponse {
        return get(
            "/api/consumer/deal/flash-deal/protected", parameters = mapOf(
                "status" to status
            )
        )
    }

    override suspend fun getFlashDealProduct(id: Long): FlashDealsResponse {
        return get("/api/consumer/deal/flash-deal/product/$id")
    }

    override suspend fun searchProducts(
        name: String,
        warehouseId: Long,
        page: Int,
        limit: Int
    ): ProductResponse {
        return get(
            endpoint = "/api/consumer/products/pricing",
            parameters = mapOf(
                "name" to name,
                "warehouse_id" to warehouseId,
                "page" to page,
                "limit" to limit
            )
        )
    }

    override suspend fun searchProductsProtected(
        name: String,
        warehouseId: Long,
        page: Int,
        limit: Int
    ): ProductResponse {
        return get(
            endpoint = "/api/consumer/products/pricing/protected",
            parameters = mapOf(
                "name" to name,
                "warehouse_id" to warehouseId,
                "page" to page,
                "limit" to limit
            )
        )
    }

    override suspend fun trendingSearch(): TrendingSearchResponse {
        return get("/api/consumer/products/trending-products-searches")
    }


    override suspend fun addBundleToCart(body: AddToCartRequest): AddToCartRawResponse {
        return post("/api/consumer/guest-cart/add-bundle-item", body)
    }

    override suspend fun addBundleToCartProtected(body: AddToCartRequest): AddToCartRawResponse {
        return post("api/consumer/cart/add-bundle-item", body)
    }

    override suspend fun decreaseBundleQuantity(body: BundleQuantity): GeneralStringResponse {
        return put("/api/consumer/guest-cart/decrement-bundle-quantity")
    }

    override suspend fun decreaseBundleQuantityProtected(body: BundleQuantity): GeneralStringResponse {
        return put("/api/consumer/cart/decrement-bundle-quantity")
    }

    override suspend fun bundleSwitch(): GeneralStringResponse {
        return put("/api/consumer/guest-cart/toggle-cart")
    }

    override suspend fun bundleSwitchProtected(): GeneralStringResponse {
        return put("/api/consumer/cart/toggle-cart")
    }

    override suspend fun increaseBundleQuantity(): CartBundleResponse {
        return put("/api/consumer/guest-cart/increment-bundle-quantity")
    }

    override suspend fun increaseBundleQuantityProtected(): CartBundleResponse {
        return put("/api/consumer/cart/increment-bundle-quantity")
    }

    override suspend fun validateBundle(): GeneralStringResponse {
        return put("api/consumer/guest-cart/validate-bundle")
    }

    override suspend fun validateBundleProtected(): GeneralStringResponse {
        return put("/api/consumer/cart/validate-bundle")
    }

    override suspend fun applyPromoCode(body: PromoCodeRequest): GeneralResponse {
        return patch("/api/consumer/cart/items", body)
    }

    override suspend fun applyPromoCodeProtected(body: PromoCodeRequest): GeneralResponse {
        return patch("/api/consumer/cart/items", body)
    }

    override suspend fun decreaseItemQuantity(itemId: Long): GeneralResponseBodyLong {
        return put("/api/consumer/guest-cart/decrease-item-quantity/$itemId")
    }

    override suspend fun increaseItemQuantity(itemId: Long): GeneralResponseBodyLong {
        return put("/api/consumer/guest-cart/$itemId/add")
    }

    override suspend fun increaseItemQuantityProtected(itemId: Long): GeneralResponseBodyLong {
        return put("/api/consumer/cart/$itemId/add")
    }

    override suspend fun decreaseItemQuantityProtected(
        itemId: Long,
        customerId: Long
    ): GeneralResponseBodyLong {
        return put("/api/consumer/cart/decrease-item-quantity/$itemId/customer/$customerId")
    }

    override suspend fun removeCartItem(cartId: Long): GeneralResponseBodyLong {
        return delete("/api/consumer/guest-cart/remove-item/$cartId")
    }

    override suspend fun removeCartItemProtected(
        customerId: Long,
        cartId: Long
    ): GeneralResponseBodyLong {
        return delete("/api/consumer/cart/remove-item/$cartId/customer/$customerId")
    }


    override suspend fun addToCart(body: AddToCartRequest): AddToCartRawResponse {
        return post("/api/consumer/guest-cart/add-item", body)
    }

    override suspend fun addToCartProtected(
        body: AddToCartRequest,
        customerId: Long
    ): AddToCartRawResponse {
        return post("/api/consumer/cart/add-item/customer/$customerId", body)
    }


    override suspend fun getCartProtected(
        addressId: Long,
        latitude: Double,
        longitude: Double,
        city: String,
        detailed: Boolean
    ): CartResponse {
        return get(
            "/api/consumer/cart/v2", parameters = mapOf(
                "latitude" to latitude,
                "longitude" to longitude,
                "city" to city,
                "addressId" to addressId,
                "detailed" to detailed
            )
        )
    }

    override suspend fun getCart(latitude: Double, longitude: Double, city: String): CartResponse {
        return get(
            "/api/consumer/guest-cart/v2", parameters = mapOf(
                "latitude" to latitude,
                "longitude" to longitude,
                "city" to city
            )
        )
    }

    override suspend fun placeOrder(body: CheckoutRequest): CheckoutResponse {
        return post("/api/consumer/sales-order", body)
    }

    override suspend fun getOneLastStepAd(): List<OneLastStepAdResponse> {
        return get("/api/consumer/ad")
    }

    override suspend fun getOrderStatus(orderId: Long): OrderStatusResponse {
        return get("/api/consumer/sales-order/$orderId/status")
    }

    override suspend fun getCurrentOrders(): CurrentOrdersResponse {
        return get(
            "/api/consumer/sales-order/customer/current-orders", parameters = mapOf(
                "limit" to PAGE_LIMIT,
                "page" to 1,
            )
        )
    }

    override suspend fun cancelOrder(
        body: CancelOrderReasonRequestBody,
        orderId: Long
    ): GeneralResponse {
        return patch("/api/consumer/sales-order/$orderId/cancel", body)
    }

    override suspend fun getInvoice(orderId: Long): DeliveredReceiptResponse {
        return get("/api/consumer/sales-order/invoice/$orderId")
    }

    override suspend fun getCancelOrderReasons(): CancelOrderReasonsResponse {
        return get("/api/consumer/sales-order/cancellation-reasons")
    }

    override suspend fun getOrderDetail(
        orderId: Long,
        latitude: Double,
        longitude: Double,
        city: String
    ): OrderDetailResponse {
        return get(
            "/api/consumer/sales-order/$orderId/order-history/order-details",
            parameters = mapOf(
                "latitude" to latitude,
                "longitude" to longitude, "city" to city
            )
        )

    }

    override suspend fun getPastOrders(
        orderStatus: String,
        month: Int
    ): PastOrderResponse {
        return get(
            "/api/consumer/sales-order/customer/past-orders",
            parameters = mapOf(
                "orderStatus" to orderStatus,
                "month" to month,
                "limit" to PAGE_LIMIT,
                "page" to 1
            )
        )
    }

    override suspend fun addOrderReview(body: AddOrderReviewRequest): GeneralResponse {
        return post("/api/consumer/sales-order-rating", body)
    }

    override suspend fun getProfileDetails(): ProfileDetailsResponse {
        return get("/api/consumer/customer/profile")
    }

    override suspend fun updateProfileDetails(body: ProfileDetails): GeneralBodyResponse {
        return put("/api/consumer/customer", body)
    }

    override suspend fun verifyEmail(): GeneralStringResponse {
        return get("/api/consumer/customer/verify-email")
    }

    override suspend fun sendEmail(): GeneralStringResponse {
        return get("/api/consumer/customer/send-verification-email")
    }

    override suspend fun fetchCustomerAddress(customerId: Long): CustomerAddressesResponse {
        return get("/api/consumer/customer/${customerId}/address")
    }

    override suspend fun removeAddress(
        customerId: Long,
        addressId: Long,
    ): GeneralResponse {
        return delete("/api/consumer/customer/${customerId}/address/${addressId}")
    }

    override suspend fun updateAddress(
        addressId: Long, body: AddAddressRequest,
    ): GeneralAddressResponse {
        return put("/api/consumer/customer/address/$addressId", body)
    }

    override suspend fun addAddress(
        customerId: Long,
        body: AddAddressRequest,
    ): GeneralAddressResponse {
        return post("/api/consumer/customer/${customerId}/address", body)
    }

    override suspend fun getCustomerDetails(): CustomerSavingDetailResponse {
        return get("/api/consumer/customer/details")
    }

    override suspend fun getLatestOrderForReview(): OrderReviewResponse {
        return get("/api/consumer/sales-order-rating/sales-order")
    }

    override suspend fun getOrderReviewCategories(screenRating: String): OrderReviewCategoriesResponse {
        return get("/api/consumer/rating-category/$screenRating")
    }

    override suspend fun cancelReview(shipmentId: Long): GeneralResponse {
        return post("/api/consumer/shipment/$shipmentId/rating-cancel")
    }

    override suspend fun cardTopUpRequest(body: CardTopUpRequest): CardPaymentDetails {
        return post(
            "/api/consumer/payments/card/payment-intent",
            requestBody = body
        )
    }

    override suspend fun getAddressMapPolygon(): MapPolygonResponse {
        return get("/api/consumer/address/polygon-geojson")
    }

    override suspend fun deleteCustomer(customerId: Long) {
        return delete("/api/consumer/customer/$customerId")
    }

    override suspend fun getPoweredByBanners(sponsorType: String): PoweredByResponse {
        return get(
            endpoint = "/api/consumer/manufacturer",
            parameters = mapOf(
                "sponsorType" to sponsorType
            )
        )
    }
}
