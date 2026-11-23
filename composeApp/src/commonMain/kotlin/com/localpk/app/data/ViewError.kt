package com.localpk.app.data


data class ViewError(
    val type: ViewErrorType = ViewErrorType.Toast,
    val message: String,
    val code: Int = 0,
    val errorCode: Int = 0,
    val userBlocked: Boolean = false,
    val action: () -> Unit = {},
    val title: String? = null,
    val isRetryable: Boolean = false
)

sealed class ViewErrorType {
    object Dialog : ViewErrorType()
    object Toast : ViewErrorType()
    object Snackbar : ViewErrorType()
}