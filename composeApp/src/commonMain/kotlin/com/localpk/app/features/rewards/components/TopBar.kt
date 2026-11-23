package com.localpk.app.features.rewards.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.AnimatedLottie
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.EMPTY_STRING
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.expanded
import dealcart.composeapp.generated.resources.ic_back_arrow_black
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import org.jetbrains.compose.resources.painterResource

@Composable
fun TopBar(
    onBackClick: () -> Unit = {},
    title: String? = EMPTY_STRING,
    onShareClick: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    var jsonString by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        jsonString = Res.readBytes("files/animation.json").decodeToString()
    }
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(shape = RoundedCornerShape(0.dp, 0.dp, 12.dp, 12.dp))
    ) {
        Image(
            modifier = Modifier.matchParentSize(),
            painter = painterResource(Res.drawable.expanded),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        jsonString?.let {
            AnimatedLottie(
                modifier = Modifier
                    .matchParentSize()
                    .wrapContentHeight(Alignment.Top)
                    .size(80.dp)
                    .align(Alignment.TopCenter),
                lottieSpec = LottieCompositionSpec.JsonString(it)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth().statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_back_arrow_black),
                        tint = Color.White,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = title.toString(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = Color.White,
                )
                /*   Spacer(modifier = Modifier.weight(1f))

                   IconButton(
                       onClick = onShareClick
                   ) {
                       Icon(
                           modifier = Modifier.size(22.dp),
                           painter = painterResource(Res.drawable.ic_share),
                           tint = Color.White,
                           contentDescription = null
                       )
                   } */
            }
            content()
        }
    }
}