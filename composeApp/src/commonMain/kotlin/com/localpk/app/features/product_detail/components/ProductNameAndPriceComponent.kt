package com.localpk.app.features.product_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.AppTypography
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSuccess

@Composable
fun ProductNameAndPriceComponent(
    showMrp: Boolean,
    productName: String,
    productPrice: String,
    productDiscount: String,
    productActualPrice: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = productName,
                    style = AppTypography().bodyMedium.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSubdued
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (showMrp.not()) {
                    Text(
                        modifier = Modifier
                            .background(
                                color = TextSuccess,
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 8.dp,
                                    bottomStart = 8.dp
                                )
                            )
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        text = productDiscount,
                        style = AppTypography().bodyMedium.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = productPrice,
                    style = AppTypography().bodyMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSubdued
                    )
                )
                if (showMrp.not()) {
                    Text(
                        text = productActualPrice,
                        style = AppTypography().bodyMedium.copy(
                            fontSize = 12.sp,
                            color = TextSubdued,
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                }
            }
        }
    }
}