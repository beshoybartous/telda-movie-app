package com.movie.app.feature.domain.usecase

import com.movie.app.feature.domain.model.MovieModel
import com.movie.app.feature.domain.repo.MovieRepository
import javax.inject.Inject

class ToggleWatchListUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(movie: MovieModel) {
        movieRepository.toggleWatchlist(movie)
    }
}