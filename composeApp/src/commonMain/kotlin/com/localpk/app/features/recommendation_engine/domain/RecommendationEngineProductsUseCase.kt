package com.localpk.app.features.recommendation_engine.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.recommendation_engine.data.RecommendationEngineProductsRequest
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecommendationEngineProductsUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<RecommendationEngineProductsRequest, ViewState<List<ProductUiData>>>(dispatcher) {

    override fun execute(parameters: RecommendationEngineProductsRequest): Flow<ViewState<List<ProductUiData>>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getRecommendedProduct(
                parameters.swimlane,
                parameters.screenType,
                parameters.productId
            ).run {
                when (this) {
                    is ResponseResult.Success -> {
                        val products = try {
                            data.body
                                ?.map { it.map() }
                                .orEmptyArrayList()
                                .filter { it.inventoryQty > 0 }
                                .sortedBy { it.slot }
                        } catch (e: Exception) {
                            arrayListOf()
                        }

                        emit(ViewState.Success(products))
                    }

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}
