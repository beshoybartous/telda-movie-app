package com.movie.app.feature.domain.usecase

import com.movie.app.feature.domain.model.*
import com.movie.app.feature.domain.repo.MovieRepository
import javax.inject.Inject

class FetchMovieDetailsUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int): MovieDetailModel = repository.getMovieDetails(movieId)
}