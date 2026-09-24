package com.example.movies.data.repositories

import com.example.movies.data.remote.api.TmdbApi
import com.example.movies.data.remote.mapper.toDomain
import com.example.movies.domain.models.MovieDetails
import com.example.movies.domain.models.MoviesPage
import com.example.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val api: TmdbApi
) : MoviesRepository {

    private var cachedGenreNamesById: Map<Int, String>? = null

    override suspend fun getPopularMovies(page: Int): MoviesPage {
        val genreNamesById = getGenreNamesById()

        return api.getPopularMovies(page).toDomain(genreNamesById)
    }

    override suspend fun searchMovies(query: String, page: Int): MoviesPage {
        val genreNamesById = getGenreNamesById()

        return api.searchMovies(query, page).toDomain(genreNamesById)
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return api.getMovieDetails(movieId).toDomain()
    }

    private suspend fun getGenreNamesById(): Map<Int, String> {
        cachedGenreNamesById?.let {
            return it
        }

        val genreNamesById = api.getMovieGenres()
            .genres
            .associate { genre ->
                genre.id to genre.name
            }

        cachedGenreNamesById = genreNamesById

        return genreNamesById
    }
}