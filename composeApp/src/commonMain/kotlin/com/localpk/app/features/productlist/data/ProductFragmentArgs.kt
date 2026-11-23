package com.localpk.app.features.productlist.data

import com.localpk.app.features.banner.data.Banner
import com.localpk.app.features.category.data.CategoryUiModel
import kotlinx.serialization.Serializable

@Serializable
data class ProductFragmentArgs(
    val parentId: Long,
    val parentName: String,
    val subCategoryId: Long,
    val subCategories: List<CategoryUiModel>,
    var categoryBanner: List<Banner>?,
    val navigateFromParent: Boolean,
    val subCategoryName: String
)
