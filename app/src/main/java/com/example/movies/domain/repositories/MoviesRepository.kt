package com.example.movies.domain.repositories

import com.example.movies.domain.models.Movie
import com.example.movies.domain.models.MovieDetails
import com.example.movies.domain.models.MoviesPage
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getPopularMovies(page: Int): MoviesPage

    suspend fun searchMovies(query: String, page: Int): MoviesPage

    suspend fun getMovieDetails(movieId: Int): MovieDetails

    fun observeFavorites(): Flow<List<Movie>>

    suspend fun addFavorite(movie: Movie)

    suspend fun removeFavorite(movieId: Int)
}