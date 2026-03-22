package com.minhdk.githubkmp.ui.state

sealed class UiState<out T> {
    data object Loading: UiState<Nothing>()
    class Success<T>(val data: T) : UiState<T>()
    class Error(val message: String? = null) : UiState<Nothing>()
}
