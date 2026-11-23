package com.localpk.app.features.checkout.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.CustomCheckbox
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.ZERO_STRING
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotZero
import com.localpk.app.utils.isNull
import com.localpk.app.utils.isZero
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.enter_amount
import dealcart.composeapp.generated.resources.not_now
import dealcart.composeapp.generated.resources.save_next_time
import dealcart.composeapp.generated.resources.support
import dealcart.composeapp.generated.resources.support_dealcart
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DealCartSupportComponent(
    isSaveFeeChecked: Boolean,
    saveCustomAmount: Int?,
    contributionValue: Int,
    onContributionSelected: (Int?) -> Unit,
    onSaveCustomAmountChanged: (Int) -> Unit,
    onSaveFeeCheckedChanged: (Boolean) -> Unit,
    onButtonSelectedChanged: (Boolean) -> Unit,
    onLearnMoreClick: () -> Unit
) {
    val contributionValues = RemoteConfigs.getContributionValues()

    var text by remember { mutableStateOf(EMPTY_STRING) }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var selectedContributionValue by remember { mutableStateOf<Int?>(null) }

    if (isSaveFeeChecked) {
        if (saveCustomAmount.isNotZero()) {
            text = saveCustomAmount.toString()
        } else if (selectedContributionValue.isNull()) {
            if (contributionValue == ZERO_INT) {
                selectedContributionValue = ZERO_INT
                onContributionSelected(ZERO_INT)
                onButtonSelectedChanged(true)
            } else {
                contributionValues.forEach {
                    if (contributionValue == it.contribution) {
                        selectedContributionValue = it.contribution
                        onContributionSelected(it.contribution)
                        onButtonSelectedChanged(true)
                    }
                }
            }
        }
    }
    else if (selectedContributionValue.isNull()) {
        selectedContributionValue = ZERO_INT
        onContributionSelected(ZERO_INT)
        onButtonSelectedChanged(true)
    }

    val annotatedString = buildAnnotatedString {
        append(RemoteConfigs.getContributionTextEnabled())
        append(" ")
        pushStringAnnotation(tag = "CLICKABLE", annotation = "Clicked!")
        withStyle(
            style = SpanStyle(
                color = ButtonSurfaceBrand,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("\nLearn more")
        }
        pop()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(
                color = Color.White,
                RoundedCornerShape(8.dp)
            )
            .padding(all = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.support),
                contentDescription = null,
                modifier = Modifier
                    .width(44.dp)
                    .height(38.dp)
            )
            Text(
                text = stringResource(Res.string.support_dealcart),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = ButtonSurfaceBrand,
                )
            )
        }
        ClickableText(
            text = annotatedString,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                color = TextSubdued,
            ),
            onClick = { offset ->
                annotatedString.getStringAnnotations(
                    tag = "CLICKABLE",
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    onLearnMoreClick()
                }
            }
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    modifier = Modifier
                        .border(
                            border = BorderStroke(
                                color = if (selectedContributionValue.isZero()) ButtonSurfaceBrand else BorderPrimaryDefault,
                                width = 1.dp
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = if (selectedContributionValue.isZero()) ButtonSurfaceBrand else SurfaceBackground,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            focusManager.clearFocus()
                            text = EMPTY_STRING
                            selectedContributionValue = ZERO_INT
                            onContributionSelected(ZERO_INT)
                            onSaveCustomAmountChanged(ZERO_INT)
                            onButtonSelectedChanged(true)
                        }
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    text = stringResource(Res.string.not_now),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = if (selectedContributionValue.isZero()) Color.White else TextSubduedHalf,
                    ),
                )
            }
            items(contributionValues.size) { index ->
                val item = contributionValues[index]
                Text(
                    modifier = Modifier
                        .border(
                            border = BorderStroke(
                                color = if (selectedContributionValue == item.contribution) ButtonSurfaceBrand else BorderPrimaryDefault,
                                width = 1.dp
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = if (selectedContributionValue == item.contribution) ButtonSurfaceBrand else SurfaceBackground,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            focusManager.clearFocus()
                            text = EMPTY_STRING
                            selectedContributionValue = item.contribution
                            onContributionSelected(item.contribution)
                            onSaveCustomAmountChanged(ZERO_INT)
                            onButtonSelectedChanged(true)
                        }
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    text = "Rs. ${item.contribution}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = if (selectedContributionValue == item.contribution) Color.White else TextSubduedHalf,
                    ),
                )
            }
            item {
                BasicTextField(
                    value = text,
                    onValueChange = { newText ->
                        val filteredText = newText.filter { it.isDigit() }.take(3)
                        text = filteredText
                            if (filteredText.isEmpty()) onContributionSelected(ZERO_INT) else onContributionSelected(filteredText.toInt())
                            if (filteredText.isEmpty()) onButtonSelectedChanged(false) else onButtonSelectedChanged(filteredText.toInt() > 0)
                            if (filteredText.isEmpty()) onSaveCustomAmountChanged(ZERO_INT) else onSaveCustomAmountChanged(filteredText.toInt())
                        selectedContributionValue =
                            if (filteredText.isEmpty()) null else if (filteredText.toInt() > 0) filteredText.toInt() else null
                    },
                    modifier = Modifier
                        .border(
                            BorderStroke(
                                width = 1.dp,
                                color = if (isFocused || saveCustomAmount.getValueOrDefault() > ZERO_INT) ButtonSurfaceBrand else BorderPrimaryDefault
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp) // Inner padding
                        .onFocusChanged { focusState ->
                            selectedContributionValue = null
                            isFocused = focusState.isFocused
                            if (focusState.isFocused) {
                                if (text.isEmpty() || text == ZERO_STRING) onButtonSelectedChanged(
                                    false
                                ) else onButtonSelectedChanged(text.toInt() > 0)
                                if (text.isEmpty()) onContributionSelected(ZERO_INT) else onContributionSelected(
                                    text.toInt()
                                )
                                if (text.isEmpty()) onSaveCustomAmountChanged(ZERO_INT) else onSaveCustomAmountChanged(
                                    text.toInt()
                                )
                            }
                        },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubduedHalf
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = stringResource(Res.string.enter_amount),
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontFamily = PoppinsFontFamily(),
                                        color = TextSubduedHalf
                                    )
                                )
                            }
                            innerTextField() // Places the text field
                        }
                    }
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            thickness = 1.dp,
            color = BorderPrimaryDefault
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomCheckbox(
                checked = isSaveFeeChecked,
                onCheckedChange = onSaveFeeCheckedChanged,
                size = 20.dp
            )
            Text(
                text = stringResource(Res.string.save_next_time),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = TextSubdued
                )
            )
        }
    }
}
