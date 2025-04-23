package com.movie.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.movie.app.core.database.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MovieDataBase : RoomDatabase() {
    abstract fun moviesDao(): MovieDao
}
