package com.localpk.app.features.cricket_live.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadComposableAsyncImage
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.Team2ScoreCard
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.bat_logo
import dealcart.composeapp.generated.resources.pakistan_logo
import dealcart.composeapp.generated.resources.yet_to_bat
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TeamScoreRow(
    teamName: String,
    flagUrl: String,
    score: String,
    wicket: String,
    over: String,
    currentRunRate: String,
    isPlaying: Boolean,
    isReducedOvers: Boolean = false,
    reducedOvers: String = "",
    requiredRunRate: String = "",
    reducedTarget: Long = 0L
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        loadComposableAsyncImage(
            imageUrl = flagUrl,
            fallbackRes = Res.drawable.pakistan_logo,
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = teamName,
            fontSize = 14.sp,
            color = if (isPlaying) Color.Black else Team2ScoreCard,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            fontFamily = PoppinsFontFamily()
        )

        if (isPlaying) {
            Image(
                painter = painterResource(Res.drawable.bat_logo),
                contentDescription = "Bat",
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            if (score.isNotEmpty()) {
                Text(
                    text = "$score/$wicket",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = PoppinsFontFamily()
                )
                Text(
                    text = if (isReducedOvers && reducedOvers.isNotEmpty()) "($over ov) / $reducedOvers ov" else "($over ov)",
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontFamily = PoppinsFontFamily()
                )
                if (reducedTarget > 0) {
                    Text(
                        text = "Revised Target $reducedTarget",
                        fontSize = 10.sp,
                        color = Color.Black,
                        fontFamily = PoppinsFontFamily()
                    )
                }
                if (requiredRunRate.isNotEmpty()) {
                    Row {
                        Text(
                            text = "CRR : $currentRunRate",
                            fontSize = 10.sp,
                            color = Color.Black,
                            fontFamily = PoppinsFontFamily()
                        )
                        Text(
                            text = " | ",
                            fontSize = 10.sp,
                            color = Color.Black,
                            fontFamily = PoppinsFontFamily()
                        )
                        Text(
                            text = "RRR : $requiredRunRate",
                            fontSize = 10.sp,
                            color = Color.Black,
                            fontFamily = PoppinsFontFamily()
                        )
                    }
                } else if (currentRunRate.isNotEmpty()) {
                    Text(
                        text = "CRR : $currentRunRate",
                        fontSize = 9.sp,
                        color = Color.Black,
                        fontFamily = PoppinsFontFamily()
                    )
                }
            } else {
                Text(
                    text = stringResource(Res.string.yet_to_bat),
                    fontSize = 12.sp,
                    color = Team2ScoreCard,
                    fontFamily = PoppinsFontFamily()
                )
            }
        }
    }
}
