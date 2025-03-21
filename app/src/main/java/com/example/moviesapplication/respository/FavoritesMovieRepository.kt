package com.example.moviesapplication.respository

import androidx.lifecycle.MutableLiveData
import com.example.moviesapplication.base.BaseRepository
import com.example.moviesapplication.data.Movie
import com.example.moviesapplication.db.MovieDatabase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FavoritesMovieRepository @Inject constructor(
  movieDb: MovieDatabase): BaseRepository() {

  private val movieDao = movieDb.movieDao()

  private val _favoriteMovieList = MutableLiveData<List<Movie>>()
  val favoriteMovieList = _favoriteMovieList

  fun fetchFavoriteMovies() : Flow<List<Movie>> {
    return movieDao.fetchFavoriteMovies()
  }

  suspend fun removeMovieFromFavorite(movieId: Int) {
   return movieDao.deleteMovieFromFavorite(movieId)
  }
}