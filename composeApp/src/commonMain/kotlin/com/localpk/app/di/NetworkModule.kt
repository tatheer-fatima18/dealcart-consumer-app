package com.localpk.app.di

import com.localpk.app.api.ApiService
import com.localpk.app.api.ApiServiceImpl
import com.localpk.app.api.AppRepository
import com.localpk.app.api.AppRepositoryImpl
import com.localpk.app.api.DolatApiService
import com.localpk.app.api.DolatApiServiceImpl
import com.localpk.app.api.DolatAppRepository
import com.localpk.app.api.DolatAppRepositoryImpl
import com.localpk.app.api.LinkApiService
import com.localpk.app.api.LinkApiServiceImpl
import com.localpk.app.api.LinkAppRepository
import com.localpk.app.api.LinkAppRepositoryImpl
import com.localpk.app.api.ORSService
import com.localpk.app.api.ORSServiceImpl
import com.localpk.app.api.RouteRepository
import com.localpk.app.api.RouteRepositoryImpl
import com.localpk.app.api.SportApiService
import com.localpk.app.api.SportApiServiceImpl
import com.localpk.app.api.SportAppRepository
import com.localpk.app.api.SportAppRepositoryImpl
import com.localpk.app.base.BASE_DOLAT_URL
import com.localpk.app.base.BASE_FOOD_URL
import com.localpk.app.base.BASE_LINKS_URL
import com.localpk.app.base.BASE_URL
import com.localpk.app.base.BASE_URL_SPORTS
import com.localpk.app.base.domain.DefaultErrorResponseViewStateMapper
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.login.api.AuthRepository
import com.localpk.app.features.login.api.AuthRepositoryImpl
import com.localpk.app.features.login.api.UserAuthService
import com.localpk.app.features.login.api.UserAuthServiceImpl
import com.localpk.app.features.restaurant.api.FoodApiService
import com.localpk.app.features.restaurant.api.FoodApiServiceImpl
import com.localpk.app.features.restaurant.api.FoodAppRepository
import com.localpk.app.features.restaurant.api.FoodAppRepositoryImpl
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.firebase.pushNotification.PushNotificationService
import com.localpk.app.firebase.pushNotification.PushNotificationServiceImpl
import com.localpk.app.network.httpClient
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.module

fun networkModule(): Module = module {
    single { Dispatchers.IO }
    single { Dispatchers.Main }
    single { Dispatchers.Default }
    single { CoroutineScope(get()) }
    single<ErrorResponseViewStateMapper> {
        DefaultErrorResponseViewStateMapper(
            networkErrorBuilder = get(),
        )
    }
    single<HttpClient> { httpClient }
    single<ApiService> {
        ApiServiceImpl(
            httpClient,
            BASE_URL,
            get()
        )
    }
    single<AppRepository> { AppRepositoryImpl(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserAuthService> { UserAuthServiceImpl(get(), BASE_URL, get()) }
    single<FoodApiService> { FoodApiServiceImpl(get(), BASE_FOOD_URL, get()) }
    single<FoodAppRepository> { FoodAppRepositoryImpl(get(), get()) }
    single<DolatApiService> { DolatApiServiceImpl(get(), BASE_DOLAT_URL, get()) }
    single<DolatAppRepository> { DolatAppRepositoryImpl(get(), get()) }
    single<LinkApiService> { LinkApiServiceImpl(get(), BASE_LINKS_URL, get()) }
    single<SportApiService> { SportApiServiceImpl(get(), BASE_URL_SPORTS, get()) }
    single<SportAppRepository> { SportAppRepositoryImpl(get()) }
    single<LinkAppRepository> { LinkAppRepositoryImpl(get(), get()) }
    single<ORSService> { ORSServiceImpl() }
    single<RouteRepository> { RouteRepositoryImpl(get()) }
    // Push Notification Service
    single<PushNotificationService> { PushNotificationServiceImpl() }
    single { RemoteConfigs }
}