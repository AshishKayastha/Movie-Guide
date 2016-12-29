package com.ashish.movies.ui.movies

import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.extensions.getSwatchWithMostPopulation
import com.ashish.movies.extensions.inflate
import com.ashish.movies.utils.Constants.Companion.POSTER_PATH_URL_PREFIX
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import kotlinx.android.synthetic.main.list_item_movie.view.*
import java.util.*

/**
 * Created by Ashish on Dec 27.
 */
class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MoviesHolder>() {

    private var moviesList: MutableList<Movie>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        return MoviesHolder(parent.inflate(R.layout.list_item_movie)!!)
    }

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        val movie = moviesList?.get(position)
        if (movie != null) holder.bindData(movie)
    }

    override fun getItemCount(): Int = moviesList?.size ?: 0

    fun updateMoviesList(moviesList: List<Movie>?) {
        this.moviesList = ArrayList(moviesList)
        notifyDataSetChanged()
    }

    class MoviesHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(movie: Movie) {
            with(movie) {
                itemView.movieTitle.text = title
                itemView.movieSubtitle.text = releaseDate

                if (!TextUtils.isEmpty(posterPath)) {
                    Glide.with(itemView.context)
                            .load(POSTER_PATH_URL_PREFIX + posterPath)
                            .asBitmap()
                            .centerCrop()
                            .into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(bitmap: Bitmap?, animation: GlideAnimation<in Bitmap>?) {
                                    if (bitmap != null) {
                                        itemView.thumbnailImage.setImageBitmap(bitmap)
                                        generatePaletteFromPosterBitmap(bitmap)
                                    }
                                }
                            })
                } else {
                    Glide.clear(itemView.thumbnailImage)
                }
            }
        }

        private fun generatePaletteFromPosterBitmap(bitmap: Bitmap) {
            Palette.from(bitmap).generate { palette ->
                val swatch = palette.getSwatchWithMostPopulation()
                if (swatch != null) {
                    with(swatch) {
                        itemView.movieInfoView.setBackgroundColor(rgb)
                        itemView.movieTitle.setTextColor(titleTextColor)
                        itemView.movieSubtitle.setTextColor(bodyTextColor)
                    }
                }
            }
        }
    }
}