package com.localpk.app.features.cricket_live.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class Scorecard(
    val status: String,
  val liveScorecard: LiveScorecard,
  val fullScorecard: FullScorecard
) : JavaSerializable

@Serializable
data class Teams(
    val teamA: String,
    val teamB: String,
    val teamASuperOver: String,
    val teamBSuperOver: String,
) : JavaSerializable

@Serializable
data class Batsman(
    val name: String,
    val runs: Long,
    val balls: Long,
    val fours: Long,
    val sixes: Long,
    val dismissal: String,
    val strikeRate: Double,
    val isBatsmanOut : Boolean ,
    val isBatsmanOnStrike: Boolean
) : JavaSerializable

@Serializable
data class Bowler(
    val name: String,
    val overs: String,
    val runs: Long,
    val maidens: Long,
    val wickets: Long,
    val economy: Double,
    val isCurrentBowler: Boolean
) : JavaSerializable

@Serializable
data class Extras(
    val total: Long,
    val legByes: Long,
    val byes: Long,
    val wides: Long,
    val noBalls: Long,
    val penalty: Long
) : JavaSerializable

@Serializable
data class YetToBat(
    val name: String,
) : JavaSerializable

@Serializable
data class FullScorecard(
    val status: String,
    val teams: Teams,
    val teamAData: TeamData,
    val teamBData: TeamData,
    val teamASuperOverData: TeamData,
    val teamBSuperOverData: TeamData,
    val isTeamBInningStarted: Boolean,
    val isSuperOverMatch: Boolean,
    val isTeamBSuperOverInningStarted: Boolean
) : JavaSerializable

@Serializable
data class LiveScorecard(
    val liveDataBatsmen: List<Batsman>?,
    val liveDataBowlers: List<Bowler>?,
) : JavaSerializable

@Serializable
data class PlayingTeam(
    val teamAName: String,
    val teamBName: String,
) : JavaSerializable

enum class MatchStatus(val key: String) {
    STARTED("started"),
    COMPLETED("completed"),
}






