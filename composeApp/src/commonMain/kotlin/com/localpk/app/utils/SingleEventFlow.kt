package com.localpk.app.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SingleEventFlow<T> {
    private val _events = MutableSharedFlow<T>(replay = 0, extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    suspend fun emit(value: T) {
        _events.emit(value)
    }

    fun tryEmit(value: T) {
        _events.tryEmit(value)
    }
}