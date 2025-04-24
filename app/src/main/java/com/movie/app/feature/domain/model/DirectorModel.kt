package com.movie.app.feature.domain.model

data class DirectorModel(
    val id: Int,
    val name: String,
    val profilePath: String?,
    val popularity: Double
)