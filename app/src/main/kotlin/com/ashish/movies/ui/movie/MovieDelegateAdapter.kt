package com.ashish.movies.ui.movie

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movies.data.models.Movie
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewTypeDelegateAdapter

/**
 * Created by Ashish on Dec 30.
 */
class MovieDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = MovieHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as MovieHolder).bindData(item as Movie)
    }

    inner class MovieHolder(parent: ViewGroup) : BaseContentHolder<Movie>(parent) {

        override fun bindData(item: Movie) = with(item) {
            contentTitle.text = title
            contentSubtitle.text = releaseDate
            super.bindData(item)
        }

        override fun getPosterPath(item: Movie) = item.posterPath
    }
}