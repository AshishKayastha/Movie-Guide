package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movieguide.data.models.tmdb.Movie
import com.ashish.movieguide.ui.base.recyclerview.BaseContentHolder
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
            ratingLabel?.setRating(if (rating != null && rating > 0) rating else voteAverage)
            super.bindData(item)
        }

        override fun getItemClickListener() = onItemClickListener

        override fun getImageUrl(item: Movie) = item.posterPath.getPosterUrl()
    }
}