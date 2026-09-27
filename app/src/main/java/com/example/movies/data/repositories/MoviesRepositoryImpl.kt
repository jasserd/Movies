package com.example.movies.data.repositories

import com.example.movies.data.local.dao.FavoriteMoviesDao
import com.example.movies.data.local.mapper.toFavoriteEntity
import com.example.movies.data.local.mapper.toDomain as toFavoriteMovieDomain
import com.example.movies.data.remote.api.TmdbApi
import com.example.movies.data.remote.mapper.toDomain
import com.example.movies.domain.models.Movie
import com.example.movies.domain.models.MovieDetails
import com.example.movies.domain.models.MoviesPage
import com.example.movies.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val api: TmdbApi,
    private val favoritesDao: FavoriteMoviesDao
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

    override fun observeFavorites(): Flow<List<Movie>> {
        return favoritesDao.observeFavorites().map { entities ->
            entities.map { entity ->
                entity.toFavoriteMovieDomain()
            }
        }
    }

    override suspend fun addFavorite(movie: Movie) {
        favoritesDao.addFavorite(movie.toFavoriteEntity())
    }

    override suspend fun removeFavorite(movieId: Int) {
        favoritesDao.deleteFavorite(movieId)
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