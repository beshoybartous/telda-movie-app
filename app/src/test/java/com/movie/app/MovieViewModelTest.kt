package com.movie.app

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.movie.app.feature.domain.model.MovieModel
import com.movie.app.feature.domain.usecase.FetchMoviesUseCase
import com.movie.app.feature.domain.usecase.SearchMoviesUseCase
import com.movie.app.feature.domain.usecase.ToggleWatchListUseCase
import com.movie.app.feature.presentation.home.MovieViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var fetchMoviesUseCase: FetchMoviesUseCase
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase
    private lateinit var toggleWatchListUseCase: ToggleWatchListUseCase
    private lateinit var viewModel: MovieViewModel

    private val fakeMovies = listOf(
        MovieModel(1, "Batman", "", "", "", false),
        MovieModel(2, "Superman", "", "", "", false)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        fetchMoviesUseCase = mockk()
        searchMoviesUseCase = mockk()
        toggleWatchListUseCase = mockk()

        coEvery { fetchMoviesUseCase.invoke() } returns flowOf(PagingData.from(fakeMovies))
        coEvery { searchMoviesUseCase.invoke(any()) } returns flowOf(PagingData.from(fakeMovies))

        viewModel = MovieViewModel(fetchMoviesUseCase, searchMoviesUseCase, toggleWatchListUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchMovie updates searchQuery`() = runTest {
        viewModel.searchMovie("spiderman")
        assertEquals("spiderman", viewModel.searchQuery.value)
    }

    @Test
    fun `moviesState emits data from fetchMoviesUseCase when query is empty`() = runTest {
        viewModel.searchMovie("") // trigger fetchMovies flow
        advanceUntilIdle()

        val testScope=this
        viewModel.moviesState.test {
            val emitted = awaitItem()
            val list = collectPagingData(emitted, MovieModelDiffCallback(),testScope)
            assertEquals(2, list.size)
            assertEquals("Batman", list[0].title)
        }
    }

    @Test
    fun `moviesState emits data from searchMoviesUseCase when query is set`() = runTest {
        viewModel.searchMovie("batman")
        advanceUntilIdle()

        val testScope=this
        viewModel.moviesState.test {
            val emitted = awaitItem()
            val list = collectPagingData(emitted, MovieModelDiffCallback(),testScope)
            assertEquals(2, list.size)
            assertEquals("Batman", list[0].title)
        }
    }


    // Helper to extract list from PagingData
    private suspend fun <T : Any> collectPagingData(
        pagingData: PagingData<T>,
        diffCallback: DiffUtil.ItemCallback<T>,
        scope: TestScope
    ): List<T> {
        val differ = AsyncPagingDataDiffer(
            diffCallback = diffCallback,
            updateCallback = NoopListCallback,
            mainDispatcher = Dispatchers.Unconfined,
            workerDispatcher = Dispatchers.Unconfined
        )
        differ.submitData(pagingData)
        scope.advanceUntilIdle()
        return List(differ.itemCount) { differ.getItem(it)!! }
    }

    private object NoopListCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    class MovieModelDiffCallback : DiffUtil.ItemCallback<MovieModel>() {
        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
            oldItem == newItem
    }
}