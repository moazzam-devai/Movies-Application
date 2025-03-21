package com.example.moviesapplication.apiservice

import com.example.moviesapplication.data.FetchMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FetchMoviesApiService {

  @GET("movie/popular")
  suspend fun fetchMoviesList(
    @Query("api_key") api_key: String = "e5ea3092880f4f3c25fbc537e9b37dc1",
    @Query("page") page: Int)
  : FetchMoviesResponse
}