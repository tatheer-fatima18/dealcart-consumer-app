package com.localpk.app.api

import com.localpk.app.features.cricket_live.data.MatchSummaryResponse
import com.localpk.app.features.cricket_live.data.PointsTableResponse
import com.localpk.app.features.cricket_live.data.ScoreCardResponse
import com.localpk.app.features.cricket_live.data.UpcomingMatchesResponse
import com.localpk.app.network.ResponseResult
import com.localpk.app.network.callApi

interface SportAppRepository {
    suspend fun getMatchScoreCard(matchKey: String): ResponseResult<ScoreCardResponse>
    suspend fun getUpcomingMatches(): ResponseResult<UpcomingMatchesResponse>
    suspend fun getPointsTableData(): ResponseResult<PointsTableResponse>
    suspend fun matchSummary(): ResponseResult<MatchSummaryResponse>
}

class SportAppRepositoryImpl(
    private val service: SportApiService
) : SportAppRepository {

    override suspend fun getMatchScoreCard(matchKey: String): ResponseResult<ScoreCardResponse> {
        return callApi {
            service.getMatchScoreCard(matchKey)
        }
    }

    override suspend fun matchSummary(): ResponseResult<MatchSummaryResponse> {
        return callApi {
            service.matchSummary()
        }
    }

    override suspend fun getPointsTableData(): ResponseResult<PointsTableResponse> {
        return callApi {
            service.getPointsTableData()
        }
    }

    override suspend fun getUpcomingMatches(): ResponseResult<UpcomingMatchesResponse> {
        return callApi {
            service.getUpcomingMatches()
        }
    }

}