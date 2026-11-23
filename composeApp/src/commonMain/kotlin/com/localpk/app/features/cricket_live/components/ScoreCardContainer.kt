package com.localpk.app.features.cricket_live.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.localpk.app.features.cricket_live.data.Batsman
import com.localpk.app.features.cricket_live.data.Bowler
import com.localpk.app.features.cricket_live.data.Extras
import com.localpk.app.features.cricket_live.data.FullScorecard
import com.localpk.app.features.cricket_live.data.PoweredByDetailUiData
import com.localpk.app.theming.ColorTermCondition
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.ProfileTextGray
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.live
import dealcart.composeapp.generated.resources.powered_by
import dealcart.composeapp.generated.resources.scorecard
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScoreCardContainer(
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
    yetToBat: String,
    scorecardSponsor: PoweredByDetailUiData?
) {
    Column {
        // Tab selection
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = onLiveTabClick
            ) {
                Text(
                    text = stringResource(Res.string.live),
                    color = if (isLiveTabSelected) ColorTermCondition else ProfileTextGray,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFontFamily(),
                    textDecoration = if (isLiveTabSelected) Underline else null
                )
            }
            TextButton(
                onClick = onScorecardTabClick,
                modifier = Modifier
            ) {
                Text(
                    text = stringResource(Res.string.scorecard),
                    color = if (!isLiveTabSelected) ColorTermCondition else ProfileTextGray,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFontFamily(),
                    style = TextStyle(
                        textDecoration = if (!isLiveTabSelected) Underline else null
                    )

                )
            }
            if (scorecardSponsor != null) {
                Column(
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
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

        if (isLiveTabSelected) {
            LiveScorecardView(
                batsmenData = batsmenData,
                bowlersData = bowlersData
            )
        } else {
            FullScorecardView(
                fullScoreCard = fullScoreCard,
                isSuperOverMatch = isSuperOverMatch,
                onTeamAClick = onTeamAClick,
                onTeamBClick = onTeamBClick,
                onTeamASuperOverClick = onTeamASuperOverClick,
                onTeamBSuperOverClick = onTeamBSuperOverClick,
                batsmenData = batsmenData,
                bowlersData = bowlersData,
                totalRuns = totalRuns,
                extrasData = extrasData,
                yetToBat = yetToBat
            )
        }
    }
}
