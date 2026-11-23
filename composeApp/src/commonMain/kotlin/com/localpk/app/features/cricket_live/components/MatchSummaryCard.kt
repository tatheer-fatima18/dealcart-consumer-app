package com.localpk.app.features.cricket_live.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.localpk.app.features.cricket_live.data.Batsman
import com.localpk.app.features.cricket_live.data.Bowler
import com.localpk.app.features.cricket_live.data.CricketSwimlaneUiData
import com.localpk.app.features.cricket_live.data.Extras
import com.localpk.app.features.cricket_live.data.FullScorecard
import com.localpk.app.features.cricket_live.data.PoweredByDetailUiData
import com.localpk.app.theming.ColorTermCondition
import com.localpk.app.theming.Horizontal_Separator
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.VersesScoreBg
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.full_details
import dealcart.composeapp.generated.resources.less_details
import dealcart.composeapp.generated.resources.live
import dealcart.composeapp.generated.resources.match
import dealcart.composeapp.generated.resources.powered_by
import dealcart.composeapp.generated.resources.vs
import org.jetbrains.compose.resources.stringResource

@Composable
fun MatchSummaryCard(
    matchData: CricketSwimlaneUiData,
    scorecardSponsor: PoweredByDetailUiData?,
    showScoreCardContainer: Boolean,
    onFullDetailsClick: () -> Unit,
    fullScoreCard: FullScorecard?,
    isLiveTabSelected: Boolean,
    onLiveTabClick: () -> Unit,
    onScorecardTabClick: () -> Unit,
    isSuperOverMatch: Boolean,
    onTeamAClick: () -> Unit,
    onTeamBClick: () -> Unit,
    onTeamASuperOverClick: () -> Unit,
    onTeamBSuperOverClick: () -> Unit,
    batsmenData: List<Batsman>,
    bowlersData: List<Bowler>,
    totalRuns: Long,
    extrasData: Extras?,
    yetToBat: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            // Match label and live pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.match),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontFamily = PoppinsFontFamily()
                )
                if (matchData.isLive) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFDA1414),
                                shape = RoundedCornerShape(9.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.live).uppercase(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = PoppinsFontFamily()
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (scorecardSponsor != null) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = stringResource(Res.string.powered_by),
                            fontSize = 10.sp,
                            color = Color.Black,
                            fontFamily = PoppinsFontFamily()
                        )

                        Image(
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp),
                            painter = rememberAsyncImagePainter(scorecardSponsor.image?.medium),
                            contentDescription = "Sponsor Logo"
                        )
                    }
                }
            }

            // Team A
            TeamScoreRow(
                teamName = matchData.teamA,
                flagUrl = matchData.flagTeamA,
                score = matchData.scoreTeamA,
                wicket = matchData.wicketTeamA,
                over = matchData.overTeamA,
                currentRunRate = matchData.currentRunRateTeamA,
                isPlaying = matchData.isTeamAPlaying,
                isReducedOvers = matchData.isReducedOversTeamA,
                reducedOvers = matchData.reducedOversTeamA
            )

            // VS label
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.vs),
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 10.sp,
                    color = ColorTermCondition,
                    modifier = Modifier
                        .background(
                            color = VersesScoreBg,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 7.dp, vertical = 6.dp)
                )
            }

            // Team B
            TeamScoreRow(
                teamName = matchData.teamB,
                flagUrl = matchData.flagTeamB,
                score = matchData.scoreTeamB,
                wicket = matchData.wicketTeamB,
                over = matchData.overTeamB,
                currentRunRate = matchData.currentRunRateTeamB,
                requiredRunRate = matchData.requiredRunRateTeamB,
                isPlaying = matchData.isTeamBPlaying,
                isReducedOvers = matchData.isReducedOversTeamB,
                reducedOvers = matchData.reducedOversTeamB,
                reducedTarget = matchData.reducedTargetTeamB
            )

            // Play Game button
//            if (!matchData.isTeamBPlaying) {
//                Spacer(modifier = Modifier.height(14.dp))
//                OutlinedButton(
//                    onClick = {},
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 10.dp),
//                    colors = outlinedButtonColors(
//                        contentColor = ColorTermCondition
//                    )
//                ) {
//                    Text(
//                        text = stringResource(Res.string.play_game),
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Medium,
//                        fontFamily = PoppinsFontFamily()
//                    )
//                }
//            }

            // Required message
            if (matchData.message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = matchData.message,
                    fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    maxLines = 2,
                    fontFamily = PoppinsFontFamily()
                )
            }

            // Time and date
            if (matchData.isToday) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = matchData.startAtTime,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        fontFamily = PoppinsFontFamily()
                    )
                    Text(
                        text = " | ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        fontFamily = PoppinsFontFamily()
                    )
                    Text(
                        text = matchData.startAtDate,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
            }

            // Scorecard divider and container
            if (matchData.wasMatchStarted) {
                Spacer(modifier = Modifier.height(10.dp))
                Divider(
                    color = Horizontal_Separator,
                    thickness = 1.dp
                )

                AnimatedVisibility(visible = showScoreCardContainer) {
                    ScoreCardContainer(
                        fullScoreCard = fullScoreCard,
                        isLiveTabSelected = isLiveTabSelected,
                        onLiveTabClick = onLiveTabClick,
                        onScorecardTabClick = onScorecardTabClick,
                        isSuperOverMatch = isSuperOverMatch,
                        onTeamAClick = onTeamAClick,
                        onTeamBClick = onTeamBClick,
                        onTeamASuperOverClick = onTeamASuperOverClick,
                        onTeamBSuperOverClick = onTeamBSuperOverClick,
                        batsmenData = batsmenData,
                        bowlersData = bowlersData,
                        totalRuns = totalRuns,
                        extrasData = extrasData,
                        yetToBat = yetToBat,
                        scorecardSponsor = scorecardSponsor
                    )
                }

                if (matchData.wasMatchStarted) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = onFullDetailsClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (showScoreCardContainer) stringResource(Res.string.less_details) else stringResource(
                                Res.string.full_details
                            ),
                            letterSpacing = (-0.1).sp,
                            color = ColorTermCondition,
                            fontFamily = PoppinsFontFamily()
                        )
                    }
                }
            }
        }
    }
}
