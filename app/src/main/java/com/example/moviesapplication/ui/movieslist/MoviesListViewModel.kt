package com.example.moviesapplication.ui.movieslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapplication.data.FavoriteMovies
import com.example.moviesapplication.data.Movie
import com.example.moviesapplication.network.Resource
import com.example.moviesapplication.respository.FetchMoviesListRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MoviesListViewModel @ViewModelInject constructor(private val repository: FetchMoviesListRepository) : ViewModel() {

  private val _moviesList =  MutableLiveData<Resource<List<Movie>>>()
  val moviesList = _moviesList


  fun fetchMoviesList(page: Int) {
    viewModelScope.launch {
      repository.getMovies(page).collect {
        _moviesList.value = it
      }
    }
  }

  fun addMovieToFavorites(movie: FavoriteMovies) {
    viewModelScope.launch {
      repository.addMovieToFavorites(movie)
    }
  }
}