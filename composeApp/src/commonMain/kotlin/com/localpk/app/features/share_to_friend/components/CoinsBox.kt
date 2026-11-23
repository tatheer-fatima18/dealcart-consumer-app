package com.localpk.app.features.share_to_friend.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.BlackText
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CoinsBox(
    coins: String,
    buttonColor: Color,
    imageResource: DrawableResource,
    coinsText: String
) {
    Box(
        modifier = Modifier
            .width(143.dp)
            .height(51.dp)
            .background(
                color = buttonColor,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = painterResource(imageResource),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
            )

            Column {
                Text(
                    text = coinsText,
                    modifier = Modifier.padding(top = 4.dp),
                    style = TextStyle(
                        color = BlackText,
                        textAlign = TextAlign.Start,
                        fontSize = 8.sp,
                        // fontFamily = PoppinsFont,
                        fontWeight = FontWeight.Medium
                    ),
                )
                Text(
                    text = coins,
                    style = TextStyle(
                        color = BlackText,
                        textAlign = TextAlign.Start,
                        fontSize = 18.sp,
                        //fontFamily = PoppinsFont,
                        fontWeight = FontWeight.Bold
                    ),
                )
            }


        }
    }
}
