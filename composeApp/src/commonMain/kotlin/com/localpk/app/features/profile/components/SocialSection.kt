package com.localpk.app.features.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_facebook
import dealcart.composeapp.generated.resources.ic_instagram
import dealcart.composeapp.generated.resources.ic_linkedin
import dealcart.composeapp.generated.resources.ic_twitter
import dealcart.composeapp.generated.resources.ic_youtube
import dealcart.composeapp.generated.resources.pakistan_ka_dealCart
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SocialSection(onSocialClick: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 24.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SocialIcon(Res.drawable.ic_facebook) {
                onSocialClick("http://facebook.com/dealcart.io")
            }
            SocialIcon(Res.drawable.ic_twitter) {
                onSocialClick("http://twitter.com/dealcart_io")
            }
            SocialIcon(Res.drawable.ic_youtube) {
                onSocialClick("http://youtube.com/@dealcart3044")
            }
            SocialIcon(Res.drawable.ic_instagram) {
                onSocialClick("http://instagram.com/dealcart.io/")
            }
            SocialIcon(Res.drawable.ic_linkedin) {
                onSocialClick("http://linkedin.com/company/dealcart/")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(Res.string.pakistan_ka_dealCart),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily(),
                color = ColorPrimaryText
            ),
        )
    }
}

@Composable
private fun SocialIcon(iconRes: DrawableResource, onClick: () -> Unit) {
    Image(
        painter = painterResource(iconRes),
        contentDescription = null,
        modifier = Modifier.clickable { onClick() }
    )
}