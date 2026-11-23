package com.localpk.app.features.profile.data

import com.localpk.app.utils.default
import com.localpk.app.utils.formatPKRCurrency
import kotlinx.serialization.Serializable

@Serializable
data class CustomerSavingDetailResponse(
    val success: Boolean,
    val body: CustomerSavingDetail
)

@Serializable
data class CustomerSavingDetail(
    val result: CustomerSavingDetailData
)

@Serializable
data class CustomerSavingDetailData(
    val totalSavings: Int,
    val monthlySavings: Int,
    val totalPetrolSaved: Int?,
    val monthlySavedPetrol: Int?
) {
    fun formattedTotalSavings(): String = totalSavings.toDouble().formatPKRCurrency()
    fun formattedTotalPetrolSaved(): String =
        totalPetrolSaved?.toDouble()?.formatPKRCurrency().default()
}