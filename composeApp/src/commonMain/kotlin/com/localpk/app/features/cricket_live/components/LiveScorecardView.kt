package com.localpk.app.features.cricket_live.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
import com.localpk.app.features.cricket_live.data.Batsman
import com.localpk.app.features.cricket_live.data.Bowler
import com.localpk.app.utils.formatToTwoDecimalString

@Composable
fun LiveScorecardView(
    batsmenData: List<Batsman>,
    bowlersData: List<Bowler>
) {
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

            Spacer(modifier = Modifier.height(8.dp))

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
