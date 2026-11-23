package com.localpk.app.features.product_detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.GlobalToastManager
import com.localpk.app.common_components.SnackbarType
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.product_detail.viewModel.ProductDetailViewModel
import com.localpk.app.features.productlist.data.AttributeUiData
import com.localpk.app.features.productlist.data.VariantDetail
import com.localpk.app.theming.AppTypography
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.SurfaceBackgroundGhosted
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_image
import dealcart.composeapp.generated.resources.ic_sold_out
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProductVariantComponent(
    attributes: List<AttributeUiData>,
    selectedVariantDetailsLiveData: List<VariantDetail>?,
    productDetailViewModel: ProductDetailViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            attributes.forEach {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 12.dp),
                        text = it.name,
                        style = AppTypography().bodyMedium.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSubdued
                        )
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        items(it.options) { option ->
                            val onItemClick = {
                                if (option.isDisabled.not()) {
                                    selectedVariantDetailsLiveData?.forEach { variantDetail ->
                                        if (it.id == variantDetail.attributeId) {
                                            variantDetail.attributeId = it.id
                                            variantDetail.optionId = option.id
                                        }
                                    }
                                    selectedVariantDetailsLiveData?.let { selectedVariantDetails ->
                                        productDetailViewModel.getVariantProductId(
                                            selectedVariantDetails
                                        )
                                    }
                                } else
                                    GlobalToastManager.showToast(
                                        message = "Sorry this item is out of stock."
                                    )
                            }
                            if (option.showImage.not()) {
                                val textBackground: Color
                                val textColor: Color
                                val textFontWeight: FontWeight
                                val textDecoration: TextDecoration

                                if (option.isSelected && option.isDisabled.not()) {
                                    textBackground = ButtonSurfaceBrand
                                    textColor = Color.White
                                    textFontWeight = FontWeight.Bold
                                    textDecoration = TextDecoration.None
                                } else if (option.isDisabled) {
                                    textBackground = SurfaceBackgroundGhosted
                                    textColor = TextSubduedHalf
                                    textFontWeight = FontWeight.Normal
                                    textDecoration = TextDecoration.LineThrough
                                } else {
                                    textBackground = SurfaceBackground
                                    textColor = TextSubdued
                                    textFontWeight = FontWeight.Normal
                                    textDecoration = TextDecoration.None
                                }


                                Text(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            color = textBackground
                                        )
                                        .clickable {
                                            onItemClick()
                                        }
                                        .padding(all = 12.dp),
                                    text = option.optionName,
                                    style = AppTypography().bodyMedium.copy(
                                        fontSize = 12.sp,
                                        color = textColor,
                                        fontWeight = textFontWeight,
                                        textDecoration = textDecoration
                                    )
                                )
                            } else {
                                val borderWidth: Dp
                                val imagePadding: Dp
                                val imageClip: Dp
                                val borderColor: Color
                                if (option.isSelected) {
                                    borderWidth = 2.dp
                                    imagePadding = 4.dp
                                    imageClip = 4.dp
                                    borderColor = ButtonSurfaceBrand
                                } else {
                                    borderWidth = 0.dp
                                    imagePadding = 0.dp
                                    imageClip = 8.dp
                                    borderColor = Color.Transparent
                                }
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    loadImagePainterWithValidation(
                                        modifier = Modifier
                                            .padding(1.5.dp)
                                            .width(40.dp)
                                            .height(40.dp)
                                            .border(
                                                BorderStroke(
                                                    width = borderWidth,
                                                    color = borderColor
                                                ),
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(imagePadding)
                                            .clip(RoundedCornerShape(imageClip))
                                            .clickable {
                                                onItemClick()
                                            },
                                        fallbackRes = Res.drawable.ic_image,
                                        imageUrl = option.imageUrl,
                                        contentDescription = null
                                    )
                                    if (option.isDisabled) {
                                        Image(
                                            modifier = Modifier.size(26.dp),
                                            painter = painterResource(Res.drawable.ic_sold_out),
                                            contentDescription = null,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}