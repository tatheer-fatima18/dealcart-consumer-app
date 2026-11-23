package com.localpk.app.utils

import com.localpk.app.base.BUILD_FLAVOR
import com.localpk.app.base.BUILD_TYPE
import com.localpk.app.features.address.data.UserAddress
import com.localpk.app.features.cart.data.CartUiData
import com.localpk.app.features.cart.data.ProductInfo
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.utils.EventConstants.ADD_TO_CART
import com.localpk.app.utils.EventConstants.APP_LAUNCHED
import com.localpk.app.utils.EventConstants.ATC_FOOD_PRODUCT
import com.localpk.app.utils.EventConstants.BANNER_CLICK
import com.localpk.app.utils.EventConstants.CLICK_CHECKOUT
import com.localpk.app.utils.EventConstants.CLICK_CHECKOUT_FOOD
import com.localpk.app.utils.EventConstants.CLICK_CONFIRM_ORDER_FOOD
import com.localpk.app.utils.EventConstants.CLICK_FOOD_PRODUCT
import com.localpk.app.utils.EventConstants.CLICK_PROMOTIONAL_CATEGORY
import com.localpk.app.utils.EventConstants.CLICK_RESTAURANT
import com.localpk.app.utils.EventConstants.CLICK_RESTAURANT_CATEGORY
import com.localpk.app.utils.EventConstants.CONFIRM_ORDER_BUTTON
import com.localpk.app.utils.EventConstants.CURRENCY
import com.localpk.app.utils.EventConstants.EVENT_PRODUCT_CLICKED
import com.localpk.app.utils.EventConstants.HOME_SCREEN
import com.localpk.app.utils.EventConstants.ITEMS
import com.localpk.app.utils.EventConstants.ITEM_ID
import com.localpk.app.utils.EventConstants.ITEM_NAME
import com.localpk.app.utils.EventConstants.LOGIN_BUTTON_CLICK
import com.localpk.app.utils.EventConstants.Logout
import com.localpk.app.utils.EventConstants.MO_CLICK_CONFIRM_ORDER
import com.localpk.app.utils.EventConstants.ORDER_CONFIRM_SCREEN
import com.localpk.app.utils.EventConstants.OTP_FAILURE
import com.localpk.app.utils.EventConstants.OTP_SCREEN
import com.localpk.app.utils.EventConstants.OTP_SUBMIT_BUTTON
import com.localpk.app.utils.EventConstants.OTP_SUBMIT_BUTTON_CLICK
import com.localpk.app.utils.EventConstants.OTP_SUCCESS
import com.localpk.app.utils.EventConstants.PLAY_BIRD
import com.localpk.app.utils.EventConstants.PLAY_CRICKET
import com.localpk.app.utils.EventConstants.PLAY_DC_CRUSH
import com.localpk.app.utils.EventConstants.PLAY_DC_MIX_GAME
import com.localpk.app.utils.EventConstants.PLAY_SPIN_WHEEL
import com.localpk.app.utils.EventConstants.PRICE
import com.localpk.app.utils.EventConstants.PRODUCT_DETAIL
import com.localpk.app.utils.EventConstants.QUANTITY
import com.localpk.app.utils.EventConstants.SEARCH_CLICK
import com.localpk.app.utils.EventConstants.SEARCH_INTERACTION
import com.localpk.app.utils.EventConstants.SIGN_UP_BUTTON_CLICK
import com.localpk.app.utils.EventConstants.SINGULAR_ADD_TO_CART
import com.localpk.app.utils.EventConstants.SINGULAR_CART_REMOVE_PRODUCT
import com.localpk.app.utils.EventConstants.SINGULAR_CLICK_VIEW_ORDERS
import com.localpk.app.utils.EventConstants.SINGULAR_CONTENT_LIST
import com.localpk.app.utils.EventConstants.SINGULAR_CONTENT_VIEW
import com.localpk.app.utils.EventConstants.SINGULAR_FIRST_PURCHASE
import com.localpk.app.utils.EventConstants.SINGULAR_LOGIN
import com.localpk.app.utils.EventConstants.SINGULAR_PURCHASE
import com.localpk.app.utils.EventConstants.SINGULAR_REGISTRATION
import com.localpk.app.utils.EventConstants.SINGULAR_SEARCH
import com.localpk.app.utils.EventConstants.Sign_up_Screen
import com.localpk.app.utils.EventConstants.VALUE
import com.localpk.app.utils.EventConstants.VIEW_CATEGORY
import com.localpk.app.utils.EventConstants.VIEW_CUISINES
import com.localpk.app.utils.EventConstants.VIEW_FOOD_CART
import com.localpk.app.utils.EventConstants.VIEW_SUPER_PARENT
import com.localpk.app.utils.EventConstants.VIEW_SUPER_PARENT_CATEGORY
import com.localpk.app.utils.EventConstants.VIEW_WALLET
import com.localpk.app.utils.EventConstants.Whatsapp_Help_Button
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics
import dev.gitlive.firebase.analytics.logEvent
import org.koin.core.component.KoinComponent
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.koin.core.component.inject
import kotlin.toString


