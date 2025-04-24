package com.movie.app.feature.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.movie.app.core.database.MovieDao
import com.movie.app.core.database.entity.MovieEntity
import com.movie.app.feature.data.source.remote.MoviePagingSource
import com.movie.app.feature.data.source.remote.api.MovieApi
import com.movie.app.feature.data.source.remote.dto.toDomain
import com.movie.app.feature.data.source.remote.dto.toMovie
import com.movie.app.feature.domain.model.CreditsPersonModel
import com.movie.app.feature.domain.model.MovieDetailModel
import com.movie.app.feature.domain.model.MovieModel
import com.movie.app.feature.domain.repo.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val dao: MovieDao
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

    override suspend fun getMovieDetails(movieId: Int): MovieDetailModel =
        api.getMovieDetails(movieId).toDomain()

    override suspend fun getSimilarMovies(movieId: Int): List<MovieModel> =
        api.getSimilarMovies(movieId).results.map { it.toMovie() }

    override suspend fun getAllCastsOfMovies(movieIds: List<Int>): List<CreditsPersonModel> {
        val allCredits = mutableListOf<CreditsPersonModel>()

        for (id in movieIds) {
            val response = api.getMovieCredits(id)

            // Map Casts
            val castList = response.cast.map { castDto ->
                castDto
                CreditsPersonModel(
                    id = castDto.id,
                    name = castDto.name,
                    profilePath = castDto.profilePath,
                    popularity = castDto.popularity,
                    department = castDto.knownForDepartment // Important here
                )
            }

            // Map Crews
            val crewList = response.crew.map { crewDto ->
                CreditsPersonModel(
                    id = crewDto.id,
                    name = crewDto.name,
                    profilePath = crewDto.profilePath,
                    popularity = crewDto.popularity,
                    department = crewDto.department
                )
            }

            allCredits.addAll(castList)
            allCredits.addAll(crewList)
        }

        return allCredits
    }
}

