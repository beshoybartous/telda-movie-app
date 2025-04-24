package com.movie.app.feature.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.feature.domain.usecase.FetchCastOfMoviesUseCase
import com.movie.app.feature.domain.usecase.FetchMovieDetailsUseCase
import com.movie.app.feature.domain.usecase.FetchSimilarMoviesUseCase
import com.movie.app.feature.presentation.details.state.CastsUiState
import com.movie.app.feature.presentation.details.state.MovieDetailsUiState
import com.movie.app.feature.presentation.details.state.UiState
import com.movie.app.navigation.Screen.Companion.MOVIE_ID_ARGUMENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val fetchMovieDetailsUseCase: FetchMovieDetailsUseCase,
    private val fetchSimilarMoviesUseCase: FetchSimilarMoviesUseCase,
    private val fetchCastOfMoviesUseCase: FetchCastOfMoviesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState

    init {
        val movieId = savedStateHandle.get<Int>(MOVIE_ID_ARGUMENT)?.toInt()
        movieId?.let { loadMovieData(it) }
    }

    private fun loadMovieData(movieId: Int) {
        fetchMovieDetails(movieId)
        fetchSimilarMoviesAndCasts(movieId)
    }

    private fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(movieDetailState = UiState.Loading) }
            try {
                val movie = fetchMovieDetailsUseCase(movieId)
                _uiState.update { it.copy(movieDetailState = UiState.Success(movie)) }
            } catch (e: Exception) {
                _uiState.update { it.copy(movieDetailState = UiState.Error(e.message)) }
            }
        }
    }

    private fun fetchSimilarMoviesAndCasts(movieId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(similarMoviesState = UiState.Loading) }
            try {
                val similar = fetchSimilarMoviesUseCase(movieId)
                _uiState.update { it.copy(similarMoviesState = UiState.Success(similar)) }

                val similarIds = similar.map { it.id }
                if (similarIds.isNotEmpty()) {
                    fetchCasts(similarIds)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(similarMoviesState = UiState.Error(e.message)) }
            }
        }
    }

    private fun fetchCasts(movieIds: List<Int>) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        castsState = UiState.Loading
                    )
                }

                val (topActors, topDirectors) = fetchCastOfMoviesUseCase(movieIds)

                _uiState.update {
                    it.copy(
                        castsState = UiState.Success(
                            CastsUiState(
                                actorsState = topActors,
                                directorsState = topDirectors
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        castsState = UiState.Error(e.message)
                    )
                }
            }
        }
    }
}