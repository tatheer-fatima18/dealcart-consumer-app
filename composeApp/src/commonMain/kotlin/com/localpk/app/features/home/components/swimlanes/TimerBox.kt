package com.localpk.app.features.home.components.swimlanes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSuccess
import com.localpk.app.theming.transparent

@Composable
fun TimerBox(text: String) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .border(
                1.dp,
                transparent,
                shape = RoundedCornerShape(2.dp)
            )
            .background(
                TextSuccess,
                shape = RoundedCornerShape(2.dp)
            )
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        )
    }
}
