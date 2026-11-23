package com.localpk.app.base

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.screen.Screen
import com.localpk.app.data.ViewError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Stable
abstract class BaseViewModel<T> : ViewModel() {
    open fun onInIt() {}

    private val _navigation = MutableSharedFlow<NavigationEvent>()
    val navigation: SharedFlow<NavigationEvent> = _navigation.asSharedFlow()

    // ViewState using StateFlow
    private val _viewState = MutableStateFlow<ViewState<T>>(ViewState.Empty)
    val viewState: StateFlow<ViewState<T>> = _viewState.asStateFlow()

    // Loading state for individual operations
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error handling
    private val _error = MutableStateFlow<ViewError?>(null)
    val error: StateFlow<ViewError?> = _error.asStateFlow()

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        viewModelScope.launch(context, block = block)
    }

    protected fun unZipViewStateFlow(
        viewStateFlow: Flow<ViewState<T>>,
        onSuccess: (T) -> Unit = {},
        onError: (ViewError) -> Unit = {},
        onLoading: () -> Unit = {},
        onEmpty: () -> Unit = {}
    ) {
        launch {
            viewStateFlow.collectLatest { viewState ->
                when (viewState) {
                    is ViewState.Success -> onSuccess(viewState.data)
                    is ViewState.Error -> onError(viewState.viewError)
                    is ViewState.Loading -> onLoading()
                    is ViewState.Empty -> onEmpty()
                }
                _viewState.value = viewState
            }
        }
    }

    protected fun setLoading(loading: Boolean = true) {
        _isLoading.value = loading
        if (loading) {
            _viewState.value = ViewState.Loading
        }
    }

    protected fun setSuccess(data: T) {
        _isLoading.value = false
        _error.value = null
        _viewState.value = ViewState.Success(data)
    }

    protected fun setError(viewError: ViewError) {
        _isLoading.value = false
        _error.value = viewError
        _viewState.value = ViewState.Error(viewError)
    }

    protected fun setEmpty() {
        _isLoading.value = false
        _error.value = null
        _viewState.value = ViewState.Empty
    }

    protected fun clearError() {
        _error.value = null
    }

    protected fun getCurrentViewState(): ViewState<T> = _viewState.value

    private fun navigate(event: NavigationEvent) {
        launch {
            _navigation.emit(event)
        }
    }

    fun push(screen: Screen) = navigate(NavigationEvent.Push(screen))
    fun pop() = navigate(NavigationEvent.Pop)
    fun popToRoot() = navigate(NavigationEvent.PopToRoot)
    fun popToUntil(screen: Any) = navigate(NavigationEvent.PopToUntil(screen))
    fun replaceAll(screen: Screen) = navigate(NavigationEvent.ReplaceAll(screen))
    fun pushBottomSheet(screen: Screen) =
        navigate(NavigationEvent.PushBottomSheet(screen))
    fun showBottomSheet(screen: Screen) =
        navigate(NavigationEvent.ShowBottomSheet(screen))
}
