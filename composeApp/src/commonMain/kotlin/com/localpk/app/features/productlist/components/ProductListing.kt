package com.localpk.app.features.productlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.SingleProductItem
import com.localpk.app.features.productlist.data.HeaderRecyclerAdapterConstants
import com.localpk.app.features.productlist.data.ProductHeaderAdapterUiData
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.theming.TextSubdued

@Composable
fun ProductListing(
    state: LazyGridState,
    dataItems: List<ProductHeaderAdapterUiData>,
    onProductClick: (ProductUiData, String) -> Unit,
    onAddClick: (ProductUiData, String) -> Unit,
    onBannerClick: (Long) -> Unit,
    onBannerToProductClick: (Long) -> Unit,
    onBannerToBottomSheetClick: () -> Unit,
    onBannerToVideoClick: (String) -> Unit,
    isLoading: Boolean
) {
    if (isLoading)
        ProductGridShimmerPlaceholder()
    else
        LazyVerticalGrid(
            state = state,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            dataItems.forEachIndexed { _, data ->
                when (data.type) {
                    HeaderRecyclerAdapterConstants.BANNER -> {
                        data.bannerData?.let {
                            item(span = { GridItemSpan(2) }) {
                                ProductBannerItem(
                                    banner = it,
                                    onBannerClick = {
                                        onBannerClick(it)
                                    },
                                    onBannerToProductClick = { onBannerToProductClick(it) },
                                    onBannerToBottomSheetClick = { onBannerToBottomSheetClick() },
                                    onBannerToVideoClick = { onBannerToVideoClick(it) }
                                )
                            }
                        }
                    }

                    HeaderRecyclerAdapterConstants.HEADER -> {
                        data.parentData?.name?.let {
                            item(span = { GridItemSpan(2) }) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = it,
                                    color = TextSubdued,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    HeaderRecyclerAdapterConstants.ITEM -> {
                        data.childData?.let {
                            item {
                                val subCategory = data.parentData?.name.orEmpty()
                                SingleProductItem(
                                    productList = true,
                                    data = it,
                                    cardClick = {
                                        onProductClick(it, subCategory)
                                    },
                                    onAddClick = {
                                        onAddClick(it, subCategory)
                                    },
                                    displayName = it.productDisplayName(),
                                    displayImage = it.productDisplayImage()
                                )
                            }
                        }
                    }
                }
            }
        }
}