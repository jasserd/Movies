package com.example.movies.data.remote.mapper

import com.example.movies.data.remote.dto.MovieDto
import com.example.movies.data.remote.dto.MoviesPageDto
import com.example.movies.domain.models.Movie
import com.example.movies.domain.models.MoviesPage

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun MovieDto.toDomain(
    genreNamesById: Map<Int, String>
): Movie {
    return Movie(
        id = id,
        title = title,
        year = releaseDate
            .take(4)
            .toIntOrNull(),
        genres = genreIds.mapNotNull { genreId ->
            genreNamesById[genreId]
        },
        posterUrl = posterPath?.let {
            "$POSTER_BASE_URL$it"
        },
        isFavorite = false,
        rating = voteAverage
    )
}

fun MoviesPageDto.toDomain(
    genreNamesById: Map<Int, String>
): MoviesPage {
    return MoviesPage(
        movies = results.map { movie ->
            movie.toDomain(genreNamesById)
        },
        currentPage = page,
        totalPages = totalPages
    )
}