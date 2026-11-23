package com.localpk.app.features.order_review.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.order_review.data.OrderReviewResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetOrderForReviewUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Unit, ViewState<OrderReviewResponse>>(dispatcher) {

    override fun execute(parameter: Unit): Flow<ViewState<OrderReviewResponse>> = flow {
        emit(ViewState.Loading)
        appRepository.getLatestOrderForReview().run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data))

                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }
}
