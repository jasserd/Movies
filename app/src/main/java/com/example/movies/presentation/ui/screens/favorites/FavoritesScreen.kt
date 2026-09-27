package com.example.movies.presentation.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movies.R
import com.example.movies.domain.models.Movie
import com.example.movies.presentation.ui.components.AppTopBar
import com.example.movies.presentation.ui.components.MovieCard

@Composable
fun FavoritesScreen(
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    fun onRetryClick() {
        viewModel.onIntent(FavoritesIntent.RetryClicked)
    }

    fun onFavoriteClick(movieId: Int) {
        viewModel.onIntent(FavoritesIntent.FavoriteClicked(movieId))
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.favorites)
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            FavoritesUiState.Loading -> FavoritesLoading(modifier.padding(innerPadding))

            is FavoritesUiState.Content -> FavoritesContent(
                movies = state.movies,
                onMovieClick = onMovieClick,
                onFavoriteClick = ::onFavoriteClick,
                modifier = modifier.padding(innerPadding)
            )

            FavoritesUiState.Error -> FavoritesError(
                onRetryClick = ::onRetryClick,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun FavoritesLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun FavoritesContent(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (movies.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.favorites_list_empty))
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = movies,
                key = { movie -> movie.id }
            ) { movie ->
                MovieCard(
                    movie = movie,
                    onClick = {
                        onMovieClick(movie.id)
                    },
                    onFavoriteClick = {
                        onFavoriteClick(movie.id)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun FavoritesError(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(stringResource(R.string.favorites_loading_error))

            Button(onClick = onRetryClick) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}
