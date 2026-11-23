package com.localpk.app.base

import com.localpk.app.data.ViewError

sealed class ViewState<out T> {
    data class Success<T>(val data: T) : ViewState<T>()
    object Empty : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()
    data class Error(val viewError: ViewError) : ViewState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$viewError]"
            is Empty -> "Empty"
            Loading -> "Loading"
        }
    }
}