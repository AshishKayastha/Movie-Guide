package com.ashish.movieguide.ui.multisearch.fragment

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movieguide.data.models.MultiSearch
import com.ashish.movieguide.ui.base.recyclerview.BaseContentHolder
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RemoveListener
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.getProfileUrl
import com.ashish.movieguide.utils.extensions.getYearOnly
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty

/**
 * Created by Ashish on Jan 05.
 */
class MultiSearchDelegateAdapter(
        private val layoutId: Int,
        private var onItemClickListener: OnItemClickListener?
) : ViewTypeDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup) = MultiSearchHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as MultiSearchHolder).bindData(item as MultiSearch)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class MultiSearchHolder(parent: ViewGroup) : BaseContentHolder<MultiSearch>(parent, layoutId) {

        override fun bindData(item: MultiSearch) = with(item) {
            contentTitle.applyText(if (title.isNotNullOrEmpty()) title else name)
            contentSubtitle.applyText(getSubtitle(this))
            averageVoteText?.setRating(voteAverage)
            super.bindData(item)
        }

        private fun getSubtitle(multiSearch: MultiSearch): String {
            with(multiSearch) {
                if (releaseDate.isNotNullOrEmpty()) {
                    return releaseDate.getYearOnly()
                } else {
                    return firstAirDate.getYearOnly()
                }
            }
        }

        override fun getItemClickListener() = onItemClickListener

        override fun getImageUrl(item: MultiSearch): String? {
            with(item) {
                if (profilePath.isNotNullOrEmpty()) {
                    return profilePath.getProfileUrl()
                } else {
                    return posterPath.getPosterUrl()
                }
            }
        }
    }
}