package com.localpk.app.features.product_detail.data

import com.localpk.app.features.productlist.data.Product
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailResponse(val body: Product)



