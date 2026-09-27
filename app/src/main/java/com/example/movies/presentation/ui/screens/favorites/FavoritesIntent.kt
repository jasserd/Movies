package com.example.movies.presentation.ui.screens.favorites

sealed interface FavoritesIntent {

    data object RetryClicked : FavoritesIntent

    data class FavoriteClicked(
        val movieId: Int
    ) : FavoritesIntent
}