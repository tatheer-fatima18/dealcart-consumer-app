package com.localpk.app.features.user_location.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.user_location.data.UserLocationRequest
import com.localpk.app.features.user_location.data.UserLocationResponse
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserLocationWarehouseUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<UserLocationRequest, ViewState<UserLocationResponse>>(dispatcher) {

    override fun execute(parameters: UserLocationRequest): Flow<ViewState<UserLocationResponse>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getWarehouse(parameters).run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(data))

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}