package com.example.moviesapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FetchMoviesResponse(
  val page: Int,
  @SerializedName("results")
  val movie: List<Movie>,
  val total_pages: Int,
  val total_results: Int
)

@Entity(tableName = "Movie")
data class Movie(
  val adult: Boolean,
  val backdrop_path: String?,
  //Have to write constructor myself if I uncomment this as room doesn't pick Ignore Fields
  /*@Ignore
  val genre_ids: List<Int>,*/
  @PrimaryKey val id: Int,
  val original_language: String,
  val original_title: String,
  val overview: String,
  val popularity: Double,
  val poster_path: String,
  val release_date: String,
  val title: String,
  val video: Boolean,
  val vote_average: Double,
  val vote_count: Int
): Serializable

@Entity(tableName = "FavoriteMovies")
data class FavoriteMovies(
  val adult: Boolean,
  val backdrop_path: String?,
  @PrimaryKey val id: Int,
  val original_language: String,
  val original_title: String,
  val overview: String,
  val popularity: Double,
  val poster_path: String,
  val release_date: String,
  val title: String,
  val video: Boolean,
  val vote_average: Double,
  val vote_count: Int,
)