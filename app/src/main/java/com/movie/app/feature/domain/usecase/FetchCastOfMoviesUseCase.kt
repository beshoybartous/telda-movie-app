package com.movie.app.feature.domain.usecase

import com.movie.app.feature.domain.model.ActorModel
import com.movie.app.feature.domain.model.DirectorModel
import com.movie.app.feature.domain.repo.MovieRepository
import javax.inject.Inject

class FetchCastOfMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieIds: List<Int>): Pair<List<ActorModel>, List<DirectorModel>> {
        val allCasts = repository.getAllCastsOfMovies(movieIds)

        // 1. Group Actors
        val actors = allCasts
            .filter { it.department == "Acting" }
            .sortedByDescending { it.popularity }
            .take(5)
            .map { person ->
                ActorModel(
                    id = person.id,
                    name = person.name,
                    profilePath = person.profilePath,
                    popularity = person.popularity
                )
            }

        // 2. Group Directors
        val directors = allCasts
            .filter { it.department == "Directing" }
            .sortedByDescending { it.popularity }
            .take(5)
            .map { person ->
                DirectorModel(
                    id = person.id,
                    name = person.name,
                    profilePath = person.profilePath,
                    popularity = person.popularity
                )
            }

        return Pair(actors, directors)
    }
}