package com.localpk.app.features.home.components.swimlanes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.SingleProductItem
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.swimlane_engine.data.SwimlaneType
import com.localpk.app.theming.DarkPink
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.theming.backgroundGreen
import org.jetbrains.compose.resources.painterResource
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.promotional_icon
import dealcart.composeapp.generated.resources.see_all
import org.jetbrains.compose.resources.stringResource

@Composable
fun DiscountSwimlaneComponent(
    products: List<ProductUiData>,
    title: String,
    subTitle: String,
    viewAllClick: () -> Unit,
    swimlaneType: SwimlaneType?,
    onCardClick: (ProductUiData) -> Unit,
    onAddClick: (ProductUiData) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (swimlaneType != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        vertical = if (swimlaneType.type == SwimlaneType.UNDER_AMOUNT.type || swimlaneType.type == SwimlaneType.MAX_ABS_DISCOUNT.type || swimlaneType.type == SwimlaneType.BEST_SELLING.type) 0.dp else 8.dp,
                        horizontal = if (swimlaneType.type == SwimlaneType.UNDER_AMOUNT.type || swimlaneType.type == SwimlaneType.MAX_ABS_DISCOUNT.type || swimlaneType.type == SwimlaneType.BEST_SELLING.type) 8.dp else 0.dp
                    )
                    .padding(start = if (swimlaneType.type == SwimlaneType.UNDER_AMOUNT.type || swimlaneType.type == SwimlaneType.MAX_ABS_DISCOUNT.type || swimlaneType.type == SwimlaneType.BEST_SELLING.type) 2.dp else 0.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .background(
                            if ((swimlaneType.type == SwimlaneType.MIN_PERCENTAGE_OFF.type || swimlaneType.type == SwimlaneType.MIN_AMOUNT_OFF.type)) backgroundGreen else Color.White,
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(horizontal = if ((swimlaneType.type == SwimlaneType.MIN_PERCENTAGE_OFF.type || swimlaneType.type == SwimlaneType.MIN_AMOUNT_OFF.type)) 11.dp else 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if ((swimlaneType.type == SwimlaneType.MIN_PERCENTAGE_OFF.type || swimlaneType.type == SwimlaneType.MIN_AMOUNT_OFF.type)) {
                                Image(
                                    painter = painterResource(Res.drawable.promotional_icon), // Placeholder icon
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(bottom = 2.dp)
                                        .width(25.dp)
                                        .height(25.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                            Column(
                                modifier = Modifier.padding(horizontal = 3.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    modifier = Modifier.width(250.dp),
                                    text = title,
                                    style = TextStyle(
                                        fontSize = if (swimlaneType.type == SwimlaneType.MIN_PERCENTAGE_OFF.type || swimlaneType.type == SwimlaneType.MIN_AMOUNT_OFF.type) 18.sp else 16.sp,
                                        lineHeight = if (swimlaneType.type == SwimlaneType.MIN_PERCENTAGE_OFF.type || swimlaneType.type == SwimlaneType.MIN_AMOUNT_OFF.type) 24.sp else 16.sp,
                                        fontFamily = PoppinsFontFamily(),
                                        fontWeight = if (swimlaneType.type == SwimlaneType.MIN_PERCENTAGE_OFF.type || swimlaneType.type == SwimlaneType.MIN_AMOUNT_OFF.type) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (swimlaneType.type == SwimlaneType.MIN_PERCENTAGE_OFF.type || swimlaneType.type == SwimlaneType.MIN_AMOUNT_OFF.type) DarkPink else TextSubdued,

                                        ),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                                if (subTitle.isNotEmpty()) {
                                    Text(
                                        modifier = Modifier
                                            .width(280.dp)
                                            .padding(bottom = 8.dp),
                                        text = subTitle,
                                        style = TextStyle(
                                            fontSize = 11.sp,
                                            lineHeight = 11.sp,
                                            fontFamily = PoppinsFontFamily(),
                                            fontWeight = FontWeight.Normal,
                                            color = TextPrimary,

                                            ),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                }
                            }
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
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            itemsIndexed(products) { _, product ->
                                SingleProductItem(
                                    productList = false,
                                    data = product,
                                    cardClick = {
                                        onCardClick(product)
                                    },
                                    onAddClick = {
                                        onAddClick(product)
                                    },
                                    swimlaneType = swimlaneType
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
} 