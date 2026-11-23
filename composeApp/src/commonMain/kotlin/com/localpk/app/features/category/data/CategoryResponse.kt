package com.localpk.app.features.category.data

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(val body: CategoryList, val success: Boolean)

@Serializable
data class CategoryList(val results: List<Category>?)

@Serializable
data class Category(
    val id: Long, val name: String, val parentId: Long?, val images: List<CategoryImage>?,
    val categorySort: Double?,
    val hasNicotineTobaccoItems: Boolean?,
    val isNonGrocery: Boolean?,
    val categoryType: String
) {
    fun map(): CategoryUiModel {
        return CategoryUiModel(
            id = id,
            name = name,
            parentId = parentId,
            imageUrl = images?.firstOrNull()?.medium.orEmpty(),
            sort = categorySort,
            isNicotine = hasNicotineTobaccoItems ?: false,
            isNonGrocery = isNonGrocery ?: false,
            categoryType = categoryType
        )
    }
}

@Serializable
data class CategoryUiModel(
    val id: Long, val name: String, val parentId: Long?, val imageUrl: String = "",
    val selectedChildCategory: CategoryUiModel? = null,
    val sort: Double?,
    val isNicotine: Boolean,
    val isNonGrocery: Boolean,
    val categoryType: String
)

@Serializable
data class CategoryImage(val medium: String?)

@Serializable
data class Body(val message: String)