interface EventLogger {
    fun logUserInfo(id: String, name: String, phoneNumber: String)
    fun signupScreen(source: String)     //Landing
    fun signupButton(phoneNumber: String)     //Button Click	Yes [Name, Phone Number & Location]
    fun otpScreen(name: String)     //Landing
    fun otpSubmitButton(status: String, otp: String)     //Button Click
    fun otpSuccess()     //Landing
    fun otpFailure(message: String)     //Landing
    fun homePage(
        isLoggedIn: Boolean, id: Int?, username: String?, phoneNumber: String?
    )     //Landing

    fun homeCategoryClick(name: String, id: Long, sort: Double?)     //Landing 	Yes [Category Name]
    fun productCategoryClick(name: String)     //Landing 	Yes [Category Name]
    fun productSubCategoryClick(name: String, id: Int?)     //Landing 	Yes [Category Name]
    fun productClicked(
        data: ProductUiData, productCategory: String, subCategoryName: String, source: String
    )    //Product Click 	Yes [Name, Group Price, Individual Price]

    fun viewCategory(categoryId: String, categoryName: String) // Product List Screen

    fun deleteItemFromCart(productData: ProductInfo)     //Button Click	Yes [Deleted Product]
    fun confirmOrder(
        cart: CartUiData?,
        orderId: Long,
        isSavyour: Boolean,
        packagingFee: Int?,
        platformFee: Int?,
        categories: String?

    )     //Button Click	Yes [Cart Value, Products]

    fun bannerClick(
        type: String, id: Long, redirectType: String, redirectURL: String, matchName: String
    )     //Click	Yes [Banner Data]

    fun orderConfirmScreen(source: String)
    fun WhatsappHelpButton()     //Button Click
    fun MyOrdersPage()     //Orders Page
    fun LogoutButton()     //Click
    fun confirmAddAddress(userAddress: UserAddress)
    fun beginCheckout(cartTotal: Double)
    fun purchaseSuccess(data: CartUiData?, totalAmount: Int)
    fun firstPurchaseSuccess(data: CartUiData?, totalAmount: Int)
    fun search(searchString: String, success: Boolean)
    fun onUserRegistration(success: Boolean)
    fun onUserLogin(success: Boolean)

    fun updateEmailAddress(email: String, userId: Long)
    fun updateDateOfBirth(dob: String, userId: Long)
    fun updateName(name: String)
    fun clickNonGroceryCategoryTile(
        userID: Long, categoryId: String, categoryName: String, destinationURL: String
    )

    fun selectBirdGameHome()
    fun selectCricketGameHome()
    fun selectSpinTheWheelGameHome()
    fun clickSpinTheWheelRewards()
    fun clickDcCricketRewards()
    fun selectDCMixGame()
    fun selectDCCrush()
    fun searchBarClick()
    fun clickUnSelectedSuperParentCategory(categoryName: String, categoryId: String)
    fun clickSelectedSuperParentCategory(categoryName: String)

    fun clickPromotionalCategory(source: String, category: String)

