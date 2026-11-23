package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.CustomCheckbox
import com.localpk.app.features.restaurant.data.ItemChoiceGroup
import com.localpk.app.features.restaurant.data.ItemVariation
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.free
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChoiceGroupSelector(
    choice: ItemVariation,
    choiceGroup: ItemChoiceGroup,
    selectedChoicesIds: Map<Long, List<Long>>,
    onChoicesSelected: (Map<Long, List<Long>>) -> Unit
) {
    val selectedIds = selectedChoicesIds[choiceGroup.id] ?: emptyList()
    val isSelected = selectedIds.contains(choice.id)
    val canSelectMore = when (val max = choiceGroup.maxSelectable) {
        null -> true
        else -> selectedIds.size < max || isSelected
    }

    val toggleSelection = {
        val newSelectedIds = selectedIds.toMutableList().apply {
            if (!isSelected && canSelectMore) {
                add(choice.id)
            } else {
                remove(choice.id)
            }
        }
        val newMap = selectedChoicesIds.toMutableMap().apply {
            put(choiceGroup.id, newSelectedIds)
        }
        onChoicesSelected(newMap)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = canSelectMore || isSelected) {
                toggleSelection()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CustomCheckbox(
                checked = isSelected,
                onCheckedChange = { toggleSelection() },
                enabled = canSelectMore || isSelected
            )

            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = choice.name.default(),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = if (isSelected) ButtonSurfaceBrand else TextSubdued,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

        Text(
            text = if (choice.price.getValueOrDefault().toInt() == 0)
                stringResource(Res.string.free)
            else
                "+${choice.formattedAmount()}",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                color = TextSubdued,
                fontWeight = FontWeight.Normal
            ),
        )
    }
}
