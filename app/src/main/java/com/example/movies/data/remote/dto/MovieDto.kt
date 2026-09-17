package com.example.movies.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val id: Int,
    val title: String = "",
    @SerialName("release_date")
    val releaseDate: String = "",
    @SerialName("genre_ids")
    val genreIds: List<Int> = emptyList(),
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0
)