    fun clickOlpyChaseGame()
    fun viewWallet()

    fun addToCartMergn(
        productId: Long,
        productName: String,
        quantity: Int,
        price: Double,
        source: String,
        type: String
    )

    fun clickAllSuperParent(isFood: Boolean)
    fun viewCuisines(cuisineNames: List<String>?)
    fun clickRestaurant(restaurantName: String, source: String)
    fun clickRestaurantCategory(restaurantName: String, categoryName: String)
    fun clickFoodCheckout()
    fun clickFoodProduct(
        id: Long,
        name: String,
        image: String,
        listPrice: Double,
        discountPrice: Double,
        restaurantName: String,
        variations: List<String?>?,
        categoryName: String
    )

    fun addFoodToCart(
        id: Long,
        name: String,
        discountPrice: Double,
        categoryName: String,
        restaurantName: String,
        variations: Long?,
        source: String,
        quantity: Int
    )

    fun viewFoodCart(source: String)
    fun foodConfirmOrder(foodItems: List<String>?, id: Long, totalItemsAmount: Int)
}

class DefaultEventLogger : EventLogger, KoinComponent {

    private val sharedPreferencesManager: SharedPreferencesManager by inject()

    private fun logMergn(eventName: String, params: Map<String, String>? = null) {
        if (BUILD_TYPE == BUILD_TYPE_RELEASE && BUILD_FLAVOR == BUILD_FLAVOUR_PROD) {
            try {
                if (params != null) {
                    EventUtils.trackEvent(eventName, params)
                } else {
                    EventUtils.trackEvent(eventName, emptyMap())
                }
            } catch (e: Exception) {
                // Log error but don't crash the app
                println("Error tracking MERGN event: $eventName - ${e.message}")
            }
        }
    }

    private fun logMergnAttribute(attributeName: String, attributeValue: String) {
        if (BUILD_TYPE == BUILD_TYPE_RELEASE && BUILD_FLAVOR == BUILD_FLAVOUR_PROD) {
            try {
                EventUtils.sendAttribute(attributeName, attributeValue)
            } catch (e: Exception) {
                println("Error sending MERGN attribute: $attributeName - ${e.message}")
            }
        }
    }

    private fun logMergnIdentification(identity: String) {
        if (BUILD_TYPE == BUILD_TYPE_RELEASE && BUILD_FLAVOR == BUILD_FLAVOUR_PROD) {
            try {
                EventUtils.postIdentification(identity)
            } catch (e: Exception) {
                println("Error posting MERGN identification: $identity - ${e.message}")
            }
        }
    }

    private fun logSingularEvent(eventName: String, params: Map<String, Any>? = null) {
        if (BUILD_TYPE == BUILD_TYPE_RELEASE && BUILD_FLAVOR == BUILD_FLAVOUR_PROD) {
            try {
                if (params != null) {
                    EventUtils.trackSingularEvent(eventName, params)
                } else {
                    EventUtils.trackSingularEvent(eventName, emptyMap())
                }
            } catch (e: Exception) {
                // Log error but don't crash the app
                println("Error tracking Singular event: $eventName - ${e.message}")
            }
        }
    }

    private fun logSingularCustomUserId(userId: String) {
        if (BUILD_TYPE == BUILD_TYPE_RELEASE && BUILD_FLAVOR == BUILD_FLAVOUR_PROD) {
            try {
                EventUtils.setSingularCustomUserId(userId)
            } catch (e: Exception) {
                println("Error setting Singular custom user ID: $userId - ${e.message}")
            }
        }
    }

    private fun logSingularUnsetCustomUserId() {
        if (BUILD_TYPE == BUILD_TYPE_RELEASE && BUILD_FLAVOR == BUILD_FLAVOUR_PROD) {
            try {
                EventUtils.unsetSingularCustomUserId()
            } catch (e: Exception) {
                println("Error unsetting Singular custom user ID: ${e.message}")
            }
        }
    }

