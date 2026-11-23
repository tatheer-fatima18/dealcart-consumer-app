package com.localpk.app.features.home.components.swimlanes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.base.getScreenWidthDp
import com.localpk.app.features.flash_deals.data.FlashDealUiData
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.swimlane_engine.data.LandingPageType
import com.localpk.app.features.swimlane_engine.data.SwimlaneType
import com.localpk.app.features.swimlane_engine.data.SwimlaneUI
import com.localpk.app.features.view_all_products.presentation.ViewAllProductsScreen
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNullOrEmpty


@Composable
fun RenderSwimlane(
    swimlaneData: SwimlaneUI?,
    recommendedItems: List<ProductUiData>,
    flashDeals: List<FlashDealUiData>,
    eventLogger: EventLogger
) {
    val navigator = LocalNavigator.currentOrThrow
    if (swimlaneData == null) {
        // Could show a loading indicator here if desired
        return
    }
    when (swimlaneData.getSwimlaneType()) {
        SwimlaneType.PROMOTIONAL -> {
            if (swimlaneData.categories.isNotNullOrEmpty()) {
                swimlaneData.categories?.let {
                    PromotionalCategoriesSwimlaneComponent(
                        promotionalCategories = it,
                        title = swimlaneData.title.default(),
                        subTitle = swimlaneData.subtitle.default(),
                        onCategoryClick = { category ->
                            eventLogger.clickPromotionalCategory(
                                source = LandingPageType.Main.type,
                                category = category.name
                            )
                            navigator.push(
                                ViewAllProductsScreen(
                                    categoryId = category.id,
                                    categoryName = category.name,
                                    isPromotional = true,
                                    swimlaneType = SwimlaneType.PROMOTIONAL.type,
                                    isDiscount = false
                                )
                            )
                        }
                    )
                }
            }
        }

        SwimlaneType.CART, SwimlaneType.HOME -> {
            RecommendedSwimlane(
                recommendedItems = recommendedItems,
                onProductClick = { product ->
                    //eventLogger.onClickProductHomeScreen(product.name)
                },
                onAddClick = { product ->
                    if (!product.hasVariants) {
                        eventLogger.addToCartMergn(
                            product.id,
                            product.name,
                            1,
                            product.price,
                            "recommendation_home",
                            type = "quick_buy"
                        )
                    }
                },
            )
        }

        SwimlaneType.NEWLY_ADDED -> {
            if (swimlaneData.products.isNotNullOrEmpty()) {
                swimlaneData.products?.let {
                    RecommendedSwimlaneComponent(
                        title = swimlaneData.title.default(),
                        isRecommended = false,
                        recommendedItems = it,
                        onProductClick = { product ->
//                        eventLogger.clickNewlyAddedProduct(
//                            sharedPreferencesManager.getUserId(), product.id, product.name
//                        )
                        },
                        onAddClick = { product ->
                            if (product.hasVariants.not()) {
                                eventLogger.addToCartMergn(
                                    product.id,
                                    product.name,
                                    1,
                                    product.price,
                                    "home_newly_added",
                                    type = "quick_buy"
                                )
                            }
//                        if (product.hasVariants.not()) {
//                            mainActivityViewModel.addToCartWithConfirmation {
//                                eventLogger.clickQuickAddNewlyAddedProduct(
//                                    sharedPreferencesManager.getUserId(),
//                                    product.id,
//                                    product.name
//                                )
//                                eventLogger.addToCartHomePage(
//                                    sharedPreferencesManager.getUserId(),
//                                    sharedPreferencesManager.getSelectedCity(),
//                                    product.id
//                                )
                            eventLogger.addToCartMergn(
                                product.id,
                                product.name,
                                1,
                                product.price,
                                "home_newly_added",
                                type = "quick_buy"
                            )
//                                mainActivityViewModel.addToCart(
//                                    CartProductType.INDIVIDUAL,
//                                    product.id,
//                                ) { success ->
//                                    if (success) {
//                                        product.itemQuantity.value += 1
//                                    }
//                                }
//                            }
//                        }
//                        else {
//                            navigate(
//                                HomeFragmentDirections.actionHomeToProductDetail(
//                                    product
//                                )
//                            )
//                        }
                        },
                        viewAllClick = {
                            navigator.push(
                                ViewAllProductsScreen(
                                    swimlaneId = swimlaneData.id,
                                    categoryName = swimlaneData.title.default(),
                                    isPromotional = false,
                                    swimlaneType = swimlaneData.getSwimlaneType()?.type,
                                    isDiscount = false
                                )
                            )
                        }
                    )
                }
            }
        }

        SwimlaneType.GROUP_DEALS -> {
            GroupDealComponent(
                flashDeals = flashDeals,
                onCardClick = { deal, product ->
//                    eventLogger.flashDealClickProductSwimlane(
//                        sharedPreferencesManager.isLoggedIn(),
//                        deal.id.getValueOrDefault(),
//                        deal.productName.default(),
//                        deal.categoryName.default(),
//                        deal.subCategoryName.default()
//                    )
                },
                onAddClick = { deal, product ->
                    eventLogger.addToCartMergn(
                        product.id,
                        product.name,
                        1,
                        product.price,
                        "home_flash_deal",
                        type = "quick_buy"
                    )

                },
                screenWidthDp = getScreenWidthDp()
            )
        }

        SwimlaneType.MAX_ABS_DISCOUNT,
        SwimlaneType.UNDER_AMOUNT,
        SwimlaneType.MIN_AMOUNT_OFF,
        SwimlaneType.MIN_PERCENTAGE_OFF,
        SwimlaneType.BEST_SELLING -> {
            if (swimlaneData.products.isNotNullOrEmpty()) {
                swimlaneData.products?.let {
                    DiscountSwimlaneComponent(
                        products = it,
                        title = swimlaneData.title.default(),
                        subTitle = swimlaneData.subtitle.default(),
                        viewAllClick = {
                            navigator.push(
                                ViewAllProductsScreen(
                                    swimlaneId = swimlaneData.id,
                                    categoryName = swimlaneData.title.default(),
                                    isPromotional = false,
                                    swimlaneType = swimlaneData.getSwimlaneType()?.type,
                                    isDiscount = true
                                )
                            )
                        },
                        swimlaneType = swimlaneData.getSwimlaneType(),
                        onCardClick = { product ->
                            eventLogger.productClicked(
                                data = product,
                                productCategory = EMPTY_STRING,
                                subCategoryName = EMPTY_STRING,
                                source = "DiscountSwimlane_[${swimlaneData.id}]"
                            )
                        },
                        onAddClick = { product ->
//                        mainActivityViewModel.addToCartWithConfirmation {
                            eventLogger.addToCartMergn(
                                productId = product.id,
                                product.name,
                                1,
                                product.price,
                                "home_discount_swimlane${swimlaneData.id.getValueOrDefault()}",
                                type = "quick_buy"
                            )
//                            mainActivityViewModel.addToCart(
//                                CartProductType.INDIVIDUAL,
//                                product.id,
//                            ) { success ->
//                                if (success) {
//                                    product.itemQuantity.value += 1
//                                }
//                            }
//                        }
                        },
                    )
                }
            }
        }

        null -> {}
    }
} 