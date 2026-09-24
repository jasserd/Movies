package com.example.movies.data.remote.mapper

import com.example.movies.data.remote.dto.MovieDetailsDto
import com.example.movies.domain.models.MovieDetails

private const val DIRECTOR_JOB = "Director"

fun MovieDetailsDto.toDomain(): MovieDetails {
    return MovieDetails(
        id = id,
        title = title,
        year = releaseDate
            .take(4)
            .toIntOrNull(),
        durationMinutes = runtime,
        genres = genres.map { genre ->
            genre.name
        },
        director = credits.crew
            .firstOrNull { crewMember ->
                crewMember.job == DIRECTOR_JOB
            }
            ?.name,
        overview = overview,
        posterUrl = posterPath?.let {
            "$POSTER_BASE_URL$it"
        },
        isFavorite = false,
        rating = voteAverage,
    )
}