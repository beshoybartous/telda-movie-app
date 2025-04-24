package com.movie.app.feature.presentation.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.movie.app.R
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
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            when (val state = uiState.movieDetailState) {
                is UiState.Loading -> CenterLoading()
                is UiState.Error -> ErrorSection(state.message)
                is UiState.Success -> MovieDetailSection(state.data)
                UiState.Idle -> {}
            }
        }

        item {
            Text(
                text = "Similar Movies",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            when (val state = uiState.similarMoviesState) {
                is UiState.Loading -> CenterLoading()
                is UiState.Error -> ErrorSection(state.message)
                is UiState.Success -> SimilarMoviesSection(state.data)
                UiState.Idle -> {}
            }
        }

        item {
            Text(
                text = "Top 5 Actors",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            when (val actorsState = uiState.castsState) {
                is UiState.Loading -> CenterLoading()
                is UiState.Error -> ErrorSection(actorsState.message)
                is UiState.Success -> CastsListSection(actorsState.data.actorsState.map {
                    PersonUiModel(it.name, it.profilePath)
                })
                UiState.Idle -> {}
            }
        }

        item {
            Text(
                text = "Top 5 Directors",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            when (val directorsState = uiState.castsState) {
                is UiState.Loading -> CenterLoading()
                is UiState.Error -> ErrorSection(directorsState.message)
                is UiState.Success -> CastsListSection(directorsState.data.directorsState.map {
                    PersonUiModel(it.name, it.profilePath)
                })
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
            .height(150.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorSection(message: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onErrorContainer
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Error: ${message ?: "Unknown Error"}",
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun MovieDetailSection(movie: MovieDetailModel) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            movie.posterPath?.let {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = "https://image.tmdb.org/t/p/w500$it",
                        error = painterResource(id = R.drawable.ic_broken_image),
                        placeholder = painterResource(id = R.drawable.ic_placeholder)
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium
            )
            movie.tagline?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = movie.overview, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Revenue: \$${movie.revenue ?: 0}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Release Date: ${movie.releaseDate ?: "Unknown"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Status: ${movie.status ?: "Unknown"}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SimilarMoviesSection(movies: List<MovieModel>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) { movie ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.width(150.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    movie.posterPath?.let {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = "https://image.tmdb.org/t/p/w500$it",
                                error = painterResource(id = R.drawable.ic_broken_image),
                                placeholder = painterResource(id = R.drawable.ic_placeholder)
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

data class PersonUiModel(
    val name: String,
    val profilePath: String?
)

@Composable
fun CastsListSection(persons: List<PersonUiModel>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(persons) { person ->
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.width(120.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    if (person.profilePath != null) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = "https://image.tmdb.org/t/p/w500${person.profilePath}",
                                error = painterResource(id = R.drawable.ic_broken_image),
                                placeholder = painterResource(id = R.drawable.ic_placeholder)
                            ),
                            contentDescription = person.name,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = person.name,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}