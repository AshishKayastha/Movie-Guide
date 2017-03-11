package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.ui.base.recyclerview.BaseContentHolder
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.getYearOnly

/**
 * Created by Ashish on Dec 30.
 */
class TVShowDelegateAdapter(
        private val layoutId: Int,
        private var onItemClickListener: OnItemClickListener?
) : ViewTypeDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup) = TVShowHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as TVShowHolder).bindData(item as TVShow)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class TVShowHolder(parent: ViewGroup) : BaseContentHolder<TVShow>(parent, layoutId) {

        override fun bindData(item: TVShow) = with(item) {
            contentTitle.applyText(name)
            contentSubtitle.applyText(firstAirDate.getYearOnly())
            ratingLabel?.setRating(if (rating != null && rating > 0) rating else voteAverage)
            super.bindData(item)
        }

        override fun getItemClickListener() = onItemClickListener

        override fun getImageUrl(item: TVShow) = item.posterPath.getPosterUrl()
    }
}