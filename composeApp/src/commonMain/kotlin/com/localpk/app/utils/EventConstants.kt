package com.localpk.app.utils

object EventConstants {

    //Sign up/ Sign in
    const val Sign_up_Screen = "view_sign_up"
    const val SIGN_UP_BUTTON_CLICK = "sign_up"

    const val OTP_SCREEN = "view_otp"
    const val OTP_SUBMIT_BUTTON = "otp"

    const val OTP_SUCCESS = "otp_success"
    const val OTP_FAILURE = "otp_failure"

    //Home
    const val BANNER_CLICK = "banner_click"
    const val HOME_SCREEN = "view_home"
    const val APP_LAUNCHED = "app_launched"
    const val HOME_CATEGORY_CLICK = "click_category"
    const val PRODUCT_CATEGORY_CLICK = "click_category"
    const val SEARCH_CLICK = "Search"
    const val VIEW_CATEGORY = "view_category"
    const val CLICK_CATEGORY_CATEGORIES = "click_category_categories"
    const val SEARCH_INTERACTION = "search_interaction"
    const val HOME_SCREEN_DEAL_TILE_CLICK = "home_screen_deal_tile_click"

    //Product Detail
    const val PRODUCT_CLICKED = "select_item"
    const val PRODUCT_CLICKED_OOS = "select_item_out_of_stock"
    const val PRODUCT_DETAIL = "view_item"

    const val ADD_TO_CART = "add_to_cart"
    const val PRODUCT_JOIN_GROUP = "join_group"
    const val PRODUCT_INDIVIDUAL_BUY = "individual_buy"
    const val PRODUCT_CREATE_GROUP = "create_group"


    //Cart
    const val CONFIRM_ORDER_BUTTON = "purchase"
    const val DELETE_ITEM_FROM_CART = "remove_from_cart"
    const val ORDER_CONFIRM_SCREEN = "view_cart"
    const val SELECT_ADDRESS = "add_shipping_info"

    //My Orders
    const val MY_ORDERS_PAGE = "view_myOrders"
    const val SHARE_GROUP_WHATSAPP = "share_group"
    const val SHARE_AND_EARN_WHATSAPP = "share_and_earn_whatsapp"
    const val VIEW_IN_PROGRESS = "view_inprogress"
    const val VIEW_CONFIRMED = "view_confirmed"
    const val VIEW_DELIVERED = "view_delivered"
    const val VIEW_RETURNED = "view_returned"
    const val VIEW_CANCELLED = "view_cancelled"

    const val SELECT_DELIVERED = "select_delivered_order"
    const val SELECT_RETURNED = "select_returned_order"
    const val SELECT_CANCELLED = "select_cancelled_order"

    const val Profile_Button = "Profile Button" // Not Needed
    const val Profile_Screen = "view_profile"

    const val Push_Notification_RECEIVED = "Push Notification"
    const val Push_Notification_CLICKED = "Push Notification"
    const val In_App_Notification_Clicked = "In App Notification"
    const val In_App_Notification_Received = "In App Notification"
    const val Change_Category = "view_category"

    // Referral Screen

    const val VIEW_REFER_WIN = "view_refer&win"

    const val Whatsapp_Help_Button = "click_whatsapp_icon"
    const val REMOVE_ADDRESS = "remove_address"
    const val ADD_ADDRESS = "add_address"
    const val Share_App_And_Win_Button = "share_app"
    const val Logout_Button = "log_out"
    const val Logout = "logout"

    // One Last Step

    const val EXTERNAL_LINK = "External Link"
    const val INTERNAL_PRODUCT = "Internal Product"

    // MoEngage Events

    const val MO_CLICK_CONFIRM_ORDER = "click_confirm_order"

    const val CLICK_PROMOTIONAL_CATEGORY = "click_promotional_category"

    const val VIEW_WALLET = "view_wallet"
    const val VIEW_SUPER_PARENT_CATEGORY = "view_super_parent_category"
    const val VIEW_CUISINES = "view_cuisines"
    const val CLICK_RESTAURANT = "click_restaurant"
    const val CLICK_RESTAURANT_CATEGORY = "click_restaurant_category"
    const val CLICK_FOOD_PRODUCT = "click_food_product"
    const val ATC_FOOD_PRODUCT = "atc_food_product"
    const val VIEW_FOOD_CART = "view_food_cart"
    const val CLICK_CONFIRM_ORDER_FOOD = "click_confirm_order_food"
    const val CLICK_CHECKOUT_FOOD = "click_checkout_food"
    const val CURRENCY = "currency"
    const val VALUE = "value"
    const val ITEM_ID = "item_id"
    const val ITEM_NAME = "item_name"
    const val ITEMS = "items"
    const val QUANTITY = "quantity"
    const val PRICE = "price"

    // SINGULAR EVENT CONSTANTS
    const val SINGULAR_LOGIN = "sng_login"
    const val SINGULAR_CONTENT_LIST = "sng_atr_content_list"
    const val SINGULAR_CONTENT_VIEW = "sng_content_view"
    const val SINGULAR_ADD_TO_CART = "sng_add_to_cart"
    const val SINGULAR_SHARE = "sng_share"
    const val SINGULAR_CART_REMOVE_PRODUCT = "cart_remove_product"
    const val SINGULAR_CLICK_VIEW_ORDERS = "click_view_orders"
    const val SINGULAR_VIEW_ORDERS = "view_order"
    const val SINGULAR_VIEW_CART = "sng_view_cart"
    const val SINGULAR_CHECKOUT_INITIATED = "sng_checkout_initiated"
    const val SINGULAR_PURCHASE = "Purchase_done"
    const val SINGULAR_FIRST_PURCHASE = "sng_first_purchase"
    const val SINGULAR_SEARCH = "sng_search"
    const val SINGULAR_SPEND_CREDITS = "sng_spend_credits"
    const val SINGULAR_USE_PROMO_CODE = "use_promo_code"
    const val SINGULAR_INVITE = "sng_invite"
    const val SINGULAR_ADD_TO_WISHLIST = "sng_add_to_wishlist"
    const val SINGULAR_REGISTRATION = "sng_complete_registration"

    const val VIEW_SUPER_PARENT = "view_super_parent"

    // COMMON
    const val CATEGORY = "category"
    const val PRODUCT = "product"
    const val DEAL = "deal"
    const val EXTERNAL = "external"
    const val CRICKET = "cricket"
    const val HOME = "home"
    const val COMMERCIAL = "commercial"
    const val SAVINGS_CARD = "savings_card"
    const val COINS_CARD = "coins_card"
    const val BUNDLE = "bundle"


    //Mergn
    const val LOGIN_BUTTON_CLICK = "click_login"
    const val OTP_SUBMIT_BUTTON_CLICK = "click_submit_otp"
    const val EVENT_PRODUCT_CLICKED = "product_click"
    const val CLICK_CHECKOUT = "click_checkout"
    const val PLAY_CRICKET = "play_cricket"
    const val PLAY_SPIN_WHEEL = "play_spin_wheel"
    const val PLAY_BIRD = "play_bird"
    const val PLAY_DC_MIX_GAME = "play_dc_mix_game"
    const val PLAY_DC_CRUSH = "play_dc_crush"


}