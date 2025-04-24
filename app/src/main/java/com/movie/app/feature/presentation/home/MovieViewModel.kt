package com.movie.app.feature.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.movie.app.feature.domain.model.MovieModel
import com.movie.app.feature.domain.usecase.FetchMoviesUseCase
import com.movie.app.feature.domain.usecase.SearchMoviesUseCase
import com.movie.app.feature.domain.usecase.ToggleWatchListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val fetchMoviesUseCase: FetchMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val toggleWatchListUseCase: ToggleWatchListUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _moviesState = MutableStateFlow<PagingData<MovieModel>>(PagingData.empty())
    val moviesState: StateFlow<PagingData<MovieModel>> = _moviesState

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val moviesFlow: Flow<PagingData<MovieModel>> = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isEmpty()) fetchMoviesUseCase()
            else searchMoviesUseCase(query)
        }
        .cachedIn(viewModelScope)

    init {
        collectMovies()
    }

    private fun collectMovies() {
        viewModelScope.launch {
            moviesFlow.collectLatest { pagingData ->
                _moviesState.value = pagingData
            }
        }
    }

    fun searchMovie(query: String) {
        _searchQuery.value = query
    }

    fun toggleWatchlist(movie: MovieModel) {
        viewModelScope.launch {
            toggleWatchListUseCase(movie)
            _moviesState.value = _moviesState.value.updateItem(
                predicate = { it.id == movie.id },
                update = { it.copy(isWatchListed = !it.isWatchListed) }
            )
        }
    }
}

inline fun <T : Any> PagingData<T>.updateItem(
    crossinline predicate: (T) -> Boolean,
    crossinline update: (T) -> T
): PagingData<T> {
    return this.map { item ->
        if (predicate(item))
            update(item)
        else
            item
    }
}