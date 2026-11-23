package com.localpk.app.features.address.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.address.data.Polygon
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetMapPolygonUseCase (
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<Unit, ViewState<Polygon>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<ViewState<Polygon>> = flow {
        emit(ViewState.Loading)
        appRepository.getAddressMapPolygon().run {
            when (this) {
                is ResponseResult.Success ->
                    emit(ViewState.Success(data.body))
                else -> emit(errorResponseViewStateMapper.map(this))
            }
        }
    }

}