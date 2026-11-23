package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.restaurant.data.FoodOrderDetailUIData
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.items_in_total
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoodItemsCard(
    data: FoodOrderDetailUIData?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BorderPrimaryDefault,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .padding(all = 12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start

            ) {

                Text(
                    text = "${data?.originalOrderItemsCount.getValueOrDefault()} Item(s) in total",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = TextSubdued
                )

                if (data?.salesOrderItems.isNullOrEmpty().not()) {
                    data.salesOrderItems.forEachIndexed { _, it ->
                        Column(
                            modifier = Modifier
                                .padding(vertical = 3.dp)
                        ) {
                            SingleFoodProduct(singleProduct = it)
                        }
                    }
                }
            }
        }
    }
}
