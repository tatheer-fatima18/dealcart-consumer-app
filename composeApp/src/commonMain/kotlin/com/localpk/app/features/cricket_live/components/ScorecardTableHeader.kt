package com.localpk.app.features.cricket_live.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.ScoreCardHeaderText
import com.localpk.app.theming.VersesScoreBg

@Composable
fun ScorecardTableHeader(
    headers: List<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(VersesScoreBg)
            .padding(vertical = 8.dp)
    ) {
        headers.forEachIndexed { index, header ->
            Text(
                text = header,
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                color = ScoreCardHeaderText,
                modifier = Modifier
                    .weight(if (index == 0) 1.5f else 1f)
                    .padding(horizontal = if (index == 0) 7.dp else 0.dp).padding(start = 7.dp),
                textAlign = if (index == 0) TextAlign.Start else TextAlign.Center
            )
        }
    }
}