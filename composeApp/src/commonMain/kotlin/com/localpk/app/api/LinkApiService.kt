package com.localpk.app.api

import com.localpk.app.base.data.BaseApiService
import com.localpk.app.features.dynamicLinksService.data.DynamicLinkRequest
import com.localpk.app.features.dynamicLinksService.data.DynamicLinkRequestFlashDeal
import com.localpk.app.features.dynamicLinksService.data.DynamicLinkResponse
import com.localpk.app.utils.SharedPreferencesManager
import io.ktor.client.HttpClient

interface LinkApiService {

    suspend fun getDynamicLink(
        body: DynamicLinkRequest
    ): DynamicLinkResponse

    suspend fun getDynamicLinkFlashDeal(
        body: DynamicLinkRequestFlashDeal
    ): DynamicLinkResponse

}

class LinkApiServiceImpl(
    httpClient: HttpClient,
    baseUrl: String,
    sharedPreferencesManager: SharedPreferencesManager
) : BaseApiService(httpClient, baseUrl, sharedPreferencesManager), LinkApiService {

    override suspend fun getDynamicLink(body: DynamicLinkRequest): DynamicLinkResponse {
        return post("/api/dynamic-link/create-referral-link", body)
    }

    override suspend fun getDynamicLinkFlashDeal(body: DynamicLinkRequestFlashDeal): DynamicLinkResponse {
        return post("/api/dynamic-link/create-flash-deal-link", body)
    }

}