package com.localpk.app.features.view_all_products.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

@Serializable
data class ProductRequestData(
    val isPromotionalCategory: Boolean,
    val categoryId: Long? = null,
    val swimlaneId: Int? = null,
    val limit: Int,
    val page: Int
)

class ViewAllProductUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<ProductRequestData, ViewState<List<ProductUiData>>>(dispatcher) {
    override fun execute(parameters: ProductRequestData): Flow<ViewState<List<ProductUiData>>> =
        flow {
            emit(ViewState.Loading)
            if (parameters.isPromotionalCategory) {
                appRepository.getAllSwimlaneItems(
                    parameters.categoryId.getValueOrDefault(),
                    parameters.limit,
                    parameters.page
                ).run {
                    when (this) {
                        is ResponseResult.Success -> {
                            try {
                                val mappedList = data.body?.map { it.map() }.orEmptyArrayList()
                                emit(
                                    ViewState.Success(
                                        mappedList
                                    )
                                )
                            } catch (e: Exception) {
                                emit(ViewState.Success(arrayListOf()))
                            }
                        }

                        else -> {
                            emit(errorResponseViewStateMapper.map(this))
                        }
                    }
                }
            } else {
                appRepository.viewAllSwimlaneItems(
                    parameters.swimlaneId.getValueOrDefault(),
                    parameters.limit,
                    parameters.page
                ).run {
                    when (this) {
                        is ResponseResult.Success -> {
                            try {
                                val mappedList = data.body?.map { it.map() }.orEmptyArrayList()
                                emit(
                                    ViewState.Success(
                                        mappedList
                                    )
                                )
                            } catch (e: Exception) {
                                emit(ViewState.Success(arrayListOf()))
                            }
                        }

                        else -> {
                            emit(errorResponseViewStateMapper.map(this))
                        }
                    }
                }
            }
        }
}
