package com.flicker.cvs.feature.flimage.presentation

import com.flicker.cvs.feature.flimage.domain.FLImage


sealed class UiState

data class StateSuccess(val data: List<FLImage>) : UiState()

data object StateLoading : UiState()

data object StateError : UiState()
