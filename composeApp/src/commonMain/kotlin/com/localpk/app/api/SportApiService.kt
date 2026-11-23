package com.localpk.app.api

import com.localpk.app.base.data.BaseApiService
import com.localpk.app.features.cricket_live.data.MatchSummaryResponse
import com.localpk.app.features.cricket_live.data.PointsTableResponse
import com.localpk.app.features.cricket_live.data.ScoreCardResponse
import com.localpk.app.features.cricket_live.data.UpcomingMatchesResponse
import com.localpk.app.utils.SharedPreferencesManager
import io.ktor.client.HttpClient

interface SportApiService {
    suspend fun getMatchScoreCard(matchKey: String): ScoreCardResponse
    suspend fun getUpcomingMatches(): UpcomingMatchesResponse
    suspend fun getPointsTableData(): PointsTableResponse
    suspend fun matchSummary(): MatchSummaryResponse
}

class SportApiServiceImpl(
    httpClient: HttpClient,
    baseUrl: String,
    sharedPreferencesManager: SharedPreferencesManager
) : BaseApiService(httpClient, baseUrl, sharedPreferencesManager), SportApiService {

    override suspend fun getMatchScoreCard(matchKey: String): ScoreCardResponse {
        return get(
            endpoint = "/cricket/match/$matchKey/details"
        )
    }

    override suspend fun matchSummary(): MatchSummaryResponse {
        return get(
            endpoint = "/cricket/summary"
        )
    }

    override suspend fun getPointsTableData(): PointsTableResponse {
        return get(
            endpoint = "/cricket/points-table"
        )
    }

    override suspend fun getUpcomingMatches(): UpcomingMatchesResponse {
        return get(
            endpoint = "/cricket/upcoming_matches"
        )
    }


}