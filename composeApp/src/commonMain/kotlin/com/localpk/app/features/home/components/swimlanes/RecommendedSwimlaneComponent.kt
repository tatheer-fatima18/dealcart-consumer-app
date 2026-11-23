package com.localpk.app.features.home.components.swimlanes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.SingleProductItem
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.swimlane_engine.data.SwimlaneType
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.see_all
import org.jetbrains.compose.resources.stringResource

@Composable
fun RecommendedSwimlaneComponent(
    title: String,
    isRecommended: Boolean,
    recommendedItems: List<ProductUiData>,
    onProductClick: (ProductUiData) -> Unit,
    onAddClick: (ProductUiData) -> Unit,
    viewAllClick: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .padding(start = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    color = TextSubdued,

                    )
            )

            if (isRecommended.not()) {
                Spacer(modifier = Modifier.weight(.1f))
                Text(
                    modifier = Modifier
                        .clickable {
                            viewAllClick()
                        },
                    text = stringResource(Res.string.see_all),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        color = TextSubduedHalf,
                    )
                )
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(recommendedItems) { _, it ->
                SingleProductItem(
                    productList = false,
                    data = it,
                    cardClick = {
                        onProductClick(it)
                    },
                    onAddClick = {
                        onAddClick(it)
                    },
                    swimlaneType = SwimlaneType.HOME
                )
            }
        }
    }
}

