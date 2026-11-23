package com.localpk.app.features.rewards.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.Savings
import dealcart.composeapp.generated.resources.coins
import org.jetbrains.compose.resources.stringResource

@Composable
fun ToggleButtonDemo(isSelected: Boolean, onToggle: (Boolean) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(shape = RoundedCornerShape(27.dp))
                .background(Color(0xFF707FF4))
                .padding(4.dp),
            Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(35.dp)
                    //.padding(1.dp)
                    .clickable { onToggle(true) }
                    .background(
                        if (isSelected) Color.White else Color(0xFF707FF4),
                        shape =
                            RoundedCornerShape(19.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.coins).uppercase(),
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) ColorPrimary else Color.White,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(35.dp)
                    //.padding(1.dp)
                    .clickable { onToggle(false) }
                    .background(
                        if (!isSelected) Color.White else Color(0xFF707FF4),
                        shape = RoundedCornerShape(19.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.Savings).uppercase(),
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 13.sp,
                    fontWeight = if (!isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if(!isSelected) ColorPrimary else Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}