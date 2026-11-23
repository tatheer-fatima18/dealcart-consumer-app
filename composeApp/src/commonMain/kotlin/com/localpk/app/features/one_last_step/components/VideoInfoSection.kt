package com.localpk.app.features.one_last_step.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.PoppinsFontFamily

@Composable
fun VideoInfoSection(
    videoName: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(top = 10.dp)
    ) {
        SponsoredText()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp)
        ) {
            Text(
                text = videoName,
                modifier = Modifier
                    .width(280.dp),
                color = ColorPrimaryText,
                fontSize = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(.1f))
            NextButton(
                onClick = onClick,
                modifier = Modifier
            )
        }
    }
}