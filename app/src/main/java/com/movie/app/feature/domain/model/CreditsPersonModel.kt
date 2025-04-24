package com.movie.app.feature.domain.model

data class CreditsPersonModel(
    val id: Int,
    val name: String,
    val profilePath: String?,
    val popularity: Double,
    val department: String?=null
)