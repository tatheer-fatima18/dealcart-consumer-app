package com.localpk.app.di

import com.localpk.app.base.domain.ExternalLinkHandler
import com.localpk.app.base.domain.ExternalLinkHandlerIOS
import com.localpk.app.base.domain.IosNetworkErrorBuilder
import com.localpk.app.base.domain.NetworkErrorBuilder
import com.localpk.app.base.domain.SocialSharingPlatform
import com.localpk.app.base.domain.SocialSharingPlatformIOS
import com.localpk.app.dbFactory.DatabaseFactory
import com.localpk.app.utils.MultiplatformSettingsFactory
import com.localpk.app.utils.StringsResourceManager
import com.localpk.app.utils.StringsResourceManagerImpl
import org.koin.core.module.Module
import org.koin.dsl.module

fun platformModule(): Module = module {
    single { DatabaseFactory() }
    single<NetworkErrorBuilder> { IosNetworkErrorBuilder() }
    single { MultiplatformSettingsFactory() }
    single<StringsResourceManager> { StringsResourceManagerImpl() }
    single<SocialSharingPlatform> { SocialSharingPlatformIOS() }
    single<ExternalLinkHandler> { ExternalLinkHandlerIOS() }
}