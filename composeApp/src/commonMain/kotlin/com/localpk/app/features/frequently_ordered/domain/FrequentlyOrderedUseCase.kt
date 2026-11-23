package com.localpk.app.features.frequently_ordered.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.restaurant.domain.RequestData
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FrequentlyOrderedUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    private val sharedPreferencesManager: SharedPreferencesManager,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<RequestData, ViewState<List<ProductUiData>>>(dispatcher) {
    override fun execute(parameters: RequestData): Flow<ViewState<List<ProductUiData>>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getFrequentlyOrderedItems(parameters.limit, parameters.page).run {
                when (this) {
                    is ResponseResult.Success -> {
                        try {
                            if (sharedPreferencesManager.isLoggedIn()) {
                                val mappedList = data.body?.map { it.map() }.orEmptyArrayList()
                                emit(
                                    ViewState.Success(
                                        mappedList
                                    )
                                )
                            } else {
                                emit(ViewState.Success(arrayListOf()))
                            }

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
