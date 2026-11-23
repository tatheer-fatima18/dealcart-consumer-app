package com.localpk.app.base

import com.localpk.app.BuildConfig

object BaseUrls {

    fun getBaseUrl(): String = BuildConfig.BASE_URL
    fun getAgentBaseUrl(): String = BuildConfig.BASE_URL_AGENT
    fun getDolatBaseUrl(): String = BuildConfig.BASE_URL_DOLAT
    fun getSportsBaseUrl(): String = BuildConfig.BASE_URL_SPORTS
    fun getLinksBaseUrl(): String = BuildConfig.BASE_URL_LINKS
    fun appId(): String = BuildConfig.APPLICATION_ID
    fun mergnApiKey(): String = BuildConfig.MERGN_API_KEY
    fun cardPublicKey(): String = BuildConfig.CARD_PUBLIC_KEY
    fun cardHmacSecretKey(): String = BuildConfig.CARD_HMAC_SECRET_KEY
    fun cardAccountId(): String = BuildConfig.CARD_ACCOUNT_ID
    fun getFoodBaseUrl(): String = BuildConfig.BASE_URL_FOOD

}