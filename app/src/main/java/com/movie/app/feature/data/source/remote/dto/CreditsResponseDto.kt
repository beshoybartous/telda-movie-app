package com.movie.app.feature.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    val id: Int,
    @SerializedName("cast")
    val cast: List<CastDto>,
    @SerializedName("crew")
    val crew: List<CrewDto>
) {
    data class CastDto(
        val id: Int,
        val name: String,
        @SerializedName("profile_path") val profilePath: String? = null,
        val popularity: Double,
        @SerializedName("known_for_department")
        val knownForDepartment: String = ""
    )

    data class CrewDto(
        val id: Int,
        val name: String,
        @SerializedName("profile_path") val profilePath: String? = null,
        val popularity: Double,
        val department: String = "",
        val job: String,
    )
}