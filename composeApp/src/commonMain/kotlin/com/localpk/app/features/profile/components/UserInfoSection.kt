package com.localpk.app.features.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_back_arrow_white
import dealcart.composeapp.generated.resources.ic_profile_avatar_female
import dealcart.composeapp.generated.resources.ic_profile_avatar_profile
import dealcart.composeapp.generated.resources.login_to_view_your_profile
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun UserInfoSection(
    isLoggedIn: Boolean,
    userName: String,
    isFemale: Boolean,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(210.dp)
            .fillMaxWidth()
            .background(
                color = ColorPrimary,
                shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
            )
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(6.dp).statusBarsPadding()
                .align(Alignment.TopStart)
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_back_arrow_white),
                contentDescription = "Back"
            )
        }

        if (isLoggedIn) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 70.dp)
            ) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier
                        .width(66.dp)
                        .height(66.dp)
                ) {
                    Image(
                        painter = painterResource(
                            if (isFemale) Res.drawable.ic_profile_avatar_female
                            else Res.drawable.ic_profile_avatar_profile
                        ),
                        contentDescription = "Profile",
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = userName,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 60.dp)
            ) {
                Text(
                    text = stringResource(Res.string.login_to_view_your_profile),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 15.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
                LoginButton { onLoginClick() }
            }
        }
    }
}