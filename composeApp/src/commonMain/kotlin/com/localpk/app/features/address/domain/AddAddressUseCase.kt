package com.localpk.app.features.address.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.ViewState.Loading
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.address.data.AddAddressRequest
import com.localpk.app.network.GeneralAddressResponse
import com.localpk.app.network.ResponseResult.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddAddressUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<AddAddressRequest, ViewState<GeneralAddressResponse>>(dispatcher) {

    override fun execute(parameter: AddAddressRequest): Flow<ViewState<GeneralAddressResponse>> =
        flow {
            emit(Loading)
            appRepository.addAddress(parameter).run {
                when (this) {
                    is Success ->
                        emit(ViewState.Success(data))

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}