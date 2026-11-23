package com.localpk.app.features.category.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.category.data.CategoryResponse
import com.localpk.app.features.category.data.CategoryUiModel
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.orEmpty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryUseCase(
    private val appRepository: AppRepository,
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, ViewState<List<CategoryUiModel>>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<ViewState<List<CategoryUiModel>>> = flow {
        emit(ViewState.Loading)

        appRepository.getCategories().run {
            when (this) {
                is ResponseResult.Success -> {
                    emit(ViewState.Success(getCategoryData()))
                }

                else -> {
                    emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
    }

    private fun ResponseResult.Success<CategoryResponse>.getCategoryData(): List<CategoryUiModel> {
        val categories = data.body.results?.map { it.map() }.orEmpty()
        return categories
    }
} 