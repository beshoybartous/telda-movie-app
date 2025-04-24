package com.movie.app.feature.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.rememberImagePainter
import com.movie.app.feature.domain.model.MovieModel

@Composable
fun MovieScreen(
    viewModel: MovieViewModel = hiltViewModel(),
    modifier: Modifier,
    onItemClick: (movieId: Int) -> Unit
) {
    val movies = viewModel.moviesState.collectAsLazyPagingItems()
    val searchQuery = viewModel.searchQuery.collectAsStateWithLifecycle().value

    Column(modifier = modifier) {
        TextField(
            value = searchQuery,
            onValueChange = { query ->
                viewModel.searchMovie(query)
            },
            label = { Text("Search movies") }
        )


        LazyColumn {
            items(count = movies.itemCount, key = movies.itemKey { it.id }) { index ->
                val movie = movies[index]
                if (movie != null)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(movie.id) }
                            .padding(16.dp)
                    ) {
                        MovieItem(movie) {
                            viewModel.toggleWatchlist(movie)
                        }
                    }

            }
        }
    }
}

@Composable
fun MovieItem(movie: MovieModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${movie.posterPath}"),
            contentDescription = movie.title,
            modifier = Modifier
                .width(100.dp)
                .height(150.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Text(
                text = movie.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = movie.overview,
                fontSize = 14.sp
            )

            Text(
                text = movie.releaseDate,
                fontSize = 14.sp
            )

            Button(onClick = onClick) {
                Text(text = if (movie.isWatchListed) "Remove from watchlist" else "Add to watchlist")
            }
        }
    }
}