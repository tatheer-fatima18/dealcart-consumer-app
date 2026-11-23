package com.localpk.app.features.cricket_live.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.utils.*
import kotlinx.serialization.Serializable

@Serializable
data class MatchSummaryResponse(val body: List<MatchSummary>?) : JavaSerializable

@Serializable
data class MatchSummary(
    val currentBattingTeam: String?,
    val currentRunRate: String?,
    val matchKey: String?,
    val message: String?,
    val over: List<Long>?,
    val requiredRunRate: String?,
    val runs: String?,
    val swimlaneStatus: String?,
    val startAt: String?,
    val teams: List<MatchTeam>,
    val wickets: String?,
    val firstBatting: String?,
    val isInningBreak: Boolean?,
    val wasMatchStarted: Boolean?,
    val target: SummaryTarget?,
    val reducedOvers: List<Long>?,
    val isSuperOver: Boolean?
) : JavaSerializable {

    fun mapToUiData(): CricketSwimlaneUiData {
        val teamA: MatchTeam
        val teamB: MatchTeam
        val oversToBeBowled: String = formatOvers(target?.oversToBeBowled)
        val reducedOversString: String = formatOvers(reducedOvers)

        if (firstBatting.isNullOrEmpty()) {
            teamA = teams.first()
            teamB = teams.last()
        } else {
            teamA = teams.first { team -> team.name == firstBatting.default() }
            teamB = teams.first { team -> team.name != firstBatting.default() }
        }

        return CricketSwimlaneUiData(
            id = matchKey.default(),
            currentBattingTeam = currentBattingTeam.default(),
            teamA = teamA.name,
            flagTeamA = teamA.flagUrl.default(),
            scoreTeamA = if ((teamA.name == currentBattingTeam) && (swimlaneStatus == "live_match")) runs.default() else {
                if (teamA.runs == ZERO_STRING && teamA.over?.first() == ZERO_LONG && teamA.over.last() == ZERO_LONG) EMPTY_STRING else teamA.runs.default()
            },
            wicketTeamA = if ((teamA.name == currentBattingTeam) && (swimlaneStatus == "live_match")) wickets.default() else teamA.wickets.default(),
            overTeamA = if ((teamA.name == currentBattingTeam) && (swimlaneStatus == "live_match")) formatOvers(
                over
            ) else formatOvers(teamA.over),
            currentRunRateTeamA = if ((teamA.name == currentBattingTeam) && (swimlaneStatus == "live_match")) currentRunRate.default() else EMPTY_STRING,
            reducedOversTeamA = reducedOversString,
            isReducedOversTeamA = reducedOvers.isNullOrEmpty().not(),
            isResultTeamA = swimlaneStatus == "match_result" || ((teamA.name == currentBattingTeam) && (swimlaneStatus == "live_match")),
            teamB = teamB.name,
            flagTeamB = teamB.flagUrl.default(),
            scoreTeamB = if ((teamB.name == currentBattingTeam) && (swimlaneStatus == "live_match")) runs.default() else if (swimlaneStatus == "match_result" || isSuperOver.default()) teamB.runs.default() else EMPTY_STRING,
            wicketTeamB = if ((teamB.name == currentBattingTeam) && (swimlaneStatus == "live_match")) wickets.default() else teamB.wickets.default(),
            overTeamB = if ((teamB.name == currentBattingTeam) && (swimlaneStatus == "live_match")) formatOvers(
                over
            ) else formatOvers(teamB.over),
            currentRunRateTeamB = if ((teamB.name == currentBattingTeam) && (swimlaneStatus == "live_match")) currentRunRate.default() else EMPTY_STRING,
            requiredRunRateTeamB = if ((teamB.name == currentBattingTeam) && (swimlaneStatus == "live_match")) requiredRunRate.default() else EMPTY_STRING,
            isResultTeamB = swimlaneStatus == "match_result" || ((teamB.name == currentBattingTeam) && (swimlaneStatus == "live_match")),
            score = runs.default(),
            wicket = wickets.default(),
            currentRunRate = currentRunRate.default(),
            requiredRunRate = requiredRunRate.default(),
            status = swimlaneStatus.default(),
            over = over?.first().getValueOrDefault().toString() + "." + over?.last()
                .getValueOrDefault(),
            message = message.default(),
            isLive = swimlaneStatus == "live_match",
            isTeamAPlaying = ((teamA.name == currentBattingTeam) && (swimlaneStatus == "live_match")),
            isTeamBPlaying = ((teamB.name == currentBattingTeam) && (swimlaneStatus == "live_match")),
            startAtTime = startAt?.formatConfirmTimelineTime().default(),
            startAtDate = startAt?.formatDate().default(),
            isToday = swimlaneStatus == "today_match",
            isResult = swimlaneStatus == "match_result",
            wasMatchStarted = swimlaneStatus != "today_match" && wasMatchStarted.default(),
            isDLApplied = target?.dlApplied.default(),
            reducedOversTeamB = if (target?.oversToBeBowled.isNotNull()) oversToBeBowled else reducedOversString,
            isReducedOversTeamB = if (target?.dlApplied.default()) true else if (target?.oversToBeBowled.isNotNull()) true else reducedOvers?.isNotEmpty() == true,
            reducedTargetTeamB = if (swimlaneStatus != "match_result" && target?.dlApplied.default()) target?.runs.getValueOrDefault() else ZERO_LONG
        )
    }
}

@Serializable
data class MatchTeam(
    val name: String,
    val flagUrl: String?,
    val runs: String?,
    val over: List<Long>?,
    val wickets: String?
) : JavaSerializable

@Serializable
data class SummaryTarget(
    val balls: Long?,
    val dlApplied: Boolean?,
    val oversToBeBowled: List<Long>?,
    val runs: Long?
) : JavaSerializable
