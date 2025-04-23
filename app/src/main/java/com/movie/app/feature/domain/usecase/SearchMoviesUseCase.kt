package com.movie.app.feature.domain.usecase

import androidx.paging.PagingData
import com.movie.app.feature.domain.model.MovieModel
import com.movie.app.feature.domain.repo.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    operator fun invoke(query: String): Flow<PagingData<MovieModel>> =
        movieRepository.searchMovie(query)
}