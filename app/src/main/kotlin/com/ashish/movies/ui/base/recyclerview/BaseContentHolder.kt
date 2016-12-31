package com.ashish.movies.ui.base.recyclerview

import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.common.ViewType
import com.ashish.movies.ui.widget.AspectRatioImageView
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.Constants.POSTER_PATH_W500_URL_PREFIX
import com.ashish.movies.utils.extensions.getSwatchWithMostPixels
import com.ashish.movies.utils.extensions.inflate
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget

/**
 * Created by Ashish on Dec 30.
 */
abstract class BaseContentHolder<in I : ViewType>(parent: ViewGroup) : RecyclerView.ViewHolder(
        parent.inflate(R.layout.list_item_content)) {

    val contentView: View by bindView(R.id.content_view)
    val contentTitle: FontTextView by bindView(R.id.content_title)
    val contentSubtitle: FontTextView by bindView(R.id.content_subtitle)
    val posterImage: AspectRatioImageView by bindView(R.id.poster_image)

    open fun bindData(item: I) {
        loadPosterImage(item)
    }

    private fun loadPosterImage(item: I) {
        val posterPath = getPosterPath(item)
        if (!TextUtils.isEmpty(posterPath)) {
            Glide.with(itemView.context)
                    .load(POSTER_PATH_W500_URL_PREFIX + posterPath)
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

    abstract fun getPosterPath(item: I): CharSequence?

    private fun generatePaletteFromPosterBitmap(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val swatch = palette.getSwatchWithMostPixels()
            if (swatch != null) {
                with(swatch) {
                    contentView.setBackgroundColor(rgb)
                    contentTitle.setTextColor(titleTextColor)
                    contentSubtitle.setTextColor(bodyTextColor)
                }
            }
        }
    }
}