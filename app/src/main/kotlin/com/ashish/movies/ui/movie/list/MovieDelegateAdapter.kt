package com.ashish.movies.ui.movie.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RemoveListener
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movies.utils.ApiConstants.POSTER_W500_URL_PREFIX
import com.ashish.movies.utils.extensions.applyText
import com.ashish.movies.utils.extensions.getYearOnly

/**
 * Created by Ashish on Dec 30.
 */
class MovieDelegateAdapter(val layoutId: Int = R.layout.list_item_content,
                           var onItemClickListener: OnItemClickListener?)
    : ViewTypeDelegateAdapter, RemoveListener {

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
            averageVoteText?.setLabelText(voteAverage.toString())
            itemView.setOnClickListener { onItemClickListener?.onItemClick(adapterPosition, it) }
            super.bindData(item)
        }

        override fun getImageUrl(item: Movie) = POSTER_W500_URL_PREFIX + item.posterPath
    }
}