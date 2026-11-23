package com.localpk.app.utils

import cafe.adriel.voyager.navigator.Navigator
import com.localpk.app.features.user_location.data.Geolocation
import com.localpk.app.features.webview.WebViewScreen

const val EMPTY_STRING = ""
const val NEXT_LINE = "\n"
const val ZERO_STRING = "0"
const val ZERO = 0.0
const val ZERO_INT = 0
const val ZERO_LONG = 0L
const val ZERO_FLOAT = 0.0f
const val SPACE = " "
const val SLASH = "/"
const val BIT_DELAY = 200L
const val AUTH_TOKEN = "Authorization"
const val WAREHOUSE = "warehouse"
const val WAREHOUSE_ID = "warehouseId"
const val SELECTED_WAREHOUSE_ID = "selectedWarehouseId"
const val PLATFORM = "platform"
const val ANDROID = "android"
const val APP_ID = "appId"
const val DEFAULT_MAP_ZOOM_LEVEL = 18f
const val PARAM_ARGS_DATA = "argsData"
const val CART_TYPE_GROUP = "group"
const val CART_TYPE_DEAL = "deal"
const val PAGE_LIMIT = 20
const val SEARCH_PAGE_LIMIT = 100
const val PRODUCT_PAGE_LIMIT = 2000
const val BANNER_PAGE_LIMIT = 100
const val PRODUCT_LIMIT_MIN = 15
const val VIEW_ALL_PRODUCT_LIMIT_MIN = 30
const val FIRST_PAGE = 1
const val ZERO_OFFSET = 0
const val LINK_TYP_GROUP = "group"
const val LINK_TYP_REFERRAL = "referralId"
const val LINK_TYPE_CATEGORY = "category"
const val LINK_TYPE_PRODUCT = "product"
const val LINK_TYPE_SUPER_PARENT = "super-parent"
const val LINK_TYPE_CURRENT_ORDERS = "current-order"
const val LINK_TYPE_PAST_ORDERS = "past-order"
const val LINK_TYPE_DELIVERED_ORDERS = "delivered-order"
const val LINK_TYPE_RETURNED_ORDERS = "returned-order"
const val LINK_TYPE_CANCELLED_ORDERS = "cancelled-order"
const val LINK_TYPE_CART = "cart"
const val LINK_TYPE_REWARDS = "rewards"
const val LINK_TYPE_GAMES = "games"
const val LINK_TYPE_CRICKET = "cricket"
const val LINK_TYPE_SPIN_THE_WHEEL = "spinwheel"
const val LINK_TYPE_DEAL_REELS = "dealreels"
const val LINK_TYPE_IN_APP_REVIEW = "in-app-review"
const val LINK_TYPE_IN_APP_COMMENTS = "in-app-comments"
const val LINK_TYPE_HELP_CENTER = "helpcenter"
const val LINK_TYPE_ADD_ADDRESS = "addaddress"
const val LINK_TYPE_IN_APP_STORY = "inappstory"
const val DEEP_LINK_DOMAIN = "https://dealcart.io/app"
const val MAP_WEB_VIEW_URL = "https://dealcart.io/webview/map"
const val PENDING_ORDERS_REDIRECT = "pending_orders"
const val CONFIRMED_ORDERS_REDIRECT = "confirmed_orders"
const val DELIVERED_ORDERS_REDIRECT = "delivered_orders"
const val RETURNED_ORDERS_REDIRECT = "returned_orders"
const val CANCELLED_ORDERS_REDIRECT = "cancelled_orders"
const val PRODUCT_REDIRECT = "product_details"
const val DOLAT_PRODUCT = "product"
const val DOLAT_APP = "user"
const val DOLAT_GROUP = "group"
const val DOLAT = "dolat"
const val UUID_HEADER = "uuid"
const val ANDROID_ID = "androidId"
const val USER_AGENT = "User-Agent"
const val VERSION_CODE = "versionCode"
const val DEVICE_ID = "deviceId"
const val PARAM_CURRENT_ITEM = "argsCurrentItem"
const val HEADER_VERSION_CODE = "versionCode"
const val EXPIRED_FLASH_DEAL = "expired"
const val EXPIRING_FLASH_DEAL = "expiring"
const val FLASH_DEAL = "flash-deal"
const val DC_ORDER_CANCEL = "DealCart ki jaanib se order cancel hua hai"
const val LINK_TYP_VERIFY_EMAIL = "verify-email"
const val KARACHI = "karachi"
const val CONTACTS_PERMISSION_CODE = 1001
const val PICK_CONTACT_REQUEST = 1002
const val LINK_TYPE_PROMOTIONAL = "promotional"
const val WALLET_HISTORY_LIMIT = 10
const val WALLET = "wallet"
const val CHAT = "chat"
const val ORDER_TRACKING = "order_tracking"
const val FOOD = "food"
const val COMMERCIAL = "commercial"
const val SAVINGS_CARD = "savings_card"
const val COINS_CARD = "coins_card"
const val BUNDLE = "bundle"
const val EXTERNAL = "external"
const val CRICKET = "cricket"
const val HOME = "home"
const val CATEGORY = "category"
const val PRODUCT = "product"
const val DEAL = "deal"
const val BUILD_FLAVOUR_PROD = "prod"
const val BUILD_TYPE_RELEASE = "Release"
const val BUILD_TYPE_DEBUG = "Debug"
const val FoodCategoryId = 2414L
const val REFRESH_CART = "refresh_cart"
const val PAYMENT_SUCCESS = "payment_success"
const val PAYMENT_FAILURE = "payment_failure"
const val DEALCART_URL = "dealcart.io"
const val DC_CITY_SELLER_ID = 1L
const val VERSION_CODE_INT = 201
const val APP_STORE_URL = "https://apps.apple.com/us/app/dealcart/id6751234427"
const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.localpk.android"
const val LOCATION_URL_SCHEME = "dealcart://location"
const val MAP_LOADED_URL_SCHEME = "dealcart://mapLoaded"
const val TAG = "MapWebView"


val DEFAULT_COORDINATES = Geolocation(24.820726, 67.141132)

expect fun getDeviceId(): String

const val webChatHtml = """
    <!DOCTYPE html>
    <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <script type="text/javascript" 
                src="https://dealcart.callcenterstudio.com/click2connects/click2connect.js?widget_key=ahRzfm11c3RlcmktaGl6bWV0bGVyaXIhCxIUQ2xpY2syQ29ubmVjdFBhY2thZ2UYgICKsdKajQoMogEdZGVhbGNhcnQuY2FsbGNlbnRlcnN0dWRpby5jb20">
            </script>
            <script type="text/javascript">
                window.onload = function() {
                    try {
                         startWidget();
                         _Click2ConnectWidgetMinimise();                        
                    } catch(e) {
                        console.error('Error initializing widget:', e);
                    }
                }
            </script>
        </head>
        <body style="margin:0; padding:0;">
        </body>
    </html>
"""

/**
 * Utility function to navigate to web chat screen with consistent configuration
 */
fun navigateToWebChat(
    navigator: Navigator
) {
    navigator.push(
        WebViewScreen(
            url = "https://dealcart.callcenterstudio.com",
            htmlContent = webChatHtml.trimIndent(),
            isWebChat = true
        )
    )
}
