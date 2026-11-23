package com.localpk.app.utils

/**
 * Utility class to handle deep links for the app
 */
object DeepLinkHandler {
    


    
    /**
     * Parse a deep link URL and return the appropriate screen
     * @param url The deep link URL
     * @return The screen to navigate to, or null if the URL is not supported
     */
    fun parseDeepLink(url: String): DeepLinkResult? {
        return try {
            // Simple URL parsing for multiplatform compatibility
            val urlLower = url.lowercase()
            
            // Check if it's our domain
            if (!urlLower.contains(DEALCART_URL)) {
                return DeepLinkResult.OpenInBrowser(url)
            }
            
            // Check if it's an app link
            if (!urlLower.contains("/app")) {
                return DeepLinkResult.OpenInBrowser(url)
            }
            
            // Extract the path after /app
            val appIndex = urlLower.indexOf("/app")
            val pathAfterApp = if (appIndex + 4 < url.length) {
                url.substring(appIndex + 4)
            } else {
                ""
            }
            
            // Remove leading slash and any query/fragment if present
            val cleanPath = pathAfterApp
                .removePrefix("/")
                .substringBefore("?")
                .substringBefore("#")
            val pathSegments = cleanPath.split("/").filter { it.isNotEmpty() }
            
            when (pathSegments.size) {
                0 -> {
                    // Empty path - could be home or search
                    DeepLinkResult.HomeScreen
                }
                1 -> {
                    // Single segment - main navigation
                    when (pathSegments[0]) {
                        "search" -> DeepLinkResult.SearchScreen
                        LINK_TYPE_CURRENT_ORDERS -> DeepLinkResult.OrdersScreen(0) // Current orders tab
                        LINK_TYPE_PAST_ORDERS -> DeepLinkResult.OrdersScreen(1) // Past orders tab
                        LINK_TYPE_DELIVERED_ORDERS -> DeepLinkResult.OrdersScreen(1, 1) // Past orders, delivered filter
                        LINK_TYPE_RETURNED_ORDERS -> DeepLinkResult.OrdersScreen(1, 2) // Past orders, returned filter
                        LINK_TYPE_CANCELLED_ORDERS -> DeepLinkResult.OrdersScreen(1, 3) // Past orders, cancelled filter
                        LINK_TYPE_CART -> DeepLinkResult.CartScreen
                        LINK_TYPE_REWARDS -> DeepLinkResult.RewardsScreen
                        LINK_TYPE_GAMES -> DeepLinkResult.SpinTheWheelScreen
                        LINK_TYPE_CRICKET -> DeepLinkResult.CricketScreen
                        LINK_TYPE_SPIN_THE_WHEEL -> DeepLinkResult.SpinTheWheelScreen
                        LINK_TYPE_DEAL_REELS -> DeepLinkResult.TikTokMainScreen
                        LINK_TYPE_HELP_CENTER -> DeepLinkResult.HelpCenterScreen
                        LINK_TYPE_ADD_ADDRESS -> DeepLinkResult.AddAddressScreen
                        LINK_TYPE_IN_APP_STORY -> DeepLinkResult.InAppStoryScreen
                        LINK_TYPE_IN_APP_REVIEW -> DeepLinkResult.InAppReviewScreen
                        LINK_TYPE_IN_APP_COMMENTS -> DeepLinkResult.InAppCommentsScreen
                        WALLET -> DeepLinkResult.WalletScreen
                        CHAT -> DeepLinkResult.ChatScreen
                        FOOD -> DeepLinkResult.FoodScreen
                        else -> DeepLinkResult.OpenInBrowser(url)
                    }
                }
                2 -> {
                    // Two segments - category/product/group/referral/super-parent
                    when (pathSegments[0]) {
                        LINK_TYP_GROUP -> DeepLinkResult.ReferralGroupScreen(pathSegments[1])
                        LINK_TYP_REFERRAL -> DeepLinkResult.ReferralUserScreen(pathSegments[1])
                        LINK_TYPE_CATEGORY -> {
                            val categoryId = pathSegments[1].trim().toLongOrNull()
                            if (categoryId != null) {
                                DeepLinkResult.CategoryScreen(categoryId)
                            } else null
                        }
                        LINK_TYPE_PRODUCT -> {
                            val productId = pathSegments[1].trim().toLongOrNull()
                            if (productId != null) {
                                DeepLinkResult.ProductScreen(productId)
                            } else null
                        }
                        LINK_TYPE_SUPER_PARENT -> {
                            val superParentCategoryId = pathSegments[1].trim().toLongOrNull()
                            if (superParentCategoryId != null) {
                                DeepLinkResult.SuperParentScreen(superParentCategoryId)
                            } else null
                        }
                        else -> DeepLinkResult.OpenInBrowser(url)
                    }
                }
                3 -> {
                    // Three segments - verify-email, dolat, promotional
                    when (pathSegments[0]) {
                        LINK_TYP_VERIFY_EMAIL -> {
                            val userId = pathSegments[1].toLongOrNull()
                            if (userId != null) {
                                DeepLinkResult.VerifyEmailScreen(userId)
                            } else null
                        }
                        DOLAT -> {
                            when (pathSegments[1]) {
                                DOLAT_APP -> {
                                    val referredById = pathSegments[2].trim().toLongOrNull()
                                    if (referredById != null) {
                                        DeepLinkResult.DolatAppScreen(referredById)
                                    } else null
                                }
                                else -> DeepLinkResult.OpenInBrowser(url)
                            }
                        }
                        LINK_TYPE_PROMOTIONAL -> {
                            val categoryId = pathSegments[1].trim().toLongOrNull()
                            val categoryName = pathSegments[2].trim()
                            if (categoryId != null) {
                                DeepLinkResult.PromotionalScreen(categoryId, categoryName)
                            } else null
                        }
                        else -> DeepLinkResult.OpenInBrowser(url)
                    }
                }
                else -> {
                    // More complex paths - dolat product/group, flash deals
                    when {
                        pathSegments.contains(DOLAT) && pathSegments.contains(DOLAT_PRODUCT) -> {
                            // dolat/product/{productId}/user/{referredById}
                            if (pathSegments.size >= 5) {
                                val productId = pathSegments[2].toLongOrNull()
                                val referredById = pathSegments[4].toLongOrNull()
                                if (productId != null && referredById != null) {
                                    DeepLinkResult.DolatProductScreen(productId, referredById)
                                } else null
                            } else DeepLinkResult.OpenInBrowser(url)
                        }
                        pathSegments.contains(DOLAT) && pathSegments.contains(DOLAT_GROUP) -> {
                            // dolat/group/{groupId}/user/{referredById}
                            if (pathSegments.size >= 5) {
                                val groupId = pathSegments[2].toLongOrNull()
                                val referredById = pathSegments[4].toLongOrNull()
                                if (groupId != null && referredById != null) {
                                    DeepLinkResult.DolatGroupScreen(groupId, referredById)
                                } else null
                            } else DeepLinkResult.OpenInBrowser(url)
                        }
                        pathSegments.contains(FLASH_DEAL) -> {
                            // flash-deal/{productId}/deal/{flashDealId}/user/{referredById}
                            if (pathSegments.size >= 6) {
                                val productId = pathSegments[1].toLongOrNull()
                                val flashDealId = pathSegments[3].toLongOrNull()
                                val referredById = pathSegments[5].toLongOrNull()
                                if (productId != null && flashDealId != null && referredById != null) {
                                    DeepLinkResult.FlashDealScreen(productId, flashDealId, referredById)
                                } else null
                            } else DeepLinkResult.OpenInBrowser(url)
                        }
                        else -> DeepLinkResult.OpenInBrowser(url)
                    }
                }
            }
        } catch (e: Exception) {
            DeepLinkResult.OpenInBrowser(url)
        }
    }
}

