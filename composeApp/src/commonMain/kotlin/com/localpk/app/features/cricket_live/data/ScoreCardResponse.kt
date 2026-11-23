package com.localpk.app.features.cricket_live.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.default
import com.localpk.app.utils.formatOvers
import kotlinx.serialization.Serializable


@Serializable
data class ScoreCardResponse(val success: Boolean, val body: MatchDetails?) : JavaSerializable

@Serializable
data class MatchDetails(
    val matchKey: String,
    val startAt: String,
    val status: String,
    val tournamentName: String,
    val isLive: Boolean,
    val teams: Map<String, Team>,
    val play: Play?,
    val msg: String?
) : JavaSerializable {

    fun map(): Scorecard {

        val teamAName = play?.firstBatting ?: EMPTY_STRING
        val teamBName = if (play?.firstBatting != null) {
            teams.keys.find { it != play.firstBatting }.default()
        } else {
            EMPTY_STRING
        }
        val teamASuperOverName =  play?.inningsOrder?.getOrNull(2) ?: EMPTY_STRING
        val teamBSuperOverName =  play?.inningsOrder?.getOrNull(3) ?: EMPTY_STRING

        var currentBowlerName = EMPTY_STRING

        val isSuperOver = (play?.inningsOrder?.size ?: 0) > 2

        val liveDataBatsmen = mutableListOf<Batsman>()
        val liveDataBowler = mutableListOf<Bowler>()

        if (status == "started") {
            if (play?.live?.striker?.name != null) {
                liveDataBatsmen.add(
                    0, Batsman(
                        name = (play.live.striker.name + "  *"),
                        runs = play.live.striker.runs ?: 0,
                        balls = play.live.striker.balls ?: 0,
                        fours = play.live.striker.fours ?: 0,
                        sixes = play.live.striker.sixes ?: 0,
                        isBatsmanOut = false,
                        isBatsmanOnStrike = true,
                        dismissal = "",
                        strikeRate = play.live.striker.strikeRate ?: 0.0
                    )
                )

                    liveDataBatsmen.add(
                        1, Batsman(
                            name = play.live.nonStriker?.name ?: EMPTY_STRING,
                            runs = play.live.nonStriker?.runs ?: ZERO_LONG,
                            balls = play.live.nonStriker?.balls ?: ZERO_LONG,
                            fours = play.live.nonStriker?.fours ?: ZERO_LONG,
                            sixes = play.live.nonStriker?.sixes ?: ZERO_LONG,
                            isBatsmanOut = false,
                            isBatsmanOnStrike = false,
                            dismissal = EMPTY_STRING,
                            strikeRate = play.live?.nonStriker?.strikeRate ?: 0.0
                        )
                    )

            } else {
                liveDataBatsmen.add(
                    0, Batsman(
                        name = play?.live?.nonStriker?.name ?: EMPTY_STRING,
                        runs = play?.live?.nonStriker?.runs ?: ZERO_LONG,
                        balls = play?.live?.nonStriker?.balls ?: ZERO_LONG,
                        fours = play?.live?.nonStriker?.fours ?: ZERO_LONG,
                        sixes = play?.live?.nonStriker?.sixes ?: ZERO_LONG,
                        isBatsmanOut = false,
                        isBatsmanOnStrike = false,
                        dismissal = EMPTY_STRING,
                        strikeRate = play?.live?.nonStriker?.strikeRate ?: 0.0
                    )
                )
            }

            if (play?.live?.bowler?.name != null) {
                liveDataBowler.add(
                    0,
                    Bowler(
                        name = play.live.bowler.name,
                        overs = formatOvers(play.live.bowler.overs),
                        runs = play.live.bowler.runs ?: ZERO_LONG,
                        maidens = play.live.bowler.maidenOvers ?: ZERO_LONG,
                        wickets = play.live.bowler.wickets ?: ZERO_LONG,
                        economy = play.live.bowler.economy ?: 0.0,
                        isCurrentBowler = true
                    )
                )
                currentBowlerName = play.live.bowler.name
                if(!isSuperOver) {
                    liveDataBowler.add(
                        1,
                        Bowler(
                            name = play.live.previousBowler?.name ?: EMPTY_STRING,
                            overs = formatOvers(play.live.previousBowler?.overs),
                            runs = play.live.previousBowler?.runs ?: ZERO_LONG,
                            maidens = play.live.previousBowler?.maidenOvers ?: ZERO_LONG,
                            wickets = play.live.previousBowler?.wickets ?: ZERO_LONG,
                            economy = play.live.previousBowler?.economy ?: 0.0,
                            isCurrentBowler = false
                        )
                    )
                }

            } else {
                liveDataBowler.add(
                    0,
                    Bowler(
                        name = play?.live?.previousBowler?.name ?: EMPTY_STRING,
                        overs = formatOvers(play?.live?.previousBowler?.overs),
                        runs = play?.live?.previousBowler?.runs ?: ZERO_LONG,
                        maidens = play?.live?.previousBowler?.maidenOvers ?: ZERO_LONG,
                        wickets = play?.live?.previousBowler?.wickets ?: ZERO_LONG,
                        economy = play?.live?.previousBowler?.economy ?: 0.0,
                        isCurrentBowler = true
                    )
                )

            }

        }

        val teamAData = play?.innings?.get(teamAName)?.extractTeamData(currentBowlerName) ?: TeamData()
        val teamBData = play?.innings?.get(teamBName)?.extractTeamData(currentBowlerName) ?: TeamData()

        val teamASuperOverData = play?.innings?.get(teamASuperOverName)?.extractTeamData(currentBowlerName) ?: TeamData()
        val teamBSuperOverData = play?.innings?.get(teamBSuperOverName)?.extractTeamData(currentBowlerName) ?: TeamData()


        val teamBInnings = play?.innings?.get(teamBName)
        val teamBSuperOverInnings = play?.innings?.get(teamBSuperOverName)

        val isTeamBInningsStarted =
            !(teamBInnings?.overs?.first() == 0 && teamBInnings.overs.last() == 0
                    && teamBInnings.runs == ZERO_LONG)

        val isTeamBSuperOverInningsStarted =
            !( teamBSuperOverInnings == null || (teamBSuperOverInnings?.overs?.first() == 0 && teamBSuperOverInnings.overs.last() == 0
                    && teamBSuperOverInnings.runs == ZERO_LONG))

        val fullScorecard = FullScorecard(
            status = status,
            teams = Teams(teamAName, teamBName, teamBName, teamAName),
            teamAData = teamAData,
            teamBData = teamBData,
            teamASuperOverData= teamASuperOverData,
            teamBSuperOverData= teamBSuperOverData,
            isTeamBInningStarted = isTeamBInningsStarted,
            isSuperOverMatch = isSuperOver,
            isTeamBSuperOverInningStarted = isTeamBSuperOverInningsStarted
        )

        val liveScorecard = LiveScorecard(
            liveDataBatsmen = liveDataBatsmen,
            liveDataBowlers = liveDataBowler,
        )

        return Scorecard(
            status = status,
            fullScorecard = fullScorecard,
            liveScorecard = liveScorecard
        )
    }
}

