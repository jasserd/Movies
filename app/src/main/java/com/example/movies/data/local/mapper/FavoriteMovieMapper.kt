package com.example.movies.data.local.mapper

import com.example.movies.data.local.entity.FavoriteMovieEntity
import com.example.movies.domain.models.Movie
import kotlinx.serialization.json.Json

fun Movie.toFavoriteEntity(): FavoriteMovieEntity {
    return FavoriteMovieEntity(
        id = id,
        title = title,
        year = year,
        genresJson = Json.encodeToString(genres),
        posterUrl = posterUrl,
        rating = rating
    )
}

fun FavoriteMovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        year = year,
        genres = Json.decodeFromString(genresJson),
        posterUrl = posterUrl,
        isFavorite = true,
        rating = rating,
    )
}