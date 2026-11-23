package com.localpk.app.firebase.remoteConfig

import com.localpk.app.features.cart.data.GetContributionValue
import com.localpk.app.features.search.data.SearchProductName
import com.localpk.app.features.wallet.data.GetTopUpAmount
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

object RemoteConfigs {
    private var isInitialized = false
    private var isRemoteConfigInitialized = false

    fun initialize() {
        if (isInitialized) {
            return
        }

        try {
            // GitLive Firebase is automatically initialized when the SDK is imported
            // We just need to configure remote config
            initializeRemoteConfig()
            isInitialized = true
        } catch (e: Exception) {
            println("Failed to initialize GitLive Firebase: ${e.message}")
        }
    }

    private fun initializeRemoteConfig() {
        if (isRemoteConfigInitialized) {
            return
        }

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val remoteConfig = Firebase.remoteConfig

                // Configure remote config settings
                remoteConfig.settings {
                    minimumFetchIntervalInSeconds = 0

                }

                // Fetch and activate remote config
                val success = withContext(Dispatchers.Default) {
                    try {
                        remoteConfig.fetchAndActivate()
                    } catch (e: Exception) {
                        println("Failed to fetch and activate remote config: ${e.message}")
                        false
                    }
                }

                if (success) {
                    println("Remote config fetched and activated successfully")
                } else {
                    println("Failed to fetch and activate remote config")
                }

                isRemoteConfigInitialized = true
            } catch (e: Exception) {
                println("Failed to initialize remote config: ${e.message}")
            }
        }
    }

    fun forceRefreshRemoteConfig() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val remoteConfig = Firebase.remoteConfig
                val success = withContext(Dispatchers.Default) {
                    try {
                        remoteConfig.fetchAndActivate()
                    } catch (e: Exception) {
                        false
                    }
                }

            } catch (e: Exception) {
                println("Failed to force refresh remote config: ${e.message}")
            }
        }
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return try {
            Firebase.remoteConfig.getValue(key).asString()
        } catch (e: Exception) {
            println("Failed to get remote config value for key $key: ${e.message}")
            defaultValue
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return try {
            Firebase.remoteConfig.getValue(key).asBoolean()
        } catch (e: Exception) {
            println("Failed to get remote config boolean for key $key: ${e.message}")
            defaultValue
        }
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return try {
            Firebase.remoteConfig.getValue(key).asLong()
        } catch (e: Exception) {
            println("Failed to get remote config int for key $key: ${e.message}")
            defaultValue
        }
    }

    fun getDouble(key: String, defaultValue: Double = 0.0): Double {
        return try {
            Firebase.remoteConfig.getValue(key).asDouble()
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun isFirebaseInitialized(): Boolean = isInitialized

    fun isRemoteConfigReady(): Boolean = isRemoteConfigInitialized

    // Convenience methods using Keys with default values
    fun getForceUpgradeVersionValue() =
        getString(
            Keys.FORCE_UPGRADE_VERSION_VALUE,
            DefaultValues.FORCE_UPGRADE_VERSION_VALUE
        ) // not needed

    fun showProductsNewScreen() =
        getBoolean(
            Keys.IS_PRODUCT_NEW_SCREEN_VALUE,
            DefaultValues.IS_PRODUCT_NEW_SCREEN_VALUE
        ) // [needs to be added in dynamic link]

    fun isForceLocationEnable() =
        getBoolean(Keys.FORCE_LOCATION_ENABLE, DefaultValues.FORCE_LOCATION_ENABLE) // Done

    fun isCricketEnabled() =
        getBoolean(
            Keys.IS_CRICKET_ENABLED,
            DefaultValues.IS_CRICKET_ENABLED
        ) // (feature not implemented in the app)

    fun isInboxEnabled() =
        getBoolean(
            Keys.IS_INBOX_ENABLED,
            DefaultValues.IS_INBOX_ENABLED
        ) // (feature not implemented in the app)

    fun isInboxDetailEnabled() =
        getBoolean(
            Keys.IS_INBOX_DETAIL_ENABLED,
            DefaultValues.IS_INBOX_DETAIL_ENABLED
        ) // (feature not implemented in the app)

    fun getOneLastStepShareButtonOnTopValue() =
        getBoolean(
            Keys.ONE_LAST_STEP_SHARE_BUTTON_ON_TOP_VALUE,
            DefaultValues.ONE_LAST_STEP_SHARE_BUTTON_ON_TOP_VALUE
        ) // not used in original app

    fun isDiscountTypeAbsolute() =
        getBoolean(
            Keys.IS_DISCOUNT_TYPE_ABSOLUTE,
            DefaultValues.IS_DISCOUNT_TYPE_ABSOLUTE
        ) // [needs to be added in event logger]

    fun getIsUxCamEnabled() =
        getBoolean(
            Keys.IS_UX_CAM_ENABLED,
            DefaultValues.IS_UX_CAM_ENABLED
        )  // (feature not implemented in the app)

    fun imsFeatureFlag() = getBoolean(Keys.IMS_FEATURE_FLAG, DefaultValues.IMS_FEATURE_FLAG) // Done
    fun appId() =
        getString(
            Keys.APP_ID,
            DefaultValues.APP_ID
        ) // needed to verify app id to stop  cloning (feature not implemented in the app)

    fun optimiseImage() =
        getBoolean(
            Keys.OPTIMISE_IMAGE,
            DefaultValues.OPTIMISE_IMAGE
        ) // used in banner adapter previously, not needed

    fun getUserAgent() = getString(Keys.USER_AGENT, DefaultValues.USER_AGENT) // done
    fun isEnabledAddToCartTiktok() =
        getBoolean(
            Keys.IS_ENABLED_ADD_TO_CART_TIKTOK,
            DefaultValues.IS_ENABLED_ADD_TO_CART_TIKTOK
        ) // not needed

    fun getBirdGameAuthKey() =
        getString(
            Keys.BIRD_GAME_AUTH_KEY,
            DefaultValues.BIRD_GAME_AUTH_KEY
        ) // (feature not implemented in the app)

    fun isCloningRestricted() =
        getBoolean(
            Keys.IS_CLONING_RESTRICTION_ENABLED,
            DefaultValues.IS_CLONING_RESTRICTION_ENABLED
        ) // (feature not implemented in the app)

    fun isCloningRestrictedDualProfile() =
        getBoolean(
            Keys.IS_CLONING_RESTRICTION_DUAL_PROFILE_ENABLED,
            DefaultValues.IS_CLONING_RESTRICTION_DUAL_PROFILE_ENABLED
        ) // (feature not implemented in the app)

    fun isCloningRestrictedParallelSpace() =
        getBoolean(
            Keys.IS_CLONING_RESTRICTION_PARALLEL_SPACE_ENABLED,
            DefaultValues.IS_CLONING_RESTRICTION_PARALLEL_SPACE_ENABLED
        ) // (feature not implemented in the app)

    fun isPathLoggingEnabled() =
        getBoolean(
            Keys.IS_PATH_LOGGING_ENABLED,
            DefaultValues.IS_PATH_LOGGING_ENABLED
        ) // (feature not implemented in the app)

    fun getOtpProviderCode() =
        getString(
            Keys.OTP_PROVIDER_CODE,
            DefaultValues.OTP_PROVIDER_CODE
        ) // needed for auto filling of OTP (feature not implemented in the app)

    fun isCloningRestrictedApiLevel() =
        getBoolean(
            Keys.IS_CLONING_RESTRICTION_API_LEVEL,
            DefaultValues.IS_CLONING_RESTRICTION_API_LEVEL
        ) // (feature not implemented in the app)

    fun isNewCLData() =
        getBoolean(
            Keys.IS_NEW_CL,
            DefaultValues.IS_NEW_CL
        ) // needed for community leader (feature not implemented in the app)

    fun isFlappyBirdGameEnabled() =
        getBoolean(Keys.FLAPPY_BIRD_GAME_ENABLED, DefaultValues.FLAPPY_BIRD_GAME_ENABLED) // Done

    fun isTraviaGameEnabled() =
        getBoolean(Keys.IS_TRAVIA_GAME_ENABLED, DefaultValues.IS_TRAVIA_GAME_ENABLED) // Done

    fun isDCMixGameEnabled() =
        getBoolean(
            Keys.IS_DC_MIX_GAME_ENABLED,
            DefaultValues.IS_DC_MIX_GAME_ENABLED
        )  // (feature not implemented in the app)

    fun getOffScreenPageLimit() =
        getLong(
            Keys.OFFSCREEN_PAGE_LIMIT,
            DefaultValues.OFFSCREEN_PAGE_LIMIT
        ).toInt() // [need understanding]

    fun getSearchPageLimit() =
        getLong(Keys.SEARCH_PAGE_LIMIT, DefaultValues.SEARCH_PAGE_LIMIT).toInt() // Done

    fun getDeliveryDays(): Int {
        return getLong(Keys.DELIVERY_DAYS, DefaultValues.DELIVERY_DAYS).toInt()
    } // Done

    fun isBannerCacheEnabled() =
        getBoolean(
            Keys.IS_BANNER_CACHE_ENABLED,
            DefaultValues.IS_BANNER_CACHE_ENABLED
        ) // (feature not implemented in the app)

    fun isCategoryCacheEnabled() =
        getBoolean(
            Keys.IS_CATEGORY_CACHE_ENABLED,
            DefaultValues.IS_CATEGORY_CACHE_ENABLED
        ) // (feature not implemented in the app)

    fun isBundleEnabled() =
        getBoolean(
            Keys.IS_BUNDLE_ENABLED,
            DefaultValues.IS_BUNDLE_ENABLED
        ) // (feature not implemented in the app)

    fun isFavouriteEnabled(): Boolean =
        getBoolean(
            Keys.IS_FAVOURITE_ENABLED,
            DefaultValues.IS_FAVOURITE_ENABLED
        ) // (feature not implemented in the app)

    fun isEnabledFlashDealTesting() =
        getBoolean(
            Keys.IS_ENABLED_FLASH_DEAL_TESTING,
            DefaultValues.IS_ENABLED_FLASH_DEAL_TESTING
        ) // (feature not implemented in the app)

    fun isEnabledThinnerSearchbar() =
        getBoolean(
            Keys.IS_ENABLED_THINNER_SEARCHBAR,
            DefaultValues.IS_ENABLED_THINNER_SEARCHBAR
        ) // Done

    fun isShareAndEarnEnabled(): Boolean =
        getBoolean(
            Keys.SHARE_AND_EARN_ENABLED,
            DefaultValues.SHARE_AND_EARN_ENABLED
        ) // (feature not implemented in the app)

    fun isEnabledSearchKeys(): Boolean =
        getBoolean(Keys.IS_ENABLE_SEARCH_KEYS, DefaultValues.IS_ENABLE_SEARCH_KEYS)

    fun isJazzcashEnabled() =
        getBoolean(
            Keys.IS_JAZZCASH_ENABLED,
            DefaultValues.IS_JAZZCASH_ENABLED
        ) // (feature not implemented in the app)

    fun isSuperParentCategoriesEnabled(): Boolean =
        getBoolean(Keys.IS_ENABLED_SUPER_CATEGORY, DefaultValues.IS_ENABLED_SUPER_CATEGORY) // Done

    fun isOptionalFeeEnabled() =
        getBoolean(Keys.OPTIONAL_FEE_ENABLED, DefaultValues.OPTIONAL_FEE_ENABLED) // Done

    fun getContributionTextEnabled() =
        getString(Keys.CONTRIBUTION_TEXT, DefaultValues.CONTRIBUTION_TEXT) // Done

    fun isLocationOutsideServiceAreaAllowed() =
        getBoolean(
            Keys.IS_LOCATION_OUTSIDE_SERVICE_AREA_ALLOWED,
            DefaultValues.IS_LOCATION_OUTSIDE_SERVICE_AREA_ALLOWED
        ) // [investigation needed]

    fun getSplashScreenTimer() =
        getLong(
            Keys.SPLASH_SCREEN_TIMER,
            DefaultValues.SPLASH_SCREEN_TIMER
        ).toInt() // (feature not implemented in the app)

    fun getSearchDelayTime() =
        getLong(Keys.SEARCH_DELAY_TIME, DefaultValues.SEARCH_DELAY_TIME).toInt() // Done

    fun getPackagingFeeAmount(): Int {
        return getLong(Keys.PACKAGING_FEE, DefaultValues.PACKAGING_FEE).toInt()
    } // Done

    fun getPlatformFeeAmount(): Int {
        return getLong(Keys.PLATFORM_FEE, DefaultValues.PLATFORM_FEE).toInt()
    } // Done

    fun getPackagingFeeName(): String {
        return getString(Keys.PACKAGING_FE_NAME, DefaultValues.PACKAGING_FE_NAME)
    } // Done

    fun getPlatformFeeName(): String {
        return getString(Keys.PLATFORM_FEE_NAME, DefaultValues.PLATFORM_FEE_NAME)
    } // Done

    fun isApiRetrievalEnabled() =
        getBoolean(
            Keys.IS_API_RETRIEVAL_ENABLED,
            DefaultValues.IS_API_RETRIEVAL_ENABLED
        ) // [needs understanding]

    fun isShowFashionCategorySection() =
        getBoolean(
            Keys.IS_SHOW_FASHION_CATEGORY_SECTION,
            DefaultValues.IS_SHOW_FASHION_CATEGORY_SECTION
        ) // not needed

    fun isDCCrushGameEnabled() =
        getBoolean(
            Keys.IS_DC_CRUSH_ENABLED,
            DefaultValues.IS_DC_CRUSH_ENABLED
        )  // (feature not implemented in the app)

    fun isCardEnabled() = getBoolean(
        Keys.IS_CARD_ENABLED,
        DefaultValues.IS_CARD_ENABLED
    )

    fun isDCChaseGameEnabled() =
        getBoolean(Keys.IS_DC_CHASE_ENABLED, DefaultValues.IS_DC_CHASE_ENABLED)  // Done

    fun getCardPublicKey(): String {
        return getString(Keys.CARD_PUBLIC_KEY, DefaultValues.CARD_PUBLIC_KEY)
    } // (feature not implemented in the app)

    fun enableDisplayOfDelivery() =
        getBoolean(
            Keys.ENABLE_DISPLAY_OF_DELIVERY,
            DefaultValues.ENABLE_DISPLAY_OF_DELIVERY
        ) // also used in notifications which is not implemented yet

    fun enableDisplayOfParcel() =
        getBoolean(Keys.ENABLE_DISPLAY_OF_PARCEL, DefaultValues.ENABLE_DISPLAY_OF_PARCEL) // Done

    fun getUserType() = getString(Keys.USER_TYPE, DefaultValues.USER_TYPE) // Done
    fun getTournamentName() =
        getString(
            Keys.TOURNAMENT_NAME,
            DefaultValues.TOURNAMENT_NAME
        ) // (feature not implemented in the app)

    fun getDirectionsPublicKey(): String {
        return getString(Keys.DIRECTIONS_PUBLIC_KEY, DefaultValues.DIRECTIONS_PUBLIC_KEY)
    } // Done

    fun getFoodSuperParentId(): Int {
        return getLong(Keys.FOOD_SUPER_PARENT_ID, DefaultValues.FOOD_SUPER_PARENT_ID).toInt()
    } // Done

    fun isEasyPaisaEnabled() = getBoolean(
        Keys.IS_EASYPAISA_ENABLED,
        DefaultValues.IS_EASYPAISA_ENABLED
    )

    fun isTopUpEnabled() = getBoolean(Keys.IS_TOPUP_ENABLED, DefaultValues.IS_TOPUP_ENABLED)

    fun getContributionValues(): List<GetContributionValue> { // Done
        return try {
            val jsonString = getString(Keys.CONTRIBUTION_VALUES, DefaultValues.CONTRIBUTION_VALUES)
            if (jsonString.isNotEmpty()) {
                Json.decodeFromString<List<GetContributionValue>>(jsonString)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


    fun getTopUpValues(): List<GetTopUpAmount> {
        return try {
            val jsonString = getString(Keys.TOP_UP_VALUES, DefaultValues.TOP_UP_VALUES)
            if (jsonString.isNotEmpty()) {
                Json.decodeFromString<List<GetTopUpAmount>>(jsonString)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun searchProductNamesList(): List<SearchProductName> {
        return try {
            val jsonString =
                getString(Keys.SEARCH_PRODUCT_NAMES_LIST, DefaultValues.SEARCH_PRODUCT_NAMES_LIST)
            if (jsonString.isNotEmpty()) {
                Json.decodeFromString<List<SearchProductName>>(jsonString)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun isAccountDeletionEnabled() =
        getBoolean(Keys.IS_ACCOUNT_DELETION_ENABLED, DefaultValues.IS_ACCOUNT_DELETION_ENABLED)

    fun isIosCardEnabled() = getBoolean(
        Keys.IS_IOS_CARD_ENABLED,
        DefaultValues.IS_IOS_CARD_ENABLED
    )

    fun isIosJazzcashEnabled() = getBoolean(
        Keys.IS_IOS_JAZZCASH_ENABLED,
        DefaultValues.IS_IOS_JAZZCASH_ENABLED
    )

    fun isIosEasyPaisaEnabled() = getBoolean(
        Keys.IS_IOS_EASYPAISA_ENABLED,
        DefaultValues.IS_IOS_EASYPAISA_ENABLED
    )

    fun isSameDayDeliveryEnabled() = getBoolean(
        Keys.IS_SAME_DAY_DELIVERY_ENABLED,
        DefaultValues.IS_SAME_DAY_DELIVERY_ENABLED
    )

    fun isForceUpdateEnabled() = getBoolean(
        Keys.IS_FORCE_UPDATE_ENABLED,
        DefaultValues.IS_FORCE_UPDATE_ENABLED
    )

    fun isGroceryRunGameEnabled() =
        getBoolean(Keys.IS_GROCERY_RUN_GAME_ENABLED, DefaultValues.IS_GROCERY_RUN_GAME_ENABLED)

    fun groceryRunGameLink() =
        getString(Keys.GROCERY_RUN_GAME_LINK, DefaultValues.GROCERY_RUN_GAME_LINK)

    fun getSearchPlacesDebounceTime(): Int {
        return getLong(Keys.SEARCH_PLACES_DEBOUNCE_TIME, DefaultValues.SEARCH_PLACES_DEBOUNCE_TIME).toInt()
    }

    fun getSearchPlacesCharacterLimit(): Int {
        return getLong(Keys.SEARCH_PLACES_CHARACTER_LIMIT, DefaultValues.SEARCH_PLACES_CHARACTER_LIMIT).toInt()
    }
}

object Keys {
    const val FORCE_UPGRADE_VERSION_VALUE = "force_upgrade_version_value"
    const val IS_PRODUCT_NEW_SCREEN_VALUE = "product_new_screen_value"
    const val FORCE_LOCATION_ENABLE = "force_location"
    const val ONE_LAST_STEP_SHARE_BUTTON_ON_TOP_VALUE = "one_last_step_share_button_on_top"
    const val IS_DISCOUNT_TYPE_ABSOLUTE = "is_discount_type_absolute"
    const val IS_CRICKET_ENABLED = "enable_cricket"
    const val IS_INBOX_ENABLED = "enable_inbox_new"
    const val IS_INBOX_DETAIL_ENABLED = "enable_inbox_detail"
    const val IMS_FEATURE_FLAG = "ims_feature_flag_consumer_app"
    const val APP_ID = "app_id"
    const val OPTIMISE_IMAGE = "optimise_image"
    const val USER_AGENT = "user_agent"
    const val IS_UX_CAM_ENABLED = "is_uxcam_enabled"
    const val IS_ENABLED_ADD_TO_CART_TIKTOK = "enable_add_to_cart_tiktok"
    const val BIRD_GAME_AUTH_KEY = "bird_game_auth_key"
    const val IS_CLONING_RESTRICTION_ENABLED = "is_cloning_restriction_enabled"
    const val IS_CLONING_RESTRICTION_DUAL_PROFILE_ENABLED =
        "is_cloning_restriction_dual_profile_enabled"
    const val IS_CLONING_RESTRICTION_PARALLEL_SPACE_ENABLED =
        "is_cloning_restriction_parallel_space_enabled"
    const val IS_PATH_LOGGING_ENABLED = "is_path_logging_enable"
    const val OTP_PROVIDER_CODE = "otp_subscriber_text_code"
    const val IS_NEW_CL = "is_CL_new_data"
    const val FLAPPY_BIRD_GAME_ENABLED = "flappy_bird_game_enabled"
    const val OFFSCREEN_PAGE_LIMIT = "offscreen_page_limit"
    const val SEARCH_PAGE_LIMIT = "search_page_size"
    const val DELIVERY_DAYS = "delivery_days"
    const val IS_BANNER_CACHE_ENABLED = "is_banner_cache_enabled"
    const val IS_CATEGORY_CACHE_ENABLED = "is_category_cache_enabled"
    const val IS_CLONING_RESTRICTION_API_LEVEL = "is_cloning_restriction_api_level"
    const val IS_BUNDLE_ENABLED = "is_bundle_enabled"
    const val IS_TRAVIA_GAME_ENABLED = "is_travia_game_enabled"
    const val IS_FAVOURITE_ENABLED = "is_favourite_enabled"
    const val IS_ENABLED_SUPER_CATEGORY = "enable_super_parent_categories"
    const val IS_ENABLED_FLASH_DEAL_TESTING = "enable_flash_deal_testing"
    const val IS_ENABLED_THINNER_SEARCHBAR = "enable_thinner_searchbar"
    const val SHARE_AND_EARN_ENABLED = "share_and_earn_enabled"
    const val SEARCH_PRODUCT_NAMES_LIST = "search_product_names_list"
    const val IS_ENABLE_SEARCH_KEYS = "enabled_search_keys"
    const val IS_JAZZCASH_ENABLED = "is_jazzcash_enabled"
    const val CONTRIBUTION_VALUES = "contribution_values"
    const val OPTIONAL_FEE_ENABLED = "optional_fee_enabled"
    const val CONTRIBUTION_TEXT = "contribution_text"
    const val IS_LOCATION_OUTSIDE_SERVICE_AREA_ALLOWED = "is_location_outside_service_area_allowed"
    const val SPLASH_SCREEN_TIMER = "splash_screen_timer"
    const val PACKAGING_FEE = "packaging_fee"
    const val PLATFORM_FEE = "platform_fee"
    const val PACKAGING_FE_NAME = "packaging_fee_name"
    const val PLATFORM_FEE_NAME = "platform_fee_name"
    const val IS_API_RETRIEVAL_ENABLED = "is_api_retrieval_enabled"
    const val IS_SHOW_FASHION_CATEGORY_SECTION = "is_show_fashion_category_section"
    const val IS_DC_MIX_GAME_ENABLED = "is_dc_mix_game_enabled"
    const val IS_DC_CRUSH_ENABLED = "is_dc_crush_enabled"
    const val SEARCH_DELAY_TIME = "search_delay_time"
    const val IS_CARD_ENABLED = "is_card_enabled"
    const val CARD_PUBLIC_KEY = "card_public_key"
    const val ENABLE_DISPLAY_OF_DELIVERY = "enable_display_of_delivery"
    const val ENABLE_DISPLAY_OF_PARCEL = "enable_display_of_parcel"
    const val USER_TYPE = "user_type"
    const val IS_DC_CHASE_ENABLED = "is_dc_chase_enabled"
    const val TOP_UP_VALUES = "top_up_values"
    const val TOURNAMENT_NAME = "tournament_name"
    const val MAP_SETTINGS = "map_settings"
    const val DIRECTIONS_PUBLIC_KEY = "directions_public_key"
    const val FOOD_SUPER_PARENT_ID = "food_super_parent_id"
    const val IS_EASYPAISA_ENABLED = "is_easypaisa_enabled"
    const val IS_TOPUP_ENABLED = "is_topup_enabled"
    const val IS_ACCOUNT_DELETION_ENABLED = "is_account_deletion_enabled"
    const val IS_IOS_CARD_ENABLED = "is_ios_card_enabled"
    const val IS_IOS_EASYPAISA_ENABLED = "is_ios_easypaisa_enabled"
    const val IS_IOS_JAZZCASH_ENABLED = "is_ios_jazzcash_enabled"
    const val IS_SAME_DAY_DELIVERY_ENABLED = "enable_same_day_delivery"
    const val IS_GROCERY_RUN_GAME_ENABLED = "grocery_run_game_enabled"
    const val GROCERY_RUN_GAME_LINK = "grocery_run_game_link"
    const val IS_FORCE_UPDATE_ENABLED = "is_force_update_enabled"
    const val SEARCH_PLACES_CHARACTER_LIMIT = "search_places_character_limit"
    const val SEARCH_PLACES_DEBOUNCE_TIME = "search_places_debounce_time"
}

object DefaultValues {
    // Boolean defaults
    const val FORCE_UPGRADE_VERSION_VALUE = ""
    const val APP_ID = ""
    const val USER_AGENT = ""
    const val BIRD_GAME_AUTH_KEY = ""
    const val OTP_PROVIDER_CODE = ""
    const val SEARCH_PRODUCT_NAMES_LIST = "[]"
    const val CONTRIBUTION_VALUES = "[]"
    const val CONTRIBUTION_TEXT = ""
    const val PACKAGING_FE_NAME = ""
    const val PLATFORM_FEE_NAME = ""
    const val CARD_PUBLIC_KEY = ""
    const val TOP_UP_VALUES = "[]"
    const val USER_TYPE = ""
    const val TOURNAMENT_NAME = ""
    const val MAP_SETTINGS = "{\"observer\":false,\"mapComponent\":false}"
    const val DIRECTIONS_PUBLIC_KEY = ""

    // Boolean values (default to false for feature flags)
    const val IS_PRODUCT_NEW_SCREEN_VALUE = false
    const val FORCE_LOCATION_ENABLE = false
    const val IS_CRICKET_ENABLED = false
    const val IS_INBOX_ENABLED = false
    const val IS_INBOX_DETAIL_ENABLED = false
    const val ONE_LAST_STEP_SHARE_BUTTON_ON_TOP_VALUE = false
    const val IS_DISCOUNT_TYPE_ABSOLUTE = false
    const val IS_UX_CAM_ENABLED = false
    const val IMS_FEATURE_FLAG = false
    const val OPTIMISE_IMAGE = false
    const val IS_ENABLED_ADD_TO_CART_TIKTOK = false
    const val IS_CLONING_RESTRICTION_ENABLED = false
    const val IS_CLONING_RESTRICTION_DUAL_PROFILE_ENABLED = false
    const val IS_CLONING_RESTRICTION_PARALLEL_SPACE_ENABLED = false
    const val IS_PATH_LOGGING_ENABLED = false
    const val IS_CLONING_RESTRICTION_API_LEVEL = false
    const val IS_NEW_CL = false
    const val FLAPPY_BIRD_GAME_ENABLED = false
    const val IS_TRAVIA_GAME_ENABLED = false
    const val IS_BANNER_CACHE_ENABLED = false
    const val IS_CATEGORY_CACHE_ENABLED = false
    const val IS_BUNDLE_ENABLED = false
    const val IS_FAVOURITE_ENABLED = false
    const val IS_ENABLED_FLASH_DEAL_TESTING = false
    const val IS_ENABLED_THINNER_SEARCHBAR = false
    const val SHARE_AND_EARN_ENABLED = false
    const val IS_ENABLE_SEARCH_KEYS = false
    const val IS_JAZZCASH_ENABLED = false
    const val OPTIONAL_FEE_ENABLED = false
    const val IS_LOCATION_OUTSIDE_SERVICE_AREA_ALLOWED = false
    const val IS_API_RETRIEVAL_ENABLED = false
    const val IS_SHOW_FASHION_CATEGORY_SECTION = false
    const val IS_DC_CRUSH_ENABLED = false
    const val IS_CARD_ENABLED = false
    const val IS_DC_CHASE_ENABLED = false
    const val ENABLE_DISPLAY_OF_DELIVERY = false
    const val ENABLE_DISPLAY_OF_PARCEL = false
    const val IS_EASYPAISA_ENABLED = false
    const val IS_TOPUP_ENABLED = false

    // Integer values
    const val OFFSCREEN_PAGE_LIMIT = 3L
    const val SEARCH_PAGE_LIMIT = 20L
    const val DELIVERY_DAYS = 1L
    const val SPLASH_SCREEN_TIMER = 3000L
    const val SEARCH_DELAY_TIME = 500L
    const val PACKAGING_FEE = 0L
    const val PLATFORM_FEE = 0L
    const val FOOD_SUPER_PARENT_ID = 0L

    // Boolean values for super category (default to true for better UX)
    const val IS_ENABLED_SUPER_CATEGORY = true

    // Boolean values for games (default to false)
    const val IS_DC_MIX_GAME_ENABLED = false
    const val IS_ACCOUNT_DELETION_ENABLED = false
    const val IS_IOS_EASYPAISA_ENABLED = false
    const val IS_IOS_JAZZCASH_ENABLED = false
    const val IS_IOS_CARD_ENABLED = false
    const val IS_SAME_DAY_DELIVERY_ENABLED = false
    const val IS_GROCERY_RUN_GAME_ENABLED = false
    const val IS_FORCE_UPDATE_ENABLED = false
    const val GROCERY_RUN_GAME_LINK = ""
    const val SEARCH_PLACES_CHARACTER_LIMIT = 2L
    const val SEARCH_PLACES_DEBOUNCE_TIME = 2000L

}