/**
 * Sealed class representing different deep link results
 */
sealed class DeepLinkResult {
    object SearchScreen : DeepLinkResult()
    object CartScreen : DeepLinkResult()
    object RewardsScreen : DeepLinkResult()
    object SpinTheWheelScreen : DeepLinkResult()
    object CricketScreen : DeepLinkResult()
    object TikTokMainScreen : DeepLinkResult()
    object HelpCenterScreen : DeepLinkResult()
    object AddAddressScreen : DeepLinkResult()
    object InAppStoryScreen : DeepLinkResult()
    object InAppReviewScreen : DeepLinkResult()
    object InAppCommentsScreen : DeepLinkResult()
    object WalletScreen : DeepLinkResult()
    object ChatScreen : DeepLinkResult()
    object FoodScreen : DeepLinkResult()
    object HomeScreen : DeepLinkResult()
    data class SuperParentScreen(val superParentCategoryId: Long) : DeepLinkResult()
    
    data class OrdersScreen(val tab: Int, val filter: Int? = null) : DeepLinkResult()
    data class ReferralGroupScreen(val groupCode: String) : DeepLinkResult()
    data class ReferralUserScreen(val userCode: String) : DeepLinkResult()
    data class CategoryScreen(val categoryId: Long) : DeepLinkResult()
    data class ProductScreen(val productId: Long) : DeepLinkResult()
    data class VerifyEmailScreen(val userId: Long) : DeepLinkResult()
    data class DolatAppScreen(val referredById: Long) : DeepLinkResult()
    data class PromotionalScreen(val categoryId: Long, val categoryName: String) : DeepLinkResult()
    data class DolatProductScreen(val productId: Long, val referredById: Long) : DeepLinkResult()
    data class DolatGroupScreen(val groupId: Long, val referredById: Long) : DeepLinkResult()
    data class FlashDealScreen(val productId: Long, val flashDealId: Long, val referredById: Long) : DeepLinkResult()
    data class OpenInBrowser(val url: String) : DeepLinkResult()
} 