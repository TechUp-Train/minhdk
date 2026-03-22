package com.minhdk.githubkmp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhdk.githubkmp.data.config.network.Response
import com.minhdk.githubkmp.data.core.storage.database.entity.EntityUser
import com.minhdk.githubkmp.data.repository.user.UserRepository
import com.minhdk.githubkmp.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel(
    private val userRepo: UserRepository
): ViewModel() {

    private val _userState = MutableStateFlow<UiState<EntityUser?>>(UiState.Loading)
    val userState = _userState.asStateFlow()

    init {
        fetchUser("KhacMinh2305")
    }

    private fun fetchUser(username: String) {
        viewModelScope.launch {
            userRepo.getUser(username).collect { response ->
                _userState.value = when (response) {
                    is Response.Success -> UiState.Success(response.data)
                    is Response.Error -> UiState.Error()
                }
            }
        }
    }

}