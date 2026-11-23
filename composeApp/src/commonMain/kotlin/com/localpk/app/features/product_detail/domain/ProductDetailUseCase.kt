package com.localpk.app.features.product_detail.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.productlist.data.ProductDetailUiData
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductDetailUseCase(
    private val appRepository: AppRepository,
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Long, ViewState<ProductDetailUiData>>(dispatcher) {

    override fun execute(parameters: Long): Flow<ViewState<ProductDetailUiData>> = flow {
        emit(ViewState.Loading)

        appRepository.getProductDetail(parameters).run {
            when (this) {
                is ResponseResult.Success -> {
                    emit(ViewState.Success(data.body.mapDetail()))
                }

                else -> {
                    emit(errorResponseViewStateMapper.map(this as ResponseResult<Any>))
                }
            }
        }
    }
}