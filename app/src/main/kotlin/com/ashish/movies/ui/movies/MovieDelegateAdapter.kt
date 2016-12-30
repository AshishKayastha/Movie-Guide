package com.ashish.movies.ui.movies

import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.ui.common.ViewType
import com.ashish.movies.ui.common.ViewTypeDelegateAdapter
import com.ashish.movies.ui.widget.AspectRatioImageView
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.Constants
import com.ashish.movies.utils.extensions.getSwatchWithMostPixels
import com.ashish.movies.utils.extensions.inflate
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget

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

        val movieInfoView: View by bindView(R.id.movie_info_view)
        val movieTitle: FontTextView by bindView(R.id.movie_title)
        val movieSubtitle: FontTextView by bindView(R.id.movie_subtitle)
        val posterImage: AspectRatioImageView by bindView(R.id.poster_image)

        fun bindData(movie: Movie) {
            with(movie) {
                movieTitle.text = title
                movieSubtitle.text = releaseDate

                if (!TextUtils.isEmpty(posterPath)) {
                    Glide.with(itemView.context)
                            .load(Constants.POSTER_PATH_W500_URL_PREFIX + posterPath)
                            .asBitmap()
                            .centerCrop()
                            .into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(bitmap: Bitmap?, animation: GlideAnimation<in Bitmap>?) {
                                    if (bitmap != null) {
                                        posterImage.setImageBitmap(bitmap)
                                        generatePaletteFromPosterBitmap(bitmap)
                                    }
                                }
                            })
                } else {
                    Glide.clear(posterImage)
                }
            }
        }

        private fun generatePaletteFromPosterBitmap(bitmap: Bitmap) {
            Palette.from(bitmap).generate { palette ->
                val swatch = palette.getSwatchWithMostPixels()
                if (swatch != null) {
                    with(swatch) {
                        movieInfoView.setBackgroundColor(rgb)
                        movieTitle.setTextColor(titleTextColor)
                        movieSubtitle.setTextColor(bodyTextColor)
                    }
                }
            }
        }
    }
}