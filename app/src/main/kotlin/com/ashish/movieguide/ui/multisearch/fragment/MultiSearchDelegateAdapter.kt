package com.ashish.movieguide.ui.multisearch.fragment

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movieguide.data.network.entities.tmdb.MultiSearch
import com.ashish.movieguide.ui.base.recyclerview.BaseContentHolder
import com.ashish.movieguide.ui.base.recyclerview.ContentDelegateAdapter
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.getProfileUrl
import com.ashish.movieguide.utils.extensions.getYearOnly
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty

/**
 * Created by Ashish on Jan 05.
 */
class MultiSearchDelegateAdapter(
        layoutId: Int,
        onItemClickListener: OnItemClickListener?
) : ContentDelegateAdapter(layoutId, onItemClickListener) {

    override fun getHolder(parent: ViewGroup, layoutId: Int) = MultiSearchHolder(parent, layoutId)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as MultiSearchHolder).bindData(item as MultiSearch)
    }

    class MultiSearchHolder(parent: ViewGroup, layoutId: Int) : BaseContentHolder<MultiSearch>(parent, layoutId) {

        override fun bindData(item: MultiSearch) {
            with(item) {
                super.bindData(item)
                contentTitle.applyText(if (title.isNotNullOrEmpty()) title else name)
                contentSubtitle.applyText(getSubtitle(this))
                ratingLabel?.setRating(voteAverage)
            }
        }

        private fun getSubtitle(multiSearch: MultiSearch): String = with(multiSearch) {
            if (releaseDate.isNotNullOrEmpty()) {
                releaseDate.getYearOnly()
            } else {
                firstAirDate.getYearOnly()
            }
        }

        override fun getImageUrl(item: MultiSearch): String? = with(item) {
            if (profilePath.isNotNullOrEmpty()) {
                profilePath.getProfileUrl()
            } else {
                posterPath.getPosterUrl()
            }
        }
    }
}