package com.ashish.movies.ui.tvshow.list

import com.ashish.movies.data.api.TVShowApi.Companion.AIRING_TODAY
import com.ashish.movies.data.api.TVShowApi.Companion.ON_THE_AIR
import com.ashish.movies.data.api.TVShowApi.Companion.POPULAR
import com.ashish.movies.data.api.TVShowApi.Companion.TOP_RATED
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewPresenter
import javax.inject.Inject

/**
 * Created by Ashish on Dec 30.
 */
class TVShowPresenter @Inject constructor(val tvShowInteractor: TVShowInteractor)
    : BaseRecyclerViewPresenter<TVShow, BaseRecyclerViewMvpView<TVShow>>() {

    companion object {
        private val TV_SHOW_TYPES = arrayOf(ON_THE_AIR, POPULAR, TOP_RATED, AIRING_TODAY)
    }

    override fun getType(type: Int?) = TV_SHOW_TYPES[type ?: 0]

    override fun getResultsObservable(type: String?, page: Int) = tvShowInteractor.getTVShowsByType(type, page)
}