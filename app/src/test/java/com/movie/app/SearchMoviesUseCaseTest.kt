package com.movie.app

import androidx.paging.PagingData
import com.movie.app.feature.domain.model.MovieModel
import com.movie.app.feature.domain.repo.MovieRepository
import com.movie.app.feature.domain.usecase.SearchMoviesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchMoviesUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase

    @Before
    fun setup() {
        repository = mockk()
        searchMoviesUseCase = SearchMoviesUseCase(repository)
    }

    @Test
    fun `invoke calls searchMovie`() = runTest {
        val query = "Batman"
        val pagingData = PagingData.empty<MovieModel>()
        coEvery { repository.searchMovie(query) } returns flowOf(pagingData)

        val result = searchMoviesUseCase(query).first()

        assertEquals(pagingData, result)
        coVerify { repository.searchMovie(query) }
    }
}