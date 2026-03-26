package ui.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.core.remote.service.base.Response
import data.repo.image.ImageRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ResultViewModel(
    private val imageRepository: ImageRepository
): ViewModel() {

    private val _downloadState = Channel<Response<Unit>>()
    val downloadState = _downloadState.receiveAsFlow()

    suspend fun loadImageData(url: String) {
        viewModelScope.launch {
            _downloadState.trySend(imageRepository.downloadImage(url))
        }
    }

}