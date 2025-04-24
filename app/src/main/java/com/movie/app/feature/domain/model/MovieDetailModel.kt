package com.movie.app.feature.domain.model

data class MovieDetailModel(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val tagline: String?,
    val revenue: Long?,
    val releaseDate: String?,
    val status: String?,
    val isWatchListed: Boolean = false
)