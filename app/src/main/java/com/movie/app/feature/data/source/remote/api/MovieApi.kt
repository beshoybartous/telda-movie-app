package com.movie.app.feature.data.source.remote.api

import com.movie.app.feature.data.source.remote.dto.CreditsResponse
import com.movie.app.feature.data.source.remote.dto.MovieDetailDto
import com.movie.app.feature.data.source.remote.dto.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("3/movie/popular")
    suspend fun getMovies(
        @Query("page") page: Int
    ): MovieResponse

    @GET("3/search/movie")
    suspend fun searchMovie(
        @Query("page") page: Int,
        @Query("query") query: String,
    ): MovieResponse

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): MovieDetailDto

    @GET("3/movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int
    ): MovieResponse

    @GET("3/movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int
    ): CreditsResponse
}