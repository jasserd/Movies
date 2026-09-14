package com.example.movies.presentation.ui.screens.movies

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.movies.R
import com.example.movies.domain.models.Movie
import com.example.movies.presentation.ui.components.AppTopBar
import com.example.movies.presentation.ui.components.MovieCard
import com.example.movies.presentation.ui.screens.movies.components.SearchField

private val testMovies = listOf(
    Movie(
        id = 1,
        title = "Дюна",
        year = 2021,
        durationMinutes = 155,
        genres = listOf("Фантастика", "Драма"),
        director = "Дени Вильнёв",
        posterUrl = null,
        isFavorite = true,
        rating = 8.1
    ),
    Movie(
        id = 2,
        title = "Интерстеллар",
        year = 2014,
        durationMinutes = 169,
        genres = listOf("Фантастика", "Драма"),
        director = "Кристофер Нолан",
        posterUrl = null,
        isFavorite = false,
        rating = 8.7
    ),
    Movie(
        id = 3,
        title = "Бегущий по лезвию 2049",
        year = 2017,
        durationMinutes = 164,
        genres = listOf("Фантастика", "Триллер"),
        director = "Дени Вильнёв",
        posterUrl = null,
        isFavorite = false,
        rating = 8.0
    ),
    Movie(
        id = 4,
        title = "Властелин колец: Возвращение короля",
        year = 2003,
        durationMinutes = 201,
        genres = listOf("Фэнтези", "Приключения"),
        director = "Питер Джексон",
        posterUrl = null,
        isFavorite = true,
        rating = 8.9
    ),
    Movie(
        id = 5,
        title = "Дюна",
        year = 2021,
        durationMinutes = 155,
        genres = listOf("Фантастика", "Драма"),
        director = "Дени Вильнёв",
        posterUrl = null,
        isFavorite = true,
        rating = 8.1
    ),
    Movie(
        id = 6,
        title = "Интерстеллар",
        year = 2014,
        durationMinutes = 169,
        genres = listOf("Фантастика", "Драма"),
        director = "Кристофер Нолан",
        posterUrl = null,
        isFavorite = false,
        rating = 8.7
    ),
    Movie(
        id = 7,
        title = "Бегущий по лезвию 2049",
        year = 2017,
        durationMinutes = 164,
        genres = listOf("Фантастика", "Триллер"),
        director = "Дени Вильнёв",
        posterUrl = null,
        isFavorite = false,
        rating = 8.0
    ),
    Movie(
        id = 8,
        title = "Властелин колец: Возвращение короля",
        year = 2003,
        durationMinutes = 201,
        genres = listOf("Фэнтези", "Приключения"),
        director = "Питер Джексон",
        posterUrl = null,
        isFavorite = true,
        rating = 8.9
    )
)

@Composable
fun MoviesScreen(
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable {
        mutableStateOf("")
    }

    fun onQueryChanged(newQuery: String) {
        query = newQuery
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.movies)
            )
        }
    ) { innerPadding ->
        MoviesContent(
            modifier = Modifier.padding(innerPadding),
            movies = testMovies,
            onMovieClick = onMovieClick,
            onFavoriteClick = { movieId ->
            },
            query = query,
            onQueryChanged = ::onQueryChanged,
        )
    }
}

@Composable
fun MoviesContent(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchField(
            value = query,
            onValueChange = onQueryChanged,
            placeholderText = stringResource(R.string.search),
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 8.dp
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .pointerInput(focusManager) {
                awaitEachGesture {
                    awaitFirstDown(
                        pass = PointerEventPass.Initial
                    )
                    focusManager.clearFocus()
                }
            },
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
