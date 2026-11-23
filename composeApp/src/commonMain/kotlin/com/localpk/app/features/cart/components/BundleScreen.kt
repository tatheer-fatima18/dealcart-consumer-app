package com.localpk.app.features.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.CartBundle
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextGray60
import com.localpk.app.theming.TextGray70
import com.localpk.app.theming.TextGray90
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.orEmptyArrayList
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.hi_order_kar_sakte_hain
import dealcart.composeapp.generated.resources.ic_cross_review_sheet
import dealcart.composeapp.generated.resources.items_aur_available
import dealcart.composeapp.generated.resources.items_out_of_stock
import dealcart.composeapp.generated.resources.keep_x_bags_only
import dealcart.composeapp.generated.resources.mojuda_combinations_mei
import dealcart.composeapp.generated.resources.x_bundles
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BundleScreen(
    outOfStockData: CartBundle?,
    onDismissRequest: () -> Unit
) {
    Box {
        IconButton(
            onClick = onDismissRequest,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_cross_review_sheet),
                contentDescription = null,
                tint = TextGray60
            )
        }
        Column(
            Modifier
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 33.dp),
                text = stringResource(Res.string.items_out_of_stock),
                style = TextStyle(
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextGray90
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = buildAnnotatedString {
                    append(stringResource(Res.string.mojuda_combinations_mei))
                    append(" ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(
                            stringResource(
                                Res.string.x_bundles,
                                outOfStockData?.maxBundles.getValueOrDefault()
                            )
                        )
                    }
                    append(" ")
                    append(stringResource(Res.string.hi_order_kar_sakte_hain))
                    append("\n")
                    append(stringResource(Res.string.items_aur_available))

                },
                style = TextStyle(
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 12.65.sp,
                    color = TextGray70,
                    textAlign = TextAlign.Center
                )
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(10.dp)
            ) {
                items(outOfStockData?.productOOSDetails.orEmptyArrayList()) {
                    OutOfStockItem(it)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
                    .padding(bottom = 25.dp, top = 6.dp)
                    .clickable {
                        onDismissRequest()
                    }
                    .height(32.dp)
                    .background(
                        color = ColorPrimary,
                        shape = RoundedCornerShape(32.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        Res.string.keep_x_bags_only,
                        outOfStockData?.maxBundles.getValueOrDefault()
                    ),
                    style = TextStyle(
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}