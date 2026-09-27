package com.example.movies.di

import android.content.Context
import androidx.room.Room
import com.example.movies.data.local.MoviesDatabase
import com.example.movies.data.local.dao.FavoriteMoviesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(
        @ApplicationContext context: Context
    ): MoviesDatabase {
        return Room.databaseBuilder(
            context,
            MoviesDatabase::class.java,
            "movies.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteMoviesDao(
        database: MoviesDatabase
    ): FavoriteMoviesDao {
        return database.favoriteMoviesDao()
    }
}