package com.ashish.movieguide.ui.base.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import butterknife.bindOptionalView
import butterknife.bindView
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.common.palette.PaletteBitmap
import com.ashish.movieguide.ui.common.palette.PaletteImageViewTarget
import com.ashish.movieguide.ui.widget.AspectRatioImageView
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.ui.widget.LabelLayout
import com.ashish.movieguide.utils.extensions.inflate
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.transcodePaletteBitmap
import com.bumptech.glide.BitmapRequestBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target

/**
 * Created by Ashish on Dec 30.
 */
abstract class BaseContentHolder<in I : ViewType>(
        parent: ViewGroup,
        layoutId: Int = R.layout.list_item_content
) : RecyclerView.ViewHolder(parent.inflate(layoutId)) {

    val contentView: View by bindView(R.id.content_view)
    val contentTitle: FontTextView by bindView(R.id.content_title)
    val contentSubtitle: FontTextView by bindView(R.id.content_subtitle)
    val posterImage: AspectRatioImageView by bindView(R.id.poster_image)
    val averageVoteText: LabelLayout? by bindOptionalView(R.id.avg_vote_text)

    @Suppress("LeakingThis")
    val target: Target<PaletteBitmap> = PaletteImageViewTarget(this)

    val requestBuilder: BitmapRequestBuilder<String, PaletteBitmap> = Glide.with(itemView.context)
            .transcodePaletteBitmap(itemView.context)

    init {
        itemView.setOnClickListener { view ->
            getItemClickListener()?.onItemClick(adapterPosition, view)
        }
    }

    open fun bindData(item: I) = loadImage(item)

    private fun loadImage(item: I) {
        val imageUrl = getImageUrl(item)
        if (imageUrl.isNotNullOrEmpty()) {
            requestBuilder.load(imageUrl)
                    .animate(R.anim.fade_in)
                    .into(target)
        } else {
            Glide.clear(posterImage)
        }
    }

    abstract fun getItemClickListener(): OnItemClickListener?

    abstract fun getImageUrl(item: I): String?
}