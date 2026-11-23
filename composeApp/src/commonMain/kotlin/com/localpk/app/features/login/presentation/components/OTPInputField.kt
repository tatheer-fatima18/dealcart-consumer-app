package com.localpk.app.features.login.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.Background
import com.localpk.app.theming.Black80
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.InputBoxStrokeColor
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.Red
import com.localpk.app.utils.EMPTY_STRING

@Composable
fun OTPInputField(
    otpValue: String,
    isOTPIncorrect: Boolean,
    onOtpChange: (String) -> Unit,
    onOtpComplete: () -> Unit
) {
    var focusedBoxIndex by remember { mutableIntStateOf(0) }
    val focusRequesters = remember { List(6) { FocusRequester() } }

    // Auto-focus first field on initial load
    LaunchedEffect(Unit) {
        if (otpValue.isEmpty()) {
            focusRequesters[0].requestFocus()
        }
    }

    BasicTextField(
        value = otpValue,
        onValueChange = { newValue ->
            // Only allow digits and limit to 6 characters
            val filteredValue = newValue.filter { it.isDigit() }.take(6)
            onOtpChange(filteredValue)

            // Handle focus management
            when {
                filteredValue.length > otpValue.length -> {
                    // New digit added
                    val newIndex = filteredValue.length - 1
                    if (newIndex < focusRequesters.size - 1) {
                        focusedBoxIndex = newIndex + 1
                        focusRequesters[newIndex + 1].requestFocus()
                    }
                }

                filteredValue.length < otpValue.length -> {
                    // Digit removed (backspace)
                    val newIndex = filteredValue.length
                    focusedBoxIndex = newIndex
                    if (newIndex < focusRequesters.size) {
                        focusRequesters[newIndex].requestFocus()
                    }
                }
            }

            // Check if OTP is complete
            if (filteredValue.length == 6) {
                onOtpComplete()
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword
        ),
        textStyle = TextStyle(
            color = Color.Transparent,
            fontSize = 18.sp,
            fontFamily = PoppinsFontFamily()
        )
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            repeat(6) { index ->
                val digit = when {
                    index < otpValue.length -> otpValue[index].toString()
                    else -> EMPTY_STRING
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .focusRequester(focusRequesters[index])
                            .size(50.dp)
                            .background(
                                color = when {
                                    isOTPIncorrect -> Background
                                    index == focusedBoxIndex -> Background
                                    else -> Color.White
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = when {
                                    isOTPIncorrect -> Red
                                    index == focusedBoxIndex -> ColorPrimary
                                    else -> InputBoxStrokeColor
                                },
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = digit,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = if (isOTPIncorrect) Red else Black80,
                        )
                    }
                }
            }
        }
    }
} 