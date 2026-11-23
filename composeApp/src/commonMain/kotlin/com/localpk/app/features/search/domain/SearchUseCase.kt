package com.localpk.app.features.search.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.productlist.data.ProductList
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.getValueOrDefault
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchUseCase(
    private val appRepository: AppRepository,
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<String, ViewState<List<ProductUiData>>>(dispatcher) {

    override fun execute(parameters: String): Flow<ViewState<List<ProductUiData>>> = flow {
        emit(ViewState.Loading)
        appRepository.searchProducts(parameters).run {
            when (this) {
                is ResponseResult.Success -> {
                    emit(ViewState.Success(getFilterProducts(data.body)))
                }
                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }

    private fun getFilterProducts(body: ProductList): List<ProductUiData> {
        val productResponseList = body.results.orEmpty().toMutableList()
        val oosProductsList = mutableListOf<ProductUiData>()
        oosProductsList.addAll(
            productResponseList.filter {
                (it.inventories?.firstOrNull()?.dcImsQuantityAvailable.getValueOrDefault()) <= 0
            }.map { it.map() }
        )

        productResponseList.removeAll {
            (it.inventories?.firstOrNull()?.dcImsQuantityAvailable.getValueOrDefault()) <= 0
        }
        val inStockProductsList = productResponseList.map { it.map() }
        val allProductsList = mutableListOf<ProductUiData>()
        allProductsList.addAll(inStockProductsList)
        allProductsList.addAll(oosProductsList)
        return allProductsList
    }
} 