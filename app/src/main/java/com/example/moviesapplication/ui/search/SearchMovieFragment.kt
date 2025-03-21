package com.example.moviesapplication.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.moviesapplication.R
import com.example.moviesapplication.base.BaseFragment
import com.example.moviesapplication.data.FavoriteMovies
import com.example.moviesapplication.data.Movie
import com.example.moviesapplication.databinding.FragmentSearchMovieBinding
import com.example.moviesapplication.datasource.SearchMovieDataSource
import com.example.moviesapplication.db.MovieDatabase
import com.example.moviesapplication.network.Resource
import com.example.moviesapplication.respository.SearchMovieRepository
import com.example.moviesapplication.ui.adapter.ClickListener
import com.example.moviesapplication.ui.adapter.MoviesListAdapter
import com.example.moviesapplication.ui.adapter.decoration.MoviesListItemDecoration
import com.example.moviesapplication.ui.moviedetail.MOVIE
import com.example.moviesapplication.util.hide
import com.example.moviesapplication.util.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchMovieFragment : BaseFragment<FragmentSearchMovieBinding, SearchMovieRepository>(),
  ClickListener {

  private val viewModel: SearchMovieViewModel by viewModels()

  @Inject
  lateinit var dataSource: SearchMovieDataSource

  @Inject
  lateinit var db: MovieDatabase
  private var totalPages: Int = 500
  private var page: Int = 2
  private lateinit var endlessScrollListener: RecyclerView.OnScrollListener
  private lateinit var moviesListAdapter: MoviesListAdapter
  private lateinit var gridLayoutManager: GridLayoutManager
  private var moviesList: ArrayList<Movie> = arrayListOf()
  private var movieName: String = ""
  private var isFromLoadMore = false

  override fun getFragmentBinding(
    inflater: LayoutInflater,
    container: ViewGroup?
  ): FragmentSearchMovieBinding {
    return FragmentSearchMovieBinding.inflate(inflater, container, false)
  }

  override fun onPostInit() {
    binding.etMovieName.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        isFromLoadMore = false
        page = 2
        movieName = s.toString()
        if (s.toString().isNotEmpty()) {
          viewModel.fetchSearchedMovie(page = 1, s.toString())
        } else {
          moviesList.clear()
          moviesListAdapter.notifyDataSetChanged()
        }
      }

      override fun afterTextChanged(s: Editable?) {
        if (s?.length == 0) {
          moviesList.clear()
          moviesListAdapter.notifyDataSetChanged()
        }
      }

    })

    setUpRecyclerView()
    setUpObservable()
  }

  override fun getRepository(): SearchMovieRepository {
    return SearchMovieRepository(dataSource, db)
  }

  private fun setUpRecyclerView() {
    moviesListAdapter = MoviesListAdapter(moviesList, this)
    binding.rvSearchedMovie.apply {
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
          if (page <= totalPages) {
            isFromLoadMore = true
            viewModel.fetchSearchedMovie(page++, movieName)
          } else {
            binding.rvSearchedMovie.removeOnScrollListener(endlessScrollListener)
          }

        }
      }
    }

    binding.rvSearchedMovie.addOnScrollListener(endlessScrollListener)

    (binding.rvSearchedMovie.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
  }

  private fun setUpObservable() {
    viewModel.searchMovieApiResponse.observe(viewLifecycleOwner, {

      if (it is Resource.Loading) {
        binding.progressBar.show()
      }

      if (it is Resource.Error) {
        binding.progressBar.hide()
        Toast.makeText(requireContext(), it.error?.message, Toast.LENGTH_SHORT).show()
      }

      if (it is Resource.Success) {
        binding.progressBar.hide()
        it.data?.movies?.let { it1 ->
          if (!isFromLoadMore) {
            if (it1.isEmpty()) {
              binding.tvNoMovieFound.show()
            } else {
              binding.tvNoMovieFound.hide()
            }
            moviesList.clear()
            moviesList.addAll(it1)
          } else {
            moviesList.addAll(it1)
          }
          moviesListAdapter.notifyDataSetChanged()
        }
        it.data?.total_pages?.let { pages ->
          totalPages = pages
        }
      }
    })
  }

  private val lastVisibleItemPosition: Int
    get() = gridLayoutManager.findLastVisibleItemPosition()

  override fun markMovieAsFavorite(movie: Movie) {
    val favoriteMovie = FavoriteMovies(
      movie.adult, movie.backdrop_path, movie.id, movie.original_language,
      movie.original_title, movie.overview, movie.popularity, movie.poster_path,
      movie.release_date, movie.title, movie.video, movie.vote_average, movie.vote_count
    )
    viewModel.addMovieToFavorites(favoriteMovie)
    Toast.makeText(requireContext(), "Movie Added to Favourites", Toast.LENGTH_SHORT).show()
  }

  override fun goToMovieDetailScreen(movie: Movie) {
    val bundle = Bundle().apply {
      putSerializable(MOVIE , movie)
    }
    findNavController().navigate(R.id.action_searchMovieFragment_to_movieDetailFragment , bundle)
  }
}