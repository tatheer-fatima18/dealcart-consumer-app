package com.localpk.app.features.share_to_friend.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.CreamColor
import com.localpk.app.theming.MintGreen
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.capitalizeFirstLetter
import com.localpk.app.utils.isNotNull
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.cashimage
import dealcart.composeapp.generated.resources.coinsimage
import dealcart.composeapp.generated.resources.landing
import dealcart.composeapp.generated.resources.login_heading
import dealcart.composeapp.generated.resources.logos
import dealcart.composeapp.generated.resources.thankyou_for_using_dealcart
import dealcart.composeapp.generated.resources.you_earned
import dealcart.composeapp.generated.resources.you_saved
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ShareDetailsBox(
    username: String,
    displayedUsername: String,
    coins: String,
    bgGender: DrawableResource,
    savings: String,
) {
    Box(
        modifier = Modifier
            .padding(top = 22.dp)
            .width(370.dp)
            .height(280.dp),
        contentAlignment = Alignment.Center

    ) {
        Image(
            painter = painterResource(Res.drawable.landing),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(32.dp, 32.dp, 32.dp, 32.dp))
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 29.dp),
                horizontalArrangement = Arrangement.Absolute.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = if (username.isNotNull()) "Hello, ${displayedUsername.capitalizeFirstLetter()}"
                    else "Hello",
                    modifier = Modifier
                        .padding(top = 6.dp, end = 8.dp),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontSize = 23.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold
                    )
                )
                if (username.isNotNull()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(bgGender),
                        contentDescription = null,
                        modifier = Modifier
                            .size(47.dp)
                            .border(
                                2.dp,
                                ColorPrimary,
                                shape = CircleShape
                            )
                            .clip(CircleShape)

                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.thankyou_for_using_dealcart),
                modifier = Modifier,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CoinsBox(
                    coins = coins,
                    buttonColor = CreamColor,
                    imageResource = Res.drawable.coinsimage,
                    coinsText = stringResource(Res.string.you_earned)
                )
                Spacer(modifier = Modifier.width(8.dp))
                CoinsBox(
                    coins = savings,
                    buttonColor = MintGreen,
                    imageResource = Res.drawable.cashimage,
                    coinsText = stringResource(Res.string.you_saved)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.login_heading),
                modifier = Modifier,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal
                )
            )
            Image(
                painter = painterResource(Res.drawable.logos),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
            )
        }
    }
}