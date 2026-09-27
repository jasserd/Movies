package com.example.movies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val year: Int?,
    val genresJson: String,
    val posterUrl: String?,
    val rating: Double,
)
