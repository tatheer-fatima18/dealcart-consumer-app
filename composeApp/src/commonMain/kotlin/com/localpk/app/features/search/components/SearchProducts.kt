package com.localpk.app.features.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.localpk.app.common_components.SingleProductItem
import com.localpk.app.features.productlist.data.ProductUiData

@Composable
fun SearchProducts(
    productsLiveData: List<ProductUiData>,
    onAddClick: (ProductUiData) -> Unit,
    onIncrement: (ProductUiData) -> Unit,
    onDecrement: (ProductUiData) -> Unit,
    onDelete: (ProductUiData) -> Unit,
    onCardClick: (ProductUiData) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(productsLiveData) {
            SingleProductItem(
                productList = false,
                data = it,
                cardClick = {
                    onCardClick(it)
                },
                onAddClick = {
                    onAddClick(it)
                },
                onDelete = {
                    onDelete(it)
                },
                onIncrement = {
                    onIncrement(it)
                },
                onDecrement = {
                    onDecrement(it)
                }
            )
        }
    }
} 