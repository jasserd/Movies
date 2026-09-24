package com.example.movies.data.remote.api

import com.example.movies.data.remote.dto.GenreResponseDto
import com.example.movies.data.remote.dto.MovieDetailsDto
import com.example.movies.data.remote.dto.MoviesPageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("language") language: String = "ru-RU"
    ): MoviesPageDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("language") language: String = "ru-RU",
        @Query("include_adult") includeAdult: Boolean = false
    ): MoviesPageDto

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") language: String = "ru-RU",
    ): GenreResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "ru-RU",
        @Query("append_to_response") appendToResponse: String = "credits"
    ): MovieDetailsDto
}