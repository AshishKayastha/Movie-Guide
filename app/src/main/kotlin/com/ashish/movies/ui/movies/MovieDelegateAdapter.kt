package com.ashish.movies.ui.movies

import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.ui.common.ViewType
import com.ashish.movies.ui.common.ViewTypeDelegateAdapter
import com.ashish.movies.utils.Constants
import com.ashish.movies.utils.extensions.getSwatchWithMostPixels
import com.ashish.movies.utils.extensions.inflate
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import kotlinx.android.synthetic.main.list_item_movie.view.*

/**
 * Created by Ashish on Dec 30.
 */
class MovieDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = MovieHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as MovieHolder
        holder.bindData(item as Movie)
    }

    inner class MovieHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_movie)) {

        fun bindData(movie: Movie) {
            with(movie) {
                itemView.movieTitle.text = title
                itemView.movieSubtitle.text = releaseDate

                if (!TextUtils.isEmpty(posterPath)) {
                    Glide.with(itemView.context)
                            .load(Constants.POSTER_PATH_W500_URL_PREFIX + posterPath)
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
                val swatch = palette.getSwatchWithMostPixels()
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