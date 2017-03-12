package com.ashish.movieguide.ui.season

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.tmdb.Season
import com.ashish.movieguide.ui.base.recyclerview.BaseContentHolder
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RemoveListener
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.getPosterUrl

/**
 * Created by Ashish on Jan 04.
 */
class SeasonDelegateAdapter(
        private val layoutId: Int,
        private var onItemClickListener: OnItemClickListener?
) : ViewTypeDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup) = SeasonHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as SeasonHolder).bindData(item as Season)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class SeasonHolder(parent: ViewGroup) : BaseContentHolder<Season>(parent, layoutId) {

        override fun bindData(item: Season) = with(item) {
            val context = itemView.context
            if (seasonNumber != null && seasonNumber > 0) {
                contentTitle.applyText(String.format(context.getString(R.string.season_number_format), seasonNumber))
            } else {
                contentTitle.setText(R.string.season_specials)
            }

            contentSubtitle.applyText(String.format(context.getString(R.string.episode_count_format), episodeCount))
            super.bindData(item)
        }

        override fun getItemClickListener() = onItemClickListener

        override fun getImageUrl(item: Season) = item.posterPath.getPosterUrl()
    }
}