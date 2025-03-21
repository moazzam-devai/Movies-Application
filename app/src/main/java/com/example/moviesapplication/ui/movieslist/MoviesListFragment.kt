package com.example.moviesapplication.ui.movieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.moviesapplication.R
import com.example.moviesapplication.data.FavoriteMovies
import com.example.moviesapplication.data.Movie
import com.example.moviesapplication.databinding.FragmentMoviesListBinding
import com.example.moviesapplication.network.Resource
import com.example.moviesapplication.ui.adapter.ClickListener
import com.example.moviesapplication.ui.adapter.MoviesListAdapter
import com.example.moviesapplication.ui.adapter.decoration.MoviesListItemDecoration
import com.example.moviesapplication.ui.moviedetail.MOVIE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesListFragment : Fragment() , ClickListener {

  private lateinit var binding: FragmentMoviesListBinding
  private val viewModel: MoviesListViewModel by viewModels()
  private var totalPages: Int = 500
  private var page: Int = 2
  private lateinit var endlessScrollListener: RecyclerView.OnScrollListener
  private lateinit var moviesListAdapter: MoviesListAdapter
  private lateinit var gridLayoutManager: GridLayoutManager
  private var moviesList: ArrayList<Movie> = arrayListOf()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentMoviesListBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.fetchMoviesList(page = 1)
    setUpClickListener()
    setUpObserver()
    setUpRecyclerView()
  }

  private fun setUpRecyclerView() {
    moviesListAdapter = MoviesListAdapter(moviesList , this)
    binding.rvMoviesList.apply {
      gridLayoutManager = GridLayoutManager(requireActivity(), 2, RecyclerView.VERTICAL, false)
      layoutManager = gridLayoutManager
      adapter = moviesListAdapter
      this.addItemDecoration(MoviesListItemDecoration(16))
    }

    endlessScrollListener = object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val totalItemCount = recyclerView.layoutManager?.itemCount
        if (totalItemCount == lastVisibleItemPosition + 1) {
          if(page <= totalPages) {
            viewModel.fetchMoviesList(page++)
          } else{
            binding.rvMoviesList.removeOnScrollListener(endlessScrollListener)
          }

        }
      }
    }

    binding.rvMoviesList.addOnScrollListener(endlessScrollListener)

    (binding.rvMoviesList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
  }

  private fun setUpClickListener() {
    binding.btnGoToFavoritesFragment.setOnClickListener {
      findNavController().navigate(R.id.action_moviesListFragment_to_favoriteMoviesFragment)
    }

    binding.btnSearchFragment.setOnClickListener {
      findNavController().navigate(R.id.action_moviesListFragment_to_searchMovieFragment)
    }
  }

  private fun setUpObserver() {
    viewModel.moviesList.observe(viewLifecycleOwner, { result ->
      result.data?.let {
        moviesList.addAll(it)
        moviesListAdapter.notifyDataSetChanged()
      }

      binding.progressBar.isVisible = result is Resource.Loading

    })
  }

  private val lastVisibleItemPosition: Int
    get() = gridLayoutManager.findLastVisibleItemPosition()

  override fun markMovieAsFavorite(movie: Movie) {
    val favoriteMovie = FavoriteMovies(movie.adult, movie.backdrop_path, movie.id, movie.original_language,
    movie.original_title, movie.overview, movie.popularity, movie.poster_path,
    movie.release_date, movie.title, movie.video, movie.vote_average, movie.vote_count)
    viewModel.addMovieToFavorites(favoriteMovie)
    Toast.makeText(requireContext(), "Movie Added to Favourites", Toast.LENGTH_SHORT).show()
  }

  override fun goToMovieDetailScreen(movie: Movie) {
    val bundle = Bundle().apply {
      putSerializable(MOVIE , movie)
    }
    findNavController().navigate(R.id.action_moviesListFragment_to_movieDetailFragment , bundle)
  }
}