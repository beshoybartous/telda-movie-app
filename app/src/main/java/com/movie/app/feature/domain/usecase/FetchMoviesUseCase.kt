package com.movie.app.feature.domain.usecase

import androidx.paging.PagingData
import com.movie.app.feature.domain.model.MovieModel
import com.movie.app.feature.domain.repo.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    operator fun invoke(): Flow<PagingData<MovieModel>> = movieRepository.getMovies()
}