package com.example.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movies.data.local.dao.FavoriteMoviesDao
import com.example.movies.data.local.entity.FavoriteMovieEntity

@Database(
    entities = [FavoriteMovieEntity::class],
    version = 1
)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun favoriteMoviesDao(): FavoriteMoviesDao
}