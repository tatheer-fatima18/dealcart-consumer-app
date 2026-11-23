package com.localpk.app.features.cricket_live.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.BlueLight
import com.localpk.app.theming.ColorTermCondition
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.ProfileTextGray
import com.localpk.app.utils.EMPTY_STRING

@Composable
fun ScorecardTableRow(
    values: List<String>,
    isColor: Boolean = false,
    subtitleText: String = EMPTY_STRING,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        values.forEachIndexed { index, value ->
            if (index == 0) {
                Column(
                    modifier = Modifier
                        .weight(1.5f)
                        .padding(horizontal = 7.dp).padding(start = 7.dp)
                ) {
                    Text(
                        text = value,
                        fontSize = 10.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = if (isColor) ColorTermCondition else Color.Black,
                        textAlign = TextAlign.Start
                    )
                    if (subtitleText.isNotEmpty())
                        Text(
                            text = subtitleText,
                            fontSize = 9.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = BlueLight,
                            textAlign = TextAlign.Start
                        )

                }
            } else {
                Text(
                    text = value,
                    fontSize = 10.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = ProfileTextGray,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}