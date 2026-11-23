package com.localpk.app.features.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.swimlane_engine.data.SwimlaneType
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import com.localpk.app.common_components.SingleProductItem
import dealcart.composeapp.generated.resources.buy_together_cart

@Composable
fun RecommendedEngineSwimlane(
    recommendationList: List<ProductUiData>?,
    cardClick: (ProductUiData) -> Unit,
    onAddClick: (ProductUiData) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp),
            text = stringResource(Res.string.buy_together_cart),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = TextSubdued,
            )
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(recommendationList.orEmpty()) {
                SingleProductItem(
                    productList = false,
                    data = it,
                    cardClick = { cardClick(it) },
                    onAddClick = { onAddClick(it) },
                    swimlaneType = SwimlaneType.HOME,
                    isCart = true
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}