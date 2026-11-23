package com.localpk.app.features.orders.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.DiscountColor
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.alert
import org.jetbrains.compose.resources.painterResource

@Composable
fun DisclaimerCard(
    title: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(
                color = DiscountColor,
                shape = RoundedCornerShape(size = 4.dp)
            )
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 5.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.alert),
                contentDescription = null,
                modifier = Modifier
                    .width(18.dp)
                    .height(19.dp)
                    .size(20.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                style = TextStyle(
                    lineHeight = 14.sp,
                ),
                color = TextSubdued
            )

        }
    }
}