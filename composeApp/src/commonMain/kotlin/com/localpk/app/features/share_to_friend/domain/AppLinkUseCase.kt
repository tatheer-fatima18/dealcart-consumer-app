package com.localpk.app.features.share_to_friend.domain

import com.localpk.app.api.DolatAppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.share_to_friend.data.CreateAppLinkRequest
import com.localpk.app.features.share_to_friend.data.LinkResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AppLinkUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: DolatAppRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<CreateAppLinkRequest, ViewState<LinkResponse>>(dispatcher) {

    override fun execute(parameters: CreateAppLinkRequest): Flow<ViewState<LinkResponse>> =
        flow {
            emit(ViewState.Loading)
            appRepository.createAppLink(parameters).run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(data))

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}

