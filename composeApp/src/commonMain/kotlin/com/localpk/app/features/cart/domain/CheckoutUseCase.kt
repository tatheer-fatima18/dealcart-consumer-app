package com.localpk.app.features.cart.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.cart.data.CheckoutRequest
import com.localpk.app.features.cart.data.CheckoutResponse
import com.localpk.app.features.cart.data.ProductOOSDetails
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckoutUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<CheckoutRequest, ViewState<CheckoutResponse>>(dispatcher) {

    override fun execute(parameters: CheckoutRequest): Flow<ViewState<CheckoutResponse>> =
        flow {
            emit(ViewState.Loading)
            appRepository.placeOrder(parameters).run {
                when (this) {
                    is ResponseResult.Success -> {
                        val filteredData = data.copy(
                            body = data.body.copy(
                                oosItems = filterOosItems(data.body.oosItems)
                            )
                        )
                        emit(ViewState.Success(filteredData))
                    }
                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }

    private fun filterOosItems(oosItems: List<ProductOOSDetails>?): List<ProductOOSDetails>? {
        if (oosItems.isNullOrEmpty()) return oosItems

        return oosItems
            .groupBy { it.product?.id }
            .values
            .flatMap { itemsWithSameId ->
                // If same product id (duplicates), filter to keep only availableQuantity > 0
                // Otherwise, keep all items (unique product ids)
                if (itemsWithSameId.size > 1) {
                    itemsWithSameId.filter { (it.availableQuantity ?: 0) > 0 }
                } else {
                    itemsWithSameId
                }
            }
            .takeIf { it.isNotEmpty() }
    }
}