package com.example.movies.presentation.ui.screens.movies.list

import com.example.movies.domain.models.Movie

data class MoviesUiState(
    val query: String = "",
    val content: MoviesContentState = MoviesContentState.Loading
)

sealed interface MoviesContentState {

    data object Loading : MoviesContentState

    data class Content(
        val movies: List<Movie>,
        val currentPage: Int,
        val totalPages: Int,
        val isNextPageLoading: Boolean = false
    ) : MoviesContentState {
        val canLoadNextPage: Boolean
            get() = currentPage < totalPages
    }

    data class Error(
        val message: String
    ) : MoviesContentState
}
