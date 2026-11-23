package com.localpk.app.features.profile.data

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper

import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class CustomerSavingDetailUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Unit, ViewState<CustomerSavingDetailData>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<ViewState<CustomerSavingDetailData>> = flow {
        emit(ViewState.Loading)

        appRepository.getCustomerDetails().run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data.body.result))
                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }
}

