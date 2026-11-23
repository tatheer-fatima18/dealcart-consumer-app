package com.localpk.app.features.productlist.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.category.data.CategoryUiModel
import com.localpk.app.features.productlist.data.HeaderRecyclerAdapterConstants
import com.localpk.app.features.productlist.data.ProductHeaderAdapterUiData
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import kotlinx.coroutines.launch

@Composable
fun SubCategoryTabs(
    listState: LazyGridState,
    selectedTabIndex: MutableIntState,
    categories: List<CategoryUiModel>,
    dataItems: List<ProductHeaderAdapterUiData>,
    onClick: (String, Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    if (categories.isEmpty()) return

    val safeTabIndex = selectedTabIndex.intValue.coerceIn(categories.indices)

    LaunchedEffect(categories) {
        if (selectedTabIndex.intValue !in categories.indices && categories.isNotEmpty()) {
            selectedTabIndex.intValue = 0
        }
    }

    ScrollableTabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = safeTabIndex,
        edgePadding = 0.dp,
        containerColor = Color.White,
        indicator = { tabPositions ->
            tabPositions.getOrNull(safeTabIndex)?.let { tabPosition ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPosition),
                    color = ButtonSurfaceBrand,
                    height = 2.dp
                )
            }
        },
        divider = {}
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = safeTabIndex == index,
                onClick = {
                    onClick(category.name, category.id.toInt())
                    selectedTabIndex.intValue = index.coerceIn(categories.indices)
                    coroutineScope.launch {
                        val tabIndex =
                            dataItems.firstOrNull { it.parentData?.id == category.id }?.parentPosition
                        val isBanner =
                            dataItems.any { it.type == HeaderRecyclerAdapterConstants.BANNER }
                        if (isBanner && index == 0) {
                            listState.animateScrollToItem(0)
                        } else if (tabIndex.isNotNull() && tabIndex != -1) {
                            listState.animateScrollToItem(tabIndex.getValueOrDefault())
                        }
                    }
                },
                text = {
                    Text(
                        text = category.name,
                        style = TextStyle(
                            fontWeight = if (safeTabIndex == index) FontWeight.Medium else FontWeight.Normal,
                            fontFamily = PoppinsFontFamily(),
                            fontSize = 14.sp
                        )
                    )
                },
                selectedContentColor = ButtonSurfaceBrand,
                unselectedContentColor = TextSubdued
            )
        }
    }
}