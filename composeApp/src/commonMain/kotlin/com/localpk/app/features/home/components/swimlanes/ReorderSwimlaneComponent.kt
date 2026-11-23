package com.localpk.app.features.home.components.swimlanes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.common_components.SingleProductItem
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.frequently_ordered
import dealcart.composeapp.generated.resources.see_all
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReOrderComposeView(
    frequentItems: List<ProductUiData>,
    viewAllClick: () -> Unit,
    onProductClick: (ProductUiData) -> Unit,
    onAddClick: (ProductUiData) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .padding(start = 2.dp)
    ) {
        ReorderSwimlaneComponent(
            frequentItems = frequentItems,
            viewAllClick = {
                viewAllClick()
            },
            onProductClick = {
                onProductClick(it)
            },
            onAddClick = {
                onAddClick(it)
            }
        )
    }
}

@Composable
fun ReorderSwimlaneComponent(
    frequentItems: List<ProductUiData>,
    viewAllClick: () -> Unit,
    onProductClick: (ProductUiData) -> Unit,
    onAddClick: (ProductUiData) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(Res.string.frequently_ordered),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    color = TextSubdued,
                )
            )
            Spacer(modifier = Modifier.weight(.1f))
            Text(
                modifier = Modifier
                    .clickable { viewAllClick() },
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
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(frequentItems) { _, it ->
                SingleProductItem(
                    productList = false,
                    data = it,
                    cardClick = {
                        onProductClick(it)
                    },
                    onAddClick = {
                        onAddClick(it)
                    },
                )
            }
        }
    }
} 