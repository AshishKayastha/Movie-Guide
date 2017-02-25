package com.ashish.movies.ui.tvshow.season

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.data.models.Season
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RemoveListener
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movies.utils.extensions.applyText
import com.ashish.movies.utils.extensions.getPosterUrl

/**
 * Created by Ashish on Jan 04.
 */
class SeasonDelegateAdapter(
        private val layoutId: Int,
        private var onItemClickListener: OnItemClickListener?
) : ViewTypeDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup) = SeasonHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as SeasonHolder).bindData(item as Season)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class SeasonHolder(parent: ViewGroup) : BaseContentHolder<Season>(parent, layoutId) {

        override fun bindData(item: Season) = with(item) {
            val context = itemView.context
            contentTitle.applyText(String.format(context.getString(R.string.season_number_format), seasonNumber))
            contentSubtitle.applyText(String.format(context.getString(R.string.episode_count_format), episodeCount))
            itemView.setOnClickListener { onItemClickListener?.onItemClick(adapterPosition, it) }
            super.bindData(item)
        }

        override fun getImageUrl(item: Season) = item.posterPath.getPosterUrl()
    }
}