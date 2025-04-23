package com.movie.app

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.movie.app.core.database.MovieDao
import com.movie.app.core.database.entity.MovieEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class MovieDaoTest {

    private lateinit var db: TestMovieDatabase
    private lateinit var dao: MovieDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TestMovieDatabase::class.java
        )
            .allowMainThreadQueries() // OK for test
            .build()
        dao = db.movieDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insert_and_get_movie() = runTest {
        val movie = MovieEntity(id = 101, isWatchListed = true)
        dao.insertMovie(movie)

        val result = dao.getMovie(101)
        assertNotNull(result)
        assertEquals(result, movie)
        assertTrue(result?.isWatchListed == true)
    }

    @Test
    fun update_movie_watchlist() = runTest {
        val movie = MovieEntity(id = 202, isWatchListed = false)
        dao.insertMovie(movie)

        dao.updateMovie(movie.copy(isWatchListed = true))
        val updated = dao.getMovie(202)
        assertTrue(updated?.isWatchListed == true)
    }

    @Test
    fun delete_movie() = runTest {
        val movie = MovieEntity(id = 303, isWatchListed = true)
        dao.insertMovie(movie)
        dao.deleteMovie(movie)

        val result = dao.getMovie(303)
        assertNull(result)
    }
}