package com.ashish.movies.ui.movies

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.extensions.inflate
import com.ashish.movies.extensions.loadImageUrl
import com.ashish.movies.utils.Constants
import kotlinx.android.synthetic.main.list_item_movie.view.*

/**
 * Created by Ashish on Dec 27.
 */
class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MoviesHolder>() {

    private var moviesList: List<Movie>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        return MoviesHolder(parent.inflate(R.layout.list_item_movie)!!)
    }

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        val movie = moviesList?.get(position)
        if (movie != null) {
            with(movie) {
                holder.itemView.movieTitle.text = title
                holder.itemView.movieSubtitle.text = releaseDate
                holder.itemView.thumbnailImage.loadImageUrl(Constants.POSTER_PATH_URL_PRFIX + posterPath)
            }
        }
    }

    override fun getItemCount(): Int = moviesList?.size ?: 0

    fun updateMoviesList(moviesList: List<Movie>?) {
        this.moviesList = moviesList
        notifyDataSetChanged()
    }

    inner class MoviesHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
}