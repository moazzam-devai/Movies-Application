package com.example.moviesapplication.ui.favorite

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapplication.data.Movie
import com.example.moviesapplication.respository.FavoritesMovieRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoriteMoviesViewModel @ViewModelInject constructor(
  private val repository: FavoritesMovieRepository
) : ViewModel() {

  private val _favoriteMovieList = MutableLiveData<List<Movie>>()
  val favoriteMovieList = _favoriteMovieList

  fun fetchFavoriteMovies() {
    viewModelScope.launch {
      repository.fetchFavoriteMovies().collect {
        _favoriteMovieList.value = it
      }
    }
  }

  fun removeFavoriteMovie(movieId: Int) {
    viewModelScope.launch {
      repository.removeMovieFromFavorite(movieId)
    }
  }
}