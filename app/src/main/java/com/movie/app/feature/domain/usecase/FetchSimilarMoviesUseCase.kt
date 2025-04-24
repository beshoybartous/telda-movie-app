package com.movie.app.feature.domain.usecase

import com.movie.app.feature.domain.model.MovieModel
import com.movie.app.feature.domain.repo.MovieRepository
import javax.inject.Inject

class FetchSimilarMoviesUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int): List<MovieModel> = repository.getSimilarMovies(movieId).take(5)
}