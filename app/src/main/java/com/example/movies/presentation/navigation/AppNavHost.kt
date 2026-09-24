package com.example.movies.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movies.presentation.ui.screens.favorites.FavoritesScreen
import com.example.movies.presentation.ui.screens.movies.details.MovieDetailsScreen
import com.example.movies.presentation.ui.screens.movies.list.MoviesScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MoviesRoute,
    ) {
        composable<MoviesRoute> {
            MoviesScreen(
                onMovieClick = { movieId ->
                    navController.navigate(MovieDetailsRoute(movieId))
                }
            )
        }

        composable<FavoritesRoute> {
            FavoritesScreen()
        }

        composable<MovieDetailsRoute> {
            MovieDetailsScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
            )
        }
    }
}