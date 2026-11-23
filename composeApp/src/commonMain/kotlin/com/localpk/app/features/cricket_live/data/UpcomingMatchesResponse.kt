package com.localpk.app.features.cricket_live.data

import com.localpk.app.utils.formatConfirmTimelineTime
import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.formatMatchDate
import kotlinx.serialization.Serializable

@Serializable
data class UpcomingMatchesResponse(val success: Boolean, val body: List<UpcomingMatchesBody>?) :
    JavaSerializable {
    fun map(): List<UpcomingMatchesUiData> =
        body?.map { it.map() }.orEmpty()
}

@Serializable
data class UpcomingMatchesBody(
    val start_at: String,
    val teams: UpcomingMatchesTeams,
    val sub_title: String
) : JavaSerializable {
    fun map(): UpcomingMatchesUiData = UpcomingMatchesUiData(
        startAt = start_at.formatConfirmTimelineTime(),
        startDate = start_at.formatMatchDate(),
        teams = teams.map(),
        subTitle = sub_title
    )
}

@Serializable
data class UpcomingMatchesTeams(
    val name_1: String,
    val code_1: String,
    val flag_1: String?,
    val name_2: String,
    val code_2: String,
    val flag_2: String?
) : JavaSerializable {
    fun map(): UpcomingMatchesTeamsUiData = UpcomingMatchesTeamsUiData(
        name1 = name_1,
        code1 = code_1,
        flag1 = flag_1 ?: EMPTY_STRING,
        name2 = name_2,
        code2 = code_2,
        flag2 = flag_2 ?: EMPTY_STRING,
    )
}

@Serializable
data class UpcomingMatchesUiData(
    val startAt: String,
    val startDate: String,
    val teams: UpcomingMatchesTeamsUiData,
    val subTitle: String
) : JavaSerializable

@Serializable
data class UpcomingMatchesTeamsUiData(
    val name1: String,
    val code1: String,
    val flag1: String,
    val name2: String,
    val code2: String,
    val flag2: String,
) : JavaSerializable