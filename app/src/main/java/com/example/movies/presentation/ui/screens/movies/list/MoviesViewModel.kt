package com.example.movies.presentation.ui.screens.movies.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.domain.models.Movie
import com.example.movies.domain.repositories.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())

    val uiState = _uiState.asStateFlow()

    private var moviesJob: Job? = null

    private var favoriteIds: Set<Int> = emptySet()

    init {
        observeFavorites()
        loadMovies(
            query = "",
            useDebounce = false
        )
    }

    fun onIntent(intent: MoviesIntent) {
        when (intent) {
            is MoviesIntent.QueryChanged -> {
                _uiState.value = _uiState.value.copy(query = intent.query)

                loadMovies(
                    query = _uiState.value.query,
                    useDebounce = _uiState.value.query.isNotBlank()
                )
            }

            MoviesIntent.RetryClicked -> {
                loadMovies(
                    query = _uiState.value.query,
                    useDebounce = false
                )
            }

            MoviesIntent.NextPageRequested -> {
                loadNextPage()
            }

            is MoviesIntent.FavoriteClicked -> {
                handleFavoriteStatus(intent.movieId)
            }
        }
    }

    private fun loadMovies(
        query: String,
        useDebounce: Boolean
    ) {
        moviesJob?.cancel()

        moviesJob = viewModelScope.launch {
            if (useDebounce) {
                delay(SEARCH_DEBOUNCE_MILLIS)
            }

            _uiState.value = _uiState.value.copy(content = MoviesContentState.Loading)

            try {
                val normalizedQuery = query.trim()

                val page = if (normalizedQuery.isNotEmpty()) {
                    repository.searchMovies(query = normalizedQuery, page = FIRST_PAGE)
                } else {
                    repository.getPopularMovies(page = FIRST_PAGE)
                }

                _uiState.value =
                    _uiState.value.copy(
                        content = MoviesContentState.Content(
                            movies = withFavoriteIds(page.movies),
                            currentPage = page.currentPage,
                            totalPages = page.totalPages,
                        )
                    )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(content = MoviesContentState.Error)
            }
        }
    }

    private fun loadNextPage() {
        val currentContent = _uiState.value.content as? MoviesContentState.Content
            ?: return

        if (
            currentContent.nextPageState == NextPageState.Loading ||
            !currentContent.canLoadNextPage
        ) {
            return
        }

        val nextPage = currentContent.currentPage + 1
        val query = _uiState.value.query.trim()

        _uiState.value = _uiState.value.copy(
            content = currentContent.copy(
                nextPageState = NextPageState.Loading
            )
        )

        moviesJob = viewModelScope.launch {
            try {
                val loadedPage = if (query.isEmpty()) {
                    repository.getPopularMovies(nextPage)
                } else {
                    repository.searchMovies(
                        query = query,
                        page = nextPage
                    )
                }

                val latestContent = _uiState.value.content as? MoviesContentState.Content
                    ?: return@launch

                _uiState.value = _uiState.value.copy(
                    content = latestContent.copy(
                        movies = (
                                latestContent.movies +
                                        withFavoriteIds(loadedPage.movies)
                                ).distinctBy { movie ->
                                movie.id
                            },
                        currentPage = loadedPage.currentPage,
                        totalPages = loadedPage.totalPages,
                        nextPageState = NextPageState.Idle
                    )
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                val latestContent =
                    _uiState.value.content as? MoviesContentState.Content
                        ?: return@launch

                _uiState.value = _uiState.value.copy(
                    content = latestContent.copy(
                        nextPageState = NextPageState.Error
                    )
                )
            }
        }
    }

    private fun handleFavoriteStatus(movieId: Int) {
        val content = _uiState.value.content as? MoviesContentState.Content
            ?: return

        val targetMovie = content.movies.firstOrNull { movie ->
            movie.id == movieId
        } ?: return

        viewModelScope.launch {
            if (targetMovie.isFavorite) {
                repository.removeFavorite(movieId)
            } else {
                repository.addFavorite(targetMovie)
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.observeFavorites().collect { favorites ->
                favoriteIds = favorites.map { movie ->
                    movie.id
                }.toSet()

                val content = _uiState.value.content as? MoviesContentState.Content
                    ?: return@collect

                _uiState.value = _uiState.value.copy(
                    content = content.copy(
                        movies = withFavoriteIds(content.movies)
                    )
                )
            }
        }
    }

    private fun withFavoriteIds(movies: List<Movie>): List<Movie> {
        return movies.map { movie ->
            movie.copy(isFavorite = movie.id in favoriteIds)
        }
    }

    private companion object {
        const val FIRST_PAGE = 1
        const val SEARCH_DEBOUNCE_MILLIS = 500L
    }
}