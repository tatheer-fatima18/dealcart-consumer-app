package com.localpk.app.di

import com.localpk.app.features.address.domain.AddAddressUseCase
import com.localpk.app.features.address.domain.FetchCustomerAddressUseCase
import com.localpk.app.features.address.domain.GetMapPolygonUseCase
import com.localpk.app.features.address.domain.RemoveAddressUseCase
import com.localpk.app.features.banner.domain.BannerUseCase
import com.localpk.app.features.cart.domain.AddToCartUseCase
import com.localpk.app.features.cart.domain.ApplyPromoCodeUseCase
import com.localpk.app.features.cart.domain.BundleSwitchUseCase
import com.localpk.app.features.cart.domain.CheckoutUseCase
import com.localpk.app.features.cart.domain.ClearCartUseCase
import com.localpk.app.features.cart.domain.DecreaseBundleQuantityUseCase
import com.localpk.app.features.cart.domain.DecreaseItemQuantityUseCase
import com.localpk.app.features.cart.domain.FetchCartUseCase
import com.localpk.app.features.cart.domain.GetCartUseCase
import com.localpk.app.features.cart.domain.GetOneLastStepAdUseCase
import com.localpk.app.features.cart.domain.GetOrderStatusUseCase
import com.localpk.app.features.cart.domain.IncreaseBundleQuantityUseCase
import com.localpk.app.features.cart.domain.IncreaseItemQuantityUseCase
import com.localpk.app.features.cart.domain.IsFoodCartUseCase
import com.localpk.app.features.cart.domain.RemoveCartUseCase
import com.localpk.app.features.cart.domain.ValidateBundleUseCase
import com.localpk.app.features.category.domain.CategoryUseCase
import com.localpk.app.features.cricket_live.domain.CricketBannerUseCase
import com.localpk.app.features.cricket_live.domain.MatchSummaryUseCase
import com.localpk.app.features.cricket_live.domain.PointsTableUseCase
import com.localpk.app.features.cricket_live.domain.PoweredByUseCase
import com.localpk.app.features.cricket_live.domain.ScoreCardUseCase
import com.localpk.app.features.cricket_live.domain.UpcomingMatchesUseCase
import com.localpk.app.features.dynamicLinksService.domain.DynamicLinkFlashDealUseCase
import com.localpk.app.features.dynamicLinksService.domain.DynamicLinkUseCase
import com.localpk.app.features.flash_deals.domain.FlashDealDetailUseCase
import com.localpk.app.features.flash_deals.domain.FlashDealsUseCase
import com.localpk.app.features.frequently_ordered.domain.FrequentlyOrderedUseCase
import com.localpk.app.features.login.domain.CheckIfNewCustomerUseCase
import com.localpk.app.features.login.domain.GenerateOTPUseCase
import com.localpk.app.features.login.domain.ResendOTPUseCase
import com.localpk.app.features.login.domain.VerifyOTPUseCase
import com.localpk.app.features.order_review.domain.AddOrderReviewUseCase
import com.localpk.app.features.order_review.domain.CancelOrderReviewUseCase
import com.localpk.app.features.order_review.domain.GetOrderForReviewUseCase
import com.localpk.app.features.order_review.domain.GetOrderReviewCategoriesUseCase
import com.localpk.app.features.orders.domain.CancelOrderRequestUseCase
import com.localpk.app.features.orders.domain.CurrentOrdersUseCase
import com.localpk.app.features.orders.domain.DeliveredReceiptUseCase
import com.localpk.app.features.orders.domain.GetCancelOrderReasonsUseCase
import com.localpk.app.features.orders.domain.OrderDetailUseCase
import com.localpk.app.features.orders.domain.PastOrdersUseCase
import com.localpk.app.features.personal.domain.DeleteCustomerUseCase
import com.localpk.app.features.personal.domain.ProfileDetailUseCase
import com.localpk.app.features.personal.domain.SendEmailUseCase
import com.localpk.app.features.personal.domain.UpdateProfileDetailUseCase
import com.localpk.app.features.personal.domain.VerifyEmailUseCase
import com.localpk.app.features.product_detail.domain.ProductDetailUseCase
import com.localpk.app.features.productlist.domain.ProductListWithSubCategoryUseCase
import com.localpk.app.features.profile.data.CustomerSavingDetailUseCase
import com.localpk.app.features.promo.domain.GetPromosUseCase
import com.localpk.app.features.recommendation_engine.domain.RecommendationEngineProductsUseCase
import com.localpk.app.features.recommendation_engine.domain.RecommendationEngineSearchUseCase
import com.localpk.app.features.recommendation_engine.domain.RecommendationEngineUseCase
import com.localpk.app.features.restaurant.domain.AddFoodItemToCartUseCase
import com.localpk.app.features.restaurant.domain.ApplyFoodPromoUseCase
import com.localpk.app.features.restaurant.domain.FoodOrderConfigurationDetailsUseCase
import com.localpk.app.features.restaurant.domain.FoodOrderDetailsUseCase
import com.localpk.app.features.restaurant.domain.FoodOrderStatusUseCase
import com.localpk.app.features.restaurant.domain.FoodOrdersUseCase
import com.localpk.app.features.restaurant.domain.GetFoodCartUseCase
import com.localpk.app.features.restaurant.domain.PlaceFoodOrderUseCase
import com.localpk.app.features.restaurant.domain.RestaurantDetailUseCase
import com.localpk.app.features.restaurant.domain.RestaurantOrderIdUseCase
import com.localpk.app.features.restaurant.domain.RestaurantUseCase
import com.localpk.app.features.restaurant.domain.UpdateFoodItemQuantityUseCase
import com.localpk.app.features.rewards.domain.CoinsHistoryUseCase
import com.localpk.app.features.rewards.domain.MilestoneRewardsInfoUseCase
import com.localpk.app.features.rewards.domain.RewardsMilestoneUseCase
import com.localpk.app.features.search.domain.SearchUseCase
import com.localpk.app.features.search.domain.TrendingSearchUseCase
import com.localpk.app.features.share_to_friend.domain.AppLinkUseCase
import com.localpk.app.features.swimlane_engine.domain.SwimlaneByIdUseCase
import com.localpk.app.features.swimlane_engine.domain.SwimlaneEngineUseCase
import com.localpk.app.features.user_location.domain.OperationalCitiesUseCase
import com.localpk.app.features.user_location.domain.UserLocationWarehouseUseCase
import com.localpk.app.features.view_all_products.domain.ViewAllProductUseCase
import com.localpk.app.features.wallet.domain.CardTopUpUseCase
import com.localpk.app.features.wallet.domain.WalletBalanceUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

fun useCaseModule(): Module = module {
    factory { CategoryUseCase(get(), get(), get()) }
    factory { GenerateOTPUseCase(get(), get(), get(), get()) }
    factory { CheckIfNewCustomerUseCase(get(), get(), get()) }
    factory { VerifyOTPUseCase(get(), get(), get()) }
    factory { ResendOTPUseCase(get(), get(), get()) }
    factory { BannerUseCase(get(), get(), get()) }
    factory { CricketBannerUseCase(get(), get(), get()) }
    factory { MatchSummaryUseCase(get(), get(), get()) }
    factory { SwimlaneByIdUseCase(get(), get(), get()) }
    factory { SwimlaneEngineUseCase(get(), get(), get()) }
    factory { ProductDetailUseCase(get(), get(), get()) }
    factory { RecommendationEngineProductsUseCase(get(), get(), get()) }
    factory { RecommendationEngineSearchUseCase(get(), get(), get()) }
    factory { RecommendationEngineUseCase(get(), get(), get()) }
    factory { ProductListWithSubCategoryUseCase(get(), get(), get()) }
    factory { UserLocationWarehouseUseCase(get(), get(), get()) }
    factory { OperationalCitiesUseCase(get(), get(), get()) }
    factory { FoodOrdersUseCase(get(), get(), get()) }
    factory { AddFoodItemToCartUseCase(get(), get(), get()) }
    factory { ApplyFoodPromoUseCase(get(), get(), get()) }
    factory { FoodOrdersUseCase(get(), get(), get()) }
    factory { FoodOrderConfigurationDetailsUseCase(get(), get(), get()) }
    factory { FoodOrderDetailsUseCase(get(), get(), get()) }
    factory { GetFoodCartUseCase(get(), get(), get()) }
    factory { RestaurantUseCase(get(), get(), get()) }
    factory { UpdateFoodItemQuantityUseCase(get(), get(), get()) }
    factory { RestaurantOrderIdUseCase(get(), get(), get()) }
    factory { RestaurantDetailUseCase(get(), get(), get()) }
    factory { PlaceFoodOrderUseCase(get(), get(), get()) }
    factory { FoodOrderStatusUseCase(get(), get(), get()) }
    factory { SearchUseCase(get(), get(), get()) }
    factory { TrendingSearchUseCase(get(), get(), get()) }

    factory { FrequentlyOrderedUseCase(get(), get(), get(), get()) }
    factory { ViewAllProductUseCase(get(), get()) }
    factory { FlashDealsUseCase(get(), get()) }
    factory { FlashDealDetailUseCase(get(), get()) }
    factory { ApplyPromoCodeUseCase(get(), get(), get()) }
    factory { BundleSwitchUseCase(get(), get(), get()) }
    factory { ClearCartUseCase(get(), get(), get(), get()) }
    factory { DecreaseItemQuantityUseCase(get(), get(), get()) }
    factory { DecreaseBundleQuantityUseCase(get(), get(), get()) }
    factory { FetchCartUseCase(get(), get(), get()) }
    factory { IncreaseItemQuantityUseCase(get(), get(), get()) }
    factory { IncreaseBundleQuantityUseCase(get(), get(), get()) }
    factory { IsFoodCartUseCase(get(), get(), get()) }
    factory { RemoveCartUseCase(get(), get(), get()) }
    factory { ValidateBundleUseCase(get(), get(), get()) }
    factory { GetCartUseCase(get(), get(), get()) }
    factory { AddToCartUseCase(get(), get(), get()) }
    factory { IncreaseItemQuantityUseCase(get(), get(), get()) }
    factory { DecreaseItemQuantityUseCase(get(), get(), get()) }
    factory { CheckoutUseCase(get(), get(), get()) }
    factory { GetOneLastStepAdUseCase(get(), get(), get()) }
    factory { GetOrderStatusUseCase(get(), get(), get()) }
    factory { MilestoneRewardsInfoUseCase(get(), get(), get()) }
    factory { DynamicLinkUseCase(get(), get(), get()) }
    factory { DynamicLinkFlashDealUseCase(get(), get(), get()) }
    factory { WalletBalanceUseCase(get(), get(), get()) }
    factory { PastOrdersUseCase(get(), get(), get()) }
    factory { OrderDetailUseCase(get(), get(), get()) }
    factory { GetCancelOrderReasonsUseCase(get(), get(), get()) }
    factory { DeliveredReceiptUseCase(get(), get(), get()) }
    factory { CancelOrderRequestUseCase(get(), get(), get()) }
    factory { CurrentOrdersUseCase(get(), get(), get()) }
    factory { AddOrderReviewUseCase(get(), get(), get()) }
    factory { GetOrderForReviewUseCase(get(), get(), get()) }
    factory { GetOrderReviewCategoriesUseCase(get(), get(), get()) }
    factory { CancelOrderReviewUseCase(get(), get(), get()) }

    factory { ProfileDetailUseCase(get(), get(), get()) }
    factory { SendEmailUseCase(get(), get(), get()) }
    factory { UpdateProfileDetailUseCase(get(), get(), get()) }
    factory { VerifyEmailUseCase(get(), get(), get()) }
    factory { FetchCustomerAddressUseCase(get(), get(), get()) }
    factory { RemoveAddressUseCase(get(), get(), get()) }
    factory { AddAddressUseCase(get(), get(), get()) }
    factory { GetPromosUseCase(get(), get(), get()) }
    factory { RewardsMilestoneUseCase(get(), get(), get()) }
    factory { CustomerSavingDetailUseCase(get(), get(), get()) }
    factory { CoinsHistoryUseCase(get(), get(), get()) }
    factory { AppLinkUseCase(get(), get(), get()) }
    factory { CoinsHistoryUseCase(get(), get(), get()) }
    factory { CardTopUpUseCase(get(), get(), get()) }
    factory { GetMapPolygonUseCase(get(), get(), get()) }
    factory { DeleteCustomerUseCase(get(), get(), get()) }
    factory { PointsTableUseCase(get(), get(), get()) }
    factory { PoweredByUseCase(get(), get(), get()) }
    factory { ScoreCardUseCase(get(), get(), get()) }
    factory { UpcomingMatchesUseCase(get(), get(), get()) }
}