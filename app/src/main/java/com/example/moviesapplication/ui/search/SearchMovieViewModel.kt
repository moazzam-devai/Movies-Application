package com.example.moviesapplication.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapplication.data.FavoriteMovies
import com.example.moviesapplication.data.SearchMovieApiResponse
import com.example.moviesapplication.network.Resource
import com.example.moviesapplication.respository.SearchMovieRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchMovieViewModel @ViewModelInject constructor(
  private val repository: SearchMovieRepository
) : ViewModel() {

  private val _searchMovieApiResponse = MutableLiveData<Resource<out SearchMovieApiResponse>>()
  val searchMovieApiResponse = _searchMovieApiResponse

  fun fetchSearchedMovie(page: Int , movieName: String) {
    viewModelScope.launch {
      repository.fetchSearchedMovie(page , movieName).collect {
        _searchMovieApiResponse.value = it
      }
    }
  }

  fun addMovieToFavorites(movie: FavoriteMovies) {
    viewModelScope.launch {
      repository.addMovieToFavorites(movie)
    }
  }
}