    private fun logSingularCustomRevenue(
        eventName: String,
        currency: String,
        amount: Double,
        params: Map<String, Any>? = null
    ) {
        if (BUILD_TYPE == BUILD_TYPE_RELEASE && BUILD_FLAVOR == BUILD_FLAVOUR_PROD) {
            try {
                if (params != null) {
                    EventUtils.trackSingularCustomRevenue(eventName, currency, amount, params)
                } else {
                    EventUtils.trackSingularCustomRevenue(eventName, currency, amount, emptyMap())
                }
            } catch (e: Exception) {
                // Log error but don't crash the app
                println("Error tracking Singular custom revenue: $eventName - ${e.message}")
            }
        }
    }

    private fun logFirebase(event: String, params: Map<String, Any>? = null) {
        Firebase.analytics.logEvent(event) {
            params?.forEach { (key, value) ->
                when (value) {
                    null -> { /* skip nulls */
                    }

                    is String -> param(key, value)
                    is Long -> param(key, value)
                    is Int -> param(key, value.toLong())
                    is Double -> param(key, value)
                    is Float -> param(key, value.toDouble())
                    is Boolean -> param(key, if (value) 1L else 0L) // GA expects numeric
                    else -> param(key, value.toString())
                }
            }
        }
    }

    override fun logUserInfo(id: String, name: String, phoneNumber: String) {
        Firebase.analytics.setUserId(id)
        Firebase.analytics.setUserProperty("name", name)
        Firebase.analytics.setUserProperty("phone_number", phoneNumber)
        logSingularCustomUserId(id)
        logMergnIdentification(phoneNumber)
        logMergnAttribute("Mobile", phoneNumber)
        logMergnAttribute("Full-Name", name)

    }

    override fun signupScreen(source: String) {
        logFirebase(Sign_up_Screen)
        logMergn(Sign_up_Screen, mutableMapOf("source" to source))
    }

    override fun signupButton(phoneNumber: String) {
        val params = mapOf("phone_number" to phoneNumber)
        logFirebase(event = SIGN_UP_BUTTON_CLICK, params = params)
        logMergn(LOGIN_BUTTON_CLICK, mutableMapOf("phone_number" to phoneNumber))
    }

    override fun otpScreen(name: String) {
        val params = mapOf("name" to name)
        logFirebase(OTP_SCREEN, params)
    }

    override fun otpSubmitButton(name: String, otp: String) {
        val params = mapOf(
            "name" to name, "otp" to otp
        )
        logFirebase(OTP_SUBMIT_BUTTON, params)
    }

    override fun otpSuccess() {
        logFirebase(OTP_SUCCESS)
        logMergn(OTP_SUBMIT_BUTTON_CLICK, mutableMapOf("status" to OTP_SUCCESS))
    }

    override fun otpFailure(message: String) {
        logFirebase(OTP_FAILURE)
        logMergn(OTP_SUBMIT_BUTTON_CLICK, mutableMapOf("status" to OTP_FAILURE))
    }

    override fun homePage(isLoggedIn: Boolean, id: Int?, username: String?, phoneNumber: String?) {
        /* var userId = "GUEST_" + getAndroidId()
        var userType = "New"
        var userName = "Guest"
        var number = "N/A"
        if (isLoggedIn) {
            userId = id.toString()
            userType = "Existing"
            userName = username.toString()
            number = phoneNumber.toString()
            MoEAnalyticsHelper.setUniqueId(context, userId)

            EventManager().login(number, context = context)
            AttributeManager().sendAttribute(context, "Mobile", number)
//            AttributeManager().sendAttribute(context, "Name", userName)
            AttributeManager().sendAttribute(context, "Full-Name", userName)


        }
        MoEAnalyticsHelper.setUserName(context, userName)
        MoEAnalyticsHelper.setMobileNumber(context, number)

        val properties = Properties()
        properties.addAttribute("status", "complete").addAttribute("userType", userType)


        logSingularEvent(HOME_SCREEN, JSONObject())*/

        logSingularEvent(HOME_SCREEN, mapOf())
        logFirebase(APP_LAUNCHED)


        logMergnAttribute("Mobile", phoneNumber.default())
        logMergnAttribute("Name", username.default())
        logMergnAttribute("Full-Name", username.default())

    }

