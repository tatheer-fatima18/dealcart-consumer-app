package com.localpk.app.features.cart.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.localpk.app.features.cart.data.ProductOOSDetails
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.theming.Cart_Item_Image_Border
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextGray
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.theming.TextSuccess
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.default
import com.localpk.app.utils.isNotNull
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder
import dealcart.composeapp.generated.resources.total_available
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CartOOSProduct(model: ProductOOSDetails) {

    val imageUrl = model.product?.images?.firstOrNull()?.let {
        it.thumbnail ?: it.path ?: EMPTY_STRING
    }

    if (model.isNotNull()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(72.dp)
            ) {
                loadImagePainterWithValidation(
                    imageUrl = imageUrl,
                    fallbackRes = Res.drawable.ic_dealcart_placeholder,
                    contentDescription = null,
                    modifier = Modifier
                        .matchParentSize()
                        .padding(4.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(11.dp)
                        )
                        .border(
                            BorderStroke(2.dp, color = Cart_Item_Image_Border),
                            shape = RoundedCornerShape(11.dp)
                        ),
                    loadingContent = {
                        Box(
                            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.ic_dealcart_placeholder),
                                contentDescription = null
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Column(
                modifier = Modifier
                    .padding(start = 4.dp, end = 12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = model.product?.name.default(),
                    fontSize = 12.sp,
                    color = TextGray,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 12.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.total_available),
                        fontSize = 11.sp,
                        color = TextSubduedHalf,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = model.availableQuantity.toString(),
                        fontSize = 11.sp,
                        color = TextSuccess,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = "Ordered: ${model.requestedQuantity}",
                    fontSize = 11.sp,
                    color = TextSubduedHalf,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
