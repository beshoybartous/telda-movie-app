package com.movie.app

import androidx.paging.PagingData
import com.movie.app.feature.domain.model.MovieModel
import com.movie.app.feature.domain.repo.MovieRepository
import com.movie.app.feature.domain.usecase.FetchMoviesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchMoviesUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var fetchMoviesUseCase: FetchMoviesUseCase

    @Before
    fun setup() {
        repository = mockk()
        fetchMoviesUseCase = FetchMoviesUseCase(repository)
    }

    @Test
    fun `invoke calls getMovies`() = runTest {
        val pagingData = PagingData.empty<MovieModel>()
        coEvery { repository.getMovies() } returns flowOf(pagingData)

        val result = fetchMoviesUseCase().first()

        assertEquals(pagingData, result)
        coVerify { repository.getMovies() }
    }
}