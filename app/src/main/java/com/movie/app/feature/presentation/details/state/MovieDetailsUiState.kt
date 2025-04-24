package com.movie.app.feature.presentation.details.state

import com.movie.app.feature.domain.model.ActorModel
import com.movie.app.feature.domain.model.DirectorModel
import com.movie.app.feature.domain.model.MovieDetailModel
import com.movie.app.feature.domain.model.MovieModel

data class MovieDetailsUiState(
    val movieDetailState: UiState<MovieDetailModel> = UiState.Idle,
    val similarMoviesState: UiState<List<MovieModel>> = UiState.Idle,
    val castsState: UiState<CastsUiState> = UiState.Idle,
)

sealed interface UiState<out T> {
    object Idle : UiState<Nothing>
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String?) : UiState<Nothing>
}

data class CastsUiState(
    val actorsState: List<ActorModel> = emptyList(),
    val directorsState: List<DirectorModel> = emptyList(),
)