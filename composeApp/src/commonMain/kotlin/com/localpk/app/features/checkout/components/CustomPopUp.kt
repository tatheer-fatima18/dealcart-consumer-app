package com.localpk.app.features.checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.WhiteBackgroundRoundedTextField
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.capitalizeFirstLetter
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.name_
import dealcart.composeapp.generated.resources.name_example
import dealcart.composeapp.generated.resources.save
import org.jetbrains.compose.resources.stringResource

@Composable
fun CustomPopUp(
    title1: String,
    title2: String,
    onDismiss: () -> Unit,
    updateName: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {},
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
                )
        ) {

            Text(
                text = title1,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 14.sp,
                letterSpacing = (-0.2).sp
            )
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = title2,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                color = TextSubdued,
                letterSpacing = (-0.2).sp
            )

            Spacer(modifier = Modifier.height(15.dp))


            var textFieldValue by remember { mutableStateOf("") }

            Text(
                text = stringResource(Res.string.name_),
                modifier = Modifier.fillMaxWidth(),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                color = TextSubdued
            )

            Spacer(modifier = Modifier.height(5.dp))

            WhiteBackgroundRoundedTextField(
                modifier = Modifier
                    .padding(top = 3.dp),
                value = textFieldValue,
                prefix = { Text(text = EMPTY_STRING, color = Color.Black) },
                placeholder = {
                    Text(
                        text = stringResource(Res.string.name_example),
                        color = TextSubduedHalf,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        fontFamily = PoppinsFontFamily()
                    )
                },
                cursorColor = Color.Black,
                readOnly = false,
                onValueChange = {
                    textFieldValue = it
                },
                keyboardOption = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                ),
                cornerShape = RoundedCornerShape(8.dp)
            )


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp
                ),
                colors = ButtonDefaults.buttonColors(ButtonSurfaceBrand),
                onClick = {
                    updateName(textFieldValue)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(Res.string.save).capitalizeFirstLetter(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    maxLines = 1,
                    lineHeight = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}