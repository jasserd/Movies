package com.example.movies.presentation.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.domain.repositories.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)

    val uiState = _uiState.asStateFlow()

    private var favoritesJob: Job? = null

    init {
        observeFavorites()
    }

    fun onIntent(intent: FavoritesIntent) {
        when (intent) {
            is FavoritesIntent.FavoriteClicked -> {
                handleFavoriteStatus(intent.movieId)
            }

            FavoritesIntent.RetryClicked -> {
                observeFavorites()
            }
        }
    }

    private fun observeFavorites() {
        _uiState.value = FavoritesUiState.Loading

        favoritesJob?.cancel()

        favoritesJob = viewModelScope.launch {
            repository.observeFavorites()
                .catch { exception ->
                    if (exception is CancellationException) throw exception
                    _uiState.value = FavoritesUiState.Error
                }
                .collect { movies ->
                    _uiState.value = FavoritesUiState.Content(movies)
                }
        }
    }

    private fun handleFavoriteStatus(movieId: Int) {
        viewModelScope.launch {
            try {
                repository.removeFavorite(movieId)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = FavoritesUiState.Error
            }
        }
    }
}