package com.movie.app.feature.domain.model

data class MovieModel(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterPath: String,
    val isWatchListed: Boolean = false
)
