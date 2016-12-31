package com.ashish.movies.ui.tvshow

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewTypeDelegateAdapter

/**
 * Created by Ashish on Dec 30.
 */
class TVShowDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = TVShowHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as TVShowHolder).bindData(item as TVShow)
    }

    inner class TVShowHolder(parent: ViewGroup) : BaseContentHolder<TVShow>(parent) {

        override fun bindData(item: TVShow) = with(item) {
            contentTitle.text = name
            contentSubtitle.text = firstAirDate
            super.bindData(item)
        }

        override fun getPosterPath(item: TVShow) = item.posterPath
    }
}