package com.example.moviesapplication.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.moviesapplication.data.Movie
import com.example.moviesapplication.databinding.FragmentMovieDetailBinding

const val MOVIE = "movie"

class MovieDetailFragment : Fragment() {

  private lateinit var binding: FragmentMovieDetailBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val movie = arguments?.getSerializable(MOVIE) as Movie

    Glide.with(requireContext()).load("https://image.tmdb.org/t/p/w92/${movie.poster_path}")
      .into(binding.ivMoviePoster)
    binding.tvMovieName.text = movie.title
    binding.tvMovieReleaseDate.text = movie.release_date
    binding.tvMovieOverView.text = movie.overview
  }
}