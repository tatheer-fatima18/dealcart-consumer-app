@file:OptIn(ExperimentalMaterial3Api::class)

package com.localpk.app.features.personal.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.personal.presentation.PersonalViewModel
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.DCLightGrey
import com.localpk.app.theming.DCPink
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.ProfileTextGray
import com.localpk.app.utils.formatMillisToDate

@Composable
fun DobField(
    label: String,
    value: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit = {},
    modifier: Modifier,
    personalViewModel: PersonalViewModel
) {
    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val selectedDate = remember {
        mutableStateOf("")
    }
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = PoppinsFontFamily(),
                color = ProfileTextGray,
                fontSize = 16.sp
            ),
        )
        Box(
            modifier = modifier.then(if (enabled) Modifier.clickable {
                openDialog.value = true
            } else Modifier),
        ) {
            BasicTextField(
                value = value,
                cursorBrush = SolidColor(DCPink),
                enabled = false,
                singleLine = true,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp),
                textStyle = TextStyle(
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 15.sp,
                    color = ColorPrimaryText
                )
            )

            HorizontalDivider(
                color = DCLightGrey,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
            )
        }

        if (openDialog.value) {
            DatePickerDialog(
                onDismissRequest = { openDialog.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        openDialog.value = false
                        val selectedMillis = datePickerState.selectedDateMillis
                        selectedMillis?.let {
                            selectedDate.value = it.formatMillisToDate()
                            personalViewModel.setDoB(selectedDate.value)
                        }
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}