fun Inning.extractTeamData(currentBowler: String): TeamData {
    val batsmen = battingStats.orEmpty().map { player ->
        Batsman(
            name = player.name ?: EMPTY_STRING,
            runs = player.runs ?: ZERO_LONG,
            balls = player.balls ?: ZERO_LONG,
            fours = player.fours ?: ZERO_LONG,
            sixes = player.sixes ?: ZERO_LONG,
            dismissal = player.dismissal ?: EMPTY_STRING,
            strikeRate = player.strikeRate ?: 0.0,
            isBatsmanOut = player.dismissal != "not out",
            isBatsmanOnStrike = false,
        )
    }

    val bowlers = bowlingStats.orEmpty().map { player ->
        Bowler(
            name = player.name ?: EMPTY_STRING,
            overs = formatOvers(player.overs),
            runs = player.runs ?: ZERO_LONG,
            maidens = player.maidenOvers ?: ZERO_LONG,
            wickets = player.wickets ?: ZERO_LONG,
            economy = player.economy ?: 0.0,
            isCurrentBowler = player.name == currentBowler && player.name!= EMPTY_STRING
        )

    }

    val extras = extraRuns?.let { extraRuns ->
        Extras(
            total = extraRuns.extra ?: ZERO_LONG,
            legByes = extraRuns.legBye ?: ZERO_LONG,
            byes = extraRuns.bye ?: ZERO_LONG,
            wides = extraRuns.wide ?: ZERO_LONG,
            noBalls = extraRuns.noBall ?: ZERO_LONG,
            penalty = extraRuns.penalty ?: ZERO_LONG
        )
    } ?: Extras(0, 0, 0, 0, 0, 0)

    val totalRuns = runs ?: 0L
    val yetToBat = yetToBat?.joinToString("    ") ?: EMPTY_STRING

    return TeamData(batsmen, bowlers, extras, totalRuns, yetToBat)
}



