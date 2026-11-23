package com.localpk.app.api

import com.localpk.app.features.address.data.AddAddressRequest
import com.localpk.app.features.address.data.CustomerAddressesResponse
import com.localpk.app.features.address.data.MapPolygonResponse
import com.localpk.app.features.banner.data.CarousalBanner
import com.localpk.app.features.cart.data.AddToCartRequest
import com.localpk.app.features.cart.data.AddToCartResponse
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
import com.localpk.app.features.orders.data.CancelOrderReasonRequest
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
import com.localpk.app.features.user_location.data.UserLocationRequest
import com.localpk.app.features.user_location.data.UserLocationResponse
import com.localpk.app.features.view_all_products.data.ViewAllProductResponse
import com.localpk.app.features.wallet.data.CardTopUpRequest
import com.localpk.app.network.GeneralAddressResponse
import com.localpk.app.network.GeneralBodyResponse
import com.localpk.app.network.GeneralResponse
import com.localpk.app.network.GeneralResponseBodyLong
import com.localpk.app.network.GeneralStringResponse
import com.localpk.app.network.ResponseResult
import com.localpk.app.network.callApi
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.isNotZero


interface AppRepository {
    suspend fun getCategories(): ResponseResult<CategoryResponse>
    suspend fun checkIfCustomerExists(phoneNumber: String): ResponseResult<CheckIfCustomerExistsResponse>
    suspend fun getBanners(params: String): ResponseResult<CarousalBanner>
    suspend fun getSwimlaneById(
        id: Int
    ): ResponseResult<SwimlaneDataResponse>

    suspend fun getSwimlaneEngine(
        landingPage: String,
        userType: String,
        superParentCategoryId: String
    ): ResponseResult<SwimlaneResponse>

    suspend fun getProductDetail(id: Long): ResponseResult<ProductDetailResponse>
    suspend fun getRecommendedProductIds(
        queryText: String,
        screenType: String
    ): ResponseResult<RecommendationEngineResponse>

    suspend fun getRecommendedProduct(
        queryText: String,
        screenType: String,
        productId: Number
    ): ResponseResult<RecommendationEngineListProducts>

    suspend fun searchPastOrderItems(): ResponseResult<SearchPastOrderResponse>
    suspend fun getProducts(categoryId: Long, page: Int): ResponseResult<ProductResponse>

    suspend fun getWarehouse(userLocationRequest: UserLocationRequest): ResponseResult<UserLocationResponse>

    suspend fun getOperationalCities(): ResponseResult<OperationalCities>
    suspend fun clearCart(): ResponseResult<Unit>
    suspend fun getFrequentlyOrderedItems(
        limit: Int,
        page: Int
    ): ResponseResult<FrequentlyOrderedResponse>

    suspend fun getAllSwimlaneItems(
        categoryId: Long,
        limit: Int,
        page: Int
    ): ResponseResult<ViewAllProductResponse>

    suspend fun viewAllSwimlaneItems(
        id: Int,
        limit: Int,
        page: Int
    ): ResponseResult<ViewAllProductResponse>

    suspend fun getFlashDeals(status: String): ResponseResult<FlashDealsResponse>
    suspend fun getFlashDealDetail(id: Long): ResponseResult<FlashDealDetailResponse>
    suspend fun getFlashDealProduct(id: Long): ResponseResult<FlashDealsResponse>
    suspend fun searchProducts(queryText: String): ResponseResult<ProductResponse>
    suspend fun trendingSearch(): ResponseResult<TrendingSearchResponse>

    suspend fun increaseBundleQuantity(): ResponseResult<CartBundleResponse>
    suspend fun decreaseBundleQuantity(bundleQuantity: Int): ResponseResult<GeneralStringResponse>
    suspend fun bundleSwitch(): ResponseResult<GeneralStringResponse>
    suspend fun validateBundle(): ResponseResult<GeneralStringResponse>
    suspend fun applyPromoCode(promoCodeRequest: PromoCodeRequest): ResponseResult<GeneralResponse>
    suspend fun decreaseCartItemQuantity(itemId: Long): ResponseResult<GeneralResponseBodyLong>
    suspend fun increaseCartItemQuantity(itemId: Long): ResponseResult<GeneralResponseBodyLong>
    suspend fun removeCartItem(cartId: Long): ResponseResult<GeneralResponseBodyLong>
    suspend fun fetchCart(
        detailed: Boolean
    ): ResponseResult<CartResponse>

