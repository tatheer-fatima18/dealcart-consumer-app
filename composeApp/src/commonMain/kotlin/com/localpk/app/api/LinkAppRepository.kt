package com.localpk.app.api

import com.localpk.app.features.dynamicLinksService.data.DynamicLinkRequest
import com.localpk.app.features.dynamicLinksService.data.DynamicLinkRequestFlashDeal
import com.localpk.app.features.dynamicLinksService.data.DynamicLinkResponse
import com.localpk.app.network.ResponseResult
import com.localpk.app.network.callApi
import com.localpk.app.utils.SharedPreferencesManager

interface LinkAppRepository {
    suspend fun getDynamicLink(dynamicLinkRequest: DynamicLinkRequest): ResponseResult<DynamicLinkResponse>
    suspend fun getDynamicLinkFlashDeal(dynamicLinkRequestFlashDeal: DynamicLinkRequestFlashDeal): ResponseResult<DynamicLinkResponse>
}

class LinkAppRepositoryImpl(
    private val service: LinkApiService,
    private val sharedPreferencesManager: SharedPreferencesManager
) : LinkAppRepository {

    override suspend fun getDynamicLink(dynamicLinkRequest: DynamicLinkRequest): ResponseResult<DynamicLinkResponse> {
        return callApi { service.getDynamicLink(body = dynamicLinkRequest) }
    }

    override suspend fun getDynamicLinkFlashDeal(dynamicLinkRequestFlashDeal: DynamicLinkRequestFlashDeal): ResponseResult<DynamicLinkResponse> {
        return callApi { service.getDynamicLinkFlashDeal(body = dynamicLinkRequestFlashDeal) }
    }

}