package com.localpk.app.features.product_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.SingleProductItem
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.swimlane_engine.data.SwimlaneType
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.frequently_bought_together
import org.jetbrains.compose.resources.stringResource

@Composable
fun FrequentlyBoughtTogetherComponent(
    recommendationList: List<ProductUiData>,
    cardClick: (product: ProductUiData) -> Unit = {},
    onAddClick: (product: ProductUiData) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
            text = stringResource(Res.string.frequently_bought_together),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = TextSubdued,
            )
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recommendationList) {
                SingleProductItem(
                    productList = false,
                    data = it,
                    cardClick = {
                        cardClick(it)
                    },
                    onAddClick = {
                        onAddClick(it)
                    },
                    swimlaneType = SwimlaneType.NEWLY_ADDED
                )
            }
        }
    }
}