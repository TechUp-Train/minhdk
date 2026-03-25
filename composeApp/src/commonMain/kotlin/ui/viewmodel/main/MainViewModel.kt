package ui.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.core.remote.service.base.Response
import data.model.Category
import data.repository.sytle.StyleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ui.view.state.UiState

class MainViewModel(
    private val styleRepository: StyleRepository
) : ViewModel() {

    private val _styleState = MutableStateFlow<UiState<List<Category>?>>(UiState.Loading)
    val styleState = _styleState.asStateFlow()

    init {
        loadStyles()
    }

    private fun loadStyles() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val res = styleRepository.getStyles()) {
                is Response.Success -> _styleState.value = UiState.Success(res.data)
                is Response.Error -> _styleState.value = UiState.Error("No available styles.")
            }
        }
    }

}