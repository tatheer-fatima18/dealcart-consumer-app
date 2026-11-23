package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.restaurant.data.MenuCategoryUI
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.default

@Composable
fun TabRowSectionFood(
    menu: List<MenuCategoryUI>,
    selectedTabIndex: MutableIntState,
    onClick: (Int) -> Unit
) {
    ScrollableTabRow(
        modifier = Modifier.background(color = Color.White),
        selectedTabIndex = selectedTabIndex.intValue,
        edgePadding = 8.dp,
        indicator = { tabPositions ->
            SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex.intValue]),
                color = ButtonSurfaceBrand
            )
        },
        divider = {}
    ) {
        menu.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier
                    .padding(0.dp)
                    .background(color = Color.White),
                selected = selectedTabIndex.intValue == index,
                onClick = { onClick(index) },
                text = {
                    Text(
                        text = title.name.default(),
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily(),
                            color = if (selectedTabIndex.intValue == index) ButtonSurfaceBrand else TextSubduedHalf,
                            fontSize = 14.sp,
                            fontWeight = if (selectedTabIndex.intValue == index) FontWeight.SemiBold else FontWeight.Normal

                        ),
                    )
                },
            )
        }
    }
}