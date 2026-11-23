package com.localpk.app.base

actual val BASE_URL: String
    get() = BaseUrls.getBaseUrl()

actual val BASE_FOOD_URL: String
    get() = BaseUrls.getFoodBaseUrl()

actual val BASE_DOLAT_URL: String
    get() = BaseUrls.getDolatBaseUrl()

actual val BASE_LINKS_URL: String
    get() = BaseUrls.getLinksBaseUrl()

actual val BASE_URL_AGENT: String
    get() = BaseUrls.getAgentBaseUrl()

actual val BASE_URL_SPORTS: String
    get() = BaseUrls.getSportsBaseUrl()