package com.movie.app.feature.presentation.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.movie.app.feature.domain.model.MovieDetailModel
import com.movie.app.feature.domain.model.MovieModel
import com.movie.app.feature.presentation.details.state.UiState

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            when (val state = uiState.movieDetailState) {
                is UiState.Loading -> CenterLoading()
                is UiState.Error -> ErrorSection(message = state.message)
                is UiState.Success -> MovieDetailSection(state.data)
                UiState.Idle -> {}
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            when (val state = uiState.similarMoviesState) {
                is UiState.Loading -> CenterLoading()
                is UiState.Error -> ErrorSection(message = state.message)
                is UiState.Success -> SimilarMoviesSection(state.data)
                UiState.Idle -> {}
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Top 5 Actors", style = MaterialTheme.typography.titleLarge)
            when (val actorsState = uiState.castsState) {
                is UiState.Loading -> CenterLoading()
                is UiState.Error -> ErrorSection(message = actorsState.message)
                is UiState.Success -> CastsListSection(actorsState.data.actorsState.map { it.name })
                UiState.Idle -> {}
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Top 5 Directors", style = MaterialTheme.typography.titleLarge)
            when (val directorsState = uiState.castsState) {
                is UiState.Loading -> CenterLoading()
                is UiState.Error -> ErrorSection(message = directorsState.message)
                is UiState.Success -> CastsListSection(directorsState.data.directorsState.map { it.name })
                UiState.Idle -> {}
            }
        }
    }
}

@Composable
fun CenterLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorSection(message: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: ${message ?: "Unknown Error"}",
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun MovieDetailSection(movie: MovieDetailModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        movie.posterPath?.let {
            Image(
                painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500$it"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
        movie.tagline?.let { Text(text = it, style = MaterialTheme.typography.titleSmall) }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = movie.overview, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Revenue: \$${movie.revenue ?: 0}")
        Text(text = "Release Date: ${movie.releaseDate ?: "Unknown"}")
        Text(text = "Status: ${movie.status ?: "Unknown"}")
    }
}

@Composable
fun SimilarMoviesSection(movies: List<MovieModel>) {
    Column {
        Text(text = "Similar Movies", style = MaterialTheme.typography.titleLarge)
        LazyRow {
            items(movies) { movie ->
                Column(
                    modifier = Modifier
                        .width(150.dp)
                        .padding(8.dp)
                ) {
                    movie.posterPath?.let {
                        Image(
                            painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500$it"),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun CastsListSection(names: List<String>) {
    LazyRow {
        items(names) { name ->
            Text(
                text = name,
                modifier = Modifier
                    .padding(8.dp)
                    .widthIn(min = 100.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}