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
import com.ashish.movies.utils.ApiConstants.MEDIA_TYPE_PERSON
import com.ashish.movies.utils.ApiConstants.MEDIA_TYPE_TV
import com.ashish.movies.utils.ApiConstants.POSTER_W500_URL_PREFIX
import com.ashish.movies.utils.ApiConstants.PROFILE_ORIGINAL_URL_PREFIX
import com.ashish.movies.utils.extensions.applyText
import com.ashish.movies.utils.extensions.getYearOnly
import com.ashish.movies.utils.extensions.hide
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.setTransitionName

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

            posterImage.setTransitionName(getTransitionName(item))
            itemView.setOnClickListener { onItemClickListener?.onItemClick(adapterPosition, it) }
            super.bindData(item)
        }

        private fun getSubtitle(multiSearch: MultiSearch): String {
            with(multiSearch) {
                if (releaseDate.isNotNullOrEmpty()) {
                    return releaseDate.getYearOnly()
                } else if (firstAirDate.isNotNullOrEmpty()) {
                    return firstAirDate.getYearOnly()
                } else {
                    return ""
                }
            }
        }

        private fun getTransitionName(multiSearch: MultiSearch): Int {
            with(multiSearch) {
                if (mediaType == MEDIA_TYPE_TV) {
                    return R.string.transition_tv_poster
                } else if (mediaType == MEDIA_TYPE_PERSON) {
                    return R.string.transition_person_profile
                } else {
                    return R.string.transition_movie_poster
                }
            }
        }

        override fun getImageUrl(item: MultiSearch): String? {
            with(item) {
                if (profilePath.isNotNullOrEmpty()) {
                    return PROFILE_ORIGINAL_URL_PREFIX + profilePath
                } else if (posterPath.isNotNullOrEmpty()) {
                    return POSTER_W500_URL_PREFIX + posterPath
                } else {
                    return ""
                }
            }
        }
    }
}