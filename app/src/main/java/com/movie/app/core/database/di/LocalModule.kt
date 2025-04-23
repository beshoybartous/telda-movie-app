package com.movie.app.core.database.di

import android.content.Context
import androidx.room.Room
import com.movie.app.core.database.MovieDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): MovieDataBase =
        Room.databaseBuilder(context, MovieDataBase::class.java, "movies.db").build()
}