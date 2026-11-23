package com.localpk.app.common_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.DarkGrayPrimary
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.please_wait
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoaderScreen() {
    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize().statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(top = 200.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(200.dp)
                ) {
//                    AnimatedPreloader(
//                        modifier = Modifier
//                            .matchParentSize()
//                            .wrapContentHeight(Alignment.CenterVertically)
//                            .align(Alignment.Center),
//                        R.raw.loader
//                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(bottom = 50.dp),
                    text = stringResource(Res.string.please_wait),
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = DarkGrayPrimary
                )
            }
        }
    }
}