    override fun homeCategoryClick(name: String, id: Long, sort: Double?) {
        logMergn(VIEW_CATEGORY, mutableMapOf("name" to name))

    }

    override fun productCategoryClick(name: String) {
        val params = mapOf("name" to name)
        logFirebase(VIEW_CATEGORY, params)
    }

    override fun productSubCategoryClick(name: String, id: Int?) {
        logSingularEvent(SINGULAR_CONTENT_LIST, mapOf("sub_category" to name))
    }

    override fun productClicked(
        data: ProductUiData, productCategory: String, subCategoryName: String, source: String
    ) {
        val params = mapOf(
            "item_id" to data.id,
            "item_name" to data.name,
            "individual_price" to data.finalPrice,
            "discount" to data.discountedPrice,
            "item_category" to productCategory,
            "currency" to "Rs",
            "source" to source,
            "content_id" to data.id,
            "fb_content_type" to "product",
            "content_type" to "product",
            "em" to sharedPreferencesManager.getEmail(),
            "fn" to sharedPreferencesManager.getUserName(),
            "ph" to sharedPreferencesManager.getPhoneNumber(),
            "external_id" to sharedPreferencesManager.getUserId(),
            "gn" to sharedPreferencesManager.getGender(),
            "country" to "pk",
        )

        logSingularEvent(SINGULAR_CONTENT_VIEW, params)

        logMergn(
            eventName = EVENT_PRODUCT_CLICKED, params = mutableMapOf(
                "item_id" to data.id.toString(),
                "item_name" to data.name,
                "item_image" to data.imageUrl.default(),
                "price" to data.finalPrice.toString(),
                "discount" to data.discountedPrice.toString(),
                "parent_category" to productCategory,
                "sub_category" to subCategoryName,
                "source" to source
            )
        )

        logFirebase(
            event = PRODUCT_DETAIL, params = mutableMapOf(
                "item_id" to data.id,
                "item_image" to data.imageUrl.default(),
                "item_name" to data.name,
                "price" to data.finalPrice.toString(),
                "discount" to data.discountedPrice.toString(),
                "parent_category" to productCategory,
                "sub_category" to subCategoryName,
                "source" to source
            )
        )

    }

    override fun orderConfirmScreen(source: String) {
        val params = mapOf("source" to source)
        logFirebase(ORDER_CONFIRM_SCREEN, params)
    }

    override fun deleteItemFromCart(
        productData: ProductInfo
    ) {

        val item = mapOf(
            ITEM_ID to productData.productId.toString(),
            ITEM_NAME to productData.productName,
            PRICE to productData.price,
            CURRENCY to "PKR",
            QUANTITY to productData.quantity
        )


        val params = mapOf(
            CURRENCY to "PKR",
            VALUE to (productData.price),
            ITEMS to item
        )

        logSingularEvent(SINGULAR_CART_REMOVE_PRODUCT, params)
    }

    override fun confirmOrder(
        cart: CartUiData?,
        orderId: Long,
        isSavyour: Boolean,
        packagingFee: Int?,
        platformFee: Int?,
        categories: String?
    ) {
        val params = mapOf(
                "discountTotal" to cart?.discountTotal.toString(),
            "totalPrice" to cart?.cartTotal.toString(),
            "order_id" to orderId.toString(),
            "dc_items" to cart?.dcCityCart?.cartDetails?.map { it.productName }.toString(),
            "multi_city_items" to cart?.multiCityCart?.cartDetails?.map { it.productName }
                .toString(),
        )
        logFirebase(CONFIRM_ORDER_BUTTON, params = params)

        logMergn(MO_CLICK_CONFIRM_ORDER,
            mutableMapOf(
                "discountTotal" to cart?.discountTotal.toString(),
                "totalPrice" to cart?.cartTotal.toString(),
                "order_id" to orderId.toString(),
                "dc_items" to cart?.dcCityCart?.cartDetails?.map { it.productName }.toString(),
                "multi_city_items" to cart?.multiCityCart?.cartDetails?.map { it.productName }
                    .toString(),
            ))
    }

