package com.example.moviesapplication.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviesapplication.respository.FavoritesMovieRepository
import com.example.moviesapplication.respository.FetchMoviesListRepository
import com.example.moviesapplication.respository.SearchMovieRepository
import com.example.moviesapplication.ui.favorite.FavoriteMoviesViewModel
import com.example.moviesapplication.ui.movieslist.MoviesListViewModel
import com.example.moviesapplication.ui.search.SearchMovieViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val repository: BaseRepository, private val paramters: Any ?= null) :
  ViewModelProvider.Factory {
  // add your view models here
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return when {
      //  example below
      modelClass.isAssignableFrom(MoviesListViewModel::class.java) -> MoviesListViewModel(repository as FetchMoviesListRepository) as T
      modelClass.isAssignableFrom(FavoriteMoviesViewModel::class.java) -> FavoriteMoviesViewModel(repository as FavoritesMovieRepository) as T
      modelClass.isAssignableFrom(SearchMovieViewModel::class.java) -> SearchMovieViewModel(repository as SearchMovieRepository) as T
      else -> throw IllegalAccessException("Unknown View Model.Please add your view model in factory")
    }
  }
}