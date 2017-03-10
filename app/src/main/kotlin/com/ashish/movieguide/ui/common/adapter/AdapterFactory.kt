package com.ashish.movieguide.ui.common.adapter

import com.ashish.movieguide.ui.episode.EpisodeDelegateAdapter
import com.ashish.movieguide.ui.movie.list.MovieDelegateAdapter
import com.ashish.movieguide.ui.multisearch.fragment.MultiSearchDelegateAdapter
import com.ashish.movieguide.ui.people.list.PersonDelegateAdapter
import com.ashish.movieguide.ui.review.ReviewDelegateAdapter
import com.ashish.movieguide.ui.season.SeasonDelegateAdapter
import com.ashish.movieguide.ui.tvshow.list.TVShowDelegateAdapter
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_CREDIT
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_EPISODE
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MULTI_SEARCH
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_PERSON
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_REVIEW
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_SEASON
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_TV_SHOW

/**
 * Created by Ashish on Feb 12.
 */
object AdapterFactory {

    fun getAdapter(layoutId: Int, adapterType: Int, onItemClickListener: OnItemClickListener?)
            : ViewTypeDelegateAdapter = when (adapterType) {
        ADAPTER_TYPE_MOVIE -> MovieDelegateAdapter(layoutId, onItemClickListener)
        ADAPTER_TYPE_TV_SHOW -> TVShowDelegateAdapter(layoutId, onItemClickListener)
        ADAPTER_TYPE_PERSON -> PersonDelegateAdapter(layoutId, onItemClickListener)
        ADAPTER_TYPE_CREDIT -> CreditDelegateAdapter(layoutId, onItemClickListener)
        ADAPTER_TYPE_SEASON -> SeasonDelegateAdapter(layoutId, onItemClickListener)
        ADAPTER_TYPE_EPISODE -> EpisodeDelegateAdapter(layoutId, onItemClickListener)
        ADAPTER_TYPE_MULTI_SEARCH -> MultiSearchDelegateAdapter(layoutId, onItemClickListener)
        ADAPTER_TYPE_REVIEW -> ReviewDelegateAdapter(layoutId, onItemClickListener)
        else -> throw IllegalArgumentException("Invalid adapter type: $adapterType")
    }
}