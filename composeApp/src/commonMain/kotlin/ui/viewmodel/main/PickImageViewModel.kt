package ui.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aigenerator.PlatformImage
import data.repo.image.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PickImageViewModel(
    private val imageRepository: ImageRepository
): ViewModel() {

    private val _imageState = MutableStateFlow<List<PlatformImage>>(emptyList())
    val imageState: StateFlow<List<PlatformImage>> = _imageState.asStateFlow()

    init {
        loadImages()
    }

    private fun loadImages() {
        viewModelScope.launch {
            try {
                val loadedImages = imageRepository.loadImage().filterNotNull()
                _imageState.value = loadedImages
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}