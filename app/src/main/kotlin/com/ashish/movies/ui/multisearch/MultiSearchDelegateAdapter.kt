package com.ashish.movies.ui.multisearch

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.data.models.MultiSearch
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RemoveListener
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movies.utils.extensions.applyText
import com.ashish.movies.utils.extensions.getPosterUrl
import com.ashish.movies.utils.extensions.getProfileUrl
import com.ashish.movies.utils.extensions.getYearOnly
import com.ashish.movies.utils.extensions.hide
import com.ashish.movies.utils.extensions.isNotNullOrEmpty

/**
 * Created by Ashish on Jan 05.
 */
class MultiSearchDelegateAdapter(val layoutId: Int = R.layout.list_item_content,
                                 var onItemClickListener: OnItemClickListener?)
    : ViewTypeDelegateAdapter, RemoveListener {

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

            if (voteAverage != null && voteAverage > 0.0) {
                averageVoteText?.setLabelText(voteAverage.toString())
            } else {
                averageVoteText?.hide()
            }

            itemView.setOnClickListener { onItemClickListener?.onItemClick(adapterPosition, it) }
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