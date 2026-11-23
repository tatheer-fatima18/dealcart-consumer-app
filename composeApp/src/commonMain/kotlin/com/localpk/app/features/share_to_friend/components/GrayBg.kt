package com.localpk.app.features.share_to_friend.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.Bitmap
import com.localpk.app.theming.FlashDealSeparator
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.add
import dealcart.composeapp.generated.resources.ic_facebook_share
import dealcart.composeapp.generated.resources.ic_message_share
import dealcart.composeapp.generated.resources.ic_messenger_share
import dealcart.composeapp.generated.resources.ic_twitter_share
import dealcart.composeapp.generated.resources.ic_whatsapp_share

@Composable
fun GrayBg(snapShot: (() -> Bitmap)?, message: String, link: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                color = FlashDealSeparator,
                shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)
            ),
        contentAlignment = Alignment.Center
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialIcons(
                imageResource = Res.drawable.ic_whatsapp_share,
                snapShot = snapShot,
                share = { }
            )
            SocialIcons(
                imageResource = Res.drawable.ic_facebook_share,
                snapShot,
                share = {
                }
            )
            SocialIcons(
                imageResource = Res.drawable.ic_messenger_share,
                snapShot,
                share = {
                }
            )
            SocialIcons(
                imageResource = Res.drawable.ic_message_share,
                snapShot,
                share = {
                }
            )
            SocialIcons(
                imageResource = Res.drawable.ic_twitter_share,
                snapShot,
                share = {
                }
            )
            SocialIcons(
                imageResource = Res.drawable.add,
                snapShot,
                share = {
                }
            )
        }

    }
}