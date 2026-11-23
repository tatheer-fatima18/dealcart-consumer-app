package com.localpk.app.features.cricket_live.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadComposableAsyncImage
import com.localpk.app.features.cricket_live.data.PoweredByDetailUiData
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.productlist.data.getProduct
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder

@Composable
fun PoweredByList(
    poweredByList: List<PoweredByDetailUiData>,
    onNavigateToProductHost: (Long) -> Unit,
    onNavigateToProductDetail: (ProductUiData) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight().padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(poweredByList) { item ->
            Card(
                modifier = Modifier
                    .width(72.dp)
                    .height(72.dp)
                    .padding(4.dp)
                    .clickable {
                        when (item.redirectType) {
                            "category" -> onNavigateToProductHost(item.redirectTypeId.getValueOrDefault())
                            "product", "deal" -> onNavigateToProductDetail(
                                getProduct(item.redirectTypeId.getValueOrDefault())
                            )
                        }
                    },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                loadComposableAsyncImage(
                    imageUrl = item.image?.thumbnail,
                    fallbackRes = Res.drawable.ic_dealcart_placeholder,
                    modifier = Modifier
                        .fillMaxSize().padding(6.dp)
                )
            }
        }
    }
}
