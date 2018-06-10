package com.ashish.movieguide.ui.base.recyclerview

import android.support.annotation.CallSuper
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.common.adapter.RecyclerViewItem
import com.ashish.movieguide.ui.widget.AspectRatioImageView
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.ui.widget.LabelLayout
import com.ashish.movieguide.utils.Constants.LIST_THUMBNAIL_HEIGHT
import com.ashish.movieguide.utils.Constants.LIST_THUMBNAIL_WIDTH
import com.ashish.movieguide.utils.extensions.bindOptionalView
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.glide.GlideApp
import com.ashish.movieguide.utils.glide.PaletteImageViewTarget
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Ashish on Dec 30.
 */
abstract class BaseContentHolder<in I : RecyclerViewItem>(view: View) : BaseHolder(view) {

    val contentView: View by bindView(R.id.content_view)
    val contentTitle: FontTextView by bindView(R.id.content_title)
    val contentSubtitle: FontTextView by bindView(R.id.content_subtitle)
    val posterImage: AspectRatioImageView by bindView(R.id.poster_image)
    val ratingLabel: LabelLayout? by bindOptionalView(R.id.rating_label)

    private val options = RequestOptions()
            .override(LIST_THUMBNAIL_WIDTH, LIST_THUMBNAIL_HEIGHT)

    @CallSuper
    open fun bindData(item: I) {
        loadImage(item)
    }

    private fun loadImage(item: I) {
        val imageUrl = getImageUrl(item)
        if (imageUrl.isNotNullOrEmpty()) {
            GlideApp.with(itemView.context)
                    .asBitmap()
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .apply(options)
                    .into(PaletteImageViewTarget(this))
        } else {
            GlideApp.with(itemView.context).clear(posterImage)
        }
    }

    abstract fun getImageUrl(item: I): String?
}