    suspend fun addToCart(
        addToCartRequest: AddToCartRequest,
        isCartBundle: Boolean
    ): ResponseResult<AddToCartResponse>

    suspend fun placeOrder(orderRequest: CheckoutRequest): ResponseResult<CheckoutResponse>
    suspend fun getOneLastStepAd(): ResponseResult<List<OneLastStepAdResponse>>
    suspend fun getOrderStatus(orderId: Long): ResponseResult<OrderStatusResponse>
    suspend fun getCurrentOrders(): ResponseResult<CurrentOrdersResponse>
    suspend fun cancelOrder(cancelOrderReasonRequest: CancelOrderReasonRequest): ResponseResult<GeneralResponse>
    suspend fun getInvoice(orderId: Long): ResponseResult<DeliveredReceiptResponse>
    suspend fun getCancelOrderReasons(): ResponseResult<CancelOrderReasonsResponse>
    suspend fun getOrderDetail(orderId: Long): ResponseResult<OrderDetailResponse>
    suspend fun getPastOrders(orderStatus: String, month: Int): ResponseResult<PastOrderResponse>
    suspend fun addOrderReview(orderReviewRequest: AddOrderReviewRequest): ResponseResult<GeneralResponse>
    suspend fun getProfileDetails(): ResponseResult<ProfileDetailsResponse>
    suspend fun updateProfileDetails(profileRequest: ProfileDetails): ResponseResult<GeneralBodyResponse>
    suspend fun verifyEmail(): ResponseResult<GeneralStringResponse>
    suspend fun sendEmail(): ResponseResult<GeneralStringResponse>
    suspend fun fetchCustomerAddress(): ResponseResult<CustomerAddressesResponse>
    suspend fun removeAddress(addressId: Long): ResponseResult<GeneralResponse>
    suspend fun addAddress(addAddressRequest: AddAddressRequest): ResponseResult<GeneralAddressResponse>
    suspend fun getCustomerDetails(): ResponseResult<CustomerSavingDetailResponse>
    suspend fun getLatestOrderForReview(): ResponseResult<OrderReviewResponse>
    suspend fun getOrderReviewCategories(screenRating: String): ResponseResult<OrderReviewCategoriesResponse>
    suspend fun cancelReview(shipmentId: Long): ResponseResult<GeneralResponse>
    suspend fun cardTopUpRequest(
        amount: Double,
        paymentType: String
    ): ResponseResult<CardPaymentDetails>

    suspend fun getAddressMapPolygon(): ResponseResult<MapPolygonResponse>
    suspend fun deleteCustomer(): ResponseResult<Unit>
    suspend fun getPoweredByBanners(sponsorType: String): ResponseResult<PoweredByResponse>
}

