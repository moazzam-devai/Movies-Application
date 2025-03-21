package com.example.moviesapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesapplication.data.FavoriteMovies
import com.example.moviesapplication.data.Movie

@Database(entities = [Movie::class , FavoriteMovies::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

  abstract fun movieDao(): MovieDao
}