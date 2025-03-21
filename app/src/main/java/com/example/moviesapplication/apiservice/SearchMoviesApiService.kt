package com.example.moviesapplication.apiservice

import com.example.moviesapplication.data.SearchMovieApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchMoviesApiService {

  @GET("search/movie")
  suspend fun searchMoviesList(
    @Query("api_key") api_key: String = "e5ea3092880f4f3c25fbc537e9b37dc1",
    @Query("query") query: String,
    @Query("page") page: Int)
    : Response<SearchMovieApiResponse>
}