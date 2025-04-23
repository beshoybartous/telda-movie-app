package com.movie.app

import androidx.paging.testing.asSnapshot
import com.movie.app.core.database.MovieDao
import com.movie.app.core.database.entity.MovieEntity
import com.movie.app.feature.data.repo.MovieRepositoryImpl
import com.movie.app.feature.data.source.remote.api.MovieApi
import com.movie.app.feature.data.source.remote.dto.MovieResponse
import com.movie.app.feature.data.source.remote.dto.MovieResponse.MovieDto
import com.movie.app.feature.domain.model.MovieModel
import com.movie.app.feature.domain.repo.MovieRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MovieRepositoryImplTest {

    private lateinit var repository: MovieRepository
    private lateinit var api: MovieApi
    private lateinit var dao: MovieDao

    @Before
    fun setup() {
        api = mockk()
        dao = mockk()
        repository = MovieRepositoryImpl(api, dao)
    }
    @Test
    fun `getMovies returns PagingData`() = runTest {
        val movieDtos = listOf(
            MovieDto(id = 1, title = "Movie 1", overview = "", releaseDate = "2023", posterPath = "/img1"),
            MovieDto(id = 2, title = "Movie 2", overview = "", releaseDate = "2023", posterPath = "/img2")
        )

        val response = MovieResponse(
            page = 1,
            results = movieDtos,
            totalPages = 1,
            totalResults = 2
        )

        coEvery { api.getMovies(page = any()) } returns response
        coEvery { dao.getMovie(any()) } returns null

        val snapshot = repository.getMovies().asSnapshot()

        assertEquals(2, snapshot.size)
        assertEquals("Movie 1", snapshot[0].title)
        assertEquals("Movie 2", snapshot[1].title)
    }


    @Test
    fun `searchMovie returns PagingData`() = runTest {
        val movieDtos = listOf(
            MovieDto(id = 3, title = "Batman", overview = "", releaseDate = "2024", posterPath = "/batman")
        )

        val response = MovieResponse(
            page = 1,
            results = movieDtos,
            totalPages = 1,
            totalResults = 1
        )

        coEvery { api.searchMovie(page = any(), query = any()) } returns response
        coEvery { dao.getMovie(any()) } returns null

        val snapshot = repository.searchMovie("batman").asSnapshot()

        assertEquals(1, snapshot.size)
        assertEquals("Batman", snapshot[0].title)
    }

    @Test
    fun `toggleWatchlist inserts when movie does not exist`() = runTest {
        val movieModel = MovieModel(
            id = 1,
            title = "Test",
            overview = "",
            releaseDate = "",
            posterPath = "",
            isWatchListed = false
        )

        coEvery { dao.getMovie(1) } returns null
        coEvery { dao.insertMovie(any()) } just Runs

        repository.toggleWatchlist(movieModel)

        coVerify { dao.insertMovie(match { it.id == 1 && it.isWatchListed }) }
    }

    @Test
    fun `toggleWatchlist updates existing movie to watchlisted`() = runTest {
        val movieModel = MovieModel(
            id = 2,
            title = "Test",
            overview = "",
            releaseDate = "",
            posterPath = "",
            isWatchListed = false
        )
        val entity = MovieEntity(id = 2, isWatchListed = false)

        coEvery { dao.getMovie(2) } returns entity
        coEvery { dao.updateMovie(any()) } just Runs

        repository.toggleWatchlist(movieModel)

        coVerify { dao.updateMovie(entity) }
    }

    @Test
    fun `toggleWatchlist deletes watchlisted movie`() = runTest {
        val movieModel = MovieModel(
            id = 3,
            title = "Test",
            overview = "",
            releaseDate = "",
            posterPath = "",
            isWatchListed = true
        )
        val entity = MovieEntity(id = 3, isWatchListed = true)

        coEvery { dao.getMovie(3) } returns entity
        coEvery { dao.deleteMovie(any()) } just Runs

        repository.toggleWatchlist(movieModel)

        coVerify { dao.deleteMovie(entity) }
    }
}