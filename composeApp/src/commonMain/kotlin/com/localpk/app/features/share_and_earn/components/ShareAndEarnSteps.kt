package com.localpk.app.features.share_and_earn.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.share_and_earn.data.ShareStepsAndCoins
import com.localpk.app.theming.ColorDividers
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.Yellow
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.NEXT_LINE
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.dc_coins_x
import org.jetbrains.compose.resources.stringResource

@Composable
fun ShareAndEarnSteps(shareSteps: List<ShareStepsAndCoins>) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
        ) {
            shareSteps.forEachIndexed { index, data ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = Color(0xFFE4E7FE),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.Medium,
                                    color = ColorPrimary,
                                    textAlign = TextAlign.Center,
                                )
                            )
                        }
                        if (shareSteps.size > index + 1) {
                            HorizontalDivider(
                                Modifier
                                    .padding(horizontal = 11.dp)
                                    .height(24.dp)
                                    .width(2.5.dp),
                                color = ColorDividers
                            )
                        }
                    }
                    Text(
                        text = buildAnnotatedString {
                            append("${data.step} ${if (index == 2) NEXT_LINE else EMPTY_STRING}")
                            withStyle(
                                style = SpanStyle(
                                    color = Yellow,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(data.coins)
                            }
                        },
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                        )
                    )
                }
            }
        }
    }
}