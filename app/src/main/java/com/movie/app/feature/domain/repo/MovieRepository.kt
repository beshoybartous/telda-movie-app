package com.movie.app.feature.domain.repo

import androidx.paging.PagingData
import com.movie.app.feature.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(): Flow<PagingData<MovieModel>>
    fun searchMovie(query: String): Flow<PagingData<MovieModel>>
    suspend fun toggleWatchlist(movie: MovieModel)
}