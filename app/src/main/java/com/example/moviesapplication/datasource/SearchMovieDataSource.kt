package com.example.moviesapplication.datasource

import com.example.moviesapplication.apiservice.SearchMoviesApiService
import com.example.moviesapplication.base.BaseResponse
import javax.inject.Inject

class SearchMovieDataSource @Inject constructor
  (private val searchMoviesApiService: SearchMoviesApiService): BaseResponse() {

  suspend fun fetchSearchedMovie(page: Int , movieName: String) = getResult {
    searchMoviesApiService.searchMoviesList(page = page , query = movieName)
  }
}