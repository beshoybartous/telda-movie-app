package com.movie.app

import com.movie.app.feature.domain.model.MovieModel
import com.movie.app.feature.domain.repo.MovieRepository
import com.movie.app.feature.domain.usecase.ToggleWatchListUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ToggleWatchListUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var toggleWatchListUseCase: ToggleWatchListUseCase

    @Before
    fun setup() {
        repository = mockk()
        toggleWatchListUseCase = ToggleWatchListUseCase(repository)
    }

    @Test
    fun `invoke calls toggleWatchlist`() = runTest {
        val movie = MovieModel(id = 1, title = "Batman", overview = "", releaseDate = "", posterPath = "", isWatchListed = false)
        coEvery { repository.toggleWatchlist(movie) } just Runs

        toggleWatchListUseCase(movie)

        coVerify { repository.toggleWatchlist(movie) }
    }
}