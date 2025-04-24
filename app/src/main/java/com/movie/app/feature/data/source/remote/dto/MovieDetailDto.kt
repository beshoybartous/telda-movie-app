package com.movie.app.feature.data.source.remote.dto

import com.google.gson.annotations.SerializedName
import com.movie.app.feature.domain.model.MovieDetailModel

data class MovieDetailDto(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName ("poster_path") val posterPath: String?,
    val tagline: String?,
    val revenue: Long?,
    @SerializedName ("release_date") val releaseDate: String?,
    val status: String?,
)

fun MovieDetailDto.toDomain(): MovieDetailModel {
    return MovieDetailModel(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        tagline = tagline,
        revenue = revenue,
        releaseDate = releaseDate,
        status = status
    )
}