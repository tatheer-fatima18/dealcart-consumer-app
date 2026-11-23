package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.restaurant.data.ItemChoiceGroup
import com.localpk.app.features.restaurant.data.ItemVariation
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackgroundWaning
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.theming.indigo
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.free
import dealcart.composeapp.generated.resources.required
import dealcart.composeapp.generated.resources.select_variation
import org.jetbrains.compose.resources.stringResource

@Composable
fun VariationSelector(
    variations: List<ItemVariation>?,
    choiceGroups: List<ItemChoiceGroup>?,
    selectedVariationId: Long,
    onVariationSelected: (Long) -> Unit,
    selectedChoicesIds: Map<Long, List<Long>>,
    onChoicesSelected: (Map<Long, List<Long>>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        if (variations.isNullOrEmpty().not()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(indigo, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.select_variation),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            color = TextSubdued
                        )
                    )
                    Text(
                        text = stringResource(Res.string.required),
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = IconDanger,
                        ),
                        modifier = Modifier
                            .background(
                                SurfaceBackgroundWaning,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                variations?.forEachIndexed { _, variation ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onVariationSelected(variation.id) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(16.dp)
                                    .padding(start = 11.dp)
                                    .height(35.dp)
                            ) {
                                RadioButton(
                                    selected = (selectedVariationId == variation.id),
                                    onClick = { onVariationSelected(variation.id) },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = ButtonSurfaceBrand,
                                        unselectedColor = TextSubduedHalf,
                                        disabledSelectedColor = ButtonSurfaceBrand,
                                        disabledUnselectedColor = TextSubduedHalf,
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = variation.name.default(),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    color = if (selectedVariationId == variation.id) ButtonSurfaceBrand else TextSubdued,
                                    fontWeight = if (selectedVariationId == variation.id) FontWeight.Medium else FontWeight.Normal
                                ),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }

                        Text(
                            text = if (variation.prices?.discountPrice.getValueOrDefault()
                                    .toInt() == 0
                            ) stringResource(Res.string.free) else variation.formattedVariationAmount(),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily(),
                                color = TextSubdued,
                                fontWeight = FontWeight.Normal
                            ),
                        )
                    }
                }
            }
        }
        if (choiceGroups.isNullOrEmpty().not()) {
            Spacer(modifier = Modifier.height(12.dp))
            choiceGroups?.forEachIndexed { _, choiceGroup ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(indigo, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(.1f),
                            text = choiceGroup.name.default(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                color = TextSubdued
                            )
                        )
                        if (choiceGroup.isRequired.default()) {
                            Text(
                                text = stringResource(Res.string.required),
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.Normal,
                                    color = IconDanger,
                                ),
                                modifier = Modifier
                                    .background(
                                        SurfaceBackgroundWaning,
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        if (choiceGroup.maxSelectable.isNotNull() && choiceGroup.minSelectable.isNotNull()) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = choiceGroup.options(),
                                style = TextStyle(
                                    fontSize = 11.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextSubdued
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    choiceGroup.choices?.forEachIndexed { _, choice ->
                        ChoiceGroupSelector(
                            choice = choice,
                            choiceGroup = choiceGroup,
                            selectedChoicesIds = selectedChoicesIds,
                            onChoicesSelected = onChoicesSelected
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}