package com.ashish.movieguide.ui.episode

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.tmdb.Episode
import com.ashish.movieguide.ui.base.recyclerview.BaseContentHolder
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RemoveListener
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.getStillImageUrl

/**
 * Created by Ashish on Jan 08.
 */
class EpisodeDelegateAdapter(
        private val layoutId: Int,
        private var onItemClickListener: OnItemClickListener?
) : ViewTypeDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup) = EpisodeHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as EpisodeHolder).bindData(item as Episode)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class EpisodeHolder(parent: ViewGroup) : BaseContentHolder<Episode>(parent, layoutId) {

        override fun bindData(item: Episode) = with(item) {
            val context = itemView.context
            contentTitle.applyText(name)
            ratingLabel?.setRating(rating)
            contentSubtitle.applyText(String.format(context.getString(R.string.episode_number_format),
                    episodeNumber))
            super.bindData(item)
        }

        override fun getItemClickListener() = onItemClickListener

        override fun getImageUrl(item: Episode) = item.stillPath.getStillImageUrl()
    }
}