package com.localpk.app.features.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.swimlane_engine.data.SwimlaneType
import com.localpk.app.common_components.SingleProductItem
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.ButtonSurfaceBrand

@Composable
fun PastOrders(
    recommendationList: List<ProductUiData>,
    onActionViewAll: () -> Unit,
    onAddClick: (ProductUiData) -> Unit,
    onIncrement: (ProductUiData) -> Unit,
    onDecrement: (ProductUiData) -> Unit,
    onDelete: (ProductUiData) -> Unit,
    onCardClick: (ProductUiData) -> Unit,
    recommendationTitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(top = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .weight(1f),
                text = recommendationTitle,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = TextSubdued,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .clickable {
                        onActionViewAll()
                    },
                text = "View All",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = ButtonSurfaceBrand,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
            )
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recommendationList) {
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
                    },
                    swimlaneType = SwimlaneType.HOME
                )
            }
        }
    }
} 