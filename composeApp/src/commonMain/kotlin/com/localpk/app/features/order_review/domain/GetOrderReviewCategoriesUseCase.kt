package com.localpk.app.features.order_review.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.order_review.data.CategoryUIData
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetOrderReviewCategoriesUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<String, ViewState<List<CategoryUIData>>>(dispatcher) {

    override fun execute(parameter: String): Flow<ViewState<List<CategoryUIData>>> = flow {
        emit(ViewState.Loading)
        appRepository.getOrderReviewCategories(parameter).run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data.body))
                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }
}
