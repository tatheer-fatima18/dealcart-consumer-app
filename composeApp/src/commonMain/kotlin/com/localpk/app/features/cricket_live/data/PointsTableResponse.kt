package com.localpk.app.features.cricket_live.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.default
import com.localpk.app.utils.orEmptyArrayList

@Serializable
data class PointsTableResponse(
    val success: Boolean,
    val body: PointsTableBody?
) : JavaSerializable

@Serializable
data class PointsTableBody(
    val tournament_name: String?,
    val groups: List<TeamsGroups>?
) : JavaSerializable {
    fun map(): PointsTableUiData = PointsTableUiData(
        tournamentName = tournament_name.default(),
        groups = groups.orEmptyArrayList(),
        groupA = groups.orEmptyArrayList().firstOrNull()?.group_name.default(),
        groupB = if (groups.orEmptyArrayList().size > 1) groups.orEmptyArrayList()
            .component2().group_name.default() else EMPTY_STRING,
        groupC = if (groups.orEmptyArrayList().size > 2) groups.orEmptyArrayList()
            .component3().group_name.default() else EMPTY_STRING,
        groupD = if (groups.orEmptyArrayList().size > 3) groups.orEmptyArrayList()
            .component4().group_name.default() else EMPTY_STRING,
    )
}

@Serializable
data class TeamsGroups(
    val group_name: String,
    val points: List<TeamsPoints>
) : JavaSerializable

@Serializable
data class TeamsPoints(
    val team_name: String,
    val team_code: String,
    val flag: String?,
    val played: Int,
    val won: Int,
    val lost: Int,
    val points: Double,
    val position: Int,
    val run_rate: String
) : JavaSerializable

@Serializable
data class PointsTableUiData(
    val tournamentName: String,
    val groups: List<TeamsGroups>,
    val groupA: String,
    val groupB: String,
    val groupC: String,
    val groupD: String,
) : JavaSerializable