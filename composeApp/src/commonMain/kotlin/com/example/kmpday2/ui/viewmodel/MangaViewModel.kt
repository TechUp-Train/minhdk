package com.example.kmpday2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmpday2.data.model.Manga
import com.example.kmpday2.data.model.RequestStatus
import com.example.kmpday2.data.model.UiState
import com.example.kmpday2.data.repository.MangaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MangaViewModel(
    private val mangaRepo: MangaRepository
) : ViewModel() {

    private val _mangaState = MutableStateFlow<UiState<List<Manga>>>(UiState.Loading)
    val mangaState = _mangaState.asStateFlow()

    init {
        getManga()
    }

    private fun getManga() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = mangaRepo.getManga()
            println(result.toString())
            when(result.status) {
                RequestStatus.SUCCESS -> _mangaState.value = UiState.Success(result.data ?: emptyList())
                RequestStatus.ERROR -> _mangaState.value = UiState.Error(result.error ?: "")
                else -> {}
            }
        }
    }
}