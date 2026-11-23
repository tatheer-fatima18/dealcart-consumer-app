package com.localpk.app.features.address.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.ViewState.Loading
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.address.data.AddressUiData
import com.localpk.app.network.ResponseResult.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchCustomerAddressUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<Unit, ViewState<List<AddressUiData>>>(dispatcher) {

    override fun execute(parameter: Unit): Flow<ViewState<List<AddressUiData>>> = flow {
        emit(Loading)
        appRepository.fetchCustomerAddress().run {
            when (this) {
                is Success ->
                    emit(ViewState.Success(data.body.map { it.map() }))

                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }
}