package com.example.movies.presentation.ui.screens.movies.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.movies.R
import com.example.movies.domain.models.MovieDetails
import com.example.movies.presentation.ui.components.AppTopBar

@Composable
fun MovieDetailsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    fun onRetryClick() {
        viewModel.onIntent(MovieDetailsIntent.RetryClicked)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.movie_details),
                icon = null,
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            MovieDetailsUiState.Loading ->
                MovieDetailsLoading(modifier = Modifier.padding(innerPadding))

            is MovieDetailsUiState.Content ->
                MovieDetailsContent(
                    movie = state.movieDetails,
                    modifier = Modifier.padding(innerPadding),
                )

            MovieDetailsUiState.Error ->
                MovieDetailsError(
                    onRetryClick = ::onRetryClick,
                    modifier = Modifier.padding(innerPadding)
                )
        }
    }
}

@Composable
fun MovieDetailsLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MovieDetailsContent(
    movie: MovieDetails,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        movie.posterUrl?.let { poster ->
            AsyncImage(
                model = poster,
                contentDescription = stringResource(R.string.movie_poster),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                movie.year?.let { year ->
                    Text(text = stringResource(R.string.movie_year, year))
                }

                movie.durationMinutes?.let { minutes ->
                    Text(text = stringResource(R.string.movie_duration, minutes))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                movie.director?.let { director ->
                    Text(
                        text = stringResource(R.string.movie_director, director),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Text(text = stringResource(R.string.movie_rating, movie.rating))
                }
            }

            if (movie.genres.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(movie.genres.joinToString(" • "))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movie.overview.ifBlank {
                    stringResource(R.string.movie_overview_empty)
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun MovieDetailsError(
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
            Text(stringResource(R.string.movie_details_loading_error))

            Button(onClick = onRetryClick) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}