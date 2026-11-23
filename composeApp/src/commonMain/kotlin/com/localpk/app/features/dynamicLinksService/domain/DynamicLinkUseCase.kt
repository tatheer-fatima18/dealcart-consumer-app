package com.localpk.app.features.dynamicLinksService.domain

import com.localpk.app.api.LinkAppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.dynamicLinksService.data.DynamicLinkRequest
import com.localpk.app.features.dynamicLinksService.data.DynamicLinkResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DynamicLinkUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: LinkAppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<DynamicLinkRequest, ViewState<DynamicLinkResponse>>(dispatcher) {

    override fun execute(parameters: DynamicLinkRequest): Flow<ViewState<DynamicLinkResponse>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getDynamicLink(parameters).run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(data))

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}

