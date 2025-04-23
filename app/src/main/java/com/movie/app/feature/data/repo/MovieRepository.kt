package com.movie.app.feature.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.movie.app.core.database.MovieDao
import com.movie.app.core.database.MovieDataBase
import com.movie.app.core.database.entity.MovieEntity
import com.movie.app.feature.data.source.remote.MoviePagingSource
import com.movie.app.feature.data.source.remote.api.MovieApi
import com.movie.app.feature.domain.model.MovieModel
import com.movie.app.feature.domain.repo.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi, private val dao: MovieDao
) : MovieRepository {

    override fun getMovies(): Flow<PagingData<MovieModel>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            initialLoadSize = 20
        ),
        pagingSourceFactory = {
            MoviePagingSource(api, dao)
        }
    ).flow

    override fun searchMovie(query: String): Flow<PagingData<MovieModel>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            initialLoadSize = 20
        ),
        pagingSourceFactory = {
            MoviePagingSource(api, dao, query)
        }
    ).flow

    override suspend fun toggleWatchlist(movie: MovieModel) {
        val entity = dao.getMovie(movie.id)
        if (entity != null) {
            if (movie.isWatchListed) {
                dao.deleteMovie(entity)
            } else {
                entity.copy(isWatchListed = true)
                dao.updateMovie(entity)
            }
        } else {
            if (!movie.isWatchListed) {
                val newEntity = MovieEntity(
                    id = movie.id,
                    isWatchListed = true
                )
                dao.insertMovie(newEntity)
            }
        }
    }
}

