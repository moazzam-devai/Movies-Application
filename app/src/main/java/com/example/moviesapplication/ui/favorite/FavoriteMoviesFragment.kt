package com.example.moviesapplication.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapplication.R
import com.example.moviesapplication.base.BaseFragment
import com.example.moviesapplication.data.Movie
import com.example.moviesapplication.databinding.FragmentFavoriteMoviesBinding
import com.example.moviesapplication.db.MovieDatabase
import com.example.moviesapplication.respository.FavoritesMovieRepository
import com.example.moviesapplication.ui.adapter.ClickListener
import com.example.moviesapplication.ui.adapter.MoviesListAdapter
import com.example.moviesapplication.ui.adapter.decoration.MoviesListItemDecoration
import com.example.moviesapplication.ui.moviedetail.MOVIE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteMoviesFragment : BaseFragment<FragmentFavoriteMoviesBinding , FavoritesMovieRepository>() , ClickListener {

  private val viewModel: FavoriteMoviesViewModel by viewModels()
  private lateinit var moviesListAdapter: MoviesListAdapter
  private var moviesList: ArrayList<Movie> = arrayListOf()

  @Inject
  lateinit var db: MovieDatabase

  override fun getFragmentBinding(
    inflater: LayoutInflater,
    container: ViewGroup?
  ): FragmentFavoriteMoviesBinding {
    return FragmentFavoriteMoviesBinding.inflate(inflater, container, false)
  }

  override fun onPostInit() {
    viewModel.fetchFavoriteMovies()
    setUpRecyclerView()
    setUpObservable()
  }

  override fun getRepository(): FavoritesMovieRepository {
    return FavoritesMovieRepository(db)
  }

  private fun setUpRecyclerView() {
    moviesListAdapter = MoviesListAdapter(moviesList , this)
    binding.rvFavoriteMovies.apply {
      layoutManager = GridLayoutManager(requireActivity(), 2, RecyclerView.VERTICAL, false)
      adapter = moviesListAdapter
      this.addItemDecoration(MoviesListItemDecoration(16))
    }
  }

  private fun setUpObservable() {
    viewModel.favoriteMovieList.observe(viewLifecycleOwner , {
      moviesList.clear()
      moviesList.addAll(it)
      moviesListAdapter.notifyDataSetChanged()
    })
  }

  override fun markMovieAsFavorite(movie: Movie) {
    viewModel.removeFavoriteMovie(movie.id)
    Toast.makeText(requireContext(), "Movie ${movie.title} removed from favorite Movies", Toast.LENGTH_SHORT).show()
  }

  override fun goToMovieDetailScreen(movie: Movie) {
    val bundle = Bundle().apply {
      putSerializable(MOVIE , movie)
    }
    findNavController().navigate(R.id.action_favoriteMoviesFragment_to_movieDetailFragment , bundle)
  }
}