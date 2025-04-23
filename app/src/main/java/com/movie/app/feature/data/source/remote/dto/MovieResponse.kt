package com.movie.app.feature.data.source.remote.dto

import com.google.gson.annotations.SerializedName
import com.movie.app.feature.domain.model.MovieModel

data class MovieResponse(
    @SerializedName("results")
    val results: List<MovieDto>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
) {
    data class MovieDto(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("overview")
        val overview: String,
        @SerializedName("release_date")
        val releaseDate: String,
        @SerializedName("poster_path")
        val posterPath: String,
        val isWatchListed: Boolean = false
    )
}

fun MovieResponse.MovieDto.toMovie(isWatchListed: Boolean = false): MovieModel =
    MovieModel(
        id = this.id,
        title = this.title,
        overview = this.overview,
        releaseDate = this.releaseDate,
        posterPath = this.posterPath,
        isWatchListed = isWatchListed
    )