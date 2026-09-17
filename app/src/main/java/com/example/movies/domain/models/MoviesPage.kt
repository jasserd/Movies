package com.example.movies.domain.models

data class MoviesPage(
    val movies: List<Movie>,
    val currentPage: Int,
    val totalPages: Int
)
