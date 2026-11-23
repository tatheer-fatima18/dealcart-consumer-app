package com.localpk.app.features.cricket_live.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.base.domain.ExternalLinkHandler
import com.localpk.app.features.cricket_live.components.MatchSummaryCard
import com.localpk.app.features.cricket_live.components.MatchSummaryCardNotLoggedIn
import com.localpk.app.features.cricket_live.components.PointsTableSection
import com.localpk.app.features.cricket_live.components.PoweredByList
import com.localpk.app.features.cricket_live.components.TopBarCricket
import com.localpk.app.features.cricket_live.components.UpcomingMatchesList
import com.localpk.app.features.cricket_live.data.CricketSwimlaneUiData
import com.localpk.app.features.cricket_live.data.TeamConstants
import com.localpk.app.features.cricket_live.viewModel.MatchDetailViewModel
import com.localpk.app.features.home.components.BannerSlider
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.login.presentation.PhoneInputScreen
import com.localpk.app.features.product_detail.presentation.ProductDetailScreen
import com.localpk.app.features.productlist.data.toProductDetailScreen
import com.localpk.app.features.productlist.presentation.ProductListScreen
import com.localpk.app.features.productlist.viewModel.ProductListViewModel
import com.localpk.app.theming.ColorTermCondition
import com.localpk.app.theming.Horizontal_Separator
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.CATEGORY
import com.localpk.app.utils.CRICKET
import com.localpk.app.utils.DEAL
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EXTERNAL
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.PRODUCT
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.points_table
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class MatchDetailScreen(
    val id: String,
    val currentBattingTeam: String,
    val teamA: String,
    val flagTeamA: String,
    val scoreTeamA: String,
    val wicketTeamA: String,
    val overTeamA: String,
    val currentRunRateTeamA: String,
    val isResultTeamA: Boolean,
    val teamB: String,
    val flagTeamB: String,
    val scoreTeamB: String,
    val wicketTeamB: String,
    val overTeamB: String,
    val currentRunRateTeamB: String,
    val requiredRunRateTeamB: String,
    val score: String,
    val wicket: String,
    val currentRunRate: String,
    val requiredRunRate: String,
    val isResultTeamB: Boolean,
    val status: String,
    val over: String,
    val message: String,
    val isLive: Boolean,
    val isTeamAPlaying: Boolean,
    val isTeamBPlaying: Boolean,
    val startAtTime: String,
    val startAtDate: String,
    val isToday: Boolean,
    val isResult: Boolean,
    val wasMatchStarted: Boolean,
    val isDLApplied: Boolean,
    val reducedOversTeamA: String,
    val isReducedOversTeamA: Boolean,
    val reducedOversTeamB: String,
    val isReducedOversTeamB: Boolean,
    val reducedTargetTeamB: Long
) : Screen {

    @Composable
    override fun Content() {

        val sharedPreferencesManager: SharedPreferencesManager = koinInject()
        val matchDetailViewModel = koinViewModel<MatchDetailViewModel>()

        LaunchedEffect(Unit) {
            matchDetailViewModel._argsData.value = id
            matchDetailViewModel.loadCarousalBanners()
            matchDetailViewModel.getMainSponsor()
            matchDetailViewModel.getPoweredBy()
            matchDetailViewModel.getScorecardSponsor()
            matchDetailViewModel.setMatchData(
                CricketSwimlaneUiData(
                    id,
                    currentBattingTeam,
                    teamA,
                    flagTeamA,
                    scoreTeamA,
                    wicketTeamA,
                    overTeamA,
                    currentRunRateTeamA,
                    isResultTeamA,
                    teamB,
                    flagTeamB,
                    scoreTeamB,
                    wicketTeamB,
                    overTeamB,
                    currentRunRateTeamB,
                    requiredRunRateTeamB,
                    score,
                    wicket,
                    currentRunRate,
                    requiredRunRate,
                    isResultTeamB,
                    status,
                    over,
                    message,
                    isLive,
                    isTeamAPlaying,
                    isTeamBPlaying,
                    startAtTime,
                    startAtDate,
                    isToday,
                    isResult,
                    wasMatchStarted,
                    isDLApplied,
                    reducedOversTeamA,
                    isReducedOversTeamA,
                    reducedOversTeamB,
                    isReducedOversTeamB,
                    reducedTargetTeamB
                )
            )
            if (sharedPreferencesManager.isLoggedIn()) {
                matchDetailViewModel.isLoggedInLiveData.value = true
                matchDetailViewModel.getUpcomingMatchesDetails()
                matchDetailViewModel.getPointsTable()
                coroutineScope {
                    launch {
                        do {
                            matchDetailViewModel.matchSummary()
                            delay(10000)
                        } while (matchDetailViewModel.match.value?.isLive.default())
                    }
                }
            }

        }

        val matchLiveData by matchDetailViewModel.match.collectAsState()
        val mainSponsor by matchDetailViewModel.mainSponsor.collectAsState()
        val scorecardSponsor by matchDetailViewModel.scoreCardSponsor.collectAsState()
        val banners by matchDetailViewModel.banners.collectAsState(emptyList())
        val upcomingMatches by matchDetailViewModel.upcomingMatches.collectAsState(emptyList())
        val poweredBy by matchDetailViewModel.poweredBy.collectAsState(emptyList())
        val pointsTable by matchDetailViewModel.pointsTable.collectAsState()
        val pointsLiveData by matchDetailViewModel.points.collectAsState(emptyList())
        val fullScoreCard by matchDetailViewModel.fullScoreCard.collectAsState()
        val batsmenData by matchDetailViewModel.batsmen.collectAsState(emptyList())
        val bowlersData by matchDetailViewModel.bowlers.collectAsState(emptyList())
        val totalRuns by matchDetailViewModel.totalRuns.collectAsState(0L)
        val extrasData by matchDetailViewModel.extras.collectAsState()
        val yetToBat by matchDetailViewModel.yetToBat.collectAsState("")
        val isLiveTabSelected by matchDetailViewModel.isLiveTabSelected.collectAsState(true)
        val isSuperOverMatch by matchDetailViewModel.isSuperOverMatch.collectAsState(false)

        var showScoreCardContainer by remember { mutableStateOf(false) }
        var selectedGroup by remember { mutableStateOf("") }
        val eventLogger: EventLogger = koinInject()
        val productListViewModel = koinViewModel<ProductListViewModel>()
        val externalLinkHandler: ExternalLinkHandler = koinInject()

        val navigator = LocalNavigator.currentOrThrow

        StatusBarIconColor(backgroundColor = ColorTermCondition)
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(Horizontal_Separator)
        ) {
            item {
                TopBarCricket(
                    mainSponsor = mainSponsor,
                    onBackClick = {
                        navigator.pop()
                    }
                )
            }

            if (matchLiveData.isNotNull()) {
                item {
                    if (sharedPreferencesManager.isLoggedIn()) {
                        MatchSummaryCard(
                            matchData = matchLiveData!!,
                            scorecardSponsor = scorecardSponsor,
                            showScoreCardContainer = showScoreCardContainer,
                            onFullDetailsClick = {
                                showScoreCardContainer = !showScoreCardContainer
                            },
                            fullScoreCard = fullScoreCard,
                            isLiveTabSelected = isLiveTabSelected,
                            onLiveTabClick = {
                                matchDetailViewModel.isLiveTabSelected.value = true
                                matchDetailViewModel.setLiveScoreCard()
                            },
                            onScorecardTabClick = {
                                matchDetailViewModel.isLiveTabSelected.value = false
                                matchDetailViewModel.setFullScoreCard()
                            },
                            isSuperOverMatch = isSuperOverMatch,
                            onTeamAClick = {
                                matchDetailViewModel.setTeamSelected(TeamConstants.TEAM_A)
                                matchDetailViewModel.setTeamScoreCard(TeamConstants.TEAM_A.key)
                            },
                            onTeamBClick = {
                                if (fullScoreCard?.isTeamBInningStarted.default()) {
                                    matchDetailViewModel.setTeamSelected(TeamConstants.TEAM_B)
                                    matchDetailViewModel.setTeamScoreCard(TeamConstants.TEAM_B.key)
                                } else {
//                                    onShowError("Yet to bat")
                                }
                            },
                            onTeamASuperOverClick = {
                                matchDetailViewModel.setTeamSelected(TeamConstants.TEAM_A_SUPER_OVER)
                                matchDetailViewModel.setTeamScoreCard(TeamConstants.TEAM_A_SUPER_OVER.key)
                            },
                            onTeamBSuperOverClick = {
                                if (fullScoreCard?.isTeamBSuperOverInningStarted.default()) {
                                    matchDetailViewModel.setTeamSelected(TeamConstants.TEAM_B_SUPER_OVER)
                                    matchDetailViewModel.setTeamScoreCard(TeamConstants.TEAM_B_SUPER_OVER.key)
                                } else {
//                                    onShowError("Yet to bat")
                                }
                            },
                            batsmenData = batsmenData,
                            bowlersData = bowlersData,
                            totalRuns = totalRuns.getValueOrDefault(),
                            extrasData = extrasData,
                            yetToBat = yetToBat.default()
                        )
                    } else {
                        MatchSummaryCardNotLoggedIn(
                            matchData = matchLiveData!!,
                            scorecardSponsor = scorecardSponsor,
                            onLoginClick = {
                                navigator.push(PhoneInputScreen(navSource = ScreenSource.MATCH_DETAIL_SCREEN.key))
                            }
                        )
                    }
                }
            }

            if (banners.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    BannerSlider(
                        banners = banners,
                        onBannerClick = { banner ->
                            eventLogger.bannerClick(
                                type = "home",
                                id = banner.id,
                                redirectType = banner.redirectType.default(),
                                redirectURL = banner.redirectURL,
                                matchName = EMPTY_STRING
                            )
                            when (banner.redirectType) {
                                CATEGORY -> {
                                    productListViewModel.argCategoryId.value = null
                                    navigator.push(
                                        ProductListScreen(
                                            catId = banner.redirectTypeId.getValueOrDefault(),
                                        )
                                    )
                                }

                                PRODUCT, DEAL -> {
                                    navigator.push(
                                        ProductDetailScreen(
                                            id = banner.redirectTypeId.getValueOrDefault(),
                                            name = EMPTY_STRING,
                                            description = EMPTY_STRING,
                                            imageUrl = EMPTY_STRING,
                                            price = ZERO,
                                            finalPrice = ZERO,
                                            discountedPrice = ZERO,
                                            inventoryQty = 0,
                                            purchasePrice = ZERO,
                                            isSponsored = false,
                                            slot = 0,
                                            showMrp = false,
                                            purchaseLimit = 0,
                                            itemQuantity = 0
                                        )
                                    )
                                }

                                EXTERNAL -> {
                                    if (banner.redirectURL.isNotEmpty()) {
                                        externalLinkHandler.openExternalLink(banner.redirectURL)
                                    }
                                }

                                CRICKET -> {}
                            }
                        }
                    )
                }
            }

            if (upcomingMatches.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Upcoming Matches",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PoppinsFontFamily(),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    UpcomingMatchesList(
                        matches = upcomingMatches
                    )
                }
            }

            if (poweredBy.isNotEmpty()) {
                item {
                    if (upcomingMatches.isEmpty())
                        Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Powered By",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    PoweredByList(
                        poweredByList = poweredBy,
                        onNavigateToProductHost = { categoryId ->
                            navigator.push(
                                ProductListScreen(
                                    catId = categoryId.getValueOrDefault(),
                                )
                            )
                        },
                        onNavigateToProductDetail = { product ->
                            navigator.push(product.toProductDetailScreen())
                        }
                    )
                }
            }

            if (pointsTable != null && pointsLiveData.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(Res.string.points_table),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                    PointsTableSection(
                        pointsTable = pointsTable!!,
                        pointsLiveData = pointsLiveData,
                        selectedGroup = selectedGroup,
                        onGroupSelected = { groupName ->
                            selectedGroup = groupName
                            matchDetailViewModel.getPointsByGroup(groupName)
                        }
                    )
                }
            }
        }
    }
}