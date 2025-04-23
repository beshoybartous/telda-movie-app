package com.movie.app

import androidx.room.Database
import androidx.room.RoomDatabase
import com.movie.app.core.database.MovieDao
import com.movie.app.core.database.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class TestMovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}