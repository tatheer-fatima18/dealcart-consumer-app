package com.localpk.app.features.address.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.isNotNullOrBlank
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.edit_icon
import dealcart.composeapp.generated.resources.number_hint
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EditablePhoneField(
    value: String,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        value = value,
        onValueChange = { newText ->
            val digitsOnly = newText.filter { it.isDigit() }

            val trimmed = if (digitsOnly.startsWith("0")) {
                digitsOnly.take(11)
            } else {
                digitsOnly.take(10)
            }

           onValueChange(trimmed)
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = if (isFocused || value.isNotNullOrBlank()) ButtonSurfaceBrand else BorderPrimaryDefault
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 14.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 12.sp,
            fontFamily = PoppinsFontFamily(),
            color = TextPrimary
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "+92",
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.number_hint),
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = TextSubduedHalf
                        )
                    }
                    innerTextField()
                }

                Image(
                    painter = painterResource(Res.drawable.edit_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            focusRequester.requestFocus()
                        }
                )
            }
        }
    )
}
