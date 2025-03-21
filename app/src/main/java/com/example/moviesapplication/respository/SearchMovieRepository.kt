package com.example.moviesapplication.respository

import com.example.moviesapplication.base.BaseRepository
import com.example.moviesapplication.data.FavoriteMovies
import com.example.moviesapplication.data.SearchMovieApiResponse
import com.example.moviesapplication.datasource.SearchMovieDataSource
import com.example.moviesapplication.db.MovieDatabase
import com.example.moviesapplication.network.Resource
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchMovieRepository @Inject constructor(
  private val searchMovieDataSource: SearchMovieDataSource,
  movieDb: MovieDatabase
) : BaseRepository() {

  private val movieDao = movieDb.movieDao()

  suspend fun fetchSearchedMovie(page: Int , movieName: String): Flow<Resource<out SearchMovieApiResponse>> {
    return flow {
      emit(Resource.Loading(null))
      val result = searchMovieDataSource.fetchSearchedMovie(page = page , movieName = movieName)
      emit(result)
    }.flowOn(Dispatchers.IO)
  }

  suspend fun addMovieToFavorites(movie: FavoriteMovies) {
    movieDao.addMovieToFavorite(movie)
  }
}