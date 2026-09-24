package com.example.movies.presentation.ui.screens.movies.details

import com.example.movies.domain.models.MovieDetails

sealed interface MovieDetailsUiState {

    data object Loading : MovieDetailsUiState

    data class Content(
        val movieDetails: MovieDetails
    ) : MovieDetailsUiState

    data object Error : MovieDetailsUiState
}