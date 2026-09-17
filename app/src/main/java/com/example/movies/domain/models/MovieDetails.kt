package com.example.movies.domain.models

data class MovieDetails(
    val id: Int,
    val title: String,
    val year: Int?,
    val durationMinutes: Int?,
    val genres: List<String>,
    val director: String?,
    val overview: String,
    val posterUrl: String?,
    val isFavorite: Boolean,
    val rating: Double
)