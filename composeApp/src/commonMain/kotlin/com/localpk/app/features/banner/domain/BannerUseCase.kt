package com.localpk.app.features.banner.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.banner.data.Banner
import com.localpk.app.features.banner.data.BannerUseCaseParams
import com.localpk.app.network.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BannerUseCase(
    private val appRepository: AppRepository,
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<BannerUseCaseParams, ViewState<List<Banner>>>(dispatcher) {

    override fun execute(parameters: BannerUseCaseParams): Flow<ViewState<List<Banner>>> = flow {
        emit(ViewState.Loading)
        appRepository.getBanners(parameters.belongsTo).run {
            when (this) {
                is ResponseResult.Success -> {
                    emit(ViewState.Success(data.body.results ?: emptyList()))
                }

                else -> {
                    emit(errorResponseViewStateMapper.map(this as ResponseResult<Any>))
                }
            }
        }
    }
} 