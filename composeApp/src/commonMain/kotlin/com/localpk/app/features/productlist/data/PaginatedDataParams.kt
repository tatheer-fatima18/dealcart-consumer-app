package com.localpk.app.features.productlist.data

import kotlinx.serialization.Serializable

@Serializable
sealed class PaginatedDataParams {
    @Serializable
    data class Refresh(val categoryId: Long) : PaginatedDataParams()

    @Serializable
    data class Load(
        val page: Int,
        val categoryId: Long,
    ) : PaginatedDataParams()
}
