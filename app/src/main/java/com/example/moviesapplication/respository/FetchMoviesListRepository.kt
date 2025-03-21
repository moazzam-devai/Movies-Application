package com.example.moviesapplication.respository

import androidx.room.withTransaction
import com.example.moviesapplication.apiservice.FetchMoviesApiService
import com.example.moviesapplication.base.BaseRepository
import com.example.moviesapplication.data.FavoriteMovies
import com.example.moviesapplication.data.Movie
import com.example.moviesapplication.db.MovieDatabase
import com.example.moviesapplication.network.networkBoundResource
import javax.inject.Inject

class FetchMoviesListRepository @Inject constructor(
  private val fetchMoviesApiService: FetchMoviesApiService,
  private val movieDb: MovieDatabase
) : BaseRepository() {

  private val movieDao = movieDb.movieDao()

  fun getMovies(page: Int) = networkBoundResource(
    query = {
      movieDao.getAllMoviesList()
    },
    fetch = {
      fetchMoviesApiService.fetchMoviesList(page = page)
    },
    saveFetchResult = { movies ->
      movieDb.withTransaction {
        movieDao.deleteAllMovies()
        movieDao.addMovie(movies.movie)
      }
    },
  )

  suspend fun addMovieToFavorites(movie: FavoriteMovies) {
    movieDao.addMovieToFavorite(movie)
  }
}