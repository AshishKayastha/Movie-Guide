package com.ashish.movies.ui.base.recyclerview

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.palette.PaletteBitmap
import com.ashish.movies.ui.common.palette.PaletteBitmapTranscoder
import com.ashish.movies.ui.common.palette.PaletteImageViewTarget
import com.ashish.movies.ui.widget.AspectRatioImageView
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.Constants.POSTER_PATH_W500_URL_PREFIX
import com.ashish.movies.utils.extensions.inflate
import com.bumptech.glide.BitmapRequestBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target

/**
 * Created by Ashish on Dec 30.
 */
abstract class BaseContentHolder<in I : ViewType>(parent: ViewGroup) : RecyclerView.ViewHolder(
        parent.inflate(R.layout.list_item_content)) {

    val contentView: View by bindView(R.id.content_view)
    val contentTitle: FontTextView by bindView(R.id.content_title)
    val contentSubtitle: FontTextView by bindView(R.id.content_subtitle)
    val posterImage: AspectRatioImageView by bindView(R.id.poster_image)

    @Suppress("LeakingThis")
    val target: Target<PaletteBitmap> = PaletteImageViewTarget(this)

    val requestBuilder: BitmapRequestBuilder<String, PaletteBitmap> = Glide.with(itemView.context)
            .fromString()
            .asBitmap()
            .transcode(PaletteBitmapTranscoder(itemView.context), PaletteBitmap::class.java)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

    open fun bindData(item: I) = loadPosterImage(item)

    private fun loadPosterImage(item: I) {
        val posterPath = getPosterPath(item)
        if (!TextUtils.isEmpty(posterPath)) {
            requestBuilder.load(POSTER_PATH_W500_URL_PREFIX + posterPath).into(target)
        } else {
            Glide.clear(posterImage)
        }
    }

    abstract fun getPosterPath(item: I): String?
}