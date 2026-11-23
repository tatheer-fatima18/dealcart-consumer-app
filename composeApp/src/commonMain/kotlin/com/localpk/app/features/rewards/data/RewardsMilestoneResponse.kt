package com.localpk.app.features.rewards.data

import com.localpk.app.utils.ZERO
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.ZERO_STRING
import com.localpk.app.utils.formatPKRCurrency
import kotlinx.serialization.Serializable

@Serializable
data class RewardsMilestoneResponse(
    val success: Boolean,
    val body: Milestones
)

@Serializable
data class Milestones(
    val shares: Int = ZERO_INT,
    val appDownloads: Int = ZERO_INT,
    val ordersDelivered: Int = ZERO_INT,
    val signUps: Int = ZERO_INT,
    val games: Int = ZERO_INT,
    val refund: Int = ZERO_INT,
    val referralBonus: Int = ZERO_INT,
    val total: Double = ZERO,
    val earning: Double = ZERO,
    val totalAppShares: Int = ZERO_INT,
    val totalAppDownloads: Int = ZERO_INT,
    val totalAppSignups: Int = ZERO_INT,
    val totalOrdersDelivered: Int = ZERO_INT,
    val totalCoinEarningsSinceBeginning: Int = ZERO_INT
) {
    fun toMilestonesUIData() = MilestonesUIData(
        formattedShares = shares.toDouble().formatPKRCurrency(),
        formattedAppDownloads = appDownloads.toDouble().formatPKRCurrency(),
        formattedOrdersDelivered = ordersDelivered.toDouble().formatPKRCurrency(),
        formattedSignUps = signUps.toDouble().formatPKRCurrency(),
        formattedGames = games.toDouble().formatPKRCurrency(),
        formattedRefund = refund.toDouble().formatPKRCurrency(),
        formattedReferralBonus = referralBonus.toDouble().formatPKRCurrency(),
        formattedTotal = total.toDouble().formatPKRCurrency(),
        formattedEarning = earning.formatPKRCurrency(),
        totalAppShares = totalAppShares,
        totalAppDownloads = totalAppDownloads,
        totalAppSignups = totalAppSignups + totalAppShares + totalAppDownloads,
        totalOrdersDelivered = totalOrdersDelivered,
        formattedTotalCoinEarningsSinceBeginning = totalCoinEarningsSinceBeginning.toDouble().formatPKRCurrency()
    )
}

@Serializable
data class MilestonesUIData(
    val formattedShares: String = ZERO_STRING,
    val formattedAppDownloads: String = ZERO_STRING,
    val formattedOrdersDelivered: String = ZERO_STRING,
    val formattedSignUps: String = ZERO_STRING,
    val formattedGames: String = ZERO_STRING,
    val formattedRefund: String = ZERO_STRING,
    val formattedReferralBonus: String = ZERO_STRING,
    val formattedTotal: String = ZERO_STRING,
    val formattedEarning: String = ZERO_STRING,
    val totalAppShares: Int = ZERO_INT,
    val totalAppDownloads: Int = ZERO_INT,
    val totalAppSignups: Int = ZERO_INT,
    val totalOrdersDelivered: Int = ZERO_INT,
    val formattedTotalCoinEarningsSinceBeginning: String = ZERO_STRING
)