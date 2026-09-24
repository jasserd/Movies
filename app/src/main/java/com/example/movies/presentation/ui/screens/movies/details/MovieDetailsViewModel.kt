package com.example.movies.presentation.ui.screens.movies.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.movies.domain.repositories.MoviesRepository
import com.example.movies.presentation.navigation.MovieDetailsRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MoviesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId =
        savedStateHandle.toRoute<MovieDetailsRoute>().movieId

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)

    val uiState = _uiState.asStateFlow()

    init {
        loadMovieDetails()
    }

    fun onIntent(intent: MovieDetailsIntent) {
        when (intent) {
            MovieDetailsIntent.RetryClicked -> {
                loadMovieDetails()
            }
        }
    }

    private fun loadMovieDetails() {
        viewModelScope.launch {
            _uiState.value = MovieDetailsUiState.Loading

            try {
                val movieDetails = repository.getMovieDetails(movieId)

                _uiState.value = MovieDetailsUiState.Content(
                    movieDetails = movieDetails
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = MovieDetailsUiState.Error
            }
        }
    }
}