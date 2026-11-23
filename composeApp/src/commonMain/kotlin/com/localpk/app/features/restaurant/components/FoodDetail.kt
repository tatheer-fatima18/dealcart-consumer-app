package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.product_detail.components.Header
import com.localpk.app.features.restaurant.data.CategoryItemUIDataAndRestaurantDetails
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.theming.TextSuccess
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.fallback
import dealcart.composeapp.generated.resources.instructions_hint
import dealcart.composeapp.generated.resources.instructions_subtext
import dealcart.composeapp.generated.resources.special_instructions
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoodDetail(
    argsData: CategoryItemUIDataAndRestaurantDetails,
    onBack: () -> Unit,
    onSearch: () -> Unit,
    selectedVariationId: Long,
    onVariationSelected: (Long) -> Unit,
    selectedChoicesIds: Map<Long, List<Long>>,
    onChoicesSelected: (Map<Long, List<Long>>) -> Unit,
    specialInstructions: String,
    onInstructionsChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
    ) {
        Header(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, end = 8.dp)
                .border(
                    BorderStroke(width = 1.dp, color = BorderPrimaryDefault),
                    shape = RoundedCornerShape(8.dp)
                )
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            onBackClick = { onBack() },
            onSearchClick = { onSearch() }
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(0.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .border(
                        BorderStroke(width = 1.dp, color = BorderPrimaryDefault),
                        RoundedCornerShape(8.dp)
                    )
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                loadImagePainterWithValidation(
                    imageUrl = argsData.image?.medium,
                    fallbackRes = Res.drawable.fallback,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
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
                            text = argsData.name.default(),
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                color = TextSubdued
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (argsData.prices?.listPrice != null && argsData.prices.listPrice > argsData.prices.discountPrice.getValueOrDefault()) {
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
                                text = argsData.percentageOff(),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = PoppinsFontFamily(),
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
                            text = if (argsData.variations.isNullOrEmpty()
                                    .not()
                            ) "From ${argsData.formattedDiscountedAmount()}"
                            else argsData.formattedDiscountedAmount(),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                color = TextSubdued
                            )
                        )
                        if (argsData.prices?.listPrice != null && argsData.prices.listPrice > argsData.prices.discountPrice.getValueOrDefault()) {
                            Text(
                                text = argsData.formattedTotalAmount(),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    color = TextSubdued,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            )
                        }
                    }
                }
                if (argsData.description.default().isEmpty().not()) {
                    Text(
                        modifier = Modifier,
                        text = argsData.description.default(),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            color = TextSubduedHalf
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            if (argsData.hasVariationsOrChoiceGroups())
                VariationSelector(
                    variations = argsData.variations,
                    choiceGroups = argsData.choiceGroups,
                    selectedVariationId = selectedVariationId,
                    onVariationSelected = onVariationSelected,
                    selectedChoicesIds = selectedChoicesIds,
                    onChoicesSelected = onChoicesSelected
                )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = stringResource(Res.string.special_instructions),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubdued
                    ),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(Res.string.instructions_subtext),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubduedHalf,
                        lineHeight = 13.sp
                    ),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    BasicTextField(
                        value = specialInstructions,
                        onValueChange = {
                            if (it.length <= 500) onInstructionsChanged(it)
                        },
                        textStyle = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = TextSubdued,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp)
                            .padding(8.dp),
                        maxLines = Int.MAX_VALUE,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Default,
                            keyboardType = KeyboardType.Text
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                if (specialInstructions.isEmpty()) {
                                    Text(
                                        text = stringResource(Res.string.instructions_hint),
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontFamily = PoppinsFontFamily(),
                                            color = TextSubduedHalf,
                                            fontWeight = FontWeight.Normal
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }
        }
    }
}

