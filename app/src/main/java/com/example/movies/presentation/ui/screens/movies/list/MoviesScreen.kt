package com.example.movies.presentation.ui.screens.movies.list

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movies.R
import com.example.movies.domain.models.Movie
import com.example.movies.presentation.ui.components.AppTopBar
import com.example.movies.presentation.ui.components.MovieCard
import com.example.movies.presentation.ui.screens.movies.components.SearchField
import kotlinx.coroutines.flow.filter

@Composable
fun MoviesScreen(
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current


    fun onQueryChanged(newQuery: String) {
        viewModel.onIntent(MoviesIntent.QueryChanged(query = newQuery))
    }

    fun onRetryClick() {
        viewModel.onIntent(MoviesIntent.RetryClicked)
    }

    fun onFavoriteClick(id: Int) {

    }

    fun onNextPageRequested() {
        viewModel.onIntent(MoviesIntent.NextPageRequested)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.movies)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pointerInput(focusManager) {
                    awaitEachGesture {
                        awaitFirstDown(
                            pass = PointerEventPass.Initial
                        )
                        focusManager.clearFocus()
                    }
                },
        ) {
            SearchField(
                value = uiState.query,
                onValueChange = ::onQueryChanged,
                placeholderText = stringResource(R.string.search),
                modifier = Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                )
            )

            when (val content = uiState.content) {
                MoviesContentState.Loading -> MoviesLoading(modifier = Modifier.weight(1f))

                is MoviesContentState.Content -> {
                    MoviesContent(
                        modifier = Modifier.weight(1f),
                        movies = content.movies,
                        canLoadNextPage = content.canLoadNextPage,
                        nextPageState = content.nextPageState,
                        onMovieClick = onMovieClick,
                        onFavoriteClick = ::onFavoriteClick,
                        onNextPageRequested = ::onNextPageRequested
                    )
                }

                MoviesContentState.Error -> MoviesError(
                    modifier = Modifier.weight(1f),
                    onRetryClick = ::onRetryClick
                )
            }
        }
    }
}

@Composable
fun MoviesLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MoviesContent(
    movies: List<Movie>,
    canLoadNextPage: Boolean,
    nextPageState: NextPageState,
    onMovieClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onNextPageRequested: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(
        listState,
        movies.size,
        canLoadNextPage,
        nextPageState,
    ) {
        snapshotFlow {
            val lastVisibleItemIndex =
                listState.layoutInfo
                    .visibleItemsInfo
                    .lastOrNull()
                    ?.index
                    ?: -1

            movies.isNotEmpty() &&
                    canLoadNextPage &&
                    nextPageState == NextPageState.Idle &&
                    lastVisibleItemIndex >=
                    movies.lastIndex - NEXT_PAGE_LOAD_OFFSET
        }
            .filter { shouldLoad ->
                shouldLoad
            }
            .collect {
                onNextPageRequested()
            }
    }

    if (movies.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.movies_list_empty))
        }
    } else {
        LazyColumn(
            modifier = modifier,
            state = listState,
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
            if (nextPageState == NextPageState.Loading) {
                item(key = "next_page_loading") {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun MoviesError(
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
            Text(stringResource(R.string.movies_loading_error))

            Button(onClick = onRetryClick) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}

private const val NEXT_PAGE_LOAD_OFFSET = 3
