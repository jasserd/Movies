package com.example.movies.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
    val id: Int,
    val title: String = "",
    @SerialName("release_date")
    val releaseDate: String = "",
    val runtime: Int? = null,
    val genres: List<GenreDto> = emptyList(),
    val overview: String = "",
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    val credits: CreditsDto = CreditsDto()
)

@Serializable
data class CreditsDto(
    val crew: List<CrewMemberDto> = emptyList()
)

@Serializable
data class CrewMemberDto(
    val name: String = "",
    val job: String = ""
)


