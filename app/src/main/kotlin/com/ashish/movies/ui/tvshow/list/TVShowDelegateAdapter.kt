package com.ashish.movies.ui.tvshow.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RemoveListener
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movies.utils.Constants.POSTER_W500_URL_PREFIX
import com.ashish.movies.utils.extensions.setTitleAndYear

/**
 * Created by Ashish on Dec 30.
 */
class TVShowDelegateAdapter(val layoutId: Int = R.layout.list_item_content,
                            var onItemClickListener: OnItemClickListener?)
    : ViewTypeDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup) = TVShowHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as TVShowHolder).bindData(item as TVShow)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class TVShowHolder(parent: ViewGroup) : BaseContentHolder<TVShow>(parent, layoutId) {

        override fun bindData(item: TVShow) = with(item) {
            contentTitle.setTitleAndYear(name, firstAirDate)
            averageVoteText?.setLabelText(voteAverage.toString())
            itemView.setOnClickListener { onItemClickListener?.onItemClick(adapterPosition, it) }
            super.bindData(item)
        }

        override fun getImageUrl(item: TVShow) = POSTER_W500_URL_PREFIX + item.posterPath
    }
}