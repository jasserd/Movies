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
        val nextPageState: NextPageState = NextPageState.Idle
    ) : MoviesContentState {
        val canLoadNextPage: Boolean
            get() = currentPage < totalPages
    }

    data object Error : MoviesContentState
}

sealed interface NextPageState {

    data object Idle : NextPageState

    data object Loading : NextPageState

    data object Error : NextPageState
}