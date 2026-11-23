package com.localpk.app.features.personal.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.DCLightGrey
import com.localpk.app.theming.DCPink
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.ProfileTextGray

@Composable
fun FormField(
    label: String,
    value: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit = {},
    modifier: Modifier,
) {
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = PoppinsFontFamily(),
                color = ProfileTextGray,
                fontSize = 17.sp
            ),
        )
        Box(
            modifier = modifier
        ) {
            BasicTextField(
                value = value,
                cursorBrush = SolidColor(DCPink),
                enabled = enabled,
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
    }
}