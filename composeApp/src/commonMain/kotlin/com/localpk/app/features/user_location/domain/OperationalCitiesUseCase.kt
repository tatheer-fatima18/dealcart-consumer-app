package com.localpk.app.features.user_location.domain

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

class OperationalCitiesUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Unit, ViewState<List<String>>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<ViewState<List<String>>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getOperationalCities().run {
                when (this) {
                    is ResponseResult.Success ->
                        emit(ViewState.Success(data.body))

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }
}