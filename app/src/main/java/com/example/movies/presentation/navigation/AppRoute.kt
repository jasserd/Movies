package com.example.movies.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MoviesRoute

@Serializable
data object FavoritesRoute

@Serializable
data class MovieDetailsRoute(val movieId: Int)