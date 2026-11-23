package com.localpk.app.api

import com.localpk.app.base.data.BaseApiService
import com.localpk.app.features.promo.domain.PromoResponse
import com.localpk.app.features.rewards.data.CoinsHistoryResponse
import com.localpk.app.features.rewards.data.MilestoneRewardsInfoResponse
import com.localpk.app.features.rewards.data.RewardsMilestoneResponse
import com.localpk.app.features.share_to_friend.data.CreateAppLinkRequest
import com.localpk.app.features.share_to_friend.data.LinkResponse
import com.localpk.app.features.wallet.data.WalletBalanceResponse
import com.localpk.app.utils.SharedPreferencesManager
import io.ktor.client.HttpClient

interface DolatApiService {
    suspend fun getMilestoneRewardsInfo(): MilestoneRewardsInfoResponse
    suspend fun getWalletBalance(
        source: String,
        customerId: Long
    ): WalletBalanceResponse

    suspend fun getPromos(customerId: Long): PromoResponse

    suspend fun getUserCoinsHistory(
        limit: Int,
        page: Int,
        source: String,
        userId: Long
    ): CoinsHistoryResponse
    suspend fun getRewardsMilestones(userId: Long): RewardsMilestoneResponse
    suspend fun createAppLink(createAppLinkRequest: CreateAppLinkRequest): LinkResponse
}

class DolatApiServiceImpl(
    httpClient: HttpClient,
    baseUrl: String,
    sharedPreferencesManager: SharedPreferencesManager
) : BaseApiService(httpClient, baseUrl, sharedPreferencesManager), DolatApiService {

    override suspend fun getMilestoneRewardsInfo(): MilestoneRewardsInfoResponse {
        return get("/api/dolat/milestone/all")
    }

    override suspend fun getWalletBalance(source: String, customerId: Long): WalletBalanceResponse {
        return get(
            "/api/dolat/wallet/balance/${customerId}", parameters = mapOf(
                "source" to source
            )
        )
    }

    override suspend fun getPromos(customerId: Long): PromoResponse {
        return get("/api/dolat/promo/user-id/${customerId}")
    }

    override suspend fun getUserCoinsHistory(
        limit: Int,
        page: Int,
        source: String,
        userId: Long
    ): CoinsHistoryResponse {
        return get(
            "/api/dolat/transaction/users/$userId/history", parameters = mapOf(
                "limit" to limit,
                "page" to page,
                "source" to source
            )
        )
    }

    override suspend fun getRewardsMilestones(userId: Long): RewardsMilestoneResponse {
       return get("/api/dolat/milestone/$userId")
    }

    override suspend fun createAppLink(createAppLinkRequest: CreateAppLinkRequest): LinkResponse {
        return post(
            "/api/dolat/firebase-link/create-app-link",
            requestBody = createAppLinkRequest
        )
    }
}