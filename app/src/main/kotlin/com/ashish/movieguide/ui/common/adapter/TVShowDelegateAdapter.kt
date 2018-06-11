package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.ui.base.adapter.BaseContentHolder
import com.ashish.movieguide.ui.base.adapter.ContentDelegateAdapter
import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.getYearOnly

/**
 * Created by Ashish on Dec 30.
 */
class TVShowDelegateAdapter(
        layoutId: Int,
        onItemClickListener: OnItemClickListener?
) : ContentDelegateAdapter(layoutId, onItemClickListener) {

    override fun getHolder(view: View) = TVShowHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: RecyclerViewItem) {
        (holder as TVShowHolder).bindData(item as TVShow)
    }

    class TVShowHolder(view: View) : BaseContentHolder<TVShow>(view) {

        override fun bindData(item: TVShow) {
            with(item) {
                super.bindData(item)
                contentTitle.applyText(name)
                contentSubtitle.applyText(firstAirDate.getYearOnly())
                ratingLabel?.setRating(if (rating != null && rating > 0) rating else voteAverage)
            }
        }

        override fun getImageUrl(item: TVShow): String? = item.posterPath.getPosterUrl()
    }
}