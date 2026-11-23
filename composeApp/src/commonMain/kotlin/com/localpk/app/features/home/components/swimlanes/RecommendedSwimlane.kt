package com.localpk.app.features.home.components.swimlanes

import androidx.compose.runtime.Composable
import com.localpk.app.features.productlist.data.ProductUiData
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.recommended_for_you
import org.jetbrains.compose.resources.stringResource

@Composable
fun RecommendedSwimlane(
    recommendedItems: List<ProductUiData>,
    onProductClick: (ProductUiData) -> Unit,
    onAddClick: (ProductUiData) -> Unit
) {
    if (recommendedItems.isNotEmpty()) {
        RecommendedSwimlaneComponent(
            title = stringResource(Res.string.recommended_for_you),
            true,
            recommendedItems = recommendedItems,
            onProductClick = {
                onProductClick(it)
            },
            onAddClick = {
                onAddClick(it)
            }
        )
    }
}