package com.ashish.movieguide.ui.movie.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.ui.base.recyclerview.BaseContentHolder
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RemoveListener
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.getYearOnly

/**
 * Created by Ashish on Dec 30.
 */
class MovieDelegateAdapter(
        private val layoutId: Int,
        private var onItemClickListener: OnItemClickListener?
) : ViewTypeDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup) = MovieHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as MovieHolder).bindData(item as Movie)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class MovieHolder(parent: ViewGroup) : BaseContentHolder<Movie>(parent, layoutId) {

        override fun bindData(item: Movie) = with(item) {
            contentTitle.applyText(title)
            contentSubtitle.applyText(releaseDate.getYearOnly())
            averageVoteText?.setRating(voteAverage)
            super.bindData(item)
        }

        override fun getItemClickListener() = onItemClickListener

        override fun getImageUrl(item: Movie) = item.posterPath.getPosterUrl()
    }
}