    override fun WhatsappHelpButton() {
        logFirebase(Whatsapp_Help_Button)
    }

    override fun MyOrdersPage() {
        logSingularEvent(SINGULAR_CLICK_VIEW_ORDERS)
    }

    override fun LogoutButton() {
        logMergn(Logout)
        logSingularUnsetCustomUserId()
    }

    override fun bannerClick(
        type: String, id: Long, redirectType: String, redirectURL: String, matchName: String
    ) {
        val params = mapOf(
            "bannerId" to id,
            "redirectType" to redirectType,
            "redirectURL" to redirectURL,
            "type" to type,
            "matchName" to matchName
        )
        logFirebase(BANNER_CLICK, params)

        logMergn(
            BANNER_CLICK, mutableMapOf(
                "bannerId" to id.toString(),
                "redirectType" to redirectType,
                "redirectURL" to redirectURL,
                "type" to type,
                "matchName" to matchName
            )
        )
    }

    override fun confirmAddAddress(userAddress: UserAddress) {
        logMergnAttribute("Area", userAddress.area)
    }

    override fun viewCategory(categoryId: String, categoryName: String) {
        val params = mapOf(
            "category_name" to categoryName, "category_id" to categoryId
        )
        logSingularEvent(SINGULAR_CONTENT_LIST, params)
    }

    override fun beginCheckout(cartTotal: Double) {
        logFirebase(CLICK_CHECKOUT)
        logMergn(CLICK_CHECKOUT, mutableMapOf())
    }

    override fun purchaseSuccess(cartData: CartUiData?, totalAmount: Int) {
        val fbContent = buildJsonArray {
            cartData?.dcCityCart?.cartDetails?.forEach {
                add(buildJsonObject {
                    put("id", it.productId.toString())
                    put("quantity", it.quantity)
                })
            }
            cartData?.multiCityCart?.cartDetails?.forEach {
                add(buildJsonObject {
                    put("id", it.productId.toString())
                    put("quantity", it.quantity)
                })
            }
        }

        val data = mapOf(
            "fb_content_type" to "product",
            "fb_currency" to "PKR",
            "fb_content" to fbContent.toString(),
            "OrderTotal" to totalAmount.toString(),
            "em" to sharedPreferencesManager.getEmail(),
            "fn" to sharedPreferencesManager.getUserName(),
            "ph" to sharedPreferencesManager.getPhoneNumber(),
            "external_id" to sharedPreferencesManager.getUserId().toString(),
            "gn" to sharedPreferencesManager.getGender(),
            "country" to "pk"
        )
        logSingularCustomRevenue(SINGULAR_PURCHASE, "PKR", totalAmount.toDouble(), data)

    }

    override fun firstPurchaseSuccess(cartData: CartUiData?, totalAmount: Int) {
        val fbContent = buildJsonArray {
            cartData?.dcCityCart?.cartDetails?.forEach {
                add(buildJsonObject {
                    put("id", it.productId.toString())
                    put("quantity", it.quantity)
                })
            }
            cartData?.multiCityCart?.cartDetails?.forEach {
                add(buildJsonObject {
                    put("id", it.productId.toString())
                    put("quantity", it.quantity)
                })
            }
        }

        val data = mapOf(
            "fb_content_type" to "product",
            "fb_currency" to "PKR",
            "fb_content" to fbContent.toString(),
            "OrderTotal" to totalAmount.toString(),
            "em" to sharedPreferencesManager.getEmail(),
            "fn" to sharedPreferencesManager.getUserName(),
            "ph" to sharedPreferencesManager.getPhoneNumber(),
            "external_id" to sharedPreferencesManager.getUserId().toString(),
            "gn" to sharedPreferencesManager.getGender(),
            "country" to "pk"
        )
        logSingularCustomRevenue(SINGULAR_FIRST_PURCHASE, "PKR", totalAmount.toDouble(), data)
    }

