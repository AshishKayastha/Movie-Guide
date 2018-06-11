package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.ui.base.adapter.BaseContentHolder
import com.ashish.movieguide.ui.base.adapter.ContentDelegateAdapter
import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.getYearOnly

/**
 * Created by Ashish on Dec 30.
 */
class MovieDelegateAdapter(
        layoutId: Int,
        onItemClickListener: OnItemClickListener?
) : ContentDelegateAdapter(layoutId, onItemClickListener) {

    override fun getHolder(view: View) = MovieHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: RecyclerViewItem) {
        (holder as MovieHolder).bindData(item as Movie)
    }

    class MovieHolder(view: View) : BaseContentHolder<Movie>(view) {

        override fun bindData(item: Movie) {
            with(item) {
                super.bindData(item)
                contentTitle.applyText(title)
                contentSubtitle.applyText(releaseDate.getYearOnly())
                ratingLabel?.setRating(if (rating != null && rating > 0) rating else voteAverage)
            }
        }

        override fun getImageUrl(item: Movie): String? = item.posterPath.getPosterUrl()
    }
}