class AppRepositoryImpl(
    private val service: ApiService,
    private val sharedPreferencesManager: SharedPreferencesManager
) : AppRepository {

    override suspend fun getCategories(): ResponseResult<CategoryResponse> {
        return callApi { service.getCategories() }
    }

    override suspend fun checkIfCustomerExists(phoneNumber: String): ResponseResult<CheckIfCustomerExistsResponse> {
        return callApi {
            service.checkIfCustomerExists(phoneNumber)
        }
    }

    override suspend fun getBanners(params: String): ResponseResult<CarousalBanner> {
        return callApi { service.getBanners(params) }
    }

    override suspend fun getSwimlaneById(
        id: Int
    ): ResponseResult<SwimlaneDataResponse> {
        return callApi {
            service.getSwimlaneById(id)
        }
    }

    override suspend fun getSwimlaneEngine(
        landingPage: String,
        userType: String,
        superParentCategoryId: String
    ): ResponseResult<SwimlaneResponse> {
        return callApi {
            service.getSwimlaneEngine(landingPage, userType, superParentCategoryId)
        }
    }


    override suspend fun getProductDetail(id: Long): ResponseResult<ProductDetailResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn())
                service.getProductDetailProtected(id)
            else
                service.getProductDetail(id)
        }
    }

    override suspend fun getRecommendedProductIds(
        queryText: String,
        screenType: String
    ): ResponseResult<RecommendationEngineResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn())
                service.getRecommendationProductsIdsProtected(queryText, screenType)
            else
                service.getRecommendationProductsIds(queryText, screenType)
        }
    }

    override suspend fun getRecommendedProduct(
        queryText: String,
        screenType: String,
        productId: Number
    ): ResponseResult<RecommendationEngineListProducts> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn())
                service.getRecommendationProductsProtected(queryText, screenType, productId)
            else
                service.getRecommendationProducts(queryText, screenType, productId)
        }
    }

    override suspend fun searchPastOrderItems(): ResponseResult<SearchPastOrderResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn())
                service.searchPastOrderItems()
            else
                service.searchRecommendedItems()
        }
    }

    override suspend fun getProducts(categoryId: Long, page: Int): ResponseResult<ProductResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn())
                service.getProductsProtected(
                    warehouseId = sharedPreferencesManager.getWarehouseId(),
                    categoryId = categoryId,
                    page = page
                )
            else
                service.getProducts(
                    warehouseId = sharedPreferencesManager.getWarehouseId(),
                    categoryId = categoryId,
                    page = page
                )
        }
    }

    override suspend fun getWarehouse(userLocationRequest: UserLocationRequest): ResponseResult<UserLocationResponse> {
        return callApi {
            service.getWarehouse(
                userLocationRequest.latitude,
                userLocationRequest.longitude,
                userLocationRequest.city
            )
        }
    }

    override suspend fun getOperationalCities(): ResponseResult<OperationalCities> {
        return callApi {
            service.getOperationalCities()
        }
    }

    override suspend fun clearCart(): ResponseResult<Unit> {
        return callApi {
            service.clearNonFoodCart()
        }
    }

    override suspend fun getFrequentlyOrderedItems(
        limit: Int,
        page: Int
    ): ResponseResult<FrequentlyOrderedResponse> {
        return callApi {
            service.getFrequentlyOrderedItems(limit, page)
        }
    }

    override suspend fun getAllSwimlaneItems(
        categoryId: Long,
        limit: Int,
        page: Int
    ): ResponseResult<ViewAllProductResponse> {
        return callApi {
            service.getAllSwimlaneItems(
                categoryId, limit, page
            )
        }
    }

    override suspend fun viewAllSwimlaneItems(
        id: Int,
        limit: Int,
        page: Int
    ): ResponseResult<ViewAllProductResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn()) {
                service.viewAllSwimlaneItemsById(
                    id = id,
                    customerId = sharedPreferencesManager.getUserId(),
                    limit = limit,
                    page = page
                )
            } else {
                service.viewAllSwimlaneItems(id, limit, page)
            }
        }
    }

    override suspend fun getFlashDeals(
        status: String
    ): ResponseResult<FlashDealsResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn())
                service.getFlashDealsProtected(status)
            else
                service.getFlashDeals(status)
        }
    }

    override suspend fun getFlashDealDetail(id: Long): ResponseResult<FlashDealDetailResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn())
                service.getFlashDealDetailProtected(id)
            else
                service.getFlashDealDetail(id)
        }
    }

    override suspend fun getFlashDealProduct(id: Long): ResponseResult<FlashDealsResponse> {
        return callApi { service.getFlashDealProduct(id) }
    }

    override suspend fun searchProducts(queryText: String): ResponseResult<ProductResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn())
                service.searchProductsProtected(
                    queryText,
                    sharedPreferencesManager.getWarehouseId()
                )
            else
                service.searchProducts(
                    queryText,
                    sharedPreferencesManager.getWarehouseId()
                )
        }
    }

    override suspend fun trendingSearch(): ResponseResult<TrendingSearchResponse> {
        return callApi { service.trendingSearch() }
    }


    override suspend fun increaseBundleQuantity(): ResponseResult<CartBundleResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn()) {
                service.increaseBundleQuantityProtected()
            } else {
                service.increaseBundleQuantity()
            }
        }
    }

    override suspend fun decreaseBundleQuantity(bundleQuantity: Int): ResponseResult<GeneralStringResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn()) {
                service.decreaseBundleQuantityProtected(BundleQuantity(bundleQuantity))
            } else {
                service.decreaseBundleQuantity(BundleQuantity(bundleQuantity))
            }
        }
    }

    override suspend fun bundleSwitch(): ResponseResult<GeneralStringResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn()) {
                service.bundleSwitchProtected()
            } else {
                service.bundleSwitch()
            }
        }
    }

    override suspend fun validateBundle(): ResponseResult<GeneralStringResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn()) {
                service.validateBundleProtected()
            } else {
                service.validateBundle()
            }
        }
    }

    override suspend fun applyPromoCode(promoCodeRequest: PromoCodeRequest): ResponseResult<GeneralResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn()) {
                service.applyPromoCodeProtected(body = promoCodeRequest)
            } else {
                service.applyPromoCode(body = promoCodeRequest)
            }
        }
    }

    override suspend fun decreaseCartItemQuantity(itemId: Long): ResponseResult<GeneralResponseBodyLong> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn()) {
                service.decreaseItemQuantityProtected(
                    itemId,
                    sharedPreferencesManager.getUserId()
                )
            } else {
                service.decreaseItemQuantity(
                    itemId
                )
            }
        }
    }

    override suspend fun increaseCartItemQuantity(itemId: Long): ResponseResult<GeneralResponseBodyLong> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn()) {
                service.increaseItemQuantityProtected(
                    itemId
                )
            } else {
                service.increaseItemQuantity(
                    itemId
                )
            }
        }
    }

    override suspend fun removeCartItem(
        cartId: Long,
    ): ResponseResult<GeneralResponseBodyLong> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn()) {
                service.removeCartItemProtected(sharedPreferencesManager.getUserId(), cartId)
            } else {
                service.removeCartItem(cartId)
            }
        }
    }

    override suspend fun fetchCart(
        detailed: Boolean
    ): ResponseResult<CartResponse> {
        return callApi {
            if (sharedPreferencesManager.isLoggedIn()) {
                service.getCartProtected(
                    sharedPreferencesManager.getDefaultAddressId(),
                    sharedPreferencesManager.getLatitude().toDouble(),
                    sharedPreferencesManager.getLongitude().toDouble(),
                    sharedPreferencesManager.getSelectedCity(),
                    detailed = detailed
                )
            } else {
                service.getCart(
                    sharedPreferencesManager.getLatitude().toDouble(),
                    sharedPreferencesManager.getLongitude().toDouble(),
                    sharedPreferencesManager.getSelectedCity()
                )
            }
        }
    }

    override suspend fun addToCart(
        addToCartRequest: AddToCartRequest,
        isCartBundle: Boolean
    ): ResponseResult<AddToCartResponse> {
        return callApi {
            val raw = if (sharedPreferencesManager.isLoggedIn()) {
                if (isCartBundle) {
                    service.addBundleToCartProtected(addToCartRequest)
                } else {
                    service.addToCartProtected(
                        addToCartRequest,
                        sharedPreferencesManager.getUserId()
                    )
                }
            } else {
                if (isCartBundle) {
                    service.addBundleToCart(addToCartRequest)
                } else {
                    service.addToCart(addToCartRequest)
                }
            }
            raw.body
        }
    }

    override suspend fun placeOrder(orderRequest: CheckoutRequest): ResponseResult<CheckoutResponse> {
        return callApi { service.placeOrder(orderRequest) }
    }

    override suspend fun getOneLastStepAd(): ResponseResult<List<OneLastStepAdResponse>> {
        return callApi {
            service.getOneLastStepAd()
        }
    }

    override suspend fun getOrderStatus(orderId: Long): ResponseResult<OrderStatusResponse> {
        return callApi { service.getOrderStatus(orderId) }
    }

    override suspend fun getCurrentOrders(): ResponseResult<CurrentOrdersResponse> {
        return callApi {
            service.getCurrentOrders()
        }
    }

    override suspend fun cancelOrder(cancelOrderReasonRequest: CancelOrderReasonRequest): ResponseResult<GeneralResponse> {
        return callApi {
            service.cancelOrder(
                cancelOrderReasonRequest.cancelOrderReasonRequestBody,
                cancelOrderReasonRequest.orderId
            )
        }
    }

    override suspend fun getInvoice(
        orderId: Long
    ): ResponseResult<DeliveredReceiptResponse> {
        return callApi {
            service.getInvoice(orderId)
        }
    }

    override suspend fun getCancelOrderReasons(): ResponseResult<CancelOrderReasonsResponse> {
        return callApi { service.getCancelOrderReasons() }
    }

    override suspend fun getOrderDetail(orderId: Long): ResponseResult<OrderDetailResponse> {
        return callApi {
            service.getOrderDetail(
                orderId,
                sharedPreferencesManager.getLatitude().toDouble(),
                sharedPreferencesManager.getLongitude().toDouble(),
                sharedPreferencesManager.getSelectedCity()
            )
        }
    }

    override suspend fun getPastOrders(
        orderStatus: String,
        month: Int
    ): ResponseResult<PastOrderResponse> {
        return callApi {
            service.getPastOrders(orderStatus, month)
        }
    }

    override suspend fun addOrderReview(orderReviewRequest: AddOrderReviewRequest): ResponseResult<GeneralResponse> {
        return callApi {
            service.addOrderReview(orderReviewRequest)
        }
    }


    override suspend fun getProfileDetails(): ResponseResult<ProfileDetailsResponse> {
        return callApi { service.getProfileDetails() }
    }

    override suspend fun updateProfileDetails(profileRequest: ProfileDetails): ResponseResult<GeneralBodyResponse> {
        return callApi { service.updateProfileDetails(profileRequest) }
    }

    override suspend fun verifyEmail(): ResponseResult<GeneralStringResponse> {
        return callApi { service.verifyEmail() }
    }

    override suspend fun sendEmail(): ResponseResult<GeneralStringResponse> {
        return callApi { service.sendEmail() }
    }

    override suspend fun fetchCustomerAddress(): ResponseResult<CustomerAddressesResponse> {
        return callApi {
            service.fetchCustomerAddress(sharedPreferencesManager.getUserId())
        }
    }

    override suspend fun removeAddress(
        addressId: Long,
    ): ResponseResult<GeneralResponse> {
        return callApi {
            service.removeAddress(sharedPreferencesManager.getUserId(), addressId)
        }
    }

    override suspend fun addAddress(addAddressRequest: AddAddressRequest): ResponseResult<GeneralAddressResponse> {
        return callApi {
            if (addAddressRequest.id.isNotZero())
                service.updateAddress(
//                    sharedPreferencesManager.getUserId(),
                    addAddressRequest.id,
                    addAddressRequest
                )
            else service.addAddress(sharedPreferencesManager.getUserId(), addAddressRequest)
        }
    }

    override suspend fun getCustomerDetails(): ResponseResult<CustomerSavingDetailResponse> {
        return callApi { service.getCustomerDetails() }
    }

    override suspend fun getLatestOrderForReview(): ResponseResult<OrderReviewResponse> {
        return callApi { service.getLatestOrderForReview() }
    }

    override suspend fun getOrderReviewCategories(screenRating: String): ResponseResult<OrderReviewCategoriesResponse> {
        return callApi { service.getOrderReviewCategories(screenRating) }
    }

    override suspend fun cancelReview(shipmentId: Long): ResponseResult<GeneralResponse> {
        return callApi { service.cancelReview(shipmentId) }
    }

    override suspend fun cardTopUpRequest(
        amount: Double,
        paymentType: String
    ): ResponseResult<CardPaymentDetails> {
        return callApi {
            service.cardTopUpRequest(CardTopUpRequest(amount, paymentType))
        }
    }

    override suspend fun getAddressMapPolygon(): ResponseResult<MapPolygonResponse> {
        return callApi { service.getAddressMapPolygon() }
    }

    override suspend fun deleteCustomer(): ResponseResult<Unit> {
        return callApi { service.deleteCustomer(sharedPreferencesManager.getUserId()) }
    }

    override suspend fun getPoweredByBanners(sponsorType: String): ResponseResult<PoweredByResponse> {
        return callApi {
            service.getPoweredByBanners(sponsorType)
        }
    }

}