package com.movie.app.feature.di

import com.movie.app.core.database.MovieDao
import com.movie.app.core.database.MovieDataBase
import com.movie.app.feature.data.repo.MovieRepositoryImpl
import com.movie.app.feature.data.source.remote.api.MovieApi
import com.movie.app.feature.domain.repo.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedModule {

    @Provides
    @Singleton
    fun provideMoviesApi(retrofit: Retrofit): MovieApi = retrofit.create(MovieApi::class.java)

    @Provides
    @Singleton
    fun provideMovieRepository(
        api: MovieApi, dao: MovieDao
    ): MovieRepository {
        return MovieRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun provideMovieDao(db: MovieDataBase): MovieDao = db.moviesDao()

}