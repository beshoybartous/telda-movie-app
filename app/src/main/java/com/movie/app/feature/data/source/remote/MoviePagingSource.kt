package com.movie.app.feature.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.movie.app.core.database.MovieDao
import com.movie.app.feature.data.source.remote.api.MovieApi
import com.movie.app.feature.data.source.remote.dto.toMovie
import com.movie.app.feature.domain.model.MovieModel
import javax.inject.Inject

class MoviePagingSource @Inject constructor(
    private val api: MovieApi,
    private val dao: MovieDao,
    private val query: String? = null
) : PagingSource<Int, MovieModel>() {

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        return try {
            val page = params.key ?: 1
            val response = if (query.isNullOrEmpty())
                api.getMovies(page = page)
            else
                api.searchMovie(page = page, query = query)

            val movies = response.results.map { movieDto ->
                val entity = dao.getMovie(movieDto.id)
                movieDto.toMovie(entity?.isWatchListed == true)
            }

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < response.totalPages) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}