package com.localpk.app.features.flash_deals.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.flash_deals.data.FlashDealUiData
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FlashDealsUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<String, ViewState<List<FlashDealUiData>>>(dispatcher) {

    override fun execute(parameters: String): Flow<ViewState<List<FlashDealUiData>>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getFlashDeals(parameters).run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(data.body?.map { it.map() }.orEmptyArrayList()))
                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}