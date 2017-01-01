package com.ashish.movies.ui.tvshow

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movies.utils.Constants.POSTER_W500_URL_PREFIX

/**
 * Created by Ashish on Dec 30.
 */
class TVShowDelegateAdapter(val onItemClickListener: OnItemClickListener) : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = TVShowHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as TVShowHolder).bindData(item as TVShow)
    }

    inner class TVShowHolder(parent: ViewGroup) : BaseContentHolder<TVShow>(parent) {

        override fun bindData(item: TVShow) = with(item) {
            contentTitle.text = name
            contentSubtitle.text = firstAirDate
            averageVoteText.setLabelText(voteAverage.toString())
            itemView.setOnClickListener { onItemClickListener.onItemClick(adapterPosition) }
            super.bindData(item)
        }

        override fun getImageUrl(item: TVShow) = POSTER_W500_URL_PREFIX + item.posterPath
    }
}