@Serializable
data class Team(
    val code: String?,
    val name: String?,
    val flag: String?
) : JavaSerializable

@Serializable
data class Play(
    val firstBatting: String?,
    val currentBattingTeam: String?,
    val inningsOrder: List<String>?,
    val result: String?,
    val innings: Map<String, Inning>?,
    val live: Live?,
    val oversPerInning: List<Int>?,
    val target: Target?,
) : JavaSerializable

@Serializable
data class Inning(
    val isCompleted: Boolean?,
    val scoreStr: String?,
    val runs: Long?,
    val overs: List<Int>?,
    val runRate: Double?,
    val requiredRunRate: Double?,
    val wickets: Int?,
    val battingStats: List<Player>?,
    val bowlingStats: List<BowlerPlayer>?,
    val extraRuns: ExtraRuns?,
    val yetToBat: List<String>?
) : JavaSerializable

@Serializable
data class TeamData(
    val batsmen: List<Batsman> = emptyList(),
    val bowlers: List<Bowler> = emptyList(),
    val extras: Extras = Extras(0, 0, 0, 0, 0, 0),
    val totalRuns: Long = 0,
    val yetToBat: String = EMPTY_STRING
) : JavaSerializable

@Serializable
data class Player(
    val name: String?,
    val runs: Long?,
    val balls: Long?,
    val fours: Long?,
    val sixes: Long?,
    val strikeRate: Double?,
    val dismissal: String?
) : JavaSerializable


@Serializable
data class BowlerPlayer(
    val name: String?,
    val overs: List<Long>?,
    val maidenOvers: Long?,
    val runs: Long?,
    val wickets: Long?,
    val economy: Double?,
) : JavaSerializable

@Serializable
data class ExtraRuns(
    val extra: Long?,
    val bye: Long?,
    val legBye: Long?,
    val wide: Long?,
    val noBall: Long?,
    val penalty: Long?
) : JavaSerializable

@Serializable
data class Live(
    val striker: Player?,
    val nonStriker: Player?,
    val bowler: BowlerPlayer?,
    val previousBowler: BowlerPlayer?,
) : JavaSerializable

@Serializable
data class Target(
    val balls: Int?,
    val runs: Int?,
    val dl_applied: Boolean?
) : JavaSerializable

enum class TeamConstants(val key: String) {
    TEAM_A("TeamA"),
    TEAM_B("TeamB"),
    TEAM_A_SUPER_OVER("TeamASuperOver"),
    TEAM_B_SUPER_OVER("TeamBSuperOver"),
}
