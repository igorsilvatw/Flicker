package com.flicker.cvs.feature.flimage.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flicker.cvs.feature.flimage.domain.FLImageUseCase
import com.flicker.cvs.feature.flimage.domain.toDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlickerViewModel(
    private val executionDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val responseDispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val flImageUseCase: FLImageUseCase = FLImageUseCase.instantiate()
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(StateLoading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchFlickerData("porcupine")
    }

    fun fetchFlickerData(tag: String = "") {
        viewModelScope.launch(executionDispatcher) {
            _uiState.emit(StateLoading)
            val response = flImageUseCase.getImages(tag)
            withContext(responseDispatcher) {
                if (response.isSuccessful) {
                    _uiState.emit(StateSuccess(response.body().toDomain()))
                } else {
                    _uiState.emit(StateError)
                }
            }
        }
    }


}
