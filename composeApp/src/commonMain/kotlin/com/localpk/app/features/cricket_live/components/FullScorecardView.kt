package com.localpk.app.features.cricket_live.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import com.localpk.app.utils.default
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cricket_live.data.Batsman
import com.localpk.app.features.cricket_live.data.Bowler
import com.localpk.app.features.cricket_live.data.Extras
import com.localpk.app.theming.ColorTermCondition
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.ProfileTextGray
import com.localpk.app.utils.formatToTwoDecimalString
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.extras_label
import dealcart.composeapp.generated.resources.total_runs
import dealcart.composeapp.generated.resources.yet_bat
import org.jetbrains.compose.resources.stringResource

@Composable
fun FullScorecardView(
    fullScoreCard: com.localpk.app.features.cricket_live.data.FullScorecard?,
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
    val selectedTeam = remember { mutableStateOf("A") }

    Column {
        // Team selection
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TeamButton(
                text = fullScoreCard?.teams?.teamA.default(),
                isSelected = selectedTeam.value == "A",
                onClick = {
                    selectedTeam.value = "A"
                    onTeamAClick()
                }
            )
            TeamButton(
                text = fullScoreCard?.teams?.teamB.default(),
                isSelected = selectedTeam.value == "B",
                onClick = {
                    if (fullScoreCard?.isTeamBInningStarted.default()) {
                        selectedTeam.value = "B"
                        onTeamBClick()
                    }
                }
            )
            if (isSuperOverMatch) {
                TeamButton(
                    text = fullScoreCard?.teams?.teamASuperOver.default(),
                    isSelected = selectedTeam.value == "A_SO",
                    onClick = {
                        selectedTeam.value = "A_SO"
                        onTeamASuperOverClick()
                    }
                )
                TeamButton(
                    text = fullScoreCard?.teams?.teamBSuperOver.default(),
                    isSelected = selectedTeam.value == "B_SO",
                    onClick = {
                        if (fullScoreCard?.isTeamBSuperOverInningStarted.default()) {
                            selectedTeam.value = "B_SO"
                            onTeamBSuperOverClick()
                        }
                    }
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column {
                // Batsmen header
                ScorecardTableHeader(
                    headers = listOf("Batsmen", "R", "B", "4s", "6s", "Sr")
                )

                // Batsmen rows
                batsmenData.forEach { batsman ->
                    ScorecardTableRow(
                        values = listOf(
                            batsman.name,
                            batsman.runs.toString(),
                            batsman.balls.toString(),
                            batsman.fours.toString(),
                            batsman.sixes.toString(),
                            batsman.strikeRate.formatToTwoDecimalString()
                        ),
                        isColor = !batsman.isBatsmanOut,
                        subtitleText = batsman.dismissal
                    )
                }

                // Extras
                if (extrasData != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.extras_label),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = PoppinsFontFamily(),
                            color = ProfileTextGray
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${extrasData.total} ( N ${extrasData.noBalls}, W ${extrasData.wides}, B ${extrasData.byes} )",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ProfileTextGray,
                            fontFamily = PoppinsFontFamily()
                        )
                    }
                }

                // Total runs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.total_runs),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ProfileTextGray,
                        fontFamily = PoppinsFontFamily()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = totalRuns.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ProfileTextGray,
                        fontFamily = PoppinsFontFamily()
                    )
                }

                // Yet to bat
                if (yetToBat.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.yet_bat),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ColorTermCondition,
                            fontFamily = PoppinsFontFamily()
                        )
                        Text(
                            text = yetToBat,
                            fontSize = 12.sp,
                            color = ProfileTextGray,
                            fontFamily = PoppinsFontFamily(),
                            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                        )
                    }
                }

                // Bowlers header
                ScorecardTableHeader(
                    headers = listOf("Bowlers", "O", "M", "R", "W", "Eco")
                )

                // Bowlers rows
                bowlersData.forEach { bowler ->
                    ScorecardTableRow(
                        values = listOf(
                            bowler.name,
                            bowler.overs,
                            bowler.maidens.toString(),
                            bowler.runs.toString(),
                            bowler.wickets.toString(),
                            bowler.economy.formatToTwoDecimalString()
                        ),
                        isColor = bowler.isCurrentBowler
                    )
                }
            }
        }
    }
}
