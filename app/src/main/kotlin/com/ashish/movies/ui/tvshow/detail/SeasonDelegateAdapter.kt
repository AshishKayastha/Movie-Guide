package com.ashish.movies.ui.tvshow.detail

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.data.models.TVShowSeason
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RemoveListener
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movies.utils.ApiConstants.POSTER_W500_URL_PREFIX
import com.ashish.movies.utils.extensions.applyText

/**
 * Created by Ashish on Jan 04.
 */
class SeasonDelegateAdapter(val layoutId: Int = R.layout.list_item_content_alt,
                            var onItemClickListener: OnItemClickListener?)
    : ViewTypeDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup) = SeasonHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as SeasonHolder).bindData(item as TVShowSeason)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class SeasonHolder(parent: ViewGroup) : BaseContentHolder<TVShowSeason>(parent, layoutId) {

        override fun bindData(item: TVShowSeason) = with(item) {
            val context = itemView.context
            contentTitle.applyText(String.format(context.getString(R.string.season_count_format), seasonNumber))
            contentSubtitle.applyText(String.format(context.getString(R.string.episode_count_format), episodeCount))
            itemView.setOnClickListener { onItemClickListener?.onItemClick(adapterPosition, it) }
            super.bindData(item)
        }

        override fun getImageUrl(item: TVShowSeason) = POSTER_W500_URL_PREFIX + item.posterPath
    }
}