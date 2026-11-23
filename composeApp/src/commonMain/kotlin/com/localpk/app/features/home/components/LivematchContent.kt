package com.localpk.app.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cricket_live.data.CricketSwimlaneUiData
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.VersesScoreBg

@Composable
fun LiveMatchContent(match: CricketSwimlaneUiData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp)
    ) {
        // Team Scores Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TeamScoreItem(
                flag = match.flagTeamA,
                showBat = match.isTeamAPlaying,
                isLive = match.isLive,
            )

            if (!(match.isTeamAPlaying || match.isTeamBPlaying)) {
                VsBox {
                    Text(
                        text = "Vs",
                        color = Color.DarkGray,
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 12.sp
                    )
                }
            }

            if (match.isLive && (match.isTeamAPlaying || match.isTeamBPlaying)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${match.score}/${match.wicket}",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "(${match.over})",
                        color = Color.Gray,
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 10.sp
                    )
                }
            }

            TeamScoreItem(
                flag = match.flagTeamB,
                showBat = match.isTeamBPlaying,
                isLive = match.isLive,
            )
        }

        // Match Message
        if (match.message.isNotEmpty()) {
            Text(
                text = match.message,
                fontSize = 11.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 4.dp)
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                color = Color.DarkGray,
                fontFamily = PoppinsFontFamily()
            )
        }

        // Time and Date
        if (match.isToday) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.startAtTime,
                    color = Color.Black,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
                Text(
                    text = "|",
                    modifier = Modifier.padding(horizontal = 6.dp),
                    color = Color.Black,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
                Text(
                    text = match.startAtDate,
                    color = Color.Black,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        }
    }
}


@Composable
fun VsBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                VersesScoreBg,
                shape = RoundedCornerShape(2.dp)
            )
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(vertical = 2.dp, horizontal = 4.dp)
    ) {
        content()
    }
}