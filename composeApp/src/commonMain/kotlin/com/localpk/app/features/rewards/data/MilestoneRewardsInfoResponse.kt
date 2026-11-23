package com.localpk.app.features.rewards.data

import com.localpk.app.utils.formatNumber
import com.localpk.app.utils.formatPKRCurrency
import kotlinx.serialization.Serializable

@Serializable
data class MilestoneRewardsInfoResponse(
    val success: Boolean,
    val body: MilestoneRewardsInfo
)

@Serializable
data class MilestoneRewardsInfo(
    val coinsForShareLink: Double,
    val coinsForAppInstall: Double,
    val coinsForAppSignUp: Double,
    val coinsForFirstOrder: Double,
    val coinsForNewSignUp: Double,
) {
    fun toMilestoneRewardsInfoUIData() = MilestoneRewardsInfoUIData(
        formattedCoinsForShareLink = coinsForShareLink.formatNumber(),
        coinsForAppInstall = coinsForAppInstall + coinsForAppSignUp + coinsForShareLink,
        coinsForAppSignUp = coinsForNewSignUp,
        coinsForFirstOrder = coinsForFirstOrder,
        formattedCashForAppInstall = ((coinsForAppInstall + coinsForAppSignUp + coinsForShareLink) / 10).formatNumber(),
        formattedCashForAppSignup = (coinsForNewSignUp / 10).formatNumber(),
        formattedCashForFirstOrder = (coinsForFirstOrder / 10).formatNumber(),
        formattedCCoinsForAppInstall = (coinsForAppInstall + coinsForAppSignUp + coinsForShareLink).formatNumber(),
        formattedCoinsForAppSignup = coinsForNewSignUp.formatNumber(),
        formattedCoinsForFirstOrder = coinsForFirstOrder.formatNumber(),
    )
}

@Serializable
data class MilestoneRewardsInfoUIData(
    val formattedCoinsForShareLink: String,
    val coinsForAppInstall: Double,
    val coinsForAppSignUp: Double,
    val coinsForFirstOrder: Double,
    val formattedCashForAppInstall: String,
    val formattedCashForAppSignup: String,
    val formattedCashForFirstOrder: String,
    val formattedCCoinsForAppInstall: String,
    val formattedCoinsForAppSignup: String,
    val formattedCoinsForFirstOrder: String
)