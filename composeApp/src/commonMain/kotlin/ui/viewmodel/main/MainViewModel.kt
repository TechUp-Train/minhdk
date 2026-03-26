package ui.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aigenerator.PlatformImage
import data.core.remote.service.base.Response
import data.model.Category
import data.model.PromptRequest
import data.model.PromptResponse
import data.model.Style
import data.repo.generation.GenerationRepository
import data.repo.style.StyleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.viewmodel.emptyState
import ui.view.exchanger.AppExchanger
import ui.view.screen.main.intents.GenerateImageIntent
import ui.view.screen.main.intents.PickImageIntent
import ui.view.state.UiState

class MainViewModel(
    private val styleRepository: StyleRepository,
    private val generationRepo: GenerationRepository
) : ViewModel() {

    private val _styleState = MutableStateFlow<UiState<List<Category>?>>(UiState.Loading)
    val styleState = _styleState.asStateFlow()

    private val _imageState = MutableStateFlow<List<PlatformImage>>(emptyList())
    val imageState = _imageState.asStateFlow()

    private val _selectedStyle = MutableStateFlow<Style?>(null)
    val selectedStyle = _selectedStyle.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState = _errorState.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _generationState = Channel<PromptResponse>()
    val generationState = _generationState.receiveAsFlow()

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

    fun selectStyle(style: Style) {
        _selectedStyle.value = style
    }

    private fun notifyError(error: String) {
        _errorState.value = error
        viewModelScope.launch {
            delay(1500L)
            _errorState.value = null
        }
    }

    fun handleGenerationIntent(intent: GenerateImageIntent) {
        when(intent) {
            is GenerateImageIntent.Wrong -> {
                notifyError(intent.content)
            }

            is GenerateImageIntent.Generation -> {
                _loadingState.value = true
                generateImage(intent.prompt, intent.images, intent.style)
            }
        }
    }

    private fun generateImage(prompt: String, images: List<PlatformImage>, style: Style) {
        viewModelScope.launch(Dispatchers.IO) {
            when(val res = generationRepo.generate(prompt, images, style)) {
                is Response.Success -> {
                    _loadingState.value = false
                    _generationState.trySend(res.data)
                }
                is Response.Error -> {
//                    _errorState.value = "Some error happen !"
//                    viewModelScope.launch {
//                        delay(1500L)
//                        _errorState.value = null
//                    }
                    notifyError("Some error happen !")
                }
            }
        }
    }



}