package com.example.moviesapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapplication.R
import com.example.moviesapplication.data.Movie

class MoviesListAdapter(
  private val moviesList: MutableList<Movie>,
  private val clickListener: ClickListener) :
  RecyclerView.Adapter<MoviesListAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view: View =
      LayoutInflater.from(parent.context).inflate(R.layout.item_movie_list, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    Glide.with(holder.itemView.context)
      .load("https://image.tmdb.org/t/p/w92/${moviesList[position].poster_path}")
      .into(holder.ivPoster)
    holder.tvMovieName.text = moviesList[position].title
    holder.tvMovieReleaseDate.text = moviesList[position].release_date
  }

  override fun getItemCount(): Int {
    return moviesList.size
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivPoster: ImageView = itemView.findViewById(R.id.ivMoviePoster)
    private val ivFavorite: ImageView = itemView.findViewById(R.id.ivFaviourite)
    val tvMovieName: TextView = itemView.findViewById(R.id.tvMovieName)
    val tvMovieReleaseDate: TextView = itemView.findViewById(R.id.tvMovieReleaseDate)

    init {
      ivFavorite.setOnClickListener {
        clickListener.markMovieAsFavorite(moviesList[adapterPosition])
      }

      itemView.setOnClickListener {
        clickListener.goToMovieDetailScreen(moviesList[adapterPosition])
      }
    }
  }
}

interface ClickListener {
  fun markMovieAsFavorite(movie: Movie)
  fun goToMovieDetailScreen(movie: Movie)
}