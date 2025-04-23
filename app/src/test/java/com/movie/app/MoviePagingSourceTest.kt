package com.movie.app;

import androidx.paging.PagingSource
import com.movie.app.core.database.MovieDao
import com.movie.app.core.database.entity.MovieEntity
import com.movie.app.feature.data.source.remote.MoviePagingSource
import com.movie.app.feature.data.source.remote.api.MovieApi
import com.movie.app.feature.data.source.remote.dto.MovieResponse
import com.movie.app.feature.data.source.remote.dto.MovieResponse.MovieDto
import com.movie.app.feature.data.source.remote.dto.toMovie
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

class MoviePagingSourceTest {

    private lateinit var api: MovieApi
    private lateinit var dao: MovieDao
    private lateinit var pagingSource: MoviePagingSource

    @Before
    fun setup() {
        api = mockk()
        dao = mockk()
        pagingSource = MoviePagingSource(api, dao, query = null)
    }

    @Test
    fun `load returns page when successful`() = runTest {
        val movieDtos = listOf(
            MovieDto(id = 1, title = "Movie 1", overview = "Desc 1", releaseDate = "2023", posterPath = "/path1"),
            MovieDto(id = 2, title = "Movie 2", overview = "Desc 2", releaseDate = "2024", posterPath = "/path2"),
        )

        val response = MovieResponse(
            page = 1,
            results = movieDtos,
            totalPages = 2,
            totalResults = 2
        )

        coEvery { api.getMovies(page = 1) } returns response
        coEvery { dao.getMovie(any()) } returns null

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        val expected = movieDtos.map { it.toMovie(false) }

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(expected, page.data)
        assertNull(page.prevKey)
        assertEquals(2, page.nextKey) // Because totalPages = 2
    }

    @Test
    fun `load maps watchlisted from database`() = runTest {
        val movieDto = MovieDto(id = 5, title = "Movie 5", overview = "Desc 5", releaseDate = "2025", posterPath = "/path5")

        val response = MovieResponse(
            page = 1,
            results = listOf(movieDto),
            totalPages = 1,
            totalResults = 1
        )

        coEvery { api.getMovies(page = 1) } returns response
        coEvery { dao.getMovie(5) } returns MovieEntity(id = 5, isWatchListed = true)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        val page = result as PagingSource.LoadResult.Page
        assertTrue(page.data[0].isWatchListed)
    }

    @Test
    fun `load returns error when exception thrown`() = runTest {
        coEvery { api.getMovies(page = 1) } throws IOException("Network error")
        coEvery { dao.getMovie(any()) } returns null

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
    }
}