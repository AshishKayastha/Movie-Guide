package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movieguide.data.network.entities.tmdb.Credit
import com.ashish.movieguide.ui.base.recyclerview.BaseContentHolder
import com.ashish.movieguide.ui.base.recyclerview.ContentDelegateAdapter
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.getProfileUrl
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty

/**
 * Created by Ashish on Jan 03.
 */
class CreditDelegateAdapter(
        layoutId: Int,
        onItemClickListener: OnItemClickListener?
) : ContentDelegateAdapter(layoutId, onItemClickListener) {

    override fun getHolder(parent: ViewGroup, layoutId: Int) = CreditHolder(parent, layoutId)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as CreditHolder).bindData(item as Credit)
    }

    class CreditHolder(parent: ViewGroup, layoutId: Int) : BaseContentHolder<Credit>(parent, layoutId) {

        override fun bindData(item: Credit) {
            with(item) {
                super.bindData(item)
                contentTitle.applyText(if (title.isNotNullOrEmpty()) title else name)
                contentSubtitle.applyText(if (job.isNotNullOrEmpty()) job else character)
            }
        }

        override fun getImageUrl(item: Credit): String? = with(item) {
            if (profilePath.isNotNullOrEmpty()) {
                profilePath.getProfileUrl()
            } else {
                posterPath.getPosterUrl()
            }
        }
    }
}