package com.example.moviesapplication.data

import com.google.gson.annotations.SerializedName

class SearchMovieApiResponse(
    val page: Int,
    @SerializedName("results")
    val movies: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)