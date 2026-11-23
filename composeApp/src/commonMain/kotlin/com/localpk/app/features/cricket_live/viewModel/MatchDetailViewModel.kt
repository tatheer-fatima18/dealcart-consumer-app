package com.localpk.app.features.cricket_live.viewModel

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.banner.data.Banner
import com.localpk.app.features.cricket_live.data.Batsman
import com.localpk.app.features.cricket_live.data.Bowler
import com.localpk.app.features.cricket_live.data.CricketSwimlaneUiData
import com.localpk.app.features.cricket_live.data.Extras
import com.localpk.app.features.cricket_live.data.FullScorecard
import com.localpk.app.features.cricket_live.data.LiveScorecard
import com.localpk.app.features.cricket_live.data.MatchStatus
import com.localpk.app.features.cricket_live.data.PointsTableUiData
import com.localpk.app.features.cricket_live.data.PoweredByDetailUiData
import com.localpk.app.features.cricket_live.data.ScoreCardResponse
import com.localpk.app.features.cricket_live.data.TeamConstants
import com.localpk.app.features.cricket_live.data.TeamsPoints
import com.localpk.app.features.cricket_live.data.UpcomingMatchesUiData
import com.localpk.app.features.cricket_live.domain.CricketBannerUseCase
import com.localpk.app.features.cricket_live.domain.MatchSummaryUseCase
import com.localpk.app.features.cricket_live.domain.PointsTableUseCase
import com.localpk.app.features.cricket_live.domain.PoweredByUseCase
import com.localpk.app.features.cricket_live.domain.ScoreCardUseCase
import com.localpk.app.features.cricket_live.domain.UpcomingMatchesUseCase
import com.localpk.app.utils.default
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.orEmpty
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MatchDetailViewModel(
    private val bannerUseCase: CricketBannerUseCase,
    private val scoreCardUseCase: ScoreCardUseCase,
    private val poweredByUseCase: PoweredByUseCase,
    private val pointsTableUseCase: PointsTableUseCase,
    private val matchSummaryUseCase: MatchSummaryUseCase,
    private val upcomingMatchesUseCase: UpcomingMatchesUseCase
) : BaseViewModel<Any>() {

    val isLiveTabSelected = MutableStateFlow(true)
    val isTeamASelected = MutableStateFlow(true)
    private val isTeamBSelected = MutableStateFlow(false)
    private val isTeamASuperOverSelected = MutableStateFlow(false)
    private val isTeamBSuperOverSelected = MutableStateFlow(false)
    val isSuperOverMatch = MutableStateFlow(false)
    val isLoggedInLiveData = MutableStateFlow(false)

    private val _upcomingMatches = MutableStateFlow<List<UpcomingMatchesUiData>>(emptyList())
    val upcomingMatches: StateFlow<List<UpcomingMatchesUiData>> = _upcomingMatches

    private val _mainSponsor = MutableStateFlow<PoweredByDetailUiData?>(null)
    val mainSponsor: StateFlow<PoweredByDetailUiData?> = _mainSponsor

    private val _scoreCardSponsor = MutableStateFlow<PoweredByDetailUiData?>(null)
    val scoreCardSponsor: StateFlow<PoweredByDetailUiData?> = _scoreCardSponsor

    private val _poweredBy = MutableStateFlow<List<PoweredByDetailUiData>>(emptyList())
    val poweredBy: StateFlow<List<PoweredByDetailUiData>> = _poweredBy

    private val _matchStatus = MutableStateFlow<String?>(null)
    val matchStatus: StateFlow<String?> = _matchStatus

    private val _pointsTable = MutableStateFlow<PointsTableUiData?>(null)
    val pointsTable: StateFlow<PointsTableUiData?> = _pointsTable

    private val _points = MutableStateFlow<List<TeamsPoints>>(emptyList())
    val points: StateFlow<List<TeamsPoints>> = _points

    private val _batsmen = MutableStateFlow<List<Batsman>>(emptyList())
    val batsmen: StateFlow<List<Batsman>> = _batsmen

    private val _liveScoreCard = MutableStateFlow<LiveScorecard?>(null)
    val liveScoreCard: StateFlow<LiveScorecard?> = _liveScoreCard

    private val _fullScoreCard = MutableStateFlow<FullScorecard?>(null)
    val fullScoreCard: StateFlow<FullScorecard?> = _fullScoreCard

    private val _totalRuns = MutableStateFlow<Long?>(null)
    val totalRuns: StateFlow<Long?> = _totalRuns

    private val _extras = MutableStateFlow<Extras?>(null)
    val extras: StateFlow<Extras?> = _extras

    private val _bowlers = MutableStateFlow<List<Bowler>>(emptyList())
    val bowlers: StateFlow<List<Bowler>> = _bowlers

    private val _yetToBat = MutableStateFlow<String?>(null)
    val yetToBat: StateFlow<String?> = _yetToBat

    private val _banners = MutableStateFlow<List<Banner>>(emptyList())
    val banners: StateFlow<List<Banner>> = _banners

    private val _match = MutableStateFlow<CricketSwimlaneUiData?>(null)
    val match: StateFlow<CricketSwimlaneUiData?> = _match

    private val _matches = MutableStateFlow<List<CricketSwimlaneUiData>>(emptyList())
    val matches: StateFlow<List<CricketSwimlaneUiData>> = _matches

    val _argsData = MutableStateFlow<String?>(null)
    val argsData: StateFlow<String?> = _argsData

    fun matchSummary() {
        unZipViewStateFlow(matchSummaryUseCase(Unit), onSuccess = {
            val data = it as List<CricketSwimlaneUiData>
            if (data.isNotEmpty() && data.isNotNull()) {
                data.firstOrNull { it.id == argsData.value }?.let { liveData ->
                    _match.value = liveData
                }
            }

        })

        if (argsData.value.default().isNotEmpty()) {
            getMatchScoreCard(argsData.value.default())
        }

    }

    fun getUpcomingMatchesDetails() {
        unZipViewStateFlow(
            upcomingMatchesUseCase(Unit),
            onSuccess = {
                val response = it as List<UpcomingMatchesUiData>
                _upcomingMatches.value = response
            },
            onError = {

            }
        )
    }

    fun loadCarousalBanners(belongsTo: String = "cricket") {
        unZipViewStateFlow(bannerUseCase(belongsTo), onSuccess = {
            val banners = it as List<Banner>
            _banners.value = banners.filter { it.bannerType == "home" }.sortedBy { it.order }
        })
    }

    fun getMainSponsor(successCallback: () -> Unit = {}) {
        unZipViewStateFlow(
            poweredByUseCase("main sponsor"),
            onSuccess = {
                val response = it as List<PoweredByDetailUiData>
                _mainSponsor.value = response.firstOrNull()
                successCallback()
            },
            onError = {

            })
    }

    fun getPoweredBy() {
        unZipViewStateFlow(
            poweredByUseCase("page sponsor"),
            onSuccess = {
                _poweredBy.value = it as List<PoweredByDetailUiData>
            },
            onError = {

            })
    }

    fun getScorecardSponsor() {
        unZipViewStateFlow(
            poweredByUseCase("scorecard sponsor"),
            onSuccess = {
                val response = it as List<PoweredByDetailUiData>
                _scoreCardSponsor.value = response.firstOrNull()
            },
            onError = {

            }
        )
    }

    fun getPointsTable() {
        unZipViewStateFlow(
            pointsTableUseCase(Unit),
            onSuccess = {
                val response = it as PointsTableUiData
                _pointsTable.value = response
                val filterGroupPoints =
                    response.groups.firstOrNull()
                _points.value = filterGroupPoints?.points.orEmptyArrayList()
            }, onError = {

            })
    }

    fun getPointsByGroup(groupName: String) {
        val filteredGroup = _pointsTable.value?.let {
            it.groups.filter { group -> group.group_name == groupName }
        }
        filteredGroup?.map { group -> _points.value = group.points }
    }

    private fun getMatchScoreCard(matchKey: String) {

        unZipViewStateFlow(
            scoreCardUseCase(matchKey),
            onSuccess = {
                val scorecard: ScoreCardResponse = it as ScoreCardResponse
                if (scorecard.body != null) {
                    val mappedScorecard = scorecard.body.map()
                    _liveScoreCard.value = mappedScorecard.liveScorecard
                    _fullScoreCard.value = mappedScorecard.fullScorecard
                    isSuperOverMatch.value = mappedScorecard.fullScorecard.isSuperOverMatch
                    if (mappedScorecard.status == MatchStatus.STARTED.key && isLiveTabSelected.value.default()) {
                        setLiveScoreCard()
                    } else {
                        setFullScoreCard()
                    }

                }
            },
        )

    }

    fun setLiveScoreCard() {
        _batsmen.value = _liveScoreCard.value?.liveDataBatsmen?.orEmpty()!!
        _bowlers.value = _liveScoreCard.value?.liveDataBowlers?.orEmpty()!!
    }

    fun setFullScoreCard() {
        val selectedTeam = when {
            isTeamASelected.value -> TeamConstants.TEAM_A.key
            isTeamBSelected.value -> TeamConstants.TEAM_B.key
            isTeamASuperOverSelected.value -> TeamConstants.TEAM_A_SUPER_OVER.key
            isTeamBSuperOverSelected.value -> TeamConstants.TEAM_B_SUPER_OVER.key
            else -> null
        }

        if (selectedTeam != null) {
            setTeamScoreCard(selectedTeam)
        } else {

        }
    }


    fun setTeamScoreCard(teamIdentifier: String) {
        val teamData = when (teamIdentifier) {
            TeamConstants.TEAM_A.key -> _fullScoreCard.value?.teamAData
            TeamConstants.TEAM_B.key -> _fullScoreCard.value?.teamBData
            TeamConstants.TEAM_A_SUPER_OVER.key -> _fullScoreCard.value?.teamASuperOverData
            TeamConstants.TEAM_B_SUPER_OVER.key -> _fullScoreCard.value?.teamBSuperOverData
            else -> null
        }
        _batsmen.value = teamData?.batsmen.orEmpty()
        _bowlers.value = teamData?.bowlers.orEmpty()
        _totalRuns.value = teamData?.totalRuns
        _extras.value = teamData?.extras
        _yetToBat.value = teamData?.yetToBat
    }


    fun setTeamSelected(team: TeamConstants) {
        isTeamASelected.value = team == TeamConstants.TEAM_A
        isTeamBSelected.value = team == TeamConstants.TEAM_B
        isTeamASuperOverSelected.value = team == TeamConstants.TEAM_A_SUPER_OVER
        isTeamBSuperOverSelected.value = team == TeamConstants.TEAM_B_SUPER_OVER
    }


    fun matchesSummary(successCallback: () -> Unit) {
        unZipViewStateFlow(matchSummaryUseCase(Unit), onSuccess = {
            val data = it as List<CricketSwimlaneUiData>
            if (data != _matches.value) {
                _matches.value = data
                successCallback()
            }
        })
    }

    fun setMatchData(match: CricketSwimlaneUiData) {
        _match.value = match
    }


}