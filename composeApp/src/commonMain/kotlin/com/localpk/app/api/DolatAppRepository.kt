package com.localpk.app.api

import com.localpk.app.features.promo.domain.PromoResponse
import com.localpk.app.features.rewards.data.CoinsHistoryResponse
import com.localpk.app.features.rewards.data.MilestoneRewardsInfoResponse
import com.localpk.app.features.rewards.data.RewardsMilestoneResponse
import com.localpk.app.features.share_to_friend.data.CreateAppLinkRequest
import com.localpk.app.features.share_to_friend.data.LinkResponse
import com.localpk.app.features.wallet.data.WalletBalanceResponse
import com.localpk.app.network.ResponseResult
import com.localpk.app.network.callApi
import com.localpk.app.utils.SharedPreferencesManager

interface DolatAppRepository {

    suspend fun getMilestoneRewardsInfo(): ResponseResult<MilestoneRewardsInfoResponse>
    suspend fun getWalletBalance(source: String): ResponseResult<WalletBalanceResponse>
    suspend fun getPromos(): ResponseResult<PromoResponse>
    suspend fun getUserCoinsHistory(
        limit: Int,
        page: Int,
        source: String
    ): ResponseResult<CoinsHistoryResponse>
    suspend fun getRewardsMilestones(): ResponseResult<RewardsMilestoneResponse>
    suspend fun createAppLink(createAppLinkRequest: CreateAppLinkRequest): ResponseResult<LinkResponse>

}

class DolatAppRepositoryImpl(
    private val service: DolatApiService,
    private val sharedPreferencesManager: SharedPreferencesManager
) : DolatAppRepository {

    override suspend fun getMilestoneRewardsInfo(): ResponseResult<MilestoneRewardsInfoResponse> {
        return callApi {
            service.getMilestoneRewardsInfo()
        }
    }

    override suspend fun getWalletBalance(source: String): ResponseResult<WalletBalanceResponse> {
        return callApi {
            service.getWalletBalance(source, sharedPreferencesManager.getUserId())
        }
    }

    override suspend fun getRewardsMilestones(): ResponseResult<RewardsMilestoneResponse> {
        return callApi {
            service.getRewardsMilestones(sharedPreferencesManager.getUserId())
        }
    }

    override suspend fun createAppLink(createAppLinkRequest: CreateAppLinkRequest): ResponseResult<LinkResponse> {
        return callApi {
            service.createAppLink(createAppLinkRequest)
        }
    }

    override suspend fun getPromos(): ResponseResult<PromoResponse> {
        return callApi {
            service.getPromos(
                sharedPreferencesManager.getUserId()
            )
        }
    }

    override suspend fun getUserCoinsHistory(
        limit: Int,
        page: Int,
        source: String
    ): ResponseResult<CoinsHistoryResponse> {
        return callApi {
            service.getUserCoinsHistory(
                limit = limit,
                page = page,
                source = source,
                userId = sharedPreferencesManager.getUserId()
            )
        }
    }


}