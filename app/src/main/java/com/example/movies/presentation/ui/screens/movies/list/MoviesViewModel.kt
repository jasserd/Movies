package com.example.movies.presentation.ui.screens.movies.list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MoviesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MoviesUiState())

    val uiState = _uiState.asStateFlow()
}