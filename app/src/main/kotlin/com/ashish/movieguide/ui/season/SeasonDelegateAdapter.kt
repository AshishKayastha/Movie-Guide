package com.ashish.movieguide.ui.season

import android.support.v7.widget.RecyclerView
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Season
import com.ashish.movieguide.ui.base.adapter.BaseContentHolder
import com.ashish.movieguide.ui.base.adapter.ContentDelegateAdapter
import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.getPosterUrl

/**
 * Created by Ashish on Jan 04.
 */
class SeasonDelegateAdapter(
        layoutId: Int,
        onItemClickListener: OnItemClickListener?
) : ContentDelegateAdapter(layoutId, onItemClickListener) {

    override fun getHolder(view: View) = SeasonHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: RecyclerViewItem) {
        (holder as SeasonHolder).bindData(item as Season)
    }

    class SeasonHolder(view: View) : BaseContentHolder<Season>(view) {

        override fun bindData(item: Season) {
            with(item) {
                super.bindData(item)
                val context = itemView.context
                if (seasonNumber != null && seasonNumber > 0) {
                    contentTitle.applyText(String.format(context.getString(R.string.season_number_format),
                            seasonNumber))
                } else {
                    contentTitle.setText(R.string.season_specials)
                }

                contentSubtitle.applyText(String.format(context.getString(R.string.episode_count_format), episodeCount))
            }
        }

        override fun getImageUrl(item: Season): String? = item.posterPath.getPosterUrl()
    }
}