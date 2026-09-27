package com.example.movies.presentation.ui.screens.favorites

import com.example.movies.domain.models.Movie

sealed interface FavoritesUiState {

    data object Loading : FavoritesUiState

    data class Content(
        val movies: List<Movie>
    ) : FavoritesUiState

    data object Error : FavoritesUiState
}