package com.example.movies.presentation.ui.screens.movies.list

sealed interface MoviesIntent {

    data class QueryChanged(
        val query: String
    ) : MoviesIntent

    data object RetryClicked : MoviesIntent

    data object NextPageRequested : MoviesIntent

//    data class FavoriteClicked(
//        val movieId: Int
//    ) : MoviesIntent
}