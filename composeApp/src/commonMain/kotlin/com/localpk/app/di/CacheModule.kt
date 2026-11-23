package com.localpk.app.di

import com.localpk.app.base.data.DefaultUserSettingsFlowData
import com.localpk.app.base.data.UserSettingsFlowData
import com.localpk.app.database.DbHelper
import com.localpk.app.database.daos.DealCartDao
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.SharedPreferencesManagerImpl
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.DefaultEventLogger
import org.koin.dsl.module

fun cacheModule() = module {
    single { DbHelper(get()) }
    single { DealCartDao(get()) }
    single<SharedPreferencesManager> { SharedPreferencesManagerImpl(get()) }
    single<UserSettingsFlowData> { DefaultUserSettingsFlowData() }
    single<EventLogger> { DefaultEventLogger() }
}