    override fun search(searchString: String, success: Boolean) {
        logSingularEvent(SINGULAR_SEARCH, mapOf("search_term" to searchString))
        logFirebase(
            SEARCH_CLICK, mapOf(
                "search_term" to searchString, "success" to if (success) 1 else 0
            )
        )
    }

    override fun onUserRegistration(success: Boolean) {
        logSingularEvent(
            SINGULAR_REGISTRATION,
            mapOf("sng_attr_registration_method" to if (success) "success" else "error")
        )
    }

    override fun onUserLogin(success: Boolean) {
        logSingularEvent(
            SINGULAR_LOGIN, mapOf("status" to if (success) "success" else "error")
        )
    }

    override fun updateDateOfBirth(dob: String, userId: Long) {
        logMergnAttribute("D.O.B", dob)
    }

    override fun updateName(name: String) {
        logMergnAttribute("Full-Name", name)
    }

    override fun updateEmailAddress(email: String, userId: Long) {
        logMergnAttribute("Email", email)
    }

    override fun clickNonGroceryCategoryTile(
        userID: Long, categoryId: String, categoryName: String, destinationURL: String
    ) {
        logMergn(VIEW_CATEGORY, mutableMapOf("name" to categoryName))
    }

    override fun selectBirdGameHome() {
        logMergn(PLAY_BIRD, mutableMapOf())
    }

    override fun selectCricketGameHome() {
        logMergn(PLAY_CRICKET, mutableMapOf())
    }

    override fun selectSpinTheWheelGameHome() {
        logMergn(PLAY_SPIN_WHEEL, mutableMapOf())
    }

    override fun clickSpinTheWheelRewards() {
        logMergn(PLAY_SPIN_WHEEL, mutableMapOf())
    }

    override fun clickDcCricketRewards() {
        logMergn(PLAY_CRICKET, mutableMapOf())
    }

    override fun selectDCMixGame() {
        logMergn(PLAY_DC_MIX_GAME, mutableMapOf())
    }

    override fun selectDCCrush() {
        logMergn(PLAY_DC_CRUSH, mutableMapOf())
    }

    override fun searchBarClick() {
        logFirebase(SEARCH_INTERACTION)
    }

    override fun clickUnSelectedSuperParentCategory(categoryName: String, categoryId: String) {
        logMergn(
            VIEW_SUPER_PARENT_CATEGORY, mutableMapOf(
                "Super Parent Name" to categoryName
            )
        )

        logFirebase(
            VIEW_SUPER_PARENT, mutableMapOf(
                "Category Name" to categoryName,
                "Category ID" to categoryId
            )
        )
    }

    override fun clickSelectedSuperParentCategory(categoryName: String) {
        logMergn(
            VIEW_SUPER_PARENT_CATEGORY, mutableMapOf(
                "Super Parent Name" to "All"
            )
        )

        logFirebase(
            VIEW_SUPER_PARENT, mutableMapOf(
                "Category Name" to "All"
            )
        )
    }

    override fun clickPromotionalCategory(source: String, category: String) {

        val params = mapOf(
            "source" to source,
            "category" to category,
        )

        logFirebase(CLICK_PROMOTIONAL_CATEGORY, params)
        logMergn(VIEW_CATEGORY, mutableMapOf())

    }

    override fun clickOlpyChaseGame() {
        //   logMoEngage(MO_CLICK_OLPY_CHASE_REWARDS, Properties())
        logMergn(PLAY_DC_MIX_GAME, mutableMapOf())
    }

    override fun viewWallet() {
        logFirebase(VIEW_WALLET)
    }

    override fun addToCartMergn(
        productId: Long,
        productName: String,
        quantity: Int,
        price: Double,
        source: String,
        type: String
    ) {
        logMergn(
            ADD_TO_CART, mutableMapOf(
                "item_id" to productId.toString(),
                "item_name" to productName,
                "price" to price.toString(),
                "currency" to "PKR",
                "quantity" to quantity.toString(),
                "source" to source
            )
        )

        logFirebase(
            ADD_TO_CART, mutableMapOf(
                "item_id" to productId.toString(),
                "item_name" to productName,
                "price" to price.toString(),
                "currency" to "PKR",
                "quantity" to quantity.toString(),
                "source" to source,
                "type" to type
            )
        )

        val fbContent = buildJsonArray {
            add(buildJsonObject {
                put("id", productId.toString())
                put("quantity", quantity)
            })
        }

        // Build the event payload including advanced matching parameters
        val fbparams = mapOf(
            "fb_content_type" to "product",
            "fb_content" to fbContent.toString(),
            "em" to sharedPreferencesManager.getEmail(),
            "fn" to sharedPreferencesManager.getUserName(),
            "pn" to sharedPreferencesManager.getPhoneNumber(),
            "external_id" to sharedPreferencesManager.getUserId().toString(),
            "ge" to sharedPreferencesManager.getGender(),
            "country" to "pk"
        )

        // Send the event data to Singular for Meta tracking
        logSingularEvent("Add to cart for Meta", fbparams)
        logSingularEvent(SINGULAR_ADD_TO_CART, fbparams)
    }

    override fun clickAllSuperParent(isFood: Boolean) {
        val param = if (isFood) "Food" else "All"
        logMergn(
            VIEW_SUPER_PARENT_CATEGORY, mutableMapOf(
                "Super Parent Name" to param
            )
        )
        logFirebase(
            VIEW_SUPER_PARENT, mutableMapOf(
                "Category Name" to param
            )
        )
    }

    override fun viewCuisines(cuisineNames: List<String>?) {
        logMergn(
            VIEW_CUISINES, mutableMapOf(
                "Cuisine Name" to JsonUtils.listToJsonArray(cuisineNames)
            )
        )
    }

    override fun clickRestaurant(restaurantName: String, source: String) {
        logMergn(
            CLICK_RESTAURANT, mutableMapOf(
                "Restaurant Name" to restaurantName,
                "source" to source
            )
        )
    }

    override fun clickRestaurantCategory(restaurantName: String, categoryName: String) {
        logMergn(
            CLICK_RESTAURANT_CATEGORY, mutableMapOf(
                "Restaurant Name" to restaurantName,
                "Category Name" to categoryName
            )
        )
    }

    override fun clickFoodCheckout() {
        logMergn(CLICK_CHECKOUT_FOOD, mutableMapOf())
    }

    override fun clickFoodProduct(
        id: Long,
        name: String,
        image: String,
        listPrice: Double,
        discountPrice: Double,
        restaurantName: String,
        variations: List<String?>?,
        categoryName: String
    ) {
        logMergn(
            CLICK_FOOD_PRODUCT, mutableMapOf(
                "Restaurant Name" to restaurantName,
                "Item Id" to id.toString(),
                "Item Name" to name,
                "Item Image" to image,
                "Item Price" to listPrice.toString(),
                "Item discounted Price" to discountPrice.toString(),
                "Category Name" to categoryName,
                "Variant" to JsonUtils.listToJsonArray(variations?.filterNotNull())
            )
        )
    }

    override fun addFoodToCart(
        id: Long,
        name: String,
        discountPrice: Double,
        categoryName: String,
        restaurantName: String,
        variations: Long?,
        source: String,
        quantity: Int
    ) {
        logMergn(
            ATC_FOOD_PRODUCT, mutableMapOf(
                "Restaurant Name" to restaurantName,
                "Item Price" to discountPrice.toString(),
                "Category Name" to categoryName,
                "Variant" to variations.toString(),
                "Item Id" to id.toString(),
                "Item Name" to name,
                "source" to source,
                "currency" to "Rs",
                "quantity" to quantity.toString()
            )
        )
    }

    override fun viewFoodCart(source: String) {
        logMergn(VIEW_FOOD_CART, mutableMapOf("source" to source))
    }

    override fun foodConfirmOrder(foodItems: List<String>?, id: Long, totalItemsAmount: Int) {
        logMergn(
            CLICK_CONFIRM_ORDER_FOOD, mutableMapOf(
                "Food Items" to JsonUtils.listToJsonArray(foodItems),
                "Order Id" to id.toString(),
                "Total Price" to totalItemsAmount.toString()
            )
        )
    }
} 