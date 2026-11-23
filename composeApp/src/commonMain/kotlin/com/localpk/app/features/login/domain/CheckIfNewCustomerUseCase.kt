package com.localpk.app.features.login.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.login.data.CheckIfCustomerExistsResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckIfNewCustomerUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<String, ViewState<CheckIfCustomerExistsResponse>>(dispatcher) {

    override fun execute(parameters: String): Flow<ViewState<CheckIfCustomerExistsResponse>> =
        flow {
            emit(ViewState.Loading)
            appRepository.checkIfCustomerExists(parameters).run {
                when (this) {
                    is ResponseResult.Success -> emit(ViewState.Success(data))
                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}