package com.localpk.app.di

import android.content.Context
import com.localpk.app.base.domain.AndroidNetworkErrorBuilder
import com.localpk.app.base.domain.ExternalLinkHandler
import com.localpk.app.base.domain.ExternalLinkHandlerAndroid
import com.localpk.app.base.domain.NetworkErrorBuilder
import com.localpk.app.base.domain.SocialSharingPlatform
import com.localpk.app.base.domain.SocialSharingPlatformAndroid
import com.localpk.app.dbFactory.DatabaseFactory
import com.localpk.app.utils.MultiplatformSettingsFactory
import com.localpk.app.utils.StringsResourceManager
import com.localpk.app.utils.StringsResourceManagerImpl
import org.koin.core.module.Module
import org.koin.dsl.module

fun platformModule(context: Context): Module = module {
    single { DatabaseFactory(context) }
    single<NetworkErrorBuilder> { AndroidNetworkErrorBuilder() }
    single { MultiplatformSettingsFactory(context) }
    single<StringsResourceManager> { StringsResourceManagerImpl(context) }
    single<SocialSharingPlatform> {
        SocialSharingPlatformAndroid(get<Context>())
    }
    single<ExternalLinkHandler> {
        ExternalLinkHandlerAndroid(get<Context>())
    }
}