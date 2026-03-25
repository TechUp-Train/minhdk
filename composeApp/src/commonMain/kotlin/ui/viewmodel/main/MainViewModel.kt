package ui.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aigenerator.PlatformImage
import data.core.remote.service.base.Response
import data.model.Category
import data.repo.style.StyleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.viewmodel.emptyState
import ui.view.exchanger.AppExchanger
import ui.view.screen.main.intents.PickImageIntent
import ui.view.state.UiState

class MainViewModel(
    private val styleRepository: StyleRepository
) : ViewModel() {

    private val _styleState = MutableStateFlow<UiState<List<Category>?>>(UiState.Loading)
    val styleState = _styleState.asStateFlow()

    private val _imageState = MutableStateFlow<List<PlatformImage>>(emptyList())
    val imageState = _imageState.asStateFlow()

    var pickImage = false
        get() {
            val temp = field
            field = false
            return temp
        }

    init {
        loadStyles()
        observeImages()
    }

    private fun loadStyles() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val res = styleRepository.getStyles()) {
                is Response.Success -> _styleState.value = UiState.Success(res.data)
                is Response.Error -> _styleState.value = UiState.Error("No available styles.")
            }
        }
    }

    private fun observeImages() {
        viewModelScope.launch {
            AppExchanger.exchangePickImageToMainPickImages.receiveAsFlow().collect {
                _imageState.value = it
            }
        }
    }

}