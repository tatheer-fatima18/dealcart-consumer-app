package com.localpk.app.common_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily

@Composable
fun WhiteBackgroundRoundedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    cursorColor: Color,
    readOnly: Boolean,
    prefix: @Composable (() -> Unit)? = null,
    cornerShape: RoundedCornerShape = RoundedCornerShape(28.dp),
    maxCharacters: Int? = null,
    keyboardOption: KeyboardOptions? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = modifier
            .background(
                Color.White,
                shape = cornerShape
            )
    ) {
        if (keyboardOption != null) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = value.take(maxCharacters ?: value.length),
                label = label,
                placeholder = placeholder,
                readOnly = readOnly,
                singleLine = true,
                prefix = prefix,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily()
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = cursorColor,
                    focusedBorderColor = Color(0xFFE4E5ED),
                    unfocusedBorderColor = Color(0xFFE4E5ED),
                    focusedLabelColor = Color.Black
                ),
                shape = cornerShape,
                keyboardOptions = keyboardOption,
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )
        }
    }
}