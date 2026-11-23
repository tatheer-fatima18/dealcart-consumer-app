package com.localpk.app.features.swimlane_engine.presentation

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.swimlane_engine.data.LandingPageType
import com.localpk.app.features.swimlane_engine.data.SwimlaneUI
import com.localpk.app.features.swimlane_engine.data.SwimlaneRequestParams
import com.localpk.app.features.swimlane_engine.domain.SwimlaneByIdUseCase
import com.localpk.app.features.swimlane_engine.domain.SwimlaneEngineUseCase
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.default
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SwimlaneEngineViewModel(
    private val swimlaneEngineUseCase: SwimlaneEngineUseCase,
    private val swimlaneByIdUseCase: SwimlaneByIdUseCase
) : BaseViewModel<Any>() {

    private val _swimlaneDataMap = mutableMapOf<Int, MutableStateFlow<SwimlaneUI?>>()
    val swimlaneDataMap: Map<Int, StateFlow<SwimlaneUI?>> get() = _swimlaneDataMap.mapValues { it.value.asStateFlow() }

    private val alreadyFetchedIds = mutableSetOf<Int>()

    private val _swimlaneMap = mutableMapOf<String, MutableStateFlow<List<SwimlaneUI>>>()
    val swimlaneMap: Map<String, StateFlow<List<SwimlaneUI>>> get() = _swimlaneMap.mapValues { it.value.asStateFlow() }

    private val _isSwimlaneLoading = MutableStateFlow(false)
    val isSwimlaneLoading: StateFlow<Boolean> = _isSwimlaneLoading.asStateFlow()

    fun getSwimlanes(
        landingPage: String,
        superParentCategoryId: String? = EMPTY_STRING,
        successCallback: () -> Unit = {}
    ) {
        val key = superParentCategoryId.default().ifEmpty { landingPage }

        val stateFlow = _swimlaneMap.getOrPut(key) { MutableStateFlow(emptyList()) }
        // Clear the state first to ensure fresh data
        stateFlow.value = emptyList()
        _isSwimlaneLoading.value = true
        unZipViewStateFlow(
            swimlaneEngineUseCase(
                SwimlaneRequestParams(
                    landingPage,
                    RemoteConfigs.getUserType(),
                    superParentCategoryId.default()
                )
            ),
            onSuccess = { result ->
                val swimlaneData = result as List<SwimlaneUI>
                stateFlow.value = swimlaneData
                _isSwimlaneLoading.value = false
                successCallback()
            },
            onError = {
                _isSwimlaneLoading.value = false
            }
        )
    }

    private fun getSwimlaneById(
        landingPage: String,
        id: Int
    ) {
        if (!_swimlaneDataMap.containsKey(id)) {
            _swimlaneDataMap[id] = MutableStateFlow(null)
        }
        val stateFlow = _swimlaneDataMap[id]!!
        unZipViewStateFlow(
            swimlaneByIdUseCase(
                SwimlaneRequestParams(
                    landingPage,
                    RemoteConfigs.getUserType(),
                    EMPTY_STRING,
                    id
                )
            ),
            onSuccess = { result ->
                val swimlaneData = result as SwimlaneUI
                stateFlow.value = swimlaneData
            },
            onError = {}
        )
    }

    fun fetchSwimlaneDataIfNeeded(swimlaneId: Int) {
        if (!alreadyFetchedIds.contains(swimlaneId)) {
            alreadyFetchedIds.add(swimlaneId)
            getSwimlaneById(
                landingPage = LandingPageType.Main.type,
                id = swimlaneId
            )
        }
    }

    fun clearSwimlaneData() {
        // Clear all swimlane data when switching categories
        _swimlaneDataMap.clear()
        alreadyFetchedIds.clear()
    }

    fun clearAllSwimlaneData() {
        // Clear ALL swimlane data including main and category data
        _swimlaneDataMap.clear()
        _swimlaneMap.clear()
        alreadyFetchedIds.clear()
    }

    fun clearSwimlaneDataForCategory(categoryId: String?) {
        // Clear swimlane data for a specific category
        val key = categoryId.default().ifEmpty { LandingPageType.Main.type }
        _swimlaneMap.remove(key)
    }

    fun clearMainSwimlaneData() {
        // Clear main swimlane data specifically
        _swimlaneMap.remove(LandingPageType.Main.type)
    }
} 