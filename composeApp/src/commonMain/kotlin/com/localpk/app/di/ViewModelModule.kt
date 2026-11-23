package com.localpk.app.di

import com.localpk.app.features.address.presentation.AddAddressBottomSheetViewModel
import com.localpk.app.features.address.presentation.AddPhoneNumberBottomSheetViewModel
import com.localpk.app.features.address.presentation.AddressListViewModel
import com.localpk.app.features.address.presentation.MapViewModel
import com.localpk.app.features.banner.presentation.BannerViewModel
import com.localpk.app.features.cart.data.OneLastStepData
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.cart.viewModel.FoodCartViewModel
import com.localpk.app.features.category.presentation.CategoryViewModel
import com.localpk.app.features.checkout.view.CartCheckoutViewModel
import com.localpk.app.features.checkout.view.SelectPaymentMethodViewModel
import com.localpk.app.features.cricket_live.viewModel.MatchDetailViewModel
import com.localpk.app.features.dynamicLinksService.presentation.DynamicLinkViewModel
import com.localpk.app.features.flash_deals.viewModel.FlashDealViewModel
import com.localpk.app.features.frequently_ordered.viewModel.FrequentlyOrderedViewModel
import com.localpk.app.features.home.presentation.HomeViewModel
import com.localpk.app.features.login.presentation.OTPViewModel
import com.localpk.app.features.login.presentation.PhoneInputViewModel
import com.localpk.app.features.one_last_step.viewModel.OneLastViewModel
import com.localpk.app.features.order_review.viewModel.OrderReviewCategoriesListingViewModel
import com.localpk.app.features.order_review.viewModel.OrderReviewSuccessViewModel
import com.localpk.app.features.order_review.viewModel.OrderReviewViewModel
import com.localpk.app.features.orders.viewModel.CancelOrderBottomSheetViewModel
import com.localpk.app.features.orders.viewModel.FoodOrderDetailViewModel
import com.localpk.app.features.orders.viewModel.OrderDetailViewModel
import com.localpk.app.features.orders.viewModel.OrderListingViewModel
import com.localpk.app.features.personal.presentation.AccountDeletionViewModel
import com.localpk.app.features.personal.presentation.PersonalViewModel
import com.localpk.app.features.product_detail.viewModel.ProductDetailViewModel
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.productlist.viewModel.ProductListViewModel
import com.localpk.app.features.profile.presentation.ProfileViewModel
import com.localpk.app.features.promo.view.PromoListingViewModel
import com.localpk.app.features.recommendation_engine.viewModel.RecommendationEngineViewModel
import com.localpk.app.features.restaurant.viewModels.FoodDetailViewModel
import com.localpk.app.features.restaurant.viewModels.GetRestaurantViewModel
import com.localpk.app.features.restaurant.viewModels.ORSDirectionViewModel
import com.localpk.app.features.restaurant.viewModels.OrderTrackingViewModel
import com.localpk.app.features.restaurant.viewModels.RestaurantDetailViewModel
import com.localpk.app.features.rewards.viewModel.CoinsHistoryViewModel
import com.localpk.app.features.rewards.viewModel.RewardsViewModel
import com.localpk.app.features.search.presentation.SearchViewModel
import com.localpk.app.features.share_and_earn.viewModel.ShareAndEarnViewModel
import com.localpk.app.features.share_to_friend.viewModel.ShareToFriendViewModel
import com.localpk.app.features.swimlane_engine.presentation.SwimlaneEngineViewModel
import com.localpk.app.features.user_location.presentation.UserLocationViewModel
import com.localpk.app.features.view_all_products.viewModel.ViewAllProductViewModel
import com.localpk.app.features.wallet.view.PaymentViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun viewModelModule(): Module = module {
    single { CategoryViewModel(get()) }
    viewModel { PhoneInputViewModel(get(), get()) }
    viewModel { OTPViewModel(get(), get(), get(), get(), get()) }
    factory { (args: ProductUiData) ->
        ProductDetailViewModel(
            args = args,
            productDetailUseCase = get(),
            sharedPreferencesManager = get()
        )
    }
    single { RecommendationEngineViewModel(get(), get(), get()) }
    factory { MatchDetailViewModel(get(), get(), get(), get(), get(), get()) }
    single {
        HomeViewModel(
            categoryViewModel = get(),
            bannerViewModel = get(),
            swimlaneEngineViewModel = get(),
            getRestaurantViewModel = get(),
            frequentlyOrderedViewModel = get(),
            sharedPreferencesManager = get(),
            flashDealsViewModel = get(),
            recommendationEngineViewModel = get(),
            cartViewModel = get(),
            addressListViewModel = get(),
            getOrderForReviewUseCase = get(),
            eventLogger = get(),
            cancelOrderReviewUseCase = get(),
            matchDetailViewModel = get()
        )
    }

    single { BannerViewModel(get(), get()) }
    single { SwimlaneEngineViewModel(get(), get()) }
    single { ProductListViewModel(get(), get(), get()) }
    viewModel { UserLocationViewModel(get(), get(), get(), get()) }
    factory { GetRestaurantViewModel(get(), get()) }
    factory { RestaurantDetailViewModel(get()) }
    single { FoodDetailViewModel() }
    factory { (argsData: Boolean) ->
        SearchViewModel(
            searchUseCase = get(),
            trendingSearchUseCase = get(),
            restaurantUseCase = get(),
            sharedPreferencesManager = get(),
            eventLogger = get(),
            argsData = argsData
        )
    }
    factory { ViewAllProductViewModel(get(), get()) }
    // Unnamed factory (not typed with `FrequentlyOrderedViewModel`)
    factory {
        FrequentlyOrderedViewModel(
            frequentlyOrderedUseCase = get()
        )
    }

    factory { FlashDealViewModel(get(), get(), get(), get()) }
    single {
        CartViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        FoodCartViewModel(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    factory { (args: OneLastStepData) ->
        OneLastViewModel(
            args = args,
            oneLastStepAdUseCase = get(),
            milestoneRewardsInfoUseCase = get(),
        )
    }
    factory { DynamicLinkViewModel(get(), get()) }
    viewModel {
        CartCheckoutViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        OrderListingViewModel(
            sharedPreferencesManager = get(),
            currentOrdersUseCase = get(),
            pastOrdersUseCase = get(),
            addOrderReviewUseCase = get(),
            foodOrdersUseCase = get(),
            eventLogger = get()
        )
    }
    factory {
        OrderDetailViewModel(
            orderId = get(),
            orderDetailUseCase = get(),
            deliveredReceiptUseCase = get(),
            addOrderReviewUseCase = get(),
            sharedPreferencesManager = get(),
            navigator = get(),
            bottomSheetNavigator = get()
        )
    }
    single { OrderTrackingViewModel(get(), get()) }
    viewModel {
        MapViewModel(
            getMapPolygonUseCase = get()
        )
    }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { PersonalViewModel(get(), get(), get(), get(), get(), get(), get()) }
    factory {
        FoodOrderDetailViewModel(
            orderId = get(),
            orderStatus = get(),
            foodOrderDetailsUseCase = get(),
            sharedPreferencesManager = get()
        )
    }
    factory { PromoListingViewModel(get(), get()) }
    factory { CancelOrderBottomSheetViewModel(get(), get(), get(), get()) }
    single { AddressListViewModel(get(), get(), get(), get()) }
    viewModel { AddAddressBottomSheetViewModel(get(), get(), get()) }
    factory {
        RewardsViewModel(
            rewardsMilestoneUseCase = get(),
            sharedPreferencesManager = get(),
            customerSavingDetailUseCase = get(),
            personalDetailUseCase = get()
        )
    }
    factory { CoinsHistoryViewModel(get()) }
    factory { ShareToFriendViewModel(get(), get()) }
    factory { ShareAndEarnViewModel(get()) }
    factory { CoinsHistoryViewModel(get()) }
    factory { PaymentViewModel(get(), get(), get(), get()) }
    factory { SelectPaymentMethodViewModel(get(), get(), get()) }
    factory { ORSDirectionViewModel(get()) }
    factory { AddPhoneNumberBottomSheetViewModel(get(), get()) }
    factory { OrderReviewViewModel(get()) }
    factory { OrderReviewCategoriesListingViewModel(get(), get()) }
    factory { OrderReviewSuccessViewModel() }
    factory {
        AccountDeletionViewModel(
            get()
        )
    }
}
