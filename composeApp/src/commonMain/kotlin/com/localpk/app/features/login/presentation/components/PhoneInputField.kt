package com.localpk.app.features.login.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.InputBoxStrokeColor
import com.localpk.app.theming.ResendOtpTextColor
import com.localpk.app.theming.TextGray
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.pakistan_flag
import org.jetbrains.compose.resources.painterResource

@Composable
fun PhoneInputField(
    phoneNumber: String,
    isContinueEnabled: Boolean,
    onPhoneChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { newValue ->
                val maxDigits = if (newValue.startsWith("0")) 11 else 10
                if (newValue.length <= maxDigits) {
                    onPhoneChange(newValue)
                }
            },
            prefix = { Text("+92") },
            placeholder = {
                Text("3331234123")
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.pakistan_flag),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextGray,
                cursorColor = ColorPrimary,
                focusedBorderColor = ColorPrimary,
                unfocusedBorderColor = InputBoxStrokeColor,
                focusedPlaceholderColor = ResendOtpTextColor,
                unfocusedPlaceholderColor = ResendOtpTextColor
            ),
            readOnly = isContinueEnabled
        )
    }
} 