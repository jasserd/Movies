package com.example.movies.domain.repositories

import com.example.movies.domain.models.MoviesPage

interface MoviesRepository {

    suspend fun getPopularMovies(page: Int): MoviesPage

    suspend fun searchMovies(query: String, page: Int): MoviesPage
}