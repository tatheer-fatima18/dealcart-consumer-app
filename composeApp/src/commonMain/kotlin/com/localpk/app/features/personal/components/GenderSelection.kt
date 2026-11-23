package com.localpk.app.features.personal.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.ColorPrimaryText70
import com.localpk.app.theming.DCLightGrey
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.ProfileTextGray
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.female
import dealcart.composeapp.generated.resources.male
import org.jetbrains.compose.resources.stringResource

@Composable
fun GenderSelection(
    selectedGender: String,
    enabled: Boolean,
    onGenderSelected: (String) -> Unit
) {
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Gender",
            style = TextStyle(
                fontFamily = PoppinsFontFamily(),
                color = ProfileTextGray,
                fontSize = 17.sp
            ),
        )
        Row(modifier = Modifier.padding(horizontal = 51.dp)) {
            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .width(80.dp)
                    .then(
                        if (selectedGender == "male") {
                            Modifier.background(color = ColorPrimary)
                        } else {
                            Modifier.border(
                                BorderStroke(
                                    width = 1.dp,
                                    color = DCLightGrey
                                ), shape = RoundedCornerShape(8.dp)
                            )
                        }
                    )
                    .then(if (enabled) Modifier.clickable { onGenderSelected("male") } else Modifier),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(16.dp)
                        .padding(start = 15.dp)
                ) {
                    RadioButton(
                        selected = selectedGender == "male",
                        onClick = { if (enabled) onGenderSelected("male") },
                        enabled = enabled,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.White,
                            unselectedColor = ColorPrimaryText70,
                            disabledSelectedColor = Color.White,
                            disabledUnselectedColor = ColorPrimaryText70
                        )
                    )
                }
                Spacer(modifier = Modifier.width(17.dp))
                Text(
                    text = stringResource(Res.string.male),
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = if (selectedGender == "male") Color.White else ColorPrimaryText70
                    ),
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .width(83.dp)
                    .then(
                        if (selectedGender == "female") {
                            Modifier.background(color = ColorPrimary)
                        } else {
                            Modifier.border(
                                BorderStroke(
                                    1.dp,
                                    color = DCLightGrey
                                ), shape = RoundedCornerShape(8.dp)
                            )
                        }
                    )
                    .then(if (enabled) Modifier.clickable { onGenderSelected("female") } else Modifier),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(16.dp)
                        .padding(start = 15.dp)
                ) {
                    RadioButton(
                        colors = RadioButtonColors(
                            selectedColor = Color.White,
                            unselectedColor = ColorPrimaryText70,
                            disabledSelectedColor = Color.White,
                            disabledUnselectedColor = ColorPrimaryText70
                        ),
                        enabled = enabled,
                        selected = selectedGender == "female",
                        onClick = { if (enabled) onGenderSelected("female") },
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    text = stringResource(Res.string.female),
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = if (selectedGender == "female") Color.White else ColorPrimaryText70
                    ),
                )
            }
        }
    }
}