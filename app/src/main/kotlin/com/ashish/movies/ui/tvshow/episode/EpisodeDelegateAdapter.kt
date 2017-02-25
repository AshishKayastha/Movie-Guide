package com.ashish.movies.ui.tvshow.episode

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.data.models.Episode
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RemoveListener
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movies.utils.extensions.applyText
import com.ashish.movies.utils.extensions.getOriginalImageUrl

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
            contentSubtitle.applyText(String.format(context.getString(R.string.episode_number_format), episodeNumber))
            itemView.setOnClickListener { onItemClickListener?.onItemClick(adapterPosition, it) }
            super.bindData(item)
        }

        override fun getImageUrl(item: Episode) = item.stillPath.getOriginalImageUrl()
    }
}