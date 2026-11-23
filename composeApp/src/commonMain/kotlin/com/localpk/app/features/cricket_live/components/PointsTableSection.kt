package com.localpk.app.features.cricket_live.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PointsTableSection(
    pointsTable: com.localpk.app.features.cricket_live.data.PointsTableUiData,
    pointsLiveData: List<com.localpk.app.features.cricket_live.data.TeamsPoints>,
    selectedGroup: String,
    onGroupSelected: (String) -> Unit
) {
    Column {
        // Group selection buttons
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            pointsTable.groups.forEachIndexed { index, group ->
                TeamButton(
                    text = group.group_name,
                    isSelected = selectedGroup == group.group_name || (selectedGroup.isEmpty() && index == 0),
                    onClick = {
                        onGroupSelected(group.group_name)
                    }
                )
            }
        }

        // Points table
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column {
                // Header
                ScorecardTableHeader(
                    headers = listOf("Teams", "M", "W", "L", "P")
                )

                // Rows
                pointsLiveData.forEach { teamPoints ->
                    ScorecardTableRow(
                        values = listOf(
                            teamPoints.team_name,
                            teamPoints.played.toString(),
                            teamPoints.won.toString(),
                            teamPoints.lost.toString(),
                            teamPoints.points.toInt().toString()
                        ),
                        isColor = true
                    )
                }
            }
        }
    }
}