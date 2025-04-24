package com.movie.app.feature.domain.model

data class ActorModel(
    val id: Int,
    val name: String,
    val profilePath: String?,
    val popularity: Double
)