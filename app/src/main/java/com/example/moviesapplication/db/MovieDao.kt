package com.example.moviesapplication.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.moviesapplication.data.FavoriteMovies
import com.example.moviesapplication.data.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addMovie(movie: List<Movie>)

  @Update
  suspend fun updateMovieToFavourite(movie: Movie)

  @Insert(onConflict = OnConflictStrategy.REPLACE , entity = FavoriteMovies::class)
  suspend fun addMovieToFavorite(movie: FavoriteMovies)

  @Query("DELETE FROM Movie")
  suspend fun deleteAllMovies()

  @Query("DELETE FROM FavoriteMovies WHERE id = :movieId")
  suspend fun deleteMovieFromFavorite(movieId: Int)

  @Query("SELECT * FROM Movie")
  fun getAllMoviesList(): Flow<List<Movie>>

  @Query("SELECT * FROM FavoriteMovies")
  fun fetchFavoriteMovies(): Flow